package repositories;

import entities.ComplaintDetails;
import entities.Ground;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroundRepository {
    private final Connection connection;

    public GroundRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Add a new ground
    public boolean addGround(Ground ground) {
        String query = "INSERT INTO grounds (name, location, price, type, owner_id, status) VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ground.getName());
            stmt.setString(2, ground.getLocation());
            stmt.setDouble(3, ground.getPrice());
            stmt.setString(4, ground.getType());
            stmt.setInt(5, ground.getOwnerId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove ground
    public boolean removeGround(int groundId) {
        String query = "DELETE FROM grounds WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Update ground details
    public boolean updateGround(int groundId, String name, String location, double price, String type) {
        String query = "UPDATE grounds SET name=?, location = ?, price = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setDouble(3, price);
            stmt.setString(4, type);
            stmt.setInt(5, groundId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all grounds with optional filters, excluding blacklisted grounds
    public List<Ground> getGrounds(String type, Double minPrice, Double maxPrice, String location) {
        StringBuilder query = new StringBuilder("SELECT * FROM grounds WHERE blacklisted = FALSE and status='VERIFIED'");
        if (type != null) query.append(" AND type = ?");
        if (minPrice != null) query.append(" AND price >= ?");
        if (maxPrice != null) query.append(" AND price <= ?");
        if (location != null) query.append(" AND location LIKE ?");

        List<Ground> grounds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (type != null) stmt.setString(paramIndex++, type);
            if (minPrice != null) stmt.setDouble(paramIndex++, minPrice);
            if (maxPrice != null) stmt.setDouble(paramIndex++, maxPrice);
            if (location != null) stmt.setString(paramIndex++, "%" + location + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grounds.add(new Ground(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getString("type"),
                        rs.getInt("owner_id"),
                        rs.getBoolean("blacklisted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grounds;
    }

    public List<Ground> getAllAvailableGrounds() {
        StringBuilder query = new StringBuilder("SELECT * FROM grounds WHERE blacklisted = FALSE and status='VERIFIED'");

        List<Ground> grounds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grounds.add(new Ground(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getString("type"),
                        rs.getInt("owner_id"),
                        rs.getBoolean("blacklisted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grounds;
    }

    public List<Ground> getGroundsByOwner(int groundOwnerId) {
        String query = "SELECT * FROM grounds WHERE owner_id = ? AND blacklisted = FALSE";
    
        List<Ground> grounds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundOwnerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grounds.add(new Ground(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getDouble("price"),
                            rs.getString("type"),
                            rs.getInt("owner_id"),
                            rs.getBoolean("blacklisted")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grounds;
    }
    

    // Blacklist a ground
    public boolean blacklistGround(ComplaintDetails complaint) {
        String fetchQuery = "SELECT ground_id FROM complaints WHERE id = ?";
        String updateQuery = "UPDATE grounds SET blacklisted = TRUE WHERE id = ?";
    
        try (PreparedStatement fetchStmt = connection.prepareStatement(fetchQuery)) {
            // Fetch ground_id using complaint_id
            fetchStmt.setInt(1, complaint.getId());
            try (ResultSet rs = fetchStmt.executeQuery()) {
                if (rs.next()) {
                    int groundId = rs.getInt("ground_id");
    
                    // Blacklist the ground
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, groundId);
                        updateStmt.executeUpdate();
                        return true; // Blacklisting successful
                    }
                } else {
                    System.out.println("No ground associated with the complaint ID.");
                    return false; // No ground found for the complaint
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
            return false; // Blacklisting failed
        }
    }
    

    // Fetch pending grounds
    public List<Ground> getPendingGrounds() {
        String query = "SELECT * FROM grounds WHERE status = 'PENDING'";
        List<Ground> pendingGrounds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pendingGrounds.add(new Ground(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getString("type"),
                    rs.getInt("owner_id"),
                    rs.getBoolean("blacklisted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingGrounds;
    }

    // Update ground status
    public boolean updateGroundStatus(int groundId, String status) {
        String query = "UPDATE grounds SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, groundId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
