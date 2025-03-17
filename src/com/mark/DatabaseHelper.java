package com.mark;
import java.sql.*;

/**
 *
 * @author marko
 */

public class DatabaseHelper {
    private static final String DB_NAME = "Secure_Password_Manager";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "Padlock@20!";

    // Ensure the database exists before proceeding
    static {
        createDatabaseIfNotExists();
    }

    private static void createDatabaseIfNotExists() {
        String url = "jdbc:mysql://localhost:3306/"; // Connect to MySQL without selecting a database

        try (Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Create database if it doesn't exist
            String createDBSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(createDBSQL);
            System.out.println("Database '" + DB_NAME + "' is ready!");

        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    // Establish connection
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Initialize database by creating the table
    public static void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS passwords (
                id INT AUTO_INCREMENT PRIMARY KEY,
                site VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'passwords' is ready.");
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    // Save password into the database
    public static void savePassword(String site, String username, String password) {
        String insertSQL = "INSERT INTO passwords (site, username, password) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, site);
            pstmt.setString(2, username);
            pstmt.setString(3, password); // Assuming encryption is handled separately
            pstmt.executeUpdate();

            System.out.println("Password saved successfully!");

        } catch (SQLException e) {
            System.out.println("Error saving password: " + e.getMessage());
        }
    }

    // Retrieve password from the database
    public static void getPassword(String site) {
        String querySQL = "SELECT username, password FROM passwords WHERE site = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setString(1, site);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Site: " + site);
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Password: " + rs.getString("password")); // Decryption if needed
            } else {
                System.out.println("No record found for this site.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        }
    }
}
