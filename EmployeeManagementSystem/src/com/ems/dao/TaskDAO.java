package com.ems.dao;

import com.ems.models.Task;
import com.ems.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public boolean create(Task t) {
        String sql = "INSERT INTO tasks (employee_id, manager_id, title, description, status) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getEmployeeId());
            if (t.getManagerId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, t.getManagerId());
            ps.setString(3, t.getTitle());
            ps.setString(4, t.getDescription());
            ps.setString(5, t.getStatus() == null ? "TODO" : t.getStatus());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) t.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("TaskDAO.create: " + e.getMessage());
        }
        return false;
    }

    public List<Task> listByEmployee(int employeeId) {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task t = mapRow(rs);
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("TaskDAO.listByEmployee: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("TaskDAO.updateStatus: " + e.getMessage());
            return false;
        }
    }

    private Task mapRow(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getInt("id"));
        t.setEmployeeId(rs.getInt("employee_id"));
        int mid = rs.getInt("manager_id");
        if (rs.wasNull()) t.setManagerId(null); else t.setManagerId(mid);
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setStatus(rs.getString("status"));
        return t;
    }
}
