package com.ems.dao;

import com.ems.models.Employee;
import com.ems.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public boolean create(Employee e) {
        String sql = "INSERT INTO employees (first_name,last_name,email,phone,gender,dob,job_title,manager_id,department_id,salary,join_date,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setString(5, e.getGender());
            if (e.getDob() != null) ps.setDate(6, e.getDob()); else ps.setNull(6, Types.DATE);
            ps.setString(7, e.getJobTitle());
            if (e.getManagerId() == null) ps.setNull(8, Types.INTEGER); else ps.setInt(8, e.getManagerId());
            if (e.getDepartmentId() == null) ps.setNull(9, Types.INTEGER); else ps.setInt(9, e.getDepartmentId());
            ps.setDouble(10, e.getSalary());
            if (e.getJoinDate() != null) ps.setDate(11, e.getJoinDate()); else ps.setNull(11, Types.DATE);
            ps.setString(12, e.getStatus());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) e.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("EmployeeDAO.create: " + ex.getMessage());
        }
        return false;
    }

    public Employee findById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.findById: " + e.getMessage());
        }
        return null;
    }

    public List<Employee> listAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.listAll: " + e.getMessage());
        }
        return list;
    }

    public List<Employee> listByManager(int managerId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE manager_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.listByManager: " + e.getMessage());
        }
        return list;
    }

    public boolean update(Employee e) {
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, phone=?, gender=?, dob=?, job_title=?, manager_id=?, department_id=?, salary=?, join_date=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setString(5, e.getGender());
            if (e.getDob() != null) ps.setDate(6, e.getDob()); else ps.setNull(6, Types.DATE);
            ps.setString(7, e.getJobTitle());
            if (e.getManagerId() == null) ps.setNull(8, Types.INTEGER); else ps.setInt(8, e.getManagerId());
            if (e.getDepartmentId() == null) ps.setNull(9, Types.INTEGER); else ps.setInt(9, e.getDepartmentId());
            ps.setDouble(10, e.getSalary());
            if (e.getJoinDate() != null) ps.setDate(11, e.getJoinDate()); else ps.setNull(11, Types.DATE);
            ps.setString(12, e.getStatus());
            ps.setInt(13, e.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("EmployeeDAO.update: " + ex.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.delete: " + e.getMessage());
            return false;
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setEmail(rs.getString("email"));
        e.setPhone(rs.getString("phone"));
        e.setGender(rs.getString("gender"));
        e.setDob(rs.getDate("dob"));
        e.setJobTitle(rs.getString("job_title"));
        int mid = rs.getInt("manager_id");
        if (rs.wasNull()) e.setManagerId(null); else e.setManagerId(mid);
        int did = rs.getInt("department_id");
        if (rs.wasNull()) e.setDepartmentId(null); else e.setDepartmentId(did);
        e.setSalary(rs.getDouble("salary"));
        e.setJoinDate(rs.getDate("join_date"));
        e.setStatus(rs.getString("status"));
        return e;
    }

}
