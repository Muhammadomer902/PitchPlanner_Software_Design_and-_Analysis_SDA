package repositories;

import entities.Booking;
import entities.Ground;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroundOwnerRepository {
    private final Connection connection;

    public GroundOwnerRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Fetch grounds owned by the ground owner
    public List<Ground> getOwnedGrounds(int ownerId) {
        String query = "SELECT * FROM grounds WHERE owner_id = ?";
        List<Ground> ownedGrounds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ownedGrounds.add(new Ground(
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
        return ownedGrounds;
    }

    // Fetch bookings for a ground owned by the ground owner with distinction between past and future bookings
    public List<Booking> getBookingsForGround(int groundId, boolean isPast) {
        String query = isPast
                ? "SELECT * FROM bookings WHERE ground_id = ? AND date < CURDATE()" // Past bookings
                : "SELECT * FROM bookings WHERE ground_id = ? AND date >= CURDATE()"; // Future bookings
        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Booking(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getInt("ground_id"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("status"),
                    rs.getString("opponent_code"),
                    rs.getObject("opponent_player_id", Integer.class)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

}
