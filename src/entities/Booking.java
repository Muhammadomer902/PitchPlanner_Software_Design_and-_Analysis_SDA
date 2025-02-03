package entities;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int id;
    private int playerId;
    private int groundId;
    private Date date;
    private Time time;
    private String status; // PENDING, CONFIRMED, MATCH-UP, CANCELLED
    private String opponentCode;
    private Integer opponentPlayerId;

    // Constructor
    public Booking(int id, int playerId, int groundId, Date date, Time time, String status, String opponentCode, Integer opponentPlayerId) {
        this.id = id;
        this.playerId = playerId;
        this.groundId = groundId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.opponentCode = opponentCode;
        this.opponentPlayerId = opponentPlayerId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public int getGroundId() { return groundId; }
    public void setGroundId(int groundId) { this.groundId = groundId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOpponentCode() { return opponentCode; }
    public void setOpponentCode(String opponentCode) { this.opponentCode = opponentCode; }
    public Integer getOpponentPlayerId() { return opponentPlayerId; }
    public void setOpponentPlayerId(Integer opponentPlayerId) { this.opponentPlayerId = opponentPlayerId; }
}
