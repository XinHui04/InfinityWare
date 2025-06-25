/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.io.IOException;

public class UserModule {

    private String customerFile;
    private String adminFile;
    private static List<User> regularUsers = new ArrayList<>();
    private List<User> adminUsers;
    Colors color = new Colors();

    public UserModule() {
    }

    public UserModule(String customerFile, String adminFile) {
        this.customerFile = customerFile;
        this.adminFile = adminFile;
        this.regularUsers = loadUsersFromFile(customerFile);
        this.adminUsers = loadUsersFromFile(adminFile);
    }

    public void addUser(User user) {
        regularUsers.add(user);
    }

    public User findUserByUsername(String username) {
        for (User user : regularUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public User findAdminByUsername(String username) {
        for (User admin : adminUsers) {
            if (admin.getUsername().equalsIgnoreCase(username)) {
                return admin;
            }
        }
        return null;
    }

    public boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println(color.RED + "Username cannot be empty. Please try again." + color.RESET);
            return false;
        } else if (username.length() < 5) {
            System.out.println(color.RED + "Username must be more than 5 characters long." + color.RESET);
            return false;
        } else if (username.contains(" ")) {
            System.out.println(color.RED + "Username cannot contain spaces. Please try again." + color.RESET);
            return false;
        }
        return true;
    }

    public boolean validatePhoneNumber(String phoneInput) {
        if (phoneInput == null || phoneInput.trim().isEmpty()) {
            System.out.println(color.RED + "Phone number cannot be empty." + color.RESET);
            return false;
        } else if (!(phoneInput.matches("\\d{10,}"))) {
            System.out.println(color.RED + "Phone number must contain at least 10 digits." + color.RESET);
            return false;
        }
        return true;
    }

    public boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println(color.RED + "Email cannot be empty." + color.RESET);
            return false;
        } else if (!(Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email))) {
            System.out.println(color.RED + "Invalid email format. Please enter a valid email." + color.RESET);
            return false;
        }
        return true;
    }

    public boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            System.out.println(color.RED + "Password cannot be empty." + color.RESET);
            return false;
        } else if (password.length() < 8) {
            System.out.println(color.RED + "Password must be at least 8 characters long." + color.RESET);
            return false;
        } else if (!password.matches(".*[A-Z].*")) {
            System.out.println(color.RED + "Password must contain at least one uppercase letter." + color.RESET);
            return false;
        } else if (!password.matches(".*[a-z].*")) {
            System.out.println(color.RED + "Password must contain at least one lowercase letter." + color.RESET);
            return false;
        } else if (!password.matches(".*\\d.*")) {
            System.out.println(color.RED + "Password must contain at least one digit." + color.RESET);
            return false;
        } else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            System.out.println(color.RED + "Password must contain at least one special character (e.g., !, @, #, $, %, ^)." + color.RESET);
            return false;
        }
        return true;
    }

    public boolean isUsernameTaken(String username) {
        for (User user : regularUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println(color.RED + "Username already taken. Please choose a different username." + color.RESET);
                return true;
            }
        }
        return false;
    }

    public boolean isPhoneNumberTaken(String phoneInput) {
        try {
            List<String> userRecords = readFile("users.txt");
            List<String> adminRecords = readFile("admin.txt");
            for (String record : userRecords) {
                String[] data = record.split(",");
                if (data.length > 1 && data[1].trim().equals(phoneInput)) {
                    System.out.println(color.RED + "Phone number already associated with an account. Please enter a different phone number." + color.RESET);
                    return true;
                }
            }
            for (String record : adminRecords) {
                String[] data = record.split(",");
                if (data.length > 1 && data[1].trim().equals(phoneInput)) {
                    System.out.println(color.RED + "Phone number already associated with an account. Please enter a different phone number." + color.RESET);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
        return false;
    }

    public boolean isEmailTaken(String email) {
        try {
            List<String> userRecords = readFile("users.txt");
            List<String> adminRecords = readFile("admin.txt");
            for (String record : userRecords) {
                String[] data = record.split(",");
                if (data.length > 2 && data[2].trim().equalsIgnoreCase(email)) {
                    System.out.println(color.RED + "Email is already registered. Please enter a different email." + color.RESET);
                    return true;
                }
            }
            for (String record : adminRecords) {
                String[] data = record.split(",");
                if (data.length > 2 && data[2].trim().equalsIgnoreCase(email)) {
                    System.out.println(color.RED + "Email is already registered. Please enter a different email." + color.RESET);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
        return false;
    }

    private List<String> readFile(String fileName) throws IOException {
        List<String> records = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            records.add(line);
        }
        br.close();
        return records;
    }

    public void saveUsersToFile() {
        saveToFile(customerFile, regularUsers);
    }

    public void saveAdminsToFile() {
        saveToFile(adminFile, adminUsers);
    }

    private void saveToFile(String filename, List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPhoneNumber() + ","
                        + user.getEmail() + "," + user.getPassword() + "," + user.getUserType());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error saving to " + filename + ": " + e.getMessage() + color.RESET);
        }
    }
    private List<User> loadUsersFromFile(String filename) {
        List<User> users = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {  
                    String username = data[0];
                    String phone = data[1];
                    String email = data[2];
                    String password = data[3];
                    String userType = data[4].trim();

                    User user = User.createUser(userType, username, phone, email, password);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error loading users: " + e.getMessage() + color.RESET);
        }

        return users;
    }
}
