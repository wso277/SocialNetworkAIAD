package socialGroup;


import exceptions.WrongProbabilityValue;
import mainPackage.Main;
import uchicago.src.sim.engine.Stepable;
import util.Date;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

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
        Random r = new Random();
        Stepable stepable;
        do {
            stepable = (Stepable) Main.getSocialModel().getAgentList().get(r.nextInt(Main.getSocialModel()
                    .getAgentList().size()));

        } while (!(stepable instanceof Requester));

        Requester req = (Requester) stepable;
        if (!alreadyAnswered.contains(req.getId())) {
            Response res = new Response();
            if (r.nextFloat() < location) {
                res.setLocation(req.getLocation());
            } else {
                res.setLocation(r.nextInt(Main.getSocialModel().MAX_DISTANCE) + req.getLocation());
            }

            if (r.nextFloat() < price) {
                res.setPrice(req.getPrice());
            } else {
                res.setPrice(r.nextInt(Main.getSocialModel().MAX_DISTANCE) + req.getPrice());
            }

            if (r.nextFloat() < date) {
                res.setDate(req.getDate2());
            } else {
                res.setDate(Date.getBiggerDate(req.getDate2()));
            }

            if (r.nextFloat() < musicType) {
                res.setMusicType(req.getMusicType());
            } else {
                if (Requester.MusicTypes.get(0) != req.getMusicType()) {
                    res.setMusicType(Requester.MusicTypes.get(0));
                } else {
                    res.setMusicType(Requester.MusicTypes.get(1));
                }
            }
            req.addResponse(res);
            alreadyAnswered.add(req.getId());
        }
    }
}
