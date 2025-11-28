package com.ems.services;

import com.ems.dao.EmployeeDAO;
import com.ems.models.Employee;

import java.util.List;

public class EmployeeService {
    private final EmployeeDAO dao = new EmployeeDAO();

    public boolean addEmployee(Employee e) {
        return dao.create(e);
    }

    public Employee getEmployeeById(int id) {
        return dao.findById(id);
    }

    public List<Employee> listAll() {
        return dao.listAll();
    }

    public List<Employee> listByManager(int managerId) {
        return dao.listByManager(managerId);
    }

    public boolean updateEmployee(Employee e) {
        return dao.update(e);
    }

    public boolean deleteEmployee(int id) {
        return dao.delete(id);
    }
}
