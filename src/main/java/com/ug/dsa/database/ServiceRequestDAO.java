package com.ug.dsa.database;

import com.ug.dsa.models.ServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {

    private static final String INSERT_SQL =
            "INSERT INTO service_requests " +
            "(request_id, source, destination, category, urgency, " +
            "time_submitted, deadline, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT request_id, source, destination, category, urgency, " +
            "time_submitted, deadline, status " +
            "FROM service_requests WHERE request_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT request_id, source, destination, category, urgency, " +
            "time_submitted, deadline, status " +
            "FROM service_requests ORDER BY request_id";

    private static final String UPDATE_SQL =
            "UPDATE service_requests SET source = ?, destination = ?, " +
            "category = ?, urgency = ?, time_submitted = ?, deadline = ?, " +
            "status = ? WHERE request_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM service_requests WHERE request_id = ?";

    private static final String PENDING_SQL =
            "SELECT request_id, source, destination, category, urgency, " +
            "time_submitted, deadline, status " +
            "FROM service_requests WHERE status = 'PENDING' " +
            "ORDER BY request_id";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE service_requests SET status = ? WHERE request_id = ?";

    public boolean create(ServiceRequest request) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, request.getRequestId());
            ps.setInt(2, request.getSource());
            ps.setInt(3, request.getDestination());
            ps.setString(4, request.getCategory());
            ps.setInt(5, request.getUrgency());
            ps.setString(6, request.getTimeSubmitted());
            ps.setString(7, request.getDeadline());
            ps.setString(8, request.getStatus());

            return ps.executeUpdate() > 0;
        }
    }

    public ServiceRequest findById(int id) throws SQLException {
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

    public List<ServiceRequest> findAll() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        }

        return requests;
    }

    public boolean update(ServiceRequest request) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, request.getSource());
            ps.setInt(2, request.getDestination());
            ps.setString(3, request.getCategory());
            ps.setInt(4, request.getUrgency());
            ps.setString(5, request.getTimeSubmitted());
            ps.setString(6, request.getDeadline());
            ps.setString(7, request.getStatus());
            ps.setInt(8, request.getRequestId());

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

    public List<ServiceRequest> getPendingRequests() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PENDING_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        }

        return requests;
    }

    public boolean updateRequestStatus(int requestId, String status)
            throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL)) {

            ps.setString(1, status);
            ps.setInt(2, requestId);

            return ps.executeUpdate() > 0;
        }
    }

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        return new ServiceRequest(
                rs.getInt("request_id"),
                rs.getInt("source"),
                rs.getInt("destination"),
                rs.getString("category"),
                rs.getInt("urgency"),
                rs.getString("time_submitted"),
                rs.getString("deadline"),
                rs.getString("status")
        );
    }
}
