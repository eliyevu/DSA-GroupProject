package com.ug.dsa.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates the PostgreSQL schema used by the Campus Service Hub.
 *
 * The column names intentionally match the project models and DAOs.
 */
public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() throws SQLException {
        String locations = """
            CREATE TABLE IF NOT EXISTS locations (
                location_id INT PRIMARY KEY,
                name VARCHAR(150) NOT NULL,
                area VARCHAR(150) NOT NULL,
                type VARCHAR(100) NOT NULL,
                latitude DOUBLE PRECISION NOT NULL,
                longitude DOUBLE PRECISION NOT NULL
            )
            """;

        String roads = """
            CREATE TABLE IF NOT EXISTS roads (
                road_id INT PRIMARY KEY,
                from_location_id INT NOT NULL,
                to_location_id INT NOT NULL,
                distance DOUBLE PRECISION NOT NULL CHECK (distance >= 0),
                travel_time DOUBLE PRECISION NOT NULL CHECK (travel_time >= 0),
                road_condition_weight DOUBLE PRECISION NOT NULL CHECK (road_condition_weight > 0),
                CONSTRAINT fk_road_from
                    FOREIGN KEY (from_location_id) REFERENCES locations(location_id) ON DELETE CASCADE,
                CONSTRAINT fk_road_to
                    FOREIGN KEY (to_location_id) REFERENCES locations(location_id) ON DELETE CASCADE
            )
            """;

        String serviceRequests = """
            CREATE TABLE IF NOT EXISTS service_requests (
                request_id INT PRIMARY KEY,
                source INT NOT NULL,
                destination INT NOT NULL,
                category VARCHAR(100) NOT NULL,
                urgency INT NOT NULL CHECK (urgency >= 1),
                time_submitted VARCHAR(50) NOT NULL,
                deadline VARCHAR(50) NOT NULL,
                status VARCHAR(30) NOT NULL,
                CONSTRAINT fk_request_source
                    FOREIGN KEY (source) REFERENCES locations(location_id),
                CONSTRAINT fk_request_destination
                    FOREIGN KEY (destination) REFERENCES locations(location_id)
            )
            """;

        String resources = """
            CREATE TABLE IF NOT EXISTS resources (
                resource_id INT PRIMARY KEY,
                type VARCHAR(100) NOT NULL,
                home_location INT NOT NULL,
                capacity INT NOT NULL CHECK (capacity >= 0),
                availability_status VARCHAR(50) NOT NULL,
                CONSTRAINT fk_resource_home
                    FOREIGN KEY (home_location) REFERENCES locations(location_id)
            )
            """;

        String algorithmRuns = """
            CREATE TABLE IF NOT EXISTS algorithm_runs (
                run_id INT PRIMARY KEY,
                algorithm_name VARCHAR(150) NOT NULL,
                input_size INT NOT NULL CHECK (input_size >= 0),
                time_ns BIGINT NOT NULL CHECK (time_ns >= 0),
                memory_kb DOUBLE PRECISION NOT NULL CHECK (memory_kb >= 0),
                date_run VARCHAR(50) NOT NULL
            )
            """;

        String auditEvents = """
            CREATE TABLE IF NOT EXISTS audit_events (
                event_id INT PRIMARY KEY,
                event_type VARCHAR(50) NOT NULL,
                request_id INT NOT NULL,
                timestamp VARCHAR(50) NOT NULL,
                description TEXT NOT NULL,
                CONSTRAINT fk_audit_request
                    FOREIGN KEY (request_id) REFERENCES service_requests(request_id) ON DELETE CASCADE
            )
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);
            try {
                statement.execute(locations);
                statement.execute(roads);
                statement.execute(serviceRequests);
                statement.execute(resources);
                statement.execute(algorithmRuns);
                statement.execute(auditEvents);
                connection.commit();
                System.out.println("  [DB] Schema initialized successfully.");
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
