import java.util.*;

// Combined User class and user management logic
public class UserModule {
    private static class User {
        private String username;
        private String password;
        private String role; // Admin or Customer

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() { return username; }
        public String getRole() { return role; }

        public boolean checkPassword(String password) {
            return this.password.equals(password);
        }
    }

    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void displayUserMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nUser Module:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    running = false;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void registerUser() {
        String username;
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
        } while (!validateUsername(username));

        String password;
        do {
            System.out.print("Enter password (min 6 characters): ");
            password = scanner.nextLine();
        } while (!validatePassword(password));

        String role;
        do {
            System.out.print("Enter role (Admin/Customer): ");
            role = scanner.nextLine();
        } while (!validateRole(role));

        if (findUserByUsername(username) == null) {
            users.add(new User(username, password, role));
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Username already exists. Please choose a different username.");
        }
    }

    public static void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = findUserByUsername(username);
        if (user != null && user.checkPassword(password)) {
            System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ").");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    private static boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be blank.");
            return false;
        }
        return true;
    }

    private static boolean validatePassword(String password) {
        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            return false;
        }
        return true;
    }

    private static boolean validateRole(String role) {
        if (role == null || (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("Customer"))) {
            System.out.println("Role must be either 'Admin' or 'Customer'.");
            return false;
        }
        return true;
    }
}
