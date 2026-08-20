package com.ug.dsa.database;

import com.ug.dsa.models.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    private static final String INSERT_SQL =
            "INSERT INTO locations " +
            "(location_id, name, area, type, latitude, longitude) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT location_id, name, area, type, latitude, longitude " +
            "FROM locations WHERE location_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT location_id, name, area, type, latitude, longitude " +
            "FROM locations ORDER BY location_id";

    private static final String UPDATE_SQL =
            "UPDATE locations SET name = ?, area = ?, type = ?, " +
            "latitude = ?, longitude = ? WHERE location_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM locations WHERE location_id = ?";

    public boolean create(Location location) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, location.getLocationId());
            ps.setString(2, location.getName());
            ps.setString(3, location.getArea());
            ps.setString(4, location.getType());
            ps.setDouble(5, location.getLatitude());
            ps.setDouble(6, location.getLongitude());

            return ps.executeUpdate() > 0;
        }
    }

    public Location findById(int id) throws SQLException {
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

    public List<Location> findAll() throws SQLException {
        List<Location> locations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                locations.add(mapRow(rs));
            }
        }

        return locations;
    }

    public boolean update(Location location) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, location.getName());
            ps.setString(2, location.getArea());
            ps.setString(3, location.getType());
            ps.setDouble(4, location.getLatitude());
            ps.setDouble(5, location.getLongitude());
            ps.setInt(6, location.getLocationId());

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

    private Location mapRow(ResultSet rs) throws SQLException {
        return new Location(
                rs.getInt("location_id"),
                rs.getString("name"),
                rs.getString("area"),
                rs.getString("type"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")
        );
    }
}
