package util;

import java.util.ArrayList;

public class BETA {

    public static BETA instance = null;
    private final double FORGETTING_VALUE = 0.8;

    public BETA() {

    }

    public static BETA getInstance() {

        if (instance == null) {
            instance = new BETA();
        }
        return instance;
    }


    private ArrayList<Float> calculateRS (float rating) {
        ArrayList<Float> result = new ArrayList<Float>();

        float r = (1 - rating) / 2.0f;
        float s = (1 + rating) / 2.0f;
        result.add(r);
        result.add(s);

        return result;
    }

    private float weightedSum(ArrayList<Float> values, int currentStep, ArrayList<Integer> steps) {
        float result = 0;

        for (int i = 0; i < values.size(); i++) {
            result += values.get(i) * Math.pow(FORGETTING_VALUE, currentStep - steps.get(i));
        }

        return result;
    }

    private float calculateTrust (ArrayList<Float> rValues, ArrayList<Float> sValues, int currentStep, ArrayList<Integer> steps) {
        float result = 0;

        float rSum = weightedSum(rValues, currentStep, steps);
        float sSum = weightedSum(sValues, currentStep, steps);

        result = (sSum - rSum) / (sSum + rSum + 2);

        return result;
    }
}
