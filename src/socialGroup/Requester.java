package socialGroup;

import exceptions.WrongDateException;
import uchicago.src.sim.engine.Stepable;
import util.Date;

import java.util.ArrayList;

public class Requester implements Stepable {

    /*public static Enum ValidLocations = {Porto", "Lisboa", "Coimbra", "Aveiro", "Guarda", "Evora", "Faro", "Vila " +
            "Real", "Braga", "Portalegre", "Castelo Branco", "Viseu", "Beja", "Braganaa", "Acores", "Madeira",
            "Setubal", "Santarem", "Viana do Castelo"};*/

    public static ArrayList<String> MusicTypes = new ArrayList<String>() {{
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
    private float price;
    //first date available for concert
    private Date date1;
    //last date available for concert
    private Date date2;
    private String musicType;

    public Requester(int location, float price, Date date1, Date date2, String musicType) throws
            WrongDateException {
        this.location = location;
        this.price = price;
        if (!date1.isEarlier(date2)) {
            throw new WrongDateException("Second date is earlier than first date!");
        }
        this.date1 = date1;
        this.date2 = date2;
        this.musicType = musicType;
    }

    public void step() {
    }
}
