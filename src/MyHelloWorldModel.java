import uchicago.src.sim.engine.SimpleModel;

public class MyHelloWorldModel extends SimpleModel {
    private int numberOfAgents;

    public MyHelloWorldModel() {
        name = "My Hello World Model";
    }

    public void setup() {
        super.setup();
        numberOfAgents = 3;
        autoStep = true;
        shuffle = true;
    }

    public void buildModel() {
        for (int i = 0; i < numberOfAgents; i++) {
            agentList.add(new MyHelloWorldAgent(i));
        }
    }

    protected void preStep() {
        System.out.println("Initiating step " + getTickCount());
    }

    protected void postStep() {
        System.out.println("Done step " + getTickCount());
    }
}