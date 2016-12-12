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

import nz.ac.waikato.modeljunit.AbstractListener;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.Transition;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.CoverageMetric;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;

import scanflow.adapter.*;

/** Simple example of a finite state machine (FSM) for testing.
 */
public class TestModel implements FsmModel
{
	private State state = State.NOT_SIGNED;
	private ScanflowInterface i;
	private ResponseCode resp;
	private List<ResponseCode> resps;

	public TestModel()
	{
		i = new ScanflowInterface();
		i.signOff();
		state = State.NOT_SIGNED;
	}

	public void TearDown(){
		i.signOff();
	}
	
	public String getState()
	{
		return String.valueOf(state.ordinal());
	}

	public void reset(boolean testing)
	{
		resp = i.signOff().get(0);
		while(!resp.equals(ResponseCode.SignedOff)){
			resp = i.signOff().get(0);
		}
		state = State.NOT_SIGNED;			
	}

	public boolean signinGuard() { return true; }
	public @Action void signin(){
		//System.out.println("signin: " + state + " --> ");
		resp = i.signOn(8, "01").get(0);
		if(resp.equals(ResponseCode.SignedOn)){
			state = State.AS_IDLE;
		}

	}
	
	public boolean signoffGuard() { return true;}
	public @Action void signoff(){
		resp = i.signOff().get(0);
		if(resp.equals(ResponseCode.SignedOff)){
			state = State.NOT_SIGNED;
		}
	}
	
	public boolean actionNoneGuard(){ return true; }
	public @Action void actionNone(){
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
		Tester tester = new RandomTester(new TestModel());

		// build the complete FSM graph for our model, just to ensure
		// that we get accurate model coverage metrics.
		tester.buildGraph();

		// set up our favourite coverage metric
		CoverageMetric trCoverage = new TransitionCoverage();
		tester.addCoverageMetric(trCoverage);

		// ask to print the generated tests
		tester.addListener("verbose", new VerboseListener(tester.getModel()));

		// generate a small test suite of 20 steps (covers 4/5 transitions)
		tester.generate(10);

		tester.getModel().printMessage(trCoverage.getName() + " was "
				+ trCoverage.toString());
		
	}
}