package com.ems.dao;

import com.ems.models.Department;
import com.ems.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public boolean create(Department d) {
        String sql = "INSERT INTO departments (name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) d.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("DepartmentDAO.create: " + e.getMessage());
        }
        return false;
    }

    public List<Department> listAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Department d = new Department(rs.getInt("id"), rs.getString("name"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.err.println("DepartmentDAO.listAll: " + e.getMessage());
        }
        return list;
    }

    public Department findById(int id) {
        String sql = "SELECT * FROM departments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Department(rs.getInt("id"), rs.getString("name"));
        } catch (SQLException e) {
            System.err.println("DepartmentDAO.findById: " + e.getMessage());
        }
        return null;
    }
}
