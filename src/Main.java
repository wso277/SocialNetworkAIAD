import socialGroup.SocialGroupModel;
import uchicago.src.sim.engine.SimInit;

public class Main {

    private static final boolean BATCH_MODE = true;

    public static void main(String[] args) {
        boolean runMode = !BATCH_MODE;

        SimInit init = new SimInit();

        SocialGroupModel socialModel = new SocialGroupModel("Social Group Model", 20);

        init.loadModel(socialModel, null, runMode);
    }
}
