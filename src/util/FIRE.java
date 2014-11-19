package util;

public class FIRE {

    private static FIRE instance = null;

    private float omega;


    public FIRE() {

    }

    private float calculateOmega(int x, float y, int nResponses) {
        omega = (nResponses * y - x) / nResponses;
        return omega;
    }

    public static FIRE getInstance() {

        if (instance == null) {
            instance = new FIRE();
        }
        return instance;
    }

    private float calculateTI() {

    }
}
