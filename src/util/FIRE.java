package util;

import com.sun.deploy.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FIRE {

    private static FIRE instance = null;
    private final int m = 200;
    private final int MAX_RESPONSES = 10;

    public FIRE() {

    }

    public ArrayList<Float> calculateOmegas(ArrayList<Float> values) {
        float step = 1.0f / values.size();
        float baseVal = step;

        ArrayList<Float> result = new ArrayList<>();
        ArrayList<Float> resultAux = new ArrayList<>();
        if (values.size() == 1) {
            result.add(1f);
        } else {

            if (values.size() % 2 == 0) {
                for (int i = 0; i < values.size() / 2; i++) {
                    result.add(baseVal * step);
                    step += baseVal;
                }
                step = 2f - baseVal;
                for (int i = values.size(); i > values.size() / 2; i--) {
                    resultAux.add(baseVal * step);
                    step -= baseVal;
                }
            } else {
                for (int i = 0; i < Math.ceil(values.size() / 2); i++) {
                    result.add(baseVal * step);
                    step += baseVal;
                }
                step = 2f - baseVal;
                result.add(1f);
                for (int i = values.size(); i > Math.ceil(values.size() / 2); i--) {
                    resultAux.add(baseVal * step);
                    step -= baseVal;
                }
            }

            Collections.reverse(resultAux);
            result.addAll(resultAux);
            Collections.sort(result);
        }

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
        float result = 0;
        ArrayList<Float> omegas = calculateOmegas(ratings);

        for (int i = 0; i < ratings.size(); i++) {
            result += omegas.get(i) * ratings.get(i);
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
