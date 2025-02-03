package entities;

public class Ground {
    private int id;
    private String name;
    private String location;
    private double price;
    private String type; // CRICKET or FOOTBALL
    private int ownerId;
    private boolean blacklisted;

    public Ground(int id, String name, String location, double price, String type, int ownerId, boolean blacklisted) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.type = type;
        this.ownerId = ownerId;
        this.blacklisted = blacklisted;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }
}
