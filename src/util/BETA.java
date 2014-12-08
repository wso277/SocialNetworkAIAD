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

    public void calculateRS(float rating, ArrayList<Float> rList, ArrayList<Float> sList) {

        float r = (1 - rating) / 2.0f;
        float s = (1 + rating) / 2.0f;
        rList.add(r);
        sList.add(s);
    }

    private float weightedSum(ArrayList<Float> values, int currentStep, ArrayList<Integer> steps) {
        float result = 0;

        for (int i = 0; i < values.size(); i++) {
            result += values.get(i) * Math.pow(FORGETTING_VALUE, currentStep - steps.get(i));
        }

        return result;
    }

    public float calculateTrust(ArrayList<Float> rValues, ArrayList<Float> sValues, int currentStep, ArrayList<Integer> steps) {
        float result = 0;

        float rSum = weightedSum(rValues, currentStep, steps);
        float sSum = weightedSum(sValues, currentStep, steps);

        result = (sSum - rSum) / (sSum + rSum + 2);

        return result;
    }
}
