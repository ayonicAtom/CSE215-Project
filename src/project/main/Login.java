package project.main;

import project.util.InputHelper;

public class Login {

    private final String password;

    public Login(String password) {
        this.password = password;
    }

    public boolean startLogin() {
        while (true) {
            String input = InputHelper.readString("Enter Password: ");

            if (input.equals(password)) {
                System.out.println("Successfully logged in.");
                return true;
            } else {
                System.out.println("Wrong password.");
                int choice = InputHelper.readInt("Press 1 to try again, 0 to exit: ");
                if (choice == 0) return false;
            }
        }
    }
}
