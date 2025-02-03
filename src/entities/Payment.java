package entities;

public class Payment {
    private int id;
    private int bookingId;
    private int payerId;
    private double amount;
    private String paymentStatus; // PENDING, COMPLETED

    // Constructor
    public Payment(int id, int bookingId, int payerId, double amount, String paymentStatus) {
        this.id = id;
        this.bookingId = bookingId;
        this.payerId = payerId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getPayerId() { return payerId; }
    public void setPayerId(int payerId) { this.payerId = payerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
