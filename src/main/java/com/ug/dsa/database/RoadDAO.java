package com.ug.dsa.database;

import com.ug.dsa.models.Road;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoadDAO {

    private static final String INSERT_SQL =
            "INSERT INTO roads " +
            "(road_id, from_location_id, to_location_id, distance, travel_time, road_condition_weight) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT road_id, from_location_id, to_location_id, distance, " +
            "travel_time, road_condition_weight FROM roads WHERE road_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT road_id, from_location_id, to_location_id, distance, " +
            "travel_time, road_condition_weight FROM roads ORDER BY road_id";

    private static final String UPDATE_SQL =
            "UPDATE roads SET from_location_id = ?, to_location_id = ?, " +
            "distance = ?, travel_time = ?, road_condition_weight = ? " +
            "WHERE road_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM roads WHERE road_id = ?";

    public boolean create(Road road) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, road.getRoadId());
            ps.setInt(2, road.getFromLocationId());
            ps.setInt(3, road.getToLocationId());
            ps.setDouble(4, road.getDistance());
            ps.setDouble(5, road.getTravelTime());
            ps.setDouble(6, road.getRoadConditionWeight());

            return ps.executeUpdate() > 0;
        }
    }

    public Road findById(int id) throws SQLException {
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

    public List<Road> findAll() throws SQLException {
        List<Road> roads = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roads.add(mapRow(rs));
            }
        }

        return roads;
    }

    public boolean update(Road road) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, road.getFromLocationId());
            ps.setInt(2, road.getToLocationId());
            ps.setDouble(3, road.getDistance());
            ps.setDouble(4, road.getTravelTime());
            ps.setDouble(5, road.getRoadConditionWeight());
            ps.setInt(6, road.getRoadId());

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

    private Road mapRow(ResultSet rs) throws SQLException {
        return new Road(
                rs.getInt("road_id"),
                rs.getInt("from_location_id"),
                rs.getInt("to_location_id"),
                rs.getDouble("distance"),
                rs.getDouble("travel_time"),
                rs.getDouble("road_condition_weight")
        );
    }
}
