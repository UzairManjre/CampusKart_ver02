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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:4030/?user=root"; // Change if needed
    private static final String USER = "root";
    private static final String PASSWORD = "12345"; // Replace with your actual password

    public static Connection initializeDB() {
        Connection conn = null;
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish Connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Oracle Database successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection Failed! " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        // Test the connection
        Connection conn = initializeDB();
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
