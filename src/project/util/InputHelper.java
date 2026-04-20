package project.util;

import java.util.Scanner;

public class InputHelper {

    private static Scanner s = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(s.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Please enter a whole number.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Double.parseDouble(s.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input. Please enter a number");
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return s.nextLine().trim();
    }

    public static String read(String prompt) {
        System.out.println(prompt);
        return s.nextLine().trim();
    }
}
