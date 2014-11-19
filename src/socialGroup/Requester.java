package socialGroup;

import exceptions.WrongDateException;
import mainPackage.Main;
import uchicago.src.sim.engine.Stepable;
import util.Date;

import java.util.ArrayList;

public class Requester implements Stepable {

    /*public static Enum ValidLocations = {Porto", "Lisboa", "Coimbra", "Aveiro", "Guarda", "Evora", "Faro", "Vila " +
            "Real", "Braga", "Portalegre", "Castelo Branco", "Viseu", "Beja", "Braganaa", "Acores", "Madeira",
            "Setubal", "Santarem", "Viana do Castelo"};*/

    public static final ArrayList<String> MusicTypes = new ArrayList<String>() {{
        add("ROCK");
        add("CLASSIC");
        add("JAZZ");
        add("POP");
        add("HOUSE");
        add("FUNK");
        add("METAL");
        add("RNB");
        add("REGGAE");
        add("PIMBA");
        add("FADO");
    }};

    //Distance the requester wants to travel
    private int location;
    private int price;
    //first date available for concert
    private Date dateStart;
    //last date available for concert
    private Date dateEnd;
    private String musicType;
    private int id;
    private ArrayList<Response> responses;
    private float locationValue, priceValue, dateValue, musicValue, deltaLocation, deltaPrice, yyLocation, yyPrice;

    public Requester(int location, int price, Date date1, Date date2, String musicType, int id) throws
            WrongDateException {
        this.location = location;
        this.price = price;
        if (!date1.isEarlier(date2)) {
            throw new WrongDateException("Second date is earlier than first date!");
        }
        this.dateStart = date1;
        this.dateEnd = date2;
        this.musicType = musicType;
        this.id = id;
        responses = new ArrayList<>();


        deltaLocation = (float) -2.0 / Main.getSocialModel().getMaxDistance();
        yyLocation = (float) - (Main.getSocialModel().getMaxDistance() + location) * deltaLocation;

        deltaPrice = (float) -2.0 / Main.getSocialModel().getMaxPrice();
        yyPrice = (float) - (Main.getSocialModel().getMaxPrice() + price) * deltaPrice;
    }

    public void step() {

        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).getLocation() <= location) {
                locationValue = 1;
            } else if (responses.get(i).getLocation() >= (location + Main.getSocialModel().getMaxDistance())) {
                locationValue = 0;
            } else {
                locationValue = deltaLocation * responses.get(i).getLocation() + yyLocation;
            }

            if (responses.get(i).getPrice() <= price) {
                priceValue = 1;
            } else if (responses.get(i).getPrice() >= (price + Main.getSocialModel().getMaxDistance())) {
                priceValue = 0;
            } else {
                priceValue = deltaPrice * responses.get(i).getPrice() + yyPrice;
            }

            if (responses.get(i).getDate().isEarlier(dateEnd)) {
                dateValue = 1;
            } else {
                dateValue = 0;
            }

            if (responses.get(i).getMusicType().equals(musicType)) {
                musicValue = 1;
            } else {
                musicValue = 0;
            }

            System.out.println("Location = "+locationValue+"\nPrice = "+priceValue+"\nDate = "+dateValue+"\nMusic = "+musicValue);
        }
    }

    public void addResponse(Response res) {
        responses.add(res);
    }

    public int getId(){
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
}
