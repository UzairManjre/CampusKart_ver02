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



import campuskart_ver02.classes.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDAO extends UserDAO {
    
    // Method to add a new Student
    public static boolean addStudent(Student student) {
        if (!addUser(student.getUsername(), student.getPassword(), student.getEmail())) {
            return false; // If adding the user fails, return false
            
        }
        
        String insertStudentQuery = "INSERT INTO Client (username, enrollment_number) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(insertStudentQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            Statement stmt = conn.createStatement();
         stmt.executeUpdate("USE campuskart");

            pstmt.setString(1, student.getUsername());
            pstmt.setString(2, student.getEnrollmentNumber());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated client ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int clientId = generatedKeys.getInt(1);
                        student.setClientId(clientId); // Set the ID in the object
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
        return false;
    }

    // Method to get Student by username
    public static Student getStudentByUsername(String username) {
        String query = "SELECT c_id, User.username, password, email, enrollment_number FROM Client JOIN User ON Client.username = User.username WHERE User.username = ?";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("c_id"), 
                    rs.getString("username"), 
                    rs.getString("enrollment_number"), 
                    rs.getString("password"), 
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student: " + e.getMessage());
        }
        return null;
    }
    public static Student getStudentById(int clientId) {
    String query = "SELECT c_id, User.username, password, email, enrollment_number " +
                   "FROM Client JOIN User ON Client.username = User.username " +
                   "WHERE c_id = ?";

    try (Connection conn = DatabaseConnection.initializeDB();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, clientId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Student(
                rs.getInt("c_id"),
                rs.getString("username"),
                rs.getString("enrollment_number"),
                rs.getString("password"),
                rs.getString("email")
            );
        }
    } catch (SQLException e) {
        System.err.println("Error fetching student by ID: " + e.getMessage());
    }
    return null;
}
}
