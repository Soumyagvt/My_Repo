package com.ems.ui;

import com.ems.models.Employee;
import com.ems.models.LeaveRequest;
import com.ems.models.Task;
import com.ems.models.User;
import com.ems.services.EmployeeService;
import com.ems.dao.LeaveDAO;
import com.ems.dao.TaskDAO;
import com.ems.utils.InputUtil;

import java.sql.Date;
import java.util.List;

public class EmployeeMenu {
    private static final EmployeeService employeeService = new EmployeeService();
    private static final LeaveDAO leaveDAO = new LeaveDAO();
    private static final TaskDAO taskDAO = new TaskDAO();

    public static void show(User user) {
        System.out.println("\nWelcome Employee: " + user.getUsername());
        Integer empId = user.getLinkedId();
        if (empId == null) {
            System.out.println("No linked employee id found. Contact admin.");
            return;
        }
        while (true) {
            System.out.println("\n--- EMPLOYEE MENU ---");
            System.out.println("1) View Profile");
            System.out.println("2) Update contact details");
            System.out.println("3) Apply Leave");
            System.out.println("4) View my leaves");
            System.out.println("5) View my tasks");
            System.out.println("6) Logout");
            int ch = InputUtil.nextInt("Choice: ");
            switch (ch) {
                case 1:
                    viewProfile(empId);
                    break;
                case 2:
                    updateContact(empId);
                    break;
                case 3:
                    applyLeave(empId);
                    break;
                case 4:
                    viewMyLeaves(empId);
                    break;
                case 5:
                    viewMyTasks(empId);
                    break;
                case 6:
                    System.out.println("Employee logout.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewProfile(int id) {
        Employee e = employeeService.getEmployeeById(id);
        if (e == null) {
            System.out.println("Profile not found.");
            return;
        }
        System.out.printf("ID:%d Name:%s %s Email:%s Phone:%s Job:%s Manager:%s Dept:%s\n",
                e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getPhone(), e.getJobTitle(), e.getManagerId(), e.getDepartmentId());
    }

    private static void updateContact(int id) {
        Employee e = employeeService.getEmployeeById(id);
        if (e == null) { System.out.println("Profile not found."); return; }
        String phone = InputUtil.nextLine("New phone or blank: ");
        if (!phone.isEmpty()) e.setPhone(phone);
        String email = InputUtil.nextLine("New email or blank: ");
        if (!email.isEmpty()) e.setEmail(email);
        boolean ok = employeeService.updateEmployee(e);
        System.out.println(ok ? "Updated." : "Update failed.");
    }

    private static void applyLeave(int id) {
        LeaveRequest lr = new LeaveRequest();
        lr.setEmployeeId(id);
        String sd = InputUtil.nextLine("Start date (yyyy-mm-dd): ");
        String ed = InputUtil.nextLine("End date (yyyy-mm-dd): ");
        lr.setStartDate(Date.valueOf(sd));
        lr.setEndDate(Date.valueOf(ed));
        lr.setReason(InputUtil.nextLine("Reason: "));
        lr.setStatus("PENDING");
        boolean ok = leaveDAO.create(lr);
        System.out.println(ok ? "Leave applied." : "Application failed.");
    }

    private static void viewMyLeaves(int id) {
        List<LeaveRequest> list = leaveDAO.listByEmployee(id);
        System.out.println("\nMy leaves:");
        for (LeaveRequest lr : list) {
            System.out.printf("%d: From:%s To:%s Status:%s Reason:%s\n", lr.getId(), lr.getStartDate(), lr.getEndDate(), lr.getStatus(), lr.getReason());
        }
    }

    private static void viewMyTasks(int id) {
        List<Task> list = taskDAO.listByEmployee(id);
        System.out.println("\nMy tasks:");
        for (Task t : list) {
            System.out.printf("%d: %s | %s | %s\n", t.getId(), t.getTitle(), t.getStatus(), t.getDescription());
        }
    }
}
