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

        public String getUsername() {
            return username;
        }
        public String getRole() {
            return role;
        }

        public boolean checkPassword(String password) {
            return this.password.equals(password);
        }
    }

    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);


    public static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (Admin/Customer): ");
        String role = scanner.nextLine();

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
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static
}
