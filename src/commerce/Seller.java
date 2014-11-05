package commerce;

import uchicago.src.sim.engine.Stepable;

public class Seller implements Stepable {

    //Probability of answering correctly to the location parameter (value between 0 and 1
    private float shippingDelay;
    private float brokenPackage;
    private float relationWithBuyer;
    private float sellerFeedback;
    private float numRatings;

    public void step() {
    }
}
