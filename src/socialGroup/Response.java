package socialGroup;

import util.Date;

public class Response {

    private int location;
    private int price;
    private Date date;
    private String musicType;
    private int id;
    private boolean processed;
    public Response() {

    }

    public Response(int location, int price, Date date, String musicType, int id) {
        this.location = location;
        this.price = price;
        this.date = date;
        this.musicType = musicType;
        this.id = id;
        this.processed = false;
    }

    public void setProcessed() {
        this.processed = true;
    }

    public boolean isProcessed() {
        return processed;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
