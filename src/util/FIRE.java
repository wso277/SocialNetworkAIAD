package util;

import java.util.ArrayList;

public class FIRE {

    private static FIRE instance = null;
    private final int m = 200;

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

    private float calculateTI(ArrayList<Float> omegas, ArrayList<Float> values) {
        float result = 0f;
        if (omegas.size() != values.size()) {
            return -1;
        }
        for (int i = 0; i < omegas.size(); i++) {
            result += omegas.get(i)*values.get(i);
        }
        return result;
    }

    private float calculatePN(int n) {
        float result;
        if (n <= m) {
            result = (float)n/m;
        } else {
            result = 1;
        }
        return result;
    }

    private float calculatePD(ArrayList<Float> omegas, ArrayList<Float> values, float ti) {
        float result = 0, res = 0;
        for (int i = 0; i < omegas.size(); i++) {
            res += (omegas.get(i)*Math.abs(values.get(i)-ti))/2;
        }
        result = 1 - res;
        return result;
    }

    private float calculatePTI(float pn, float pd) {
        float pti = pn*pd;
        return pti;
    }
}
