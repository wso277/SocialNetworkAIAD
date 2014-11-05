package commerce;

import uchicago.src.sim.engine.Stepable;

public class Buyer implements Stepable {

    //Probability of answering correctly to the location parameter (value between 0 and 1
    private float relationWithSeller;
    private float sellerProximity;
    private float badPayment;
    private float wrongDelivery;
    private float wrongFeedback;

    public void step() {
    }
}
