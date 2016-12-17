package org.myorg.testautomation;

import static org.junit.Assert.assertTrue;

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

public class ScanflowTest extends ExecutionContext implements Scanflow {
    public final static Path MODEL_PATH = Paths.get("org/myorg/testautomation/Scanflow.graphml");
    private static Random random;

    private ScanflowInterface sf;
    public ScanflowState previousState = ScanflowState.Not_Signed;

    public static <T extends Enum<?>> T randomEnumValue(Class<T> cls){
        int x = random.nextInt(cls.getEnumConstants().length);
        return cls.getEnumConstants()[x];
    }

    public ScanflowTest(){
        random = new Random();
    }

    @Override
    public void Disconnected(){
        
    }

    @Override
    public void Not_Signed(){
    	
    }

     @Override
    public void AS_Open(){
        previousState = ScanflowState.AS_Open;
    }

    @Override
    public void AS_Idle(){
        previousState = ScanflowState.AS_Idle;
    }

    @Override
    public void AS_Closed(){
        previousState = ScanflowState.AS_Closed;
    }

    @Override
    public void AS_Ending(){
        previousState = ScanflowState.AS_Ending;
    }

    @Override
    public void connect(){
        this.sf = new ScanflowInterface();
    }

    @Override
    public void invalid_sign_on(){
    	List<ResponseCode> response = sf.signOn(7, "81");
    	assertTrue(response.size() == 1);
    	boolean valid = response.contains(ResponseCode.AuthenticationFailed);
    	assertTrue(valid);
    }

    @Override
    public void valid_sign_on(){
    	List<ResponseCode> response = sf.signOn(8, "01");
    	assertTrue(response.size() == 1);
    	boolean valid = response.contains(ResponseCode.SignedOn);
    	assertTrue(valid);
    }

    @Override
    public void valid_sign_off(){
    	List<ResponseCode> response = sf.signOff();
    	assertTrue(response.size() == 1);
    	boolean valid = response.contains(ResponseCode.SignedOff);
    	assertTrue(valid);
    }

    @Override
    public void invalid_sign_off(){
        List<ResponseCode> response = sf.signOff();
        assertTrue(response.size() == 1);
        boolean valid = response.contains(ResponseCode.NotSignedOn);
        assertTrue(valid);
    }

    @Override
    public void idle(){
        List<ResponseCode> response = sf.idle();
        assertTrue(response.size() == 1);
        assertTrue(response.get(0) == ResponseCode.AccountIdled);
    }

    @Override
    public void close_acc(){
        List<ResponseCode> response = sf.closeAccount();
        assertTrue(response.size() == 1);
        assertTrue(response.get(0) == ResponseCode.AccountClosed);
    }

    @Override
    public void open_acc(){
        List<ResponseCode> response = sf.openAccount();
        assertTrue(response.size() == 1);
        assertTrue(response.get(0) == ResponseCode.AccountOpened);
    }

    @Override
    public void valid_trans(){
        List<ResponseCode> response = sf.trans("TM_CASH");
        assertTrue(response.size() == 1);
        assertTrue(response.get(0) == ResponseCode.TransactionSucceeded);
    }

    @Override
    public void valid_art_reg(){
        ValidArtId id = randomEnumValue(ValidArtId.class);
        List<ResponseCode> response = sf.artReg(id.value());
        assertTrue(response.size() == 2);
        assertTrue(response.get(0) == ResponseCode.AccountBalance);
        assertTrue(response.get(1) == ResponseCode.ArtRegistered);
    }

    @Override
    public void invalid_art_reg(){
        InvalidArtId id = randomEnumValue(InvalidArtId.class);
        List<ResponseCode> response = sf.artReg(id.value());
        assertTrue(response.size() == 1);
        assertTrue(response.get(0) == ResponseCode.NoSuchArticle);
    }

    @Override
    public void valid_get(){
        
    }

    @Override
    public void invalid_trans(){

    }

    @Override
    public void invalid_get(){

    }

    @Test
    public void StandardTest(){
    	new TestBuilder()
    		.setModel(MODEL_PATH)
    		.setContext(this)
    		.setPathGenerator(new RandomPath(new EdgeCoverage(97)))
    		.setStart("Disconnected")
            .execute();
    }














}