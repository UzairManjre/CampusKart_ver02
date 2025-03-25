package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static Database.DatabaseConnection.getURL;
import static Database.DatabaseConnection.setURL;

public class DatabaseSetup {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.initializeDB();
        if (conn == null) {
            System.err.println("Database connection failed. Exiting...");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            // Check if database exists
            String checkDBQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'campuskart'";
            ResultSet rs = stmt.executeQuery(checkDBQuery);
            boolean dbExists = rs.next();

            if (!dbExists) {
                System.out.println("Database 'campuskart' not found. Creating...");

                // Create database
                stmt.executeUpdate("CREATE DATABASE campuskart");
                System.out.println("Database 'campuskart' created successfully!");
            }

            // ✅ Always select the database, whether it was just created or already exists
            stmt.executeUpdate("USE campuskart");

            // Create Tables
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS User ("
                    + "username VARCHAR(50) PRIMARY KEY, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(100) UNIQUE NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Client ("
                    + "c_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) UNIQUE NOT NULL, "
                    + "enrollment_number VARCHAR(20) UNIQUE NOT NULL, "
                    + "FOREIGN KEY (username) REFERENCES User(username) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Moderator ("
                    + "m_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) UNIQUE NOT NULL, "
                    + "enrollment_number VARCHAR(20) UNIQUE NOT NULL, "
                    + "FOREIGN KEY (username) REFERENCES User(username) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Products ("
                    + "pr_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "pname VARCHAR(100) NOT NULL, "
                    + "pdesc VARCHAR(500), "
                    + "p_price DECIMAL(10,2) NOT NULL, "
                    + "qty INT NOT NULL, "
                    + "category VARCHAR(50), "
                    + "status ENUM('Available', 'Sold') NOT NULL DEFAULT 'Available', "
                    + "p_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "s_id INT NOT NULL, "
                    + "FOREIGN KEY (s_id) REFERENCES Client(c_id) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Transactions ("
                    + "t_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "t_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "p_id INT NOT NULL, "
                    + "buyer_id INT NOT NULL, "
                    + "seller_id INT NOT NULL, "
                    + "FOREIGN KEY (p_id) REFERENCES Products(pr_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (buyer_id) REFERENCES Client(c_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (seller_id) REFERENCES Client(c_id) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Payment ("
                    + "pay_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "t_id INT UNIQUE NOT NULL, "
                    + "amount DECIMAL(10,2) NOT NULL, "
                    + "payment_status ENUM('Completed', 'Pending', 'Failed') NOT NULL DEFAULT 'Pending', "
                    + "payment_method ENUM('Credit Card', 'UPI', 'Cash') NOT NULL, "
                    + "pay_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (t_id) REFERENCES Transactions(t_id) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Reviews ("
                    + "r_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "u_id INT NOT NULL, "
                    + "p_id INT NOT NULL, "
                    + "rating TINYINT CHECK (rating BETWEEN 1 AND 5), "
                    + "comment VARCHAR(500), "
                    + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (u_id) REFERENCES Client(c_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (p_id) REFERENCES Products(pr_id) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Favourites ("
                    + "c_id INT NOT NULL, "
                    + "product_id INT NOT NULL, "
                    + "PRIMARY KEY (c_id, product_id), "
                    + "FOREIGN KEY (c_id) REFERENCES Client(c_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (product_id) REFERENCES Products(pr_id) ON DELETE CASCADE)");

            System.out.println("All tables created successfully!");

            setURL(getURL()+"campuskart");

        } catch (SQLException e) {
            System.err.println("Error while setting up database: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
