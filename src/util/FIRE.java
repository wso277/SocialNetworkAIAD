package util;

import java.util.ArrayList;

public class FIRE {

    private static FIRE instance = null;
    private final int m = 200;
    private final int MAX_RESPONSES = 10;

    public FIRE() {

    }

    private float calculateOmega(int posResponse) {
        return (posResponse + 1) / MAX_RESPONSES;
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

    private float calculateTW(ArrayList<Float> omegas, ArrayList<Float> values) {
        float result = 0f;
        if (omegas.size() != values.size()) {
            return -1;
        }
        for (int i = 0; i < omegas.size(); i++) {
            result += omegas.get(i) * values.get(i);
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

    private float calculateRT(ArrayList<Rule> rules) {
        float result = 0, res1 = 0, res2 = 0;
        for (int i = 0; i < rules.size(); i++) {
            res1 += rules.get(i).getProb() * rules.get(i).getCert();
            res2 += rules.get(i).getProb();
        }
        result = res1 / res2;
        return result;
    }

}
