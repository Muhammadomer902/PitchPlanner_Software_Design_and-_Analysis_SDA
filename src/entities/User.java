package entities;

public class User {
    private int id;
    private String username;
    private String name;
    private int age;
    private String city;
    private String phone;
    private String password; // Hashed password
    private String role; // PLAYER or GROUND_OWNER
    private String status; // PENDING, VERIFIED, REJECTED
    private boolean blacklisted;

    // Constructor
    public User(int id, String username, String name, int age, String city, String phone, String password, String role, String status, boolean blacklisted) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.age = age;
        this.city = city;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = status;
        this.blacklisted = blacklisted;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }
}
