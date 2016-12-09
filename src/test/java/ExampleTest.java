import org.graphwalker.core.condition.VertexCoverage;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.*;
import org.graphwalker.core.model.*;
import org.junit.Assert;
import org.junit.Test;


import static org.hamcrest.core.Is.is;

public class ExampleTest extends ExecutionContext {
	boolean signedIn = false;

    public void NOT_SIGNED() {
        System.out.println("I'm not signed in");
    }

    public void signin() {
        System.out.println("signing in");
    }

    public void SIGNED_IN() {
        System.out.println("I'm signed in!");
    }

    public void vertex3() {
        throw new RuntimeException();
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isTrue() {
        return true;
    }

    public void myAction() {
		signedIn = true;
		TestStuff.LoL();
        System.out.println("Action called");
    }

    @Test
    public void success() {
        Vertex start = new Vertex();
        Model model = new Model().addEdge(new Edge()
                .setName("signin")
                .setGuard(new Guard("isTrue()"))
                .setSourceVertex(start
                        .setName("NOT_SIGNED"))
                .setTargetVertex(new Vertex()
                        .setName("SIGNED_IN"))
                .addAction(new Action("myAction();")));
        this.setModel(model.build());
        this.setPathGenerator(new RandomPath(new VertexCoverage(100)));
        setNextElement(start);
        Machine machine = new SimpleMachine(this);
        while (machine.hasNextStep()) {
            machine.getNextStep();
        }
    }
/*
    @Test(expected = MachineException.class)
    public void failure() {
        Vertex start = new Vertex();
        Model model = new Model().addEdge(new Edge()
                .setName("edge1")
                .setGuard(new Guard("isFalse()"))
                .setSourceVertex(start
                        .setName("vertex1"))
                .setTargetVertex(new Vertex()
                        .setName("vertex2")));
        this.setModel(model.build());
        this.setPathGenerator(new RandomPath(new VertexCoverage(100)));
        setNextElement(start);
        Machine machine = new SimpleMachine(this);
        while (machine.hasNextStep()) {
            machine.getNextStep();
        }
    }

    @Test
    public void exception() {
        Vertex start = new Vertex();
        Model model = new Model().addEdge(new Edge()
                .setName("edge1")
                .setGuard(new Guard("isTrue()"))
                .setSourceVertex(start
                        .setName("vertex3"))
                .setTargetVertex(new Vertex()
                        .setName("vertex2")));
        this.setModel(model.build());
        this.setPathGenerator(new RandomPath(new VertexCoverage(100)));
        setNextElement(start);
        Machine machine = new SimpleMachine(this);
        Assert.assertThat(getExecutionStatus(), is(ExecutionStatus.NOT_EXECUTED));
        try {
            while (machine.hasNextStep()) {
                machine.getNextStep();
                Assert.assertThat(getExecutionStatus(), is(ExecutionStatus.EXECUTING));
            }
        } catch (Throwable t) {
            Assert.assertTrue(MachineException.class.isAssignableFrom(t.getClass()));
            Assert.assertThat(getExecutionStatus(), is(ExecutionStatus.FAILED));
        }
    }*/
}

