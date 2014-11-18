package mainPackage;

import socialGroup.SocialGroupModel;
import uchicago.src.sim.engine.SimInit;

import java.util.HashMap;

public class Main {

    private static final boolean BATCH_MODE = true;
    private static final int MAX_NUMBER_AGENTS = 30;
    private static SocialGroupModel socialModel;

    public static void main(String[] args) {

        boolean runMode = !BATCH_MODE;

        SimInit init = new SimInit();
        socialModel = new SocialGroupModel("Social Group Model", MAX_NUMBER_AGENTS);
        init.loadModel(socialModel, null, runMode);
    }

    public static SocialGroupModel getSocialModel() {
        return socialModel;
    }
}
