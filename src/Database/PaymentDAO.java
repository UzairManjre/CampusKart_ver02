/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author uzair
 */

import campuskart_ver02.classes.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private static final Connection conn = DatabaseConnection.initializeDB();

    // Insert a new payment
    public static boolean addPayment(int transactionId, double amount, String method) {
        String sql = "INSERT INTO Payment (t_id, amount, payment_method) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            stmt.setDouble(2, amount);
            stmt.setString(3, method);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding payment: " + e.getMessage());
        }
        return false;
    }

    // Get payment by transaction ID
    public static Payment getPaymentByTransactionId(int transactionId) {
        String sql = "SELECT * FROM Payment WHERE t_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Payment(
                    rs.getInt("pay_id"),
                    rs.getInt("t_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_status"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("pay_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching payment: " + e.getMessage());
        }
        return null;
    }

    // Update payment status
    public static boolean updatePaymentStatus(int paymentId, String newStatus) {
        String sql = "UPDATE Payment SET payment_status = ? WHERE pay_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, paymentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
        }
        return false;
    }

    // Get all payments (for admin/moderators)
    public static List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(new Payment(
                    rs.getInt("pay_id"),
                    rs.getInt("t_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_status"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("pay_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all payments: " + e.getMessage());
        }
        return payments;
    }
}
