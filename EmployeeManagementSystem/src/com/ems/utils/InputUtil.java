package com.ems.utils;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static Scanner scanner() {
        return scanner;
    }

    public static String nextLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int nextInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = scanner.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}
