package entities;

public class Feedback {
    private int id;
    private int bookingId;
    private int userId;
    private int groundId;
    private int rating; // 1 to 5
    private String comment;

    // Constructor
    public Feedback(int id, int bookingId, int userId, int groundId, int rating, String comment) {
        this.id = id;
        this.bookingId = bookingId;
        this.userId = userId;
        this.groundId = groundId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getGroundId() { return groundId; }
    public void setGroundId(int groundId) { this.groundId = groundId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
