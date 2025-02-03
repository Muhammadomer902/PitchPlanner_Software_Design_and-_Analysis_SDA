package repositories;

import entities.Payment;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final Connection connection;

    public PaymentRepository() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Save payment
    public boolean savePayment(Payment payment) {
        String query = "INSERT INTO payments (booking_id, payer_id, amount, payment_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, payment.getBookingId());
            stmt.setInt(2, payment.getPayerId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentStatus());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get payments by booking ID
    public List<Payment> getPaymentsByBookingId(int bookingId) {
        String query = "SELECT * FROM payments WHERE booking_id = ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(new Payment(
                    rs.getInt("id"),
                    rs.getInt("booking_id"),
                    rs.getInt("payer_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Update payment status by booking ID
    public boolean updatePaymentStatusByBookingId(int bookingId, String newStatus) {
        String query = "UPDATE payments SET payment_status = ? WHERE booking_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get payments by user ID
    public List<Payment> getPaymentsByUserId(int userId) {
        String query = "SELECT * FROM payments WHERE payer_id = ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(new Payment(
                    rs.getInt("id"),
                    rs.getInt("booking_id"),
                    rs.getInt("payer_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Process payment
    public boolean processPayment(int paymentID) {
        String query = "UPDATE payments SET payment_status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) 
        {
            stmt.setString(1, "COMPLETED");
            stmt.setInt(2, paymentID);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
