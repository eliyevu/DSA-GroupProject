package com.ug.dsa.database;

import com.ug.dsa.models.AlgorithmRun;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmRunDAO {

    private static final String INSERT_SQL =
            "INSERT INTO algorithm_runs " +
            "(run_id, algorithm_name, input_size, time_ns, memory_kb, date_run) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT run_id, algorithm_name, input_size, time_ns, " +
            "memory_kb, date_run FROM algorithm_runs WHERE run_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT run_id, algorithm_name, input_size, time_ns, " +
            "memory_kb, date_run FROM algorithm_runs ORDER BY run_id";

    private static final String UPDATE_SQL =
            "UPDATE algorithm_runs SET algorithm_name = ?, input_size = ?, " +
            "time_ns = ?, memory_kb = ?, date_run = ? WHERE run_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM algorithm_runs WHERE run_id = ?";

    public boolean create(AlgorithmRun run) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, run.getRunId());
            ps.setString(2, run.getAlgorithmName());
            ps.setInt(3, run.getInputSize());
            ps.setLong(4, run.getTimeNs());
            ps.setDouble(5, run.getMemoryKb());
            ps.setString(6, run.getDateRun());

            return ps.executeUpdate() > 0;
        }
    }

    public AlgorithmRun findById(int id) throws SQLException {
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

    public List<AlgorithmRun> findAll() throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                runs.add(mapRow(rs));
            }
        }

        return runs;
    }

    public boolean update(AlgorithmRun run) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, run.getAlgorithmName());
            ps.setInt(2, run.getInputSize());
            ps.setLong(3, run.getTimeNs());
            ps.setDouble(4, run.getMemoryKb());
            ps.setString(5, run.getDateRun());
            ps.setInt(6, run.getRunId());

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

    private AlgorithmRun mapRow(ResultSet rs) throws SQLException {
        return new AlgorithmRun(
                rs.getInt("run_id"),
                rs.getString("algorithm_name"),
                rs.getInt("input_size"),
                rs.getLong("time_ns"),
                rs.getDouble("memory_kb"),
                rs.getString("date_run")
        );
    }
}
