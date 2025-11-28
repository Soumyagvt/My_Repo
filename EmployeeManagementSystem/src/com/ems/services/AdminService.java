package com.ems.services;

import com.ems.dao.DepartmentDAO;
import com.ems.dao.EmployeeDAO;
import com.ems.dao.UserDAO;
import com.ems.models.Department;
import com.ems.models.Employee;

import java.util.List;

public class AdminService {
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final UserDAO userDAO = new UserDAO();
    private final AuthService authService = new AuthService();

    // Department
    public boolean addDepartment(String name) {
        Department d = new Department();
        d.setName(name);
        return departmentDAO.create(d);
    }

    public List<Department> listDepartments() {
        return departmentDAO.listAll();
    }

    // Employee
    public boolean addEmployee(Employee e) {
        boolean ok = employeeDAO.create(e);
        return ok;
    }

    public List<Employee> listEmployees() {
        return employeeDAO.listAll();
    }

    public boolean assignManagerToEmployee(int employeeId, int managerId) {
        Employee e = employeeDAO.findById(employeeId);
        if (e == null) return false;
        e.setManagerId(managerId);
        return employeeDAO.update(e);
    }

    public boolean createUserForEmployee(String username, String password, String role, Integer linkedId) {
        return authService.register(username, password, role, linkedId);
    }
}
