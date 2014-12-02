package util;

import java.util.ArrayList;

public class FIRE {

    private static FIRE instance = null;
    private final int m = 200;
    private final int MAX_RESPONSES = 10;

    public FIRE() {

    }

    public float calculateOmega(int posResponse) {
        return (posResponse + 1) / MAX_RESPONSES;
    }

    public ArrayList<Float> calculateOmegas(ArrayList<Float> values) {
        ArrayList<Float> result = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            result.add(values.get(i) * calculateOmega(i));
        }

        //System.out.println("OMEGAS TOTAL: " +);
        return result;
    }

    public static FIRE getInstance() {

        if (instance == null) {
            instance = new FIRE();
        }
        return instance;
    }

    public float calculateIT(ArrayList<Float> omegas, ArrayList<Float> values) {
        float result = 0f;
        if (omegas.size() != values.size()) {
            return -1;
        }
        for (int i = 0; i < omegas.size(); i++) {
            result += omegas.get(i) * values.get(i);
        }
        return result;
    }

    public float calculateWT(ArrayList<Float> omegas, ArrayList<Float> values) {
        float result = 0f;
        if (omegas.size() != values.size()) {
            return -1;
        }
        for (int i = 0; i < omegas.size(); i++) {
            result += omegas.get(i) * values.get(i);
        }
        return result;
    }

    public float calculatePN(int n) {
        float result;
        if (n <= m) {
            result = (float) n / m;
        } else {
            result = 1;
        }
        return result;
    }

    public float calculatePD(ArrayList<Float> omegas, ArrayList<Float> values, float ti) {
        float result = 0, res = 0;
        for (int i = 0; i < omegas.size(); i++) {
            res += (omegas.get(i) * Math.abs(values.get(i) - ti)) / 2;
        }
        result = 1 - res;
        return result;
    }

    public float calculatePTI(float pn, float pd) {
        return (float) pn * pd;
    }

    public float calculateRT(ArrayList<Rule> rules) {
        float result = 0, res1 = 0, res2 = 0;
        for (int i = 0; i < rules.size(); i++) {
            res1 += rules.get(i).getProb() * rules.get(i).getCert();
            res2 += rules.get(i).getProb();
        }
        result = res1 / res2;
        return result;
    }

    public float calculatePTW(float pn, float pd) {
        return (float) pn * pd;
    }

    public float calculateCRT(ArrayList<Float> ratings) {
        float result = 0, omega = 0;

        for (int i = 0; i < ratings.size(); i++) {
            omega = calculateOmega(i + 1);
            result += omega * ratings.get(i);
        }

        return result;
    }

    public float calculatePTCR(float pn, float pd) {
        return (float) pn * pd;
    }

    public float finalTrust(float it, float rt, float wt, float crt) {
        return (float) ((0.8 * it + 0.2 * rt + 0.5 * wt + 0.2 * crt) / (0.8 + 0.2 + 0.5 + 0.2));
    }

    public int getMAX_RESPONSES() {
        return MAX_RESPONSES;
    }
}
