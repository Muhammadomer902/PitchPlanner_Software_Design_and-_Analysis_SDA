package repositories;

import entities.Booking;
import entities.BookingDetails;
import utils.DatabaseConnection;

import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private final Connection connection;

    public BookingRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch booking details
    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Booking(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getInt("ground_id"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("status"),
                    rs.getString("opponent_code"),
                    rs.getObject("opponent_player_id", Integer.class)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Generate a match-up request
    public boolean generateMatchUpRequest(int bookingId, String opponentCode) {
        String query = "UPDATE bookings SET status = 'MATCH-UP', opponent_code = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, opponentCode);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch booking by match-up code
    public Booking getBookingByOpponentCode(String opponentCode) {
        String query = "SELECT * FROM bookings WHERE opponent_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, opponentCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Booking(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getInt("ground_id"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("status"),
                    rs.getString("opponent_code"),
                    rs.getObject("opponent_player_id", Integer.class)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Booking Details
    public BookingDetails getBookingDetails(String matchUpCode) {
        String query = """
                SELECT b.id, b.date, b.time, b.status, g.name AS ground_name, g.location AS ground_location, g.price
                FROM bookings b
                JOIN grounds g ON b.ground_id = g.id
                WHERE b.opponent_code = ? AND b.status = 'PENDING'
                """;


        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, matchUpCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BookingDetails(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("ground_name"),
                        rs.getString("ground_location"),
                        rs.getDouble("price"),
                        rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BookingDetails> getBookingsByUser(int playerId) {
        String query = """
                SELECT b.id, b.date, b.time, b.status, g.name AS ground_name, g.location AS ground_location, g.price
                FROM bookings b
                JOIN grounds g ON b.ground_id = g.id
                WHERE b.player_id = ?
                ORDER BY b.date, b.time
                """;

        List<BookingDetails> bookings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new BookingDetails(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("ground_name"),
                        rs.getString("ground_location"),
                        rs.getDouble("price"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<BookingDetails> getBookingsByGround(int groundId) {
        String query = """
                SELECT b.id, b.date, b.time, b.status, g.name AS ground_name, g.location AS ground_location, g.price
                FROM bookings b
                JOIN grounds g ON b.ground_id = g.id
                WHERE g.id = ?
                ORDER BY b.date, b.time
                """;

        List<BookingDetails> bookings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new BookingDetails(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("ground_name"),
                        rs.getString("ground_location"),
                        rs.getDouble("price"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // get player name
    public String getPlayerNameByBookingId(int bookingId)
    {
        String query = """
            SELECT u.name AS player_name
            FROM bookings b
            JOIN users u ON b.player_id = u.id
            WHERE b.id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("player_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no name found
    }

    // Check if a ground is available
    public boolean isGroundAvailable(int groundId, Date date, Time time) {
        String query = "SELECT * FROM bookings WHERE ground_id = ? AND date = ? AND time = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            stmt.setDate(2, date);
            stmt.setTime(3, time);
            ResultSet rs = stmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save a booking
    public int saveBooking(Booking booking) {
        String query = "INSERT INTO bookings (player_id, ground_id, date, time, status, opponent_code, opponent_player_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getPlayerId());
            stmt.setInt(2, booking.getGroundId());
            stmt.setDate(3, booking.getDate());
            stmt.setTime(4, booking.getTime());
            stmt.setString(5, booking.getStatus());
            stmt.setString(6, booking.getOpponentCode());
            stmt.setObject(7, booking.getOpponentPlayerId()); // Nullable
    
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows > 0) {
                // Retrieve the generated booking ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the booking ID
                    }
                }
            }
            return -1; // Return -1 if no key was generated
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate failure
        }
    }
    

    // Join an existing match-up using code
    public boolean joinMatchUp(int bookingId, int opponentPlayerId) {
        String query = "UPDATE bookings SET opponent_player_id = ?, status = 'CONFIRMED' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, opponentPlayerId);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch an existing booking for match-up
    public Booking findBookingForMatchUp(int groundId, Date date, Time time) {
        String query = "SELECT * FROM bookings WHERE ground_id = ? AND date = ? AND time = ? AND status = 'MATCH-UP'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            stmt.setDate(2, date);
            stmt.setTime(3, time);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Booking(
                    rs.getInt("id"),
                    rs.getInt("player_id"),
                    rs.getInt("ground_id"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("status"),
                    rs.getString("opponent_code"),
                    rs.getObject("opponent_player_id", Integer.class)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
