package com.ug.dsa.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles connection management to the PostgreSQL database.
 */
public class DatabaseConnection {

    // Safely retrieve credentials from environment variables to avoid hardcoding
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/ug_smart_service_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "postgres");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("CRITICAL ERROR: PostgreSQL JDBC Driver not found in classpath.");
            e.printStackTrace();
        }
    }

    private DatabaseConnection() {}

    /**
     * Establishes and returns a database connection.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Connection Failed: Unable to connect to PostgreSQL database at " + URL);
            throw e;
        }
    }
}
