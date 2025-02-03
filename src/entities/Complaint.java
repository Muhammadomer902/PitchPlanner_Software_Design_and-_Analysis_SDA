package entities;

public class Complaint {
    private int id;
    private int userId;
    private int groundId;
    private Integer bookingId; // Nullable
    private String issue;
    private String status; // OPEN or RESOLVED

    // Constructor
    public Complaint(int id, int userId, int groundId, Integer bookingId, String issue, String status) {
        this.id = id;
        this.userId = userId;
        this.groundId = groundId;
        this.bookingId = bookingId;
        this.issue = issue;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getGroundId() { return groundId; }
    public void setGroundId(int groundId) { this.groundId = groundId; }
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
