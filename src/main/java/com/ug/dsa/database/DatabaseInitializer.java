package com.ug.dsa.database;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializer class to create the database schema and constraints.
 */
public class DatabaseInitializer {

    public static void initializeDatabase() {
        // 1. Locations Table
        String createLocationsTable = """
            CREATE TABLE IF NOT EXISTS locations (
                location_id INT PRIMARY KEY,
                name VARCHAR(150) NOT NULL,
                latitude DOUBLE PRECISION NOT NULL,
                longitude DOUBLE PRECISION NOT NULL
            );
        """;

        // 2. Roads Table (Graph Edges)
        String createRoadsTable = """
            CREATE TABLE IF NOT EXISTS roads (
                road_id INT PRIMARY KEY,
                source_location_id INT NOT NULL REFERENCES locations(location_id) ON DELETE CASCADE,
                destination_location_id INT NOT NULL REFERENCES locations(location_id) ON DELETE CASCADE,
                distance_km DOUBLE PRECISION NOT NULL CHECK (distance_km >= 0),
                travel_time_mins DOUBLE PRECISION NOT NULL CHECK (travel_time_mins >= 0)
            );
        """;

        // 3. Service Requests Table
        String createServiceRequestsTable = """
            CREATE TABLE IF NOT EXISTS service_requests (
                request_id INT PRIMARY KEY,
                location_id INT NOT NULL REFERENCES locations(location_id) ON DELETE CASCADE,
                service_type VARCHAR(100) NOT NULL,
                priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
                status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED')),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        // 4. Resources Table
        String createResourcesTable = """
            CREATE TABLE IF NOT EXISTS resources (
                resource_id INT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                type VARCHAR(50) NOT NULL,
                current_location_id INT NOT NULL REFERENCES locations(location_id) ON DELETE CASCADE,
                is_available BOOLEAN DEFAULT TRUE
            );
        """;

        // 5. Algorithm Runs Table
        String createAlgorithmRunsTable = """
            CREATE TABLE IF NOT EXISTS algorithm_runs (
                run_id INT PRIMARY KEY,
                algorithm_name VARCHAR(100) NOT NULL,
                execution_time_ms BIGINT NOT NULL CHECK (execution_time_ms >= 0),
                run_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        // 6. Audit Events Table
        String createAuditEventsTable = """
            CREATE TABLE IF NOT EXISTS audit_events (
                event_id INT PRIMARY KEY,
                action VARCHAR(100) NOT NULL,
                entity_affected VARCHAR(50) NOT NULL,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                details TEXT
            );
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false); // Transaction management

            stmt.execute(createLocationsTable);
            stmt.execute(createRoadsTable);
            stmt.execute(createServiceRequestsTable);
            stmt.execute(createResourcesTable);
            stmt.execute(createAlgorithmRunsTable);
            stmt.execute(createAuditEventsTable);

            conn.commit();
            System.out.println("✅ All 6 tables and constraints created successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper runner method to test database initialization.
     */
    public static void main(String[] args) {
        System.out.println("Starting database initialization test...");
        initializeDatabase();
    }
}

