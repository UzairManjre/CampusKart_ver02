package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String URL = "jdbc:mysql://localhost:3306/"; // Change database name if needed
    private static  String USER = "root";
    private static  String PASSWORD = "12345"; // Replace with actual password

    public static Connection initializeDB() {
        Connection conn = null;
        try {
            // Load MySQL JDBC Driver (Fixed)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Connected to MySQL Database successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            if (e.getSQLState().equals("28000")) {
                System.err.println("Authentication failed: Incorrect username or password.");
            } else {
                System.err.println("Connection Failed! " + e.getMessage());
            }
        }
        return conn;
    }

    public static void setURL(String URL) {
        DatabaseConnection.URL = URL;
    }

    public static void setUSER(String USER) {
        DatabaseConnection.USER = USER;
    }

    public static void setPASSWORD(String PASSWORD) {
        DatabaseConnection.PASSWORD = PASSWORD;
    }

    public static String getURL() {
        return URL;
    }
}
