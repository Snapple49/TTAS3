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
import scanflow.adapter.ScanflowInterface;
import scanflow.adapter.ResponseCode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class ScanflowTest extends ExecutionContext implements Scanflow {
    public final static Path MODEL_PATH = Paths.get("org/myorg/testautomation/Scanflow.graphml");

    private ScanflowInterface sf;

    public ScanflowTest(){
    	this.sf = new ScanflowInterface();
    }

    @Override
    public void Signed(){
    	
    }

    @Override
    public void Not_Signed(){
    	
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
    public void sign_off(){
    	List<ResponseCode> response = sf.signOff();
    	assertTrue(response.size() == 1);
    	boolean valid = response.contains(ResponseCode.SignedOff);
    	assertTrue(valid);
    }

    @Test
    public void StandardTest(){
    	new TestBuilder()
    		.setModel(MODEL_PATH)
    		.setContext(new ScanflowTest())
    		.setPathGenerator(new RandomPath(new EdgeCoverage(100)))
    		.setStart("Not_Signed")
            .execute();
    }














}