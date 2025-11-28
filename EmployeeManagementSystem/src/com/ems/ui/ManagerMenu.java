package com.ems.ui;

import com.ems.models.LeaveRequest;
import com.ems.models.Task;
import com.ems.models.User;
import com.ems.services.EmployeeService;
import com.ems.services.ManagerService;
import com.ems.utils.InputUtil;

import java.util.List;

public class ManagerMenu {
    private static final ManagerService managerService = new ManagerService();
    private static final EmployeeService employeeService = new EmployeeService();

    public static void show(User manager) {
        System.out.println("\nWelcome Manager: " + manager.getUsername());
        Integer managerEmployeeId = manager.getLinkedId();
        if (managerEmployeeId == null) {
            System.out.println("No linked manager employee id found. Contact admin.");
            return;
        }
        while (true) {
            System.out.println("\n--- MANAGER MENU ---");
            System.out.println("1) View my team");
            System.out.println("2) View pending leave requests");
            System.out.println("3) Approve/Reject leave");
            System.out.println("4) Assign Task");
            System.out.println("5) View tasks for an employee");
            System.out.println("6) Logout");
            int ch = InputUtil.nextInt("Choice: ");
            switch (ch) {
                case 1:
                    viewTeam(managerEmployeeId);
                    break;
                case 2:
                    viewPendingLeaves(managerEmployeeId);
                    break;
                case 3:
                    handleLeaveDecision();
                    break;
                case 4:
                    assignTask(managerEmployeeId);
                    break;
                case 5:
                    viewTasksForEmployee();
                    break;
                case 6:
                    System.out.println("Manager logout.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewTeam(int managerId) {
        System.out.println("\nTeam members:");
        employeeService.listByManager(managerId).forEach(e ->
            System.out.printf("%d: %s %s | %s\n", e.getId(), e.getFirstName(), e.getLastName(), e.getJobTitle())
        );
    }

    private static void viewPendingLeaves(int managerId) {
        List<LeaveRequest> list = managerService.viewPendingLeaves(managerId);
        System.out.println("\nPending leaves:");
        for (LeaveRequest lr : list) {
            System.out.printf("%d: Employee:%d From:%s To:%s Reason:%s\n",
                    lr.getId(), lr.getEmployeeId(), lr.getStartDate(), lr.getEndDate(), lr.getReason());
        }
    }

    private static void handleLeaveDecision() {
        int lid = InputUtil.nextInt("Leave ID: ");
        String dec = InputUtil.nextLine("Enter APPROVE or REJECT: ").toUpperCase();
        if (!dec.equals("APPROVED") && !dec.equals("REJECTED") && !dec.equals("APPROVE") && !dec.equals("REJECT")) {
            System.out.println("Invalid choice. Use APPROVE or REJECT.");
            return;
        }
        String status = dec.startsWith("APPROV") ? "APPROVED" : "REJECTED";
        boolean ok = managerService.updateLeaveStatus(lid, status);
        System.out.println(ok ? "Leave updated." : "Failed to update leave.");
    }

    private static void assignTask(int managerId) {
        Task t = new Task();
        t.setManagerId(managerId);
        t.setEmployeeId(InputUtil.nextInt("Employee ID: "));
        t.setTitle(InputUtil.nextLine("Title: "));
        t.setDescription(InputUtil.nextLine("Description: "));
        boolean ok = managerService.assignTask(t);
        System.out.println(ok ? "Task assigned." : "Failed to assign task.");
    }

    private static void viewTasksForEmployee() {
        int eid = InputUtil.nextInt("Employee ID: ");
        List<com.ems.models.Task> tasks = managerService.viewTasksForEmployee(eid);
        System.out.println("\nTasks:");
        for (com.ems.models.Task t : tasks) {
            System.out.printf("%d: %s | %s | %s\n", t.getId(), t.getTitle(), t.getStatus(), t.getDescription());
        }
    }
}
