package socialGroup;

import exceptions.WrongProbabilityValue;
import mainPackage.Main;
import uchicago.src.sim.engine.Stepable;
import util.Date;
import util.FIRE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Wilson on 04/11/2014.
 */
public class Responder implements Stepable {

    public static ArrayList<String> music = new ArrayList<String>() {{
        add("ROCK");
        add("CLASSIC");
        add("JAZZ");
        add("POP");
        add("HOUSE");
        add("FUNK");
        add("METAL");
        add("RNB");
        add("REGGAE");
        add("FADO");
        add("DRUMNBASS");
        add("BIGROOM");
    }};

    //Probability of answering correctly to the location parameter (value between 0 and 1
    private float location;
    //Probability of answering correctly to the price parameter (value between 0 and 1
    private float price;
    //Probability of answering with a date in the range specified (value between 0 and 1
    private float date;
    //Probability of answering correctly to the musicType parameter (value between 0 and 1
    private float musicType;
    private int id;
    private ArrayList<Float> locationRatings;
    private ArrayList<Float> priceRatings;
    private ArrayList<Float> dateRatings;
    private ArrayList<Float> musicTypeRatings;

    public Responder(float location, float price, float date, float musicType, int id) throws WrongProbabilityValue {
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
        this.id = id;
        locationRatings = new ArrayList<>(Collections.nCopies(FIRE.getInstance().getMAX_RESPONSES(), 0f));
        priceRatings = new ArrayList<>(Collections.nCopies(FIRE.getInstance().getMAX_RESPONSES(), 0f));
        dateRatings = new ArrayList<>(Collections.nCopies(FIRE.getInstance().getMAX_RESPONSES(), 0f));
        musicTypeRatings = new ArrayList<>(Collections.nCopies(FIRE.getInstance().getMAX_RESPONSES(), 0f));
    }

    public void step() {
        Random r = new Random();
        Stepable stepable;
        do {
            stepable = (Stepable) Main.getSocialModel().getAgentList().get(r.nextInt(Main.getSocialModel().getAgentList().size()));

        } while (!(stepable instanceof Requester));

        Requester req = (Requester) stepable;
        Response res = new Response();
        if (r.nextFloat() <= location) {
            res.setLocation(req.getLocation());
        } else {
            res.setLocation(r.nextInt(Main.getSocialModel().getMaxDistance()) + req.getLocation());
        }

        if (r.nextFloat() <= price) {
            res.setPrice(req.getPrice());
        } else {
            res.setPrice(r.nextInt(Main.getSocialModel().getMaxPrice()) + req.getPrice());
        }

        if (r.nextFloat() <= date) {
            res.setDate(req.getDateEnd());
        } else {
            res.setDate(Date.getBiggerDate(req.getDateEnd()));
        }

        if (r.nextFloat() <= musicType) {
            res.setMusicType(req.getMusicType());
        } else {
            res.setMusicType(music.get(r.nextInt(music.size())));
        }
        res.setId(this.id);
        req.addResponse(this.id, res);
    }

    public void isHigher(ArrayList<Float> array, float val) {
        Collections.sort(array);
        if (array.get(0) < val) {
            array.set(0, val);
        }

    }

    public void addLocationRating(float val) {
        this.isHigher(locationRatings, val);
    }

    public void addPriceRating(float val) {

        this.isHigher(priceRatings, val);
    }

    public void addDateRating(float val) {
        this.isHigher(dateRatings, val);
    }

    public void addMusicTypeRating(float val) {
        this.isHigher(musicTypeRatings, val);
    }

    public int getId() {
        return id;
    }

    public ArrayList<Float> getLocationRatings() {
        return locationRatings;
    }

    public ArrayList<Float> getPriceRatings() {
        return priceRatings;
    }

    public ArrayList<Float> getDateRatings() {
        return dateRatings;
    }

    public ArrayList<Float> getMusicTypeRatings() {
        return musicTypeRatings;
    }
}
