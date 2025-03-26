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


import AbstactClasses.UserDetails;
import campuskart_ver02.classes.Moderator;
import campuskart_ver02.classes.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public static boolean addUser(String username, String password, String email) {
        
        String insertUserQuery = "INSERT INTO User (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.initializeDB();
                
                
                
             PreparedStatement pstmt = conn.prepareStatement(insertUserQuery)) {
                        

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Returns true if insertion was successful

        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    public static UserDetails loginUser(String username, String password) {
        String userQuery = "SELECT username, email FROM User WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(userQuery)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                String enrollmentNumber = null;

                if (isStudent(username)) {
                    enrollmentNumber = getEnrollmentNumber(username, "Client");
                    return new Student(username, enrollmentNumber, password, email);
                } else if (isModerator(username)) {
                    enrollmentNumber = getEnrollmentNumber(username, "Moderator");
                    return new Moderator(username, enrollmentNumber, password, email);
                } else {
                    System.out.println("User found, but role not recognized.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null; // Login failed
    }

    // Helper method to get enrollment number from the respective table
    private static String getEnrollmentNumber(String username, String tableName) {
        String query = "SELECT enrollment_number FROM " + tableName + " WHERE username = ?";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("enrollment_number");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching enrollment number: " + e.getMessage());
        }
        return null;
    }


    private static boolean isStudent(String username) {
        String query = "SELECT username FROM client WHERE username = ?";
        return checkRole(username, query);
    }

    private static boolean isModerator(String username) {
        String query = "SELECT username FROM Moderator WHERE username = ?";
        return checkRole(username, query);
    }

    private static boolean checkRole(String username, String query) {
        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if user exists in that table
        } catch (SQLException e) {
            System.err.println("Role check error: " + e.getMessage());
            return false;
        }
    }
}