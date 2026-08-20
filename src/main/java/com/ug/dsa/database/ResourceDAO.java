package com.ug.dsa.database;

import com.ug.dsa.models.Resource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    private static final String INSERT_SQL =
            "INSERT INTO resources " +
            "(resource_id, type, home_location, capacity, availability_status) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT resource_id, type, home_location, capacity, " +
            "availability_status FROM resources WHERE resource_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT resource_id, type, home_location, capacity, " +
            "availability_status FROM resources ORDER BY resource_id";

    private static final String UPDATE_SQL =
            "UPDATE resources SET type = ?, home_location = ?, " +
            "capacity = ?, availability_status = ? " +
            "WHERE resource_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM resources WHERE resource_id = ?";

    private static final String AVAILABLE_SQL =
            "SELECT resource_id, type, home_location, capacity, " +
            "availability_status FROM resources " +
            "WHERE availability_status = 'AVAILABLE' " +
            "ORDER BY resource_id";

    private static final String UPDATE_AVAILABILITY_SQL =
            "UPDATE resources SET availability_status = ? " +
            "WHERE resource_id = ?";

    public boolean create(Resource resource) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, resource.getResourceId());
            ps.setString(2, resource.getType());
            ps.setInt(3, resource.getHomeLocation());
            ps.setInt(4, resource.getCapacity());
            ps.setString(5, resource.getAvailabilityStatus());

            return ps.executeUpdate() > 0;
        }
    }

    public Resource findById(int id) throws SQLException {
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

    public List<Resource> findAll() throws SQLException {
        List<Resource> resources = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resources.add(mapRow(rs));
            }
        }

        return resources;
    }

    public boolean update(Resource resource) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, resource.getType());
            ps.setInt(2, resource.getHomeLocation());
            ps.setInt(3, resource.getCapacity());
            ps.setString(4, resource.getAvailabilityStatus());
            ps.setInt(5, resource.getResourceId());

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

    public List<Resource> getAvailableResources() throws SQLException {
        List<Resource> resources = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(AVAILABLE_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resources.add(mapRow(rs));
            }
        }

        return resources;
    }

    public boolean updateResourceAvailability(
            int resourceId,
            String availabilityStatus) throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(UPDATE_AVAILABILITY_SQL)) {

            ps.setString(1, availabilityStatus);
            ps.setInt(2, resourceId);

            return ps.executeUpdate() > 0;
        }
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        return new Resource(
                rs.getInt("resource_id"),
                rs.getString("type"),
                rs.getInt("home_location"),
                rs.getInt("capacity"),
                rs.getString("availability_status")
        );
    }
}
