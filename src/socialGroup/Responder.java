package socialGroup;

import uchicago.src.sim.engine.Stepable;
import util.Date;

/**
 * Created by Wilson on 04/11/2014.
 */
public class Responder implements Stepable {

    //Probability of answering correctly to the location parameter (value between 0 and 1
    private float location;
    //Probability of answering correctly to the price parameter (value between 0 and 1
    private float price;
    //Probability of answering with a date in the range specified (value between 0 and 1
    private float date;
    //Probability of answering correctly to the musicType parameter (value between 0 and 1
    private float musicType;

    public Responder(float location, float price, float date, float musicType) {

    }

    public void step() {
    }
}
