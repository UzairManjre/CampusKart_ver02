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

import static Database.UserDAO.addUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModeratorDAO  {
    public static boolean addModerator(String username, String password, String email, String enrollmentNumber) {
        // First, add the user
        if (!addUser(username, password, email)) {
            System.err.println("Failed to add user. Cannot proceed with moderator creation.");
            return false;
        }

        // Now, insert into Moderator table
        String insertModeratorQuery = "INSERT INTO Moderator (username, enrollment_number) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(insertModeratorQuery)) {

            pstmt.setString(1, username);
            pstmt.setString(2, enrollmentNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding moderator: " + e.getMessage());
            return false;
        }
    }
}
