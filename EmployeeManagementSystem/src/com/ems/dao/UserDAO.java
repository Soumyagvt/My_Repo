package com.ems.dao;

import com.ems.models.User;
import com.ems.utils.DBConnection;

import java.sql.*;

public class UserDAO {

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                int linked = rs.getInt("linked_id");
                if (rs.wasNull()) u.setLinkedId(null); else u.setLinkedId(linked);
                return u;
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.findByUsername: " + e.getMessage());
        }
        return null;
    }

    public boolean createUser(String username, String password, String role, Integer linkedId) {
        String sql = "INSERT INTO users (username, password, role, linked_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            if (linkedId == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, linkedId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("UserDAO.createUser: " + e.getMessage());
            return false;
        }
    }
}
