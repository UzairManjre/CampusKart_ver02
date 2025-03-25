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

import campuskart_ver02.classes.Reviews;
import campuskart_ver02.classes.Product;
import campuskart_ver02.classes.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewsDAO {
    
    private Connection conn;
 
    public ReviewsDAO() {
        conn = DatabaseConnection.initializeDB();
   
    }

    // Add a review
    public boolean addReview(int studentId, int productId, int rating, String comment) {
        String query = "INSERT INTO Reviews (c_id, p_id, rating, comment, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, comment);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding review: " + e.getMessage());
            return false;
        }
    }

    // Get all reviews for a product
    public List<Reviews> getReviewsByProduct(int productId) {
        List<Reviews> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE p_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = ProductDAO.getProductById(rs.getInt("p_id"));
                Student student = StudentDAO.getStudentById(rs.getInt("c_id"));

                reviews.add(new Reviews(
                    rs.getInt("r_id"), product, student,
                    rs.getInt("rating"), rs.getString("comment"), rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews: " + e.getMessage());
        }
        return reviews;
    }

    // Delete a review by ID
    public boolean deleteReview(int reviewId) {
        String query = "DELETE FROM Reviews WHERE r_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
            return false;
        }
    }
}
