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
    public static boolean addStudent(String username, String password, String email, String enrollmentNumber) {
        // First, add the user
        if (!addUser(username, password, email)) {
            System.err.println("Failed to add user. Cannot proceed with student creation.");
            return false;
        }

        // Now, insert into Client (Student) table
        String insertStudentQuery = "INSERT INTO Client (username, enrollment_number) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(insertStudentQuery)) {

            pstmt.setString(1, username);
            pstmt.setString(2, enrollmentNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
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
//        System.out.println("Fetching student for ID: " + clientId);  // Debug Print

        String query = "SELECT c_id, User.username, password, email, enrollment_number " +
                "FROM Client JOIN User ON Client.username = User.username " +
                "WHERE c_id = ?";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
//                System.out.println("Student found: " + rs.getString("username"));  // Debug Print

                return new Student(
                        rs.getInt("c_id"),
                        rs.getString("username"),
                        rs.getString("enrollment_number"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            } else {
                System.out.println("No student found with ID: " + clientId);  // Debug Print
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student by ID: " + e.getMessage());
        }
        return null;
    }
//    public static void main(String[] args) {
//        DatabaseSetup.main(new String[]{});
//        int testStudentId = 1; // Change this to an actual c_id from your database
//
//        System.out.println("Testing getStudentById with ID: " + testStudentId);
//        Student student = StudentDAO.getStudentById(testStudentId);
//
//        if (student != null) {
//            System.out.println("Student Retrieved Successfully:");
//            System.out.println("ID: " + student.getClientId());
//            System.out.println("Username: " + student.getUsername());
//            System.out.println("Enrollment Number: " + student.getEnrollmentNumber());
//            System.out.println("Email: " + student.getEmail());
//        } else {
//            System.out.println("No student found with ID: " + testStudentId);
//        }
//    }
}

