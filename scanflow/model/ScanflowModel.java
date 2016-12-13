package scanflow.model;
/**
Copyright (C) 2006 Mark Utting
This file is part of the CZT project.

The CZT project contains free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

The CZT project is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CZT; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.util.List;

import junit.framework.Assert;
import nz.ac.waikato.modeljunit.AbstractListener;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.Transition;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.CoverageMetric;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;

import scanflow.adapter.ResponseCode;
import scanflow.adapter.ScanflowInterface;

/** Simple example of a finite state machine (FSM) for testing.
 */
public class ScanflowModel implements FsmModel
{
	private State state;
	private ScanflowInterface i;
	private ResponseCode resp;
	private List<ResponseCode> resps;
	private boolean isSignedIn;
	private State prevState;
	

	public ScanflowModel()
	{
		isSignedIn = false;
		i = new ScanflowInterface();
		if(i.interfaceIsConnected()){
			state = State.NOT_SIGNED;
			prevState = state;
		}else{
			System.out.println("Something went wrong connecting, exiting!");
			System.exit(1);
		}
	}
	
	public String getState()
	{
		return String.valueOf(state);
	}

	public void reset(boolean testing)
	{
		i.signOff();
		state = State.NOT_SIGNED;
		isSignedIn = false;
	}

	public boolean signinGuard() { return state.equals(State.NOT_SIGNED); }
	public @Action void signin(){
		//System.out.println("signin: " + state + " --> ");
		resps = i.signOn(8, "01");
		if(resps.contains(ResponseCode.SignedOn)){
			state = State.AS_IDLE;
		}
		
		System.out.println(resp);
	}
	
	public boolean signOffGuard() { return false;}
	public @Action void signOff(){
		prevState = state;
		if(isSignedIn){
			resp = i.signOff().get(0);
			if(resp.equals(ResponseCode.SignedOff)){
				isSignedIn = false;
				state = State.NOT_SIGNED;
			}
		}
		System.out.println(resp);
	}
	
	public boolean openGuard() { return state.equals(State.AS_IDLE); }
	public @Action void open(){
		prevState = state;
		resp = i.openAccount().get(0);
		if(resp.equals(ResponseCode.AccountOpened)){
			state = State.AS_OPEN;				
		}
		System.out.println(resp);
	}
	
	public boolean artRegGuard() { return false; }
	public @Action void artReg(){
		
	}
	
	public boolean closeGuard() { return state.equals(State.AS_OPEN); }
	public @Action void close(){
		prevState = state;
		resps = i.closeAccount();
		if(resps.contains(ResponseCode.AccountClosed)){
			state = State.AS_CLOSED;
		}
		System.out.println(resp);
	}
	
	public boolean transGuard() { return state.equals(State.AS_CLOSED); }
	public @Action void trans(){
		prevState = state;
		resps = i.trans("TM_BANK");
		if(resps.contains(ResponseCode.TransactionSucceeded)){
			state = State.AS_ENDING;
		}
		System.out.println(resp);
	}
	
	public boolean idleGuard() { return state.equals(State.AS_ENDING); }
	public @Action void idle(){
		prevState = state;
		resp = i.idle().get(0);
		if(resp.equals(ResponseCode.AccountIdled)){
			state = State.AS_IDLE;
		}
		
	}
	
	
	
	public boolean actionNoneGuard(){ return false; }
	public @Action void actionNone(){
		System.out.println("Doing nothing");
	}
	

	/** This main method illustrates how we can use ModelJUnit
	 *  to generate a small test suite.
	 *  If we had an implementation of this model that we wanted
	 *  to test, we would extend each of the above methods so that
	 *  they called the methods of the implementation and checked
	 *  the results of those methods.
	 *
	 *  We also report the transition coverage of the model.
	 */
	public static void main(String args[]){
		
		// create our model and a test generation algorithm
		RandomTester rTester = new RandomTester(new ScanflowModel());

		rTester.setResetProbability(0);
		
		Tester tester = rTester;
		
		// build the complete FSM graph for our model, just to ensure
		// that we get accurate model coverage metrics.
		tester.buildGraph();

		// set up our favourite coverage metric
		CoverageMetric trCoverage = new TransitionCoverage();
		tester.addCoverageMetric(trCoverage);

		// ask to print the generated tests
		tester.addListener("verbose", new VerboseListener(tester.getModel()));

		// generate a small test suite of 20 steps (covers 4/5 transitions)
		tester.generate(100);
		

		tester.getModel().printMessage(trCoverage.getName() + " was "
				+ trCoverage.toString());
		
	}
}