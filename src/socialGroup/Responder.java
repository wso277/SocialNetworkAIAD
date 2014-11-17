package socialGroup;


import exceptions.WrongProbabilityValue;
import mainPackage.Main;
import uchicago.src.sim.engine.Stepable;
import util.Date;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    private ArrayList<Integer> alreadyAnswered;


    public Responder(float location, float price, float date, float musicType) throws WrongProbabilityValue {
        if (location < 0 || location > 1) {
            throw new WrongProbabilityValue("Wrong location probability: " + location);
        }
        if (price < 0 || price > 1) {
            throw new WrongProbabilityValue("Wrong price probability: " + price);
        }
        if (date < 0 || date > 1) {
            throw new WrongProbabilityValue("Wrong date probability: " + date);
        }
        if (musicType < 0 || musicType > 1) {
            throw new WrongProbabilityValue("Wrong musicType probability: " + musicType);
        }

        this.location = location;
        this.date = date;
        this.musicType = musicType;
        this.price = price;

    }

    public void step() {
        Stepable stepable = (Stepable) Main.socialModel.getAgentList().get(0);
        if(stepable instanceof Requester && alreadyAnswered.f){

        }
    }
}
