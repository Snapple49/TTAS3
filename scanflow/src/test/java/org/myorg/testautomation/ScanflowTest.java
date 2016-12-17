package org.myorg.testautomation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.graphwalker.core.condition.EdgeCoverage;
import org.graphwalker.core.condition.ReachedVertex;
import org.graphwalker.core.condition.TimeDuration;
import org.graphwalker.core.generator.AStarPath;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.test.TestBuilder;
import org.junit.Test;
import org.junit.Before;
import scanflow.adapter.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class ScanflowTest extends ExecutionContext implements Scanflow {
    public final static Path MODEL_PATH = Paths.get("org/myorg/testautomation/Scanflow.graphml");
    private static Random random;

    private ScanflowInterface sf;
    private boolean signed = false;
    private List<ResponseCode> response;
    private List<ResponseCode> expected;

    public static <T extends Enum<?>> T randomEnumValue(Class<T> cls){
        int x = random.nextInt(cls.getEnumConstants().length);
        return cls.getEnumConstants()[x];
    }

    public ScanflowTest(){
        random = new Random();
    }

    private boolean validateResponse(){
        return response.equals(expected);
    }

    @Override
    public void Disconnected(){
        this.sf = new ScanflowInterface();
    }

    @Override
    public void Not_Signed(){
        assertFalse(signed);
    	assertTrue(validateResponse());
    }

     @Override
    public void AS_Open(){
        assertTrue(signed);
        assertTrue(validateResponse());
    }

    @Override
    public void AS_Idle(){
        assertTrue(signed);
        assertTrue(validateResponse());
    }

    @Override
    public void AS_Closed(){
        assertTrue(signed);
        assertTrue(validateResponse());
    }

    @Override
    public void AS_Ending(){
        assertTrue(signed);
        assertTrue(validateResponse());
    }

    @Override
    public void connect(){
        response = sf.connect();
        expected = Arrays.asList(ResponseCode.ServiceReady);
    }

    @Override
    public void invalid_sign_on(){
    	response = sf.signOn(7, "81");
        expected = Arrays.asList(ResponseCode.AuthenticationFailed);
    }

    @Override
    public void valid_sign_on(){
        signed = true;
    	response = sf.signOn(8, "01");
        expected = Arrays.asList(ResponseCode.SignedOn);
    }

    @Override
    public void valid_sign_off(){
        signed = false;
    	response = sf.signOff();
        expected = Arrays.asList(ResponseCode.SignedOff);
    }

    @Override
    public void invalid_sign_off(){
        response = sf.signOff();
        expected = Arrays.asList(ResponseCode.NotSignedOn);
    }

    @Override
    public void idle(){
        response = sf.idle();
        expected = Arrays.asList(ResponseCode.AccountIdled);
    }

    @Override
    public void close_acc(){
        response = sf.closeAccount();
        expected = Arrays.asList(ResponseCode.AccountClosed);
    }

    @Override
    public void open_acc(){
        response = sf.openAccount();
        expected = Arrays.asList(ResponseCode.AccountOpened);
    }

    @Override
    public void valid_trans(){
        response = sf.trans("TM_CASH");
        expected = Arrays.asList(ResponseCode.TransactionSucceeded);
    }

    @Override
    public void invalid_trans(){
        response = sf.trans("TM_CASHhhhh");
        expected = Arrays.asList(ResponseCode.NoSuchTransactionMethod);
    }

    @Override
    public void valid_art_reg(){
        ValidArtId id = randomEnumValue(ValidArtId.class);
        response = sf.artReg(id.value());
        if(signed){
            expected = Arrays.asList(ResponseCode.AccountBalance, ResponseCode.ArtRegistered);
        }
        else{
            expected = Arrays.asList(ResponseCode.NotSignedOn);
        }
    }

    @Override
    public void invalid_art_reg(){
        InvalidArtId id = randomEnumValue(InvalidArtId.class);
        response = sf.artReg(id.value());
        if(signed){
            expected = Arrays.asList(ResponseCode.NoSuchArticle);
        }
        else{
            expected = Arrays.asList(ResponseCode.NotSignedOn);
        }
    }

    @Override
    public void valid_art_id(){
        ValidArtId id = randomEnumValue(ValidArtId.class);
        response = sf.artId(id.value());
        if(signed){
            expected = Arrays.asList(ResponseCode.ArtDescription2);
        }
        else{
            expected = Arrays.asList(ResponseCode.NotSignedOn);
        }
        
    }

    public void invalid_art_id(){
        InvalidArtId id = randomEnumValue(InvalidArtId.class);
        response = sf.artId(id.value());
        if(signed){
            expected = Arrays.asList(ResponseCode.NoSuchArticle);
        }
        else{
            expected = Arrays.asList(ResponseCode.NotSignedOn);
        }
    }

    @Override
    public void valid_get(){
        ValidCRVariable var = randomEnumValue(ValidCRVariable.class);
        response = sf.getVariable(var.toString());
        expected = Arrays.asList(ResponseCode.CRVariable);
    }

    @Override
    public void invalid_get(){
        InvalidCRVariable var = randomEnumValue(InvalidCRVariable.class);
        response = sf.getVariable(var.toString());
        expected = Arrays.asList(ResponseCode.NoSuchVariable);
    }

    @Test
    public void StandardTest(){
    	new TestBuilder()
    		.setModel(MODEL_PATH)
    		.setContext(this)
    		.setPathGenerator(new RandomPath(new EdgeCoverage(98)))
    		.setStart("Disconnected")
            .execute();
    }

}