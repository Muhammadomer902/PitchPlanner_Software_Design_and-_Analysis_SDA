package entities;

import java.sql.Date;
import java.sql.Time;

public class BookingDetails {
    private int id;
    private Date date;
    private Time time;
    private String groundName;
    private String groundLocation;
    private double price;
    private String status; // Added status attribute

    public BookingDetails(int id, Date date, Time time, String groundName, String groundLocation, double price, String status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.groundName = groundName;
        this.groundLocation = groundLocation;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String getGroundName() {
        return groundName;
    }

    public String getGroundLocation() {
        return groundLocation;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() { // Getter for status
        return status;
    }

    public void setStatus(String status) { // Setter for status
        this.status = status;
    }
}
