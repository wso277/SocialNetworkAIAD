package socialGroup;

import exceptions.WrongDateException;
import uchicago.src.sim.engine.Stepable;
import util.Date;

public class Requester implements Stepable {

    //Distance the requester wants to travel
    private int location;
    private float price;
    //first date available for concert
    private Date date1;
    //last date available for concert
    private Date date2;
    private String musicType;

    public Requester(int location, float price, Date date1, Date date2, String musicType) throws WrongDateException {
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
