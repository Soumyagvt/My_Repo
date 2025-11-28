package com.ems.ui;

import com.ems.models.Employee;
import com.ems.models.User;
import com.ems.services.AdminService;
import com.ems.services.EmployeeService;
import com.ems.utils.InputUtil;

import java.sql.Date;
import java.util.List;

public class AdminMenu {
    private static final AdminService adminService = new AdminService();
    private static final EmployeeService employeeService = new EmployeeService();

    public static void show(User admin) {
        System.out.println("\nWelcome Admin: " + admin.getUsername());
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1) Add Department");
            System.out.println("2) List Departments");
            System.out.println("3) Add Employee");
            System.out.println("4) List Employees");
            System.out.println("5) Assign Manager to Employee");
            System.out.println("6) Create Login for Employee/Manager");
            System.out.println("7) Logout");
            int ch = InputUtil.nextInt("Choice: ");
            switch (ch) {
                case 1:
                    addDepartment();
                    break;
                case 2:
                    listDepartments();
                    break;
                case 3:
                    addEmployee();
                    break;
                case 4:
                    listEmployees();
                    break;
                case 5:
                    assignManager();
                    break;
                case 6:
                    createUser();
                    break;
                case 7:
                    System.out.println("Admin logout.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addDepartment() {
        String name = InputUtil.nextLine("Department name: ");
        boolean ok = adminService.addDepartment(name);
        System.out.println(ok ? "Department added." : "Failed to add department.");
    }

    private static void listDepartments() {
        System.out.println("\nDepartments:");
        adminService.listDepartments().forEach(d -> 
            System.out.println(d.getId() + " - " + d.getName())
        );
    }

    private static void addEmployee() {
        Employee e = new Employee();
        e.setFirstName(InputUtil.nextLine("First name: "));
        e.setLastName(InputUtil.nextLine("Last name: "));
        e.setEmail(InputUtil.nextLine("Email: "));
        e.setPhone(InputUtil.nextLine("Phone: "));
        e.setGender(InputUtil.nextLine("Gender: "));
        String dobStr = InputUtil.nextLine("DOB (yyyy-mm-dd) or blank: ");
        if (!dobStr.isEmpty()) e.setDob(Date.valueOf(dobStr));
        e.setJobTitle(InputUtil.nextLine("Job title: "));
        String mgr = InputUtil.nextLine("Manager ID or blank: ");
        if (!mgr.isEmpty()) e.setManagerId(Integer.parseInt(mgr));
        String did = InputUtil.nextLine("Department ID or blank: ");
        if (!did.isEmpty()) e.setDepartmentId(Integer.parseInt(did));
        String sal = InputUtil.nextLine("Salary or blank: ");
        if (!sal.isEmpty()) e.setSalary(Double.parseDouble(sal));
        String jdate = InputUtil.nextLine("Join date (yyyy-mm-dd) or blank: ");
        if (!jdate.isEmpty()) e.setJoinDate(Date.valueOf(jdate));
        e.setStatus("Active");
        boolean ok = adminService.addEmployee(e);
        System.out.println(ok ? "Employee added with ID: " + e.getId() : "Failed to add employee.");
    }

    private static void listEmployees() {
        List<Employee> list = adminService.listEmployees();
        System.out.println("\nEmployees:");
        for (Employee e : list) {
            System.out.printf("%d: %s %s | %s | Manager:%s | Dept:%s\n",
                    e.getId(), e.getFirstName(), e.getLastName(), e.getJobTitle(),
                    e.getManagerId(), e.getDepartmentId());
        }
    }

    private static void assignManager() {
        int eid = InputUtil.nextInt("Employee ID: ");
        int mid = InputUtil.nextInt("Manager ID: ");
        boolean ok = adminService.assignManagerToEmployee(eid, mid);
        System.out.println(ok ? "Manager assigned." : "Failed to assign manager.");
    }

    private static void createUser() {
        String username = InputUtil.nextLine("Username: ");
        String password = InputUtil.nextLine("Password: ");
        String role = InputUtil.nextLine("Role (MANAGER/EMPLOYEE): ").toUpperCase();
        String linked = InputUtil.nextLine("Linked employee ID (or blank): ");
        Integer linkedId = null;
        if (!linked.isEmpty()) linkedId = Integer.parseInt(linked);
        boolean ok = adminService.createUserForEmployee(username, password, role, linkedId);
        System.out.println(ok ? "User created." : "Failed to create user.");
    }
}
