package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static Database.DatabaseConnection.*;

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

//            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Payment ("
//                    + "pay_id INT AUTO_INCREMENT PRIMARY KEY, "
//                    + "t_id INT UNIQUE NOT NULL, "
//                    + "amount DECIMAL(10,2) NOT NULL, "
//                    + "payment_status ENUM('Completed', 'Pending', 'Failed') NOT NULL DEFAULT 'Pending', "
//                    + "payment_method ENUM('Credit Card', 'UPI', 'Cash') NOT NULL, "
//                    + "pay_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
//                    + "FOREIGN KEY (t_id) REFERENCES Transactions(t_id) ON DELETE CASCADE)");

//            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Reviews ("
//                    + "r_id INT AUTO_INCREMENT PRIMARY KEY, "
//                    + "u_id INT NOT NULL, "
//                    + "p_id INT NOT NULL, "
//                    + "rating TINYINT CHECK (rating BETWEEN 1 AND 5), "
//                    + "comment VARCHAR(500), "
//                    + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
//                    + "FOREIGN KEY (u_id) REFERENCES Client(c_id) ON DELETE CASCADE, "
//                    + "FOREIGN KEY (p_id) REFERENCES Products(pr_id) ON DELETE CASCADE)");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Favourites ("
                        + "c_id INT NOT NULL, "
                        + "product_id INT NOT NULL, "
                        + "PRIMARY KEY (c_id, product_id), "
                        + "FOREIGN KEY (c_id) REFERENCES Client(c_id) ON DELETE CASCADE, "
                        + "FOREIGN KEY (product_id) REFERENCES Products(pr_id) ON DELETE CASCADE)");

                System.out.println("All tables created successfully!");

                setURL(getURL() + "campuskart");
                save();

                insertDefaultUsers(stmt);
//                insertDefaultModerators(stmt);
                // Insert default clients (MUST be done before inserting products)
                insertDefaultClients(stmt);

                // Insert default products
                insertDefaultProducts(stmt);

            }

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
    private static void insertDefaultUsers(Statement stmt) throws SQLException {
        String checkUserQuery = "SELECT COUNT(*) FROM User WHERE username IN ('Uzair', 'shlok', 'admin1')";
        ResultSet rs = stmt.executeQuery(checkUserQuery);
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO User (username, password, email) VALUES "
                    + "('Uzair', '1234', 'uzair@example.com'), "
                    + "('shlok', '1234', 'shlok@example.com')");

            ModeratorDAO.addModerator("admin1", "12345","admin@example.com","12345567");
            System.out.println("Default users inserted successfully!");
        } else {
            System.out.println("Default users already exist. Skipping insertion.");
        }
    }

    private static void insertDefaultClients(Statement stmt) throws SQLException {
        String checkClientQuery = "SELECT COUNT(*) FROM Client WHERE username IN ('Uzair', 'shlok')";
        ResultSet rs = stmt.executeQuery(checkClientQuery);
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO Client (username, enrollment_number) VALUES "
                    + "('Uzair', '1001'), "
                    + "('shlok', '1002')");
            System.out.println("Default clients inserted successfully!");
        } else {
            System.out.println("Default clients already exist. Skipping insertion.");
        }
    }

    private static void insertDefaultProducts(Statement stmt) throws SQLException {
        // Ensure Clients exist before inserting products
        String checkClientCountQuery = "SELECT COUNT(*) FROM Client";
        ResultSet rs = stmt.executeQuery(checkClientCountQuery);
        if (rs.next() && rs.getInt(1) == 0) {
            System.out.println("No clients found. Skipping product insertion.");
            return;
        }

        // Get the first Client ID (since AUTO_INCREMENT can vary)
        String getFirstClientIdQuery = "SELECT c_id FROM Client ORDER BY c_id LIMIT 1";
        rs = stmt.executeQuery(getFirstClientIdQuery);
        int clientId = rs.next() ? rs.getInt(1) : -1;

        if (clientId == -1) {
            System.out.println("Could not determine a valid client ID. Skipping product insertion.");
            return;
        }

        String checkProductQuery = "SELECT COUNT(*) FROM Products";
        rs = stmt.executeQuery(checkProductQuery);
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO Products (pname, pdesc, p_price, qty, category, status, s_id) VALUES "
                    + "('Laptop', 'Dell XPS 13 - 16GB RAM, 512GB SSD', 12000.00, 2, 'Electronics', 'Available', " + clientId + "), "
                    + "('Smartphone', 'iPhone 13 - 128GB', 9990.99, 5, 'Electronics', 'Available', " + clientId + "), "
                    + "('Backpack', 'Nike Sport Backpack', 559.99, 10, 'Accessories', 'Available', " + clientId + ")");
            System.out.println("Default products inserted successfully!");
        } else {
            System.out.println("Default products already exist. Skipping insertion.");
        }
    }
//    private static void insertDefaultModerators(Statement stmt) throws SQLException {
//        String checkModeratorQuery = "SELECT COUNT(*) FROM User WHERE username = 'admin1'";
//        ResultSet rs = stmt.executeQuery(checkModeratorQuery);
//        if (rs.next() && rs.getInt(1) == 0) {
//            stmt.executeUpdate("INSERT INTO User (username, password, email) VALUES "
//                    + "('admin1', '12345', 'admin@example.com')");
//            System.out.println("Default moderator inserted successfully!");
//        } else {
//            System.out.println("Default moderator already exists. Skipping insertion.");
//        }
//    }

}
