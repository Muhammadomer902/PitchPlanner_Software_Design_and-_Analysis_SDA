package repositories;

import entities.User;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection connection;

    public UserRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Save a new user
    public boolean saveUser(User user) {
        String query = "INSERT INTO users (username, name, age, city, phone, password, role, status, blacklisted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setInt(3, user.getAge());
            stmt.setString(4, user.getCity());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole());
            stmt.setString(8, user.getStatus());
            stmt.setBoolean(9, user.isBlacklisted());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find user by username
    public User findUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ? and blacklisted = false";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("city"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getBoolean("blacklisted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fetch pending users
    public List<User> getPendingUsers() {
        String query = "SELECT * FROM users WHERE status = 'PENDING'";
        List<User> pendingUsers = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pendingUsers.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("city"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getBoolean("blacklisted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingUsers;
    }

    // Update user status
    public boolean updateUserStatus(int userId, String status) {
        String query = "UPDATE users SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
