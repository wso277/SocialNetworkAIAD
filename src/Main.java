import uchicago.src.sim.engine.SimInit;

public class Main {

    private static final boolean BATCH_MODE = true;

    public static void main(String[] args) {
        boolean runMode = !BATCH_MODE;

        SimInit init = new SimInit();

        MyHelloWorldModel model = new MyHelloWorldModel();

        init.loadModel(model, null, runMode);
    }
}
