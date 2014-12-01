package util;

import uchicago.src.sim.engine.Stepable;

public class Rule {
    public String a;
    public String b;
    public String action;
    public float prob;
    public float cert;

    public Rule(String a, String b, String action, float prob, float cert) {
        this.a = a;
        this.b = b;
        this.action = action;
        this.prob = prob;
        this.cert = cert;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getAction() {
        return action;
    }

    public float getProb() {
        return prob;
    }

    public float getCert() {
        return cert;
    }
}
