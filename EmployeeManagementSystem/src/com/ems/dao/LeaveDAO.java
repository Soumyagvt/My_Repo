package com.ems.dao;

import com.ems.models.LeaveRequest;
import com.ems.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {
    public boolean create(LeaveRequest lr) {
        String sql = "INSERT INTO leave_requests (employee_id, manager_id, start_date, end_date, reason, status) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lr.getEmployeeId());
            if (lr.getManagerId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, lr.getManagerId());
            ps.setDate(3, lr.getStartDate());
            ps.setDate(4, lr.getEndDate());
            ps.setString(5, lr.getReason());
            ps.setString(6, lr.getStatus() == null ? "PENDING" : lr.getStatus());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) lr.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("LeaveDAO.create: " + e.getMessage());
        }
        return false;
    }

    public List<LeaveRequest> listByManager(int managerId) {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests WHERE manager_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeaveRequest lr = mapRow(rs);
                list.add(lr);
            }
        } catch (SQLException e) {
            System.err.println("LeaveDAO.listByManager: " + e.getMessage());
        }
        return list;
    }

    public List<LeaveRequest> listByEmployee(int employeeId) {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("LeaveDAO.listByEmployee: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE leave_requests SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("LeaveDAO.updateStatus: " + e.getMessage());
            return false;
        }
    }

    private LeaveRequest mapRow(ResultSet rs) throws SQLException {
        LeaveRequest lr = new LeaveRequest();
        lr.setId(rs.getInt("id"));
        lr.setEmployeeId(rs.getInt("employee_id"));
        int mid = rs.getInt("manager_id");
        if (rs.wasNull()) lr.setManagerId(null); else lr.setManagerId(mid);
        lr.setStartDate(rs.getDate("start_date"));
        lr.setEndDate(rs.getDate("end_date"));
        lr.setReason(rs.getString("reason"));
        lr.setStatus(rs.getString("status"));
        return lr;
    }
}
