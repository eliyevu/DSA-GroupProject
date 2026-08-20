package com.ug.dsa.database;

import com.ug.dsa.models.AuditEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditEventDAO {

    private static final String INSERT_SQL =
            "INSERT INTO audit_events " +
            "(event_id, event_type, request_id, timestamp, description) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT event_id, event_type, request_id, timestamp, description " +
            "FROM audit_events WHERE event_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT event_id, event_type, request_id, timestamp, description " +
            "FROM audit_events ORDER BY event_id";

    private static final String UPDATE_SQL =
            "UPDATE audit_events SET event_type = ?, request_id = ?, " +
            "timestamp = ?, description = ? WHERE event_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM audit_events WHERE event_id = ?";

    public boolean create(AuditEvent event) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, event.getEventId());
            ps.setString(2, event.getEventType().name());
            ps.setInt(3, event.getRequestId());
            ps.setString(4, event.getTimestamp());
            ps.setString(5, event.getDescription());

            return ps.executeUpdate() > 0;
        }
    }

    public AuditEvent findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }

        return null;
    }

    public List<AuditEvent> findAll() throws SQLException {
        List<AuditEvent> events = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(mapRow(rs));
            }
        }

        return events;
    }

    public boolean update(AuditEvent event) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, event.getEventType().name());
            ps.setInt(2, event.getRequestId());
            ps.setString(3, event.getTimestamp());
            ps.setString(4, event.getDescription());
            ps.setInt(5, event.getEventId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private AuditEvent mapRow(ResultSet rs) throws SQLException {
        return new AuditEvent(
                rs.getInt("event_id"),
                AuditEvent.EventType.valueOf(rs.getString("event_type")),
                rs.getInt("request_id"),
                rs.getString("timestamp"),
                rs.getString("description")
        );
    }
}
