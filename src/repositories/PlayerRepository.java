package repositories;

import entities.Booking;
import entities.Ground;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private final Connection connection;

    public PlayerRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Fetch available grounds with optional filters
    public List<Ground> getAvailableGrounds(String type, Double minPrice, Double maxPrice, String location) {
        StringBuilder query = new StringBuilder("SELECT * FROM grounds WHERE blacklisted = FALSE");
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

    // Fetch player's bookings
    public List<Booking> getBookings(int playerId, boolean isPast) {
        String query = isPast
                ? "SELECT * FROM bookings WHERE player_id = ? AND date < CURDATE()"
                : "SELECT * FROM bookings WHERE player_id = ? AND date >= CURDATE()";

        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);
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

    // Cancel a booking
    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ? AND booking_date > CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
