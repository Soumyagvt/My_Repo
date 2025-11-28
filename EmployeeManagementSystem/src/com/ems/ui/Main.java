package com.ems.ui;

import com.ems.models.User;
import com.ems.services.AuthService;
import com.ems.utils.InputUtil;

public class Main {
    private static final AuthService authService = new AuthService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== EMPLOYEE MANAGEMENT SYSTEM =====");
            System.out.println("1) Admin Login");
            System.out.println("2) Manager Login");
            System.out.println("3) Employee Login");
            System.out.println("4) Exit");
            int choice = InputUtil.nextInt("Enter choice: ");
            switch (choice) {
                case 1:
                    handleLogin("ADMIN");
                    break;
                case 2:
                    handleLogin("MANAGER");
                    break;
                case 3:
                    handleLogin("EMPLOYEE");
                    break;
                case 4:
                    System.out.println("Exiting. Bye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleLogin(String expectedRole) {
        String username = InputUtil.nextLine("Username: ");
        String password = InputUtil.nextLine("Password: ");
        User u = authService.login(username, password);
        if (u == null) return;
        if (!u.getRole().equalsIgnoreCase(expectedRole)) {
            System.out.println("Access denied. You are not " + expectedRole);
            return;
        }
        switch (expectedRole) {
            case "ADMIN":
                AdminMenu.show(u);
                break;
            case "MANAGER":
                ManagerMenu.show(u);
                break;
            case "EMPLOYEE":
                EmployeeMenu.show(u);
                break;
            default:
                System.out.println("Unknown role");
        }
    }
}
