package repositories;

import entities.Complaint;
import entities.ComplaintDetails;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintRepository {
    private final Connection connection;

    public ComplaintRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Validate ground ownership
    public boolean groundOwnedByOwner(int groundId, int ownerId) {
        String query = "SELECT COUNT(*) AS count FROM grounds WHERE id = ? AND owner_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            stmt.setInt(2, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validate booking association with ground
    public boolean bookingBelongsToGround(int bookingId, int groundId) {
        String query = "SELECT COUNT(*) AS count FROM bookings WHERE id = ? AND ground_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, groundId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Validate ground existence
    public boolean groundExists(int groundId) {
        String query = "SELECT COUNT(*) AS count FROM grounds WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validate booking existence and ownership
    public boolean bookingExistsAndOwnedByPlayer(int bookingId, int playerId) {
        String query = "SELECT COUNT(*) AS count FROM bookings WHERE id = ? AND player_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, playerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // File a new complaint
    public boolean fileComplaint(int playerId, Integer bookingId, String issue) {
        // Query to fetch groundId from the booking
        String fetchGroundIdQuery = "SELECT ground_id FROM bookings WHERE id = ?";
        
        // Query to insert a new complaint
        String insertComplaintQuery = "INSERT INTO complaints (user_id, ground_id, booking_id, issue, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement fetchStmt = connection.prepareStatement(fetchGroundIdQuery);
            PreparedStatement insertStmt = connection.prepareStatement(insertComplaintQuery)) {

            // Fetch groundId using bookingId
            fetchStmt.setInt(1, bookingId);
            try (ResultSet rs = fetchStmt.executeQuery()) {
                if (rs.next()) {
                    int groundId = rs.getInt("ground_id");

                    // Insert the complaint
                    insertStmt.setInt(1, playerId);
                    insertStmt.setInt(2, groundId);
                    insertStmt.setObject(3, bookingId); // Nullable
                    insertStmt.setString(4, issue);
                    insertStmt.setString(5, "OPEN");
                    insertStmt.executeUpdate();
                    return true;
                } else {
                    System.err.println("No groundId found for the given bookingId: " + bookingId);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasComplaint(int userId, Integer bookingId) {
        String query = "SELECT COUNT(*) AS count FROM complaints WHERE booking_id = ? and user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set query parameters
            stmt.setInt(1, bookingId);
            stmt.setInt(2, userId);

            // Execute query and get the result
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Moves to the first row of the result set
                // Check if the count is greater than 0
                return rs.getInt("count") > 0; // Returns true if feedback exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Logs any SQL errors
        }
        return false; // Returns false if no feedback exists or an error occurs
    }


    // Fetch open complaints
    public List<Complaint> getOpenComplaints() {
        String query = "SELECT * FROM complaints WHERE status = 'OPEN'";
        List<Complaint> complaints = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complaints.add(new Complaint(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("ground_id"),
                        rs.getObject("booking_id", Integer.class),
                        rs.getString("issue"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaints;
    }

    // Resolve a complaint
    public boolean resolveComplaint(int complaintId) {
        String query = "UPDATE complaints SET status = 'RESOLVED' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, complaintId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch unresolved complaints
    public List<ComplaintDetails> fetchOpenComplaintDetails() {
        String query = """
                SELECT c.id, u.name AS complainant_name, 
                    CASE WHEN u.role = 'GROUND_OWNER' THEN 'Ground Owner'
                            WHEN u.role = 'PLAYER' THEN 'Player'
                            ELSE 'Admin' END AS complainant_role,
                    c.issue, c.status, g.name AS ground_name,
                    b.opponent_player_id AS other_user_id_2, b.player_id AS other_user_id_1
                FROM complaints c
                JOIN users u ON c.user_id = u.id
                JOIN grounds g ON c.ground_id = g.id
                LEFT JOIN bookings b ON c.booking_id = b.id
                WHERE c.status = 'OPEN'
                """;

        List<ComplaintDetails> complaintDetailsList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complaintDetailsList.add(new ComplaintDetails(
                    rs.getInt("id"),
                    rs.getString("complainant_name"),
                    rs.getString("complainant_role"),
                    rs.getString("issue"),
                    rs.getString("status"),
                    rs.getString("ground_name"),
                    rs.getObject("other_user_id_1") != null ? rs.getInt("other_user_id_1") : null,
                    rs.getObject("other_user_id_2") != null ? rs.getInt("other_user_id_2") : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaintDetailsList;
    }

}
