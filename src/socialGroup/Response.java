package socialGroup;

import util.Date;

public class Response {

    private int location;
    private int price;
    private Date date;
    private String musicType;

    public Response() {

    }

    public Response(int location, int price, Date date, String musicType) {
        this.location = location;
        this.price = price;
        this.date = date;
        this.musicType = musicType;
    }

    //Method to calculate FIRE Values


    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }
}
