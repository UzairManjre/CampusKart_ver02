package Database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static final String CONFIG_FILE = "sqlcon.txt";

    public static Connection initializeDB() {
        if (!loadConfig()) {
            saveConfig(); // If no config exists, ask user and save
        }

        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
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

    public static boolean loadConfig() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            URL = br.readLine();
            USER = br.readLine();
            PASSWORD = br.readLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
            return false;
        }
    }

    public static void saveConfig() {
        Scanner scanner = new Scanner(System.in);

        // Set default URL
        System.out.print("Enter MySQL Database URL (default: jdbc:mysql://localhost:3306/): ");
        String inputURL = scanner.nextLine();
        URL = inputURL.isEmpty() ? "jdbc:mysql://localhost:3306/" : inputURL;

        System.out.print("Enter MySQL Username: ");
        USER = scanner.nextLine();

        System.out.print("Enter MySQL Password: ");
        PASSWORD = scanner.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write(URL + "\n");
            bw.write(USER + "\n");
            bw.write(PASSWORD + "\n");
            System.out.println("Database connection details saved to " + CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("Error saving config file: " + e.getMessage());
        }
    }
    public static void save() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write(URL + "\n");
            bw.write(USER + "\n");
            bw.write(PASSWORD + "\n");
            System.out.println("Database connection details saved to " + CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("Error saving config file: " + e.getMessage());
        }
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
