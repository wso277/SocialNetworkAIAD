package socialGroup;

import exceptions.WrongDateException;
import mainPackage.Main;
import uchicago.src.sim.engine.Stepable;
import util.BETA;
import util.Date;
import util.FIRE;
import util.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Requester implements Stepable {

    private static final ArrayList<Rule> RULES = new ArrayList<Rule>() {{
        add(new Rule("requester", "responder", "location", -0.3f, 0.4f));
        add(new Rule("requester", "responder", "price", -0.1f, 0.5f));
        add(new Rule("requester", "responder", "date", 0.7f, 0.6f));
        add(new Rule("requester", "responder", "musicType", 0.9f, 0.3f));
    }};

    public static HashMap<String, String> MusicTypes;/* = new ArrayList<String>() {{
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
    }};*/

    //Distance the requester wants to travel
    private int location;
    private int price;
    //first date available for concert
    private Date dateStart;
    //last date available for concert
    private Date dateEnd;
    private String musicType;
    private int id;
    private Requester neighbour;
    private HashMap<Integer, ArrayList<Response>> responses;
    private float locationValue, priceValue, dateValue, musicValue, deltaLocation, deltaPrice, yyLocation, yyPrice;
    private HashMap<Integer, ArrayList<Float>> ratings = new HashMap<>();
    private HashMap<Integer, ArrayList<Float>> rLocation, rPrice, rDate, rMusic, sLocation, sPrice, sDate, sMusic;

    public Requester(int location, int price, Date date1, Date date2, String musicType, int id) throws WrongDateException {
        this.location = location;
        this.price = price;
        if (!date1.isEarlier(date2)) {
            throw new WrongDateException("Second date is earlier than first date!");
        }
        this.dateStart = date1;
        this.dateEnd = date2;
        this.musicType = musicType;
        this.id = id;
        responses = new HashMap<>();

        deltaLocation = (float) 2.0 / (-Main.getSocialModel().getMaxDistance());
        yyLocation = (float) (-Main.getSocialModel().getMaxDistance() - 2.0 * location) / (-Main.getSocialModel().getMaxDistance());

        deltaPrice = (float) 2.0 / (-Main.getSocialModel().getMaxPrice());
        yyPrice = (float) (-Main.getSocialModel().getMaxPrice() - 2.0 * price) / (-Main.getSocialModel().getMaxPrice());

        rLocation = new HashMap<>();
        rPrice = new HashMap<>();
        rDate = new HashMap<>();
        rMusic = new HashMap<>();
        sLocation = new HashMap<>();
        sPrice = new HashMap<>();
        sDate = new HashMap<>();
        sMusic = new HashMap<>();

        MusicTypes = new HashMap<>();

        MusicTypes.put("ROCK", "ROCK");
        MusicTypes.put("METAL", "ROCK");
        MusicTypes.put("POP", "ROCK");
        MusicTypes.put("CLASSIC", "CLASSIC");
        MusicTypes.put("JAZZ", "CLASSIC");
        MusicTypes.put("FADO", "CLASSIC");
        MusicTypes.put("RNB", "RNB");
        MusicTypes.put("REGGAE", "RNB");
        MusicTypes.put("FUNK", "RNB");
        MusicTypes.put("HOUSE", "HOUSE");
        MusicTypes.put("DRUMNBASS", "HOUSE");
        MusicTypes.put("BIGROOM", "HOUSE");
    }

    public void step() {

        System.out.println("REQUESTER NUMBER " + id + " -----------");

        ArrayList<Requester> reqs = new ArrayList<>();
        ArrayList<Responder> resps = new ArrayList<>();
        int position = 0;

        position = separateAgents(reqs, resps, position);

        ArrayList<Float> locationRatings = new ArrayList<>();
        ArrayList<Float> priceRatings = new ArrayList<>();
        ArrayList<Float> dateRatings = new ArrayList<>();
        ArrayList<Float> musicRatings = new ArrayList<>();

        Iterator it = responses.entrySet().iterator();

        float crLocationTrust = 0, crPriceTrust = 0, crDateTrust = 0, crMusicTrust = 0;

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            ArrayList<Response> res = (ArrayList) pairs.getValue();

            Responder resp = getResponder(resps, (int) pairs.getKey());

            int nResponses = -1;
            for (int i = 0; i < res.size(); i++) {
                crLocationTrust = FIRE.getInstance().calculateCRT(resp.getLocationRatings());
                crPriceTrust = FIRE.getInstance().calculateCRT(resp.getPriceRatings());
                crDateTrust = FIRE.getInstance().calculateCRT(resp.getDateRatings());
                crMusicTrust = FIRE.getInstance().calculateCRT(resp.getMusicTypeRatings());

                if (!res.get(i).isProcessed()) {
                    calculateRatings(res, resp, i);
                }
                nResponses = i;
            }

            if (nResponses >= 0) {

                ArrayList<Float> respRatings = ratings.get(resp.getId());

                locationRatings.clear();
                priceRatings.clear();
                dateRatings.clear();
                musicRatings.clear();

                calculateFireTrust(reqs, position, locationRatings, priceRatings, dateRatings, musicRatings, crLocationTrust, crPriceTrust, crDateTrust, crMusicTrust, pairs, resp, respRatings, nResponses + 1);

                locationRatings.clear();
                priceRatings.clear();
                dateRatings.clear();
                musicRatings.clear();
                getSeparateRatings(locationRatings, priceRatings, dateRatings, musicRatings, respRatings);
                //System.out.println("SIZE BEFORE BETA: " + locationRatings.size());
                float betaTrust = calculateBetaTrust((int) pairs.getKey(), locationRatings, priceRatings, dateRatings, musicRatings);

                for (AgentNode node : SocialGroupModel.drawableAgents) {
                    if (node.getType().equals("Responder") && node.getAgentId() == resp.getId()) {
                        for (AgentNode myNode : SocialGroupModel.drawableAgents) {
                            if (myNode.getType().equals("Requester") && myNode.getAgentId() == id) {
                                node.makeEdgeTo(myNode, betaTrust);
                                System.out.println("FROM " + Integer.toString(node.getAgentId()) + " TO " + Integer.toString(myNode.getAgentId()));
                                SocialGroupModel.surface.updateDisplay();
                            }
                        }
                    }
                }
            }

            locationRatings.clear();
            priceRatings.clear();
            dateRatings.clear();
            musicRatings.clear();
        }

        //it.remove(); // avoids a ConcurrentModificationException
    }

    private float calculateBetaTrust(int id, ArrayList<Float> locationRatings, ArrayList<Float> priceRatings, ArrayList<Float> dateRatings, ArrayList<Float> musicRatings) {
        ArrayList<Float> locationR, priceR, dateR, musicR, locationS, priceS, dateS, musicS;
        locationR = rLocation.get(id);
        priceR = rPrice.get(id);
        dateR = rDate.get(id);
        musicR = rMusic.get(id);
        locationS = sLocation.get(id);
        priceS = sPrice.get(id);
        dateS = sDate.get(id);
        musicS = sMusic.get(id);

        if (locationR == null) {
            locationR = new ArrayList<>();
            priceR = new ArrayList<>();
            dateR = new ArrayList<>();
            musicR = new ArrayList<>();
            locationS = new ArrayList<>();
            priceS = new ArrayList<>();
            dateS = new ArrayList<>();
            musicS = new ArrayList<>();
        }

        if (locationRatings.size() > locationR.size()) {

            BETA.getInstance().calculateRS(locationRatings.get(locationRatings.size() - 1), locationR, locationS);
            BETA.getInstance().calculateRS(priceRatings.get(priceRatings.size() - 1), priceR, priceS);
            BETA.getInstance().calculateRS(dateRatings.get(dateRatings.size() - 1), dateR, dateS);
            BETA.getInstance().calculateRS(musicRatings.get(musicRatings.size() - 1), musicR, musicS);
        }

        rLocation.put(id, locationR);
        rPrice.put(id, priceR);
        rDate.put(id, dateR);
        rMusic.put(id, musicR);
        sLocation.put(id, locationS);
        sPrice.put(id, priceS);
        sDate.put(id, dateS);
        sMusic.put(id, musicS);

        ArrayList<Integer> steps = new ArrayList<>();
        for (int i = 0; i < locationRatings.size(); i++) {
            steps.add(i);
        }

        //System.out.println("SIZE IN BETA " + locationRatings.size());
        //System.out.println("LOCATION RATINGS SIZE: " + locationRatings.size() + " LOCATIONR SIZE: " + locationR.size());
        float locationBeta = BETA.getInstance().calculateTrust(locationR, locationS, locationRatings.size(), steps);
        float priceBeta = BETA.getInstance().calculateTrust(priceR, priceS, priceRatings.size(), steps);
        float dateBeta = BETA.getInstance().calculateTrust(dateR, dateS, dateRatings.size(), steps);
        float musicBeta = BETA.getInstance().calculateTrust(musicR, musicS, musicRatings.size(), steps);

        float finalBetaTrust = (0.2f * locationBeta + 0.4f * priceBeta + 0.6f * dateBeta + 0.8f * musicBeta) / (2.0f);

        System.out.println("FINAL BETA TRUST: " + finalBetaTrust);

        return finalBetaTrust;

    }

    private void calculateFireTrust(ArrayList<Requester> reqs, int position, ArrayList<Float> locationRatings, ArrayList<Float> priceRatings, ArrayList<Float> dateRatings, ArrayList<Float> musicRatings, float crLocationTrust, float crPriceTrust, float crDateTrust, float crMusicTrust, Map.Entry pairs, Responder resp, ArrayList<Float> respRatings, int nResponses) {
        float interactionLocalTrust;
        float interactionPriceTrust;
        float interactionDateTrust;
        float interactionMusicTrust;
        float roleLocalTrust;
        float rolePriceTrust;
        float roleDateTrust;
        float roleMusicTrust;
        float witnessLocalTrust;
        float witnessPriceTrust;
        float witnessDateTrust;
        float witnessMusicTrust;

        float pnCR = FIRE.getInstance().calculatePN(resp.getLocationRatings().size());
        float reliabilityCRLocation = FIRE.getInstance().calculatePTCR(pnCR, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(resp.getLocationRatings()), resp.getLocationRatings(), crLocationTrust));
        float reliabilityCRPrice = FIRE.getInstance().calculatePTCR(pnCR, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(resp.getPriceRatings()), resp.getPriceRatings(), crPriceTrust));
        float reliabilityCRDate = FIRE.getInstance().calculatePTCR(pnCR, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(resp.getDateRatings()), resp.getDateRatings(), crDateTrust));
        float reliabilityCRMusic = FIRE.getInstance().calculatePTCR(pnCR, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(resp.getMusicTypeRatings()), resp.getMusicTypeRatings(), crMusicTrust));

        getSeparateRatings(locationRatings, priceRatings, dateRatings, musicRatings, respRatings);
        /*System.out.println("ID RESPONDER: " + (int) pairs.getKey() + " ID REQUESTER: " + id);
        System.out.println("RESPRATINGS: " + respRatings);
        System.out.println("LOCATIONRATINGS: " + locationRatings);
        System.out.println("\n\n");
        //System.out.println("LOCATION RATINGS SIZE SEPARATE : " + locationRatings.size() + " ID REQUESTER: " + id + " ID RESPONDER: " + (int) pairs.getKey());
        //System.out.println("SIZE IN TRUST: " + locationRatings.size());
        /*System.out.println("---------------------------------------");
        System.out.println("LOCATIONRATINGS: " + locationRatings);
        System.out.println("PRICERATINGS: " + priceRatings);
        System.out.println("DATERATINGS: " + dateRatings);
        System.out.println("MUSICRATINGS: " + musicRatings);
        System.out.println("---------------------------------------");*/

        interactionLocalTrust = FIRE.getInstance().calculateIT(FIRE.getInstance().calculateOmegas(locationRatings), locationRatings);
        interactionPriceTrust = FIRE.getInstance().calculateIT(FIRE.getInstance().calculateOmegas(priceRatings), priceRatings);
        interactionDateTrust = FIRE.getInstance().calculateIT(FIRE.getInstance().calculateOmegas(dateRatings), dateRatings);
        interactionMusicTrust = FIRE.getInstance().calculateIT(FIRE.getInstance().calculateOmegas(musicRatings), musicRatings);

        float pnIT = FIRE.getInstance().calculatePN(nResponses);
        float reliabilityITLocation = FIRE.getInstance().calculatePTI(pnIT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(locationRatings), locationRatings, interactionLocalTrust));
        float reliabilityITPrice = FIRE.getInstance().calculatePTI(pnIT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(priceRatings), priceRatings, interactionPriceTrust));
        float reliabilityITDate = FIRE.getInstance().calculatePTI(pnIT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(dateRatings), dateRatings, interactionDateTrust));
        float reliabilityITMusic = FIRE.getInstance().calculatePTI(pnIT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(musicRatings), musicRatings, interactionMusicTrust));

        /*System.out.println("---------------------------------------");
        System.out.println("LOCATIONTRUST: " + interactionLocalTrust);
        System.out.println("PRICETRUST: " + interactionPriceTrust);
        System.out.println("DATETRUST: " + interactionDateTrust);
        System.out.println("MUSICTRUST: " + interactionMusicTrust);
        System.out.println("---------------------------------------");*/

        roleLocalTrust = FIRE.getInstance().calculateRT(new ArrayList<Rule>() {{
            add(RULES.get(0));
        }});
        rolePriceTrust = FIRE.getInstance().calculateRT(new ArrayList<Rule>() {{
            add(RULES.get(1));
        }});
        roleDateTrust = FIRE.getInstance().calculateRT(new ArrayList<Rule>() {{
            add(RULES.get(2));
        }});
        roleMusicTrust = FIRE.getInstance().calculateRT(new ArrayList<Rule>() {{
            add(RULES.get(3));
        }});

        locationRatings.clear();
        priceRatings.clear();
        dateRatings.clear();
        musicRatings.clear();

        ArrayList<Float> rates = neighbour.getNeighbourRatings((Integer) pairs.getKey(), this.id);
        if (rates != null) {

            for (int j = 0; j < rates.size(); j++) {
                locationRatings.add(rates.get(j++));
                priceRatings.add(rates.get(j++));
                dateRatings.add(rates.get(j++));
                musicRatings.add(rates.get(j));
            }
        }

        //System.out.println("SIZE AFTER WITNESS: " + locationRatings.size());

        witnessLocalTrust = FIRE.getInstance().calculateWT(FIRE.getInstance().calculateOmegas(locationRatings), locationRatings);
        witnessPriceTrust = FIRE.getInstance().calculateWT(FIRE.getInstance().calculateOmegas(locationRatings), priceRatings);
        witnessDateTrust = FIRE.getInstance().calculateWT(FIRE.getInstance().calculateOmegas(locationRatings), dateRatings);
        witnessMusicTrust = FIRE.getInstance().calculateWT(FIRE.getInstance().calculateOmegas(locationRatings), musicRatings);

        float pnWT = FIRE.getInstance().calculatePN(locationRatings.size());
        float reliabilityWTLocation = FIRE.getInstance().calculatePTW(pnWT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(locationRatings), locationRatings, witnessLocalTrust));
        float reliabilityWTPrice = FIRE.getInstance().calculatePTW(pnWT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(priceRatings), priceRatings, witnessPriceTrust));
        float reliabilityWTDate = FIRE.getInstance().calculatePTW(pnWT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(dateRatings), dateRatings, witnessDateTrust));
        float reliabilityWTMusic = FIRE.getInstance().calculatePTW(pnWT, FIRE.getInstance().calculatePD(FIRE.getInstance().calculateOmegas(musicRatings), musicRatings, witnessMusicTrust));

                /*System.out.println("---------------------------------------");
                System.out.println("INTERACTION : " + interactionLocalTrust);
                System.out.println("ROLE: " + roleLocalTrust);
                System.out.println("WITNESS : " + witnessLocalTrust);
                System.out.println("CR : " + crLocationTrust);
                System.out.println("---------------------------------------");*/

        float localFinalTrust = FIRE.getInstance().finalTrust(interactionLocalTrust, roleLocalTrust, witnessLocalTrust, crLocationTrust, reliabilityITLocation, 0.5f, reliabilityWTLocation, reliabilityCRLocation);

        float priceFinalTrust = FIRE.getInstance().finalTrust(interactionPriceTrust, rolePriceTrust, witnessPriceTrust, crPriceTrust, reliabilityITPrice, 0.5f, reliabilityWTPrice, reliabilityCRPrice);
        float dateFinalTrust = FIRE.getInstance().finalTrust(interactionDateTrust, roleDateTrust, witnessDateTrust, crDateTrust, reliabilityITDate, 0.5f, reliabilityWTDate, reliabilityCRDate);

        float musicFinalTrust = FIRE.getInstance().finalTrust(interactionMusicTrust, roleMusicTrust, witnessMusicTrust, crMusicTrust, reliabilityITMusic, 0.5f, reliabilityWTMusic, reliabilityCRMusic);

        float finalTrust = (0.2f * localFinalTrust + 0.4f * priceFinalTrust + 0.6f * dateFinalTrust + 0.8f * musicFinalTrust) / (2.0f);


               /* System.out.println("TRUST-------------------");
                System.out.println("LOCAL TRUST: " + localFinalTrust);
                System.out.println("PRICE TRUST: " + priceFinalTrust);
                System.out.println("DATE TRUST: " + dateFinalTrust);
                System.out.println("MUSIC TRUST: " + musicFinalTrust);*/
        System.out.println("FINAL FIRE TRUST IN RESPONDER " + resp.getId() + ": " + finalTrust);

        //System.out.println("---------------------------------------");
    }

    private void getSeparateRatings(ArrayList<Float> locationRatings, ArrayList<Float> priceRatings, ArrayList<Float> dateRatings, ArrayList<Float> musicRatings, ArrayList<Float> respRatings) {
        //System.out.println("RESP RATINGS SIZE: " + respRatings.size());
        //System.out.println(("RESP RATINGS /4: " + respRatings.size()/4));
        for (int j = 0; j < respRatings.size(); j++) {
            locationRatings.add(respRatings.get(j++));
            priceRatings.add(respRatings.get(j++));
            dateRatings.add(respRatings.get(j++));
            musicRatings.add(respRatings.get(j));
        }
        //System.out.println("LOCATION RATINGS SIZE SEPARATE : " + locationRatings.size() + " ID: " + id);
    }

    private void calculateRatings(ArrayList<Response> res, Responder resp, int i) {
        if (res.get(i).getLocation() <= location) {
            locationValue = 1;
        } else if (res.get(i).getLocation() >= (location + Main.getSocialModel().getMaxDistance())) {
            locationValue = -1;
        } else {
            locationValue = deltaLocation * res.get(i).getLocation() + yyLocation;
        }

        if (res.get(i).getPrice() <= price) {
            priceValue = 1;
        } else if (res.get(i).getPrice() >= (price + Main.getSocialModel().getMaxDistance())) {
            priceValue = -1;
        } else {
            priceValue = deltaPrice * res.get(i).getPrice() + yyPrice;
        }

        if (res.get(i).getDate().isEarlier(dateEnd)) {
            dateValue = 1;
        } else {
            dateValue = -1;
        }

        if (res.get(i).getMusicType().equals(musicType)) {
            musicValue = 1;
        } else if (MusicTypes.get(res.get(i).getMusicType()).equals(MusicTypes.get(musicType))) {
            musicValue = 0.5f;
        } else {
            musicValue = -1;
        }

                /*System.out.println("---------------------------------------");
                System.out.println("LOCATION: "  + locationValue);
                System.out.println("PRICE: "  + priceValue);
                System.out.println("DATE: "  + dateValue);
                System.out.println("MUSIC: "  + musicValue);
                System.out.println("---------------------------------------");*/

        ArrayList<Float> rate = getRatings(res.get(i).getId());
        if (rate == null) {
            rate = new ArrayList<>();
        }

        rate.add(locationValue);
        rate.add(priceValue);
        rate.add(dateValue);
        rate.add(musicValue);

        /*System.out.println("---------------------------------------");
        System.out.println("ID: " + res.get(i).getId());
        System.out.println("RATINGS: " + rate);
        System.out.println("---------------------------------------");*/

        ratings.put(res.get(i).getId(), rate);
        res.get(i).setProcessed();

        resp.addLocationRating(locationValue);
        resp.addPriceRating(priceValue);
        resp.addDateRating(dateValue);
        resp.addMusicTypeRating(musicValue);
    }

    private int separateAgents(ArrayList<Requester> reqs, ArrayList<Responder> resps, int position) {
        for (int i = 0; i < Main.getSocialModel().getAgentList().size(); i++) {
            Stepable step = (Stepable) Main.getSocialModel().getAgentList().get(i);

            if (step instanceof Requester) {
                if (((Requester) step).getId() == id) {
                    position = reqs.size();
                }
                reqs.add((Requester) step);
            } else {
                resps.add((Responder) step);
            }
        }
        return position;
    }

    private ArrayList getResponsesFromId(int id) {
        return responses.get(id);
    }

    public ArrayList<Float> getRatings(int id) {
        return ratings.get(id);
    }

    public ArrayList<Float> getNeighbourRatings(int resId, int id) {
        ArrayList<Float> res = neighbour.getRatings(resId);

        if (res == null && neighbour.getNeighbour().getId() != id) {
            res = neighbour.getNeighbourRatings(resId, id);
        }

        return res;
    }

    public void addResponse(int resId, Response res) {
        ArrayList<Response> resp = responses.get(resId);
        //System.out.println("RESPONDER: " + resId + " REQUESTER: " + id);
        //System.out.println("ID: " + resId);
        if (resp != null) {
            //System.out.println("RESP SIZE BEFORE: " + resp.size());
            resp.add(res);
            responses.remove(resId);
            responses.put(resId, resp);
        } else {

            resp = new ArrayList<>();

            //System.out.println("RESP SIZE BEFORE: " + resp.size());
            resp.add(res);
            responses.put(resId, resp);
        }
        //System.out.println("RESP SIZE AFTER: " + resp.size());
    }

    public Responder getResponder(ArrayList<Responder> resp, int id) {
        Responder rep = null;
        for (int i = 0; i < resp.size(); i++) {
            if (resp.get(i).getId() == id) {
                rep = resp.get(i);
                break;
            }
        }
        return rep;
    }

    public int getId() {
        return id;
    }

    public int getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setNeighbour(Requester req) {
        neighbour = req;
    }

    public Requester getNeighbour() {
        return neighbour;
    }
}
