package repositories;

import entities.Feedback;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {
    private final Connection connection;

    public FeedbackRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Save feedback
    public boolean saveFeedback(int bookingId, int userId, int rating, String comment) {
        // Query to fetch the ground_id based on booking_id
        String getGroundQuery = "SELECT ground_id FROM bookings WHERE id = ?";
        // Query to insert feedback
        String insertFeedbackQuery = "INSERT INTO feedbacks (booking_id, user_id, ground_id, rating, comment) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement getGroundStmt = connection.prepareStatement(getGroundQuery);
            PreparedStatement insertFeedbackStmt = connection.prepareStatement(insertFeedbackQuery)) {

            // Get the ground_id associated with the booking
            getGroundStmt.setInt(1, bookingId);
            try (ResultSet rs = getGroundStmt.executeQuery()) {
                if (rs.next()) {
                    int groundId = rs.getInt("ground_id");

                    // Insert feedback using retrieved ground_id
                    insertFeedbackStmt.setInt(1, bookingId);
                    insertFeedbackStmt.setInt(2, userId);
                    insertFeedbackStmt.setInt(3, groundId);
                    insertFeedbackStmt.setInt(4, rating);
                    insertFeedbackStmt.setString(5, comment);

                    insertFeedbackStmt.executeUpdate();
                    return true;
                } else {
                    System.err.println("No ground found for booking ID: " + bookingId);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Check if feedback exists for a booking
    public boolean feedbackExistsForBooking(int bookingId, int PlayerId) {
        String query = "SELECT COUNT(*) AS count FROM feedbacks WHERE booking_id = ? and user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set query parameters
            stmt.setInt(1, bookingId);
            stmt.setInt(2, PlayerId);

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


    // Fetch feedback for a specific ground
    public List<Feedback> getFeedbackByGroundId(int groundId) {
        String query = "SELECT * FROM feedbacks WHERE ground_id = ?";
        List<Feedback> feedbackList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                feedbackList.add(new Feedback(
                    rs.getInt("id"),
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getInt("ground_id"),
                    rs.getInt("rating"),
                    rs.getString("comment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
}
