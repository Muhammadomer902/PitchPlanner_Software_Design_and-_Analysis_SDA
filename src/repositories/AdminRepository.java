package repositories;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRepository {
    private final Connection connection;

    public AdminRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Verify admin credentials
    public boolean verifyAdmin(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a record is found
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Blacklist a user
    public boolean blacklistUser(int userId) {
        String query = "UPDATE users SET blacklisted = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Blacklist a ground
    public boolean blacklistGround(int groundId) {
        String query = "UPDATE grounds SET blacklisted = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, groundId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
