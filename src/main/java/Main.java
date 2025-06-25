/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import javax.swing.*;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Timer;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

    public static void main(String[] args) {
        Colors color = new Colors();
        Timers time = new Timers();
        Timer countdownTimer = new Timer();
        Scanner scanner = new Scanner(System.in);
        String identity = "";
        String username;
        String phoneInput;
        String email;
        String password;
        String userStatus = "";
        String confirmPassword;
        UserModule regularUsers = new UserModule("users.txt", "admin.txt");
        JFrame frame;
        JPasswordField passwordField;
        int option;
        User user;
        boolean running = true;
        Timers.countdownActive = false;
        OrderHistory.updateOrderStatusIfDelivered();
        System.out.println(" ___  _  _  ___  ___  _  _  ___  _____ __  __ _      __ _    ____  ___");
        System.out.println("|_ _|| \\| || __||_ _|| \\| ||_ _||_   _|\\ \\/ /\\ \\ /\\ / // \\  | _  \\| __|");
        System.out.println(" | | | .` || _|  | | | .` | | |   | |   \\  /  \\ V  V // _ \\ | `  /| _|");
        System.out.println("|___||_|\\_||_|  |___||_|\\_||___|  |_|   /_/    \\_/\\_//_/ \\_\\|_|\\_\\|___|");

        while (running) {
            boolean status;
            System.out.println("\nInfinityWare Retail Management:");
            System.out.println("1. Register");
            System.out.println("2. Login as customer/student");
            System.out.println("3. Login as admin/manager");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty() || !input.matches("\\d+")) {
                System.out.println(Colors.RED + "Invalid input. Please enter a valid number (1-4)." + Colors.RESET);
                continue;
            }

            int choice = Integer.parseInt(input);

            if (choice < 1 || choice > 4) {
                System.out.println(color.RED + "Please choose a number between 1 and 4." + color.RESET);
                continue;
            }

            switch (choice) {
                case 1:
                    boolean backToMenu = false;
                    boolean validIdentity = false;
                    String selectedIdentity = "";
                    System.out.println("\n Identity");
                    System.out.println("1. Student");
                    System.out.println("2. Customer");
                    do {
                        System.out.print("\nSelect your identity (1 or 2 (or 0 to go back)): ");
                        identity = scanner.nextLine().trim();
                        if (identity.isEmpty() || !identity.matches("\\d+")) {
                            System.out.println(Colors.RED + "Invalid input. Please enter a valid number (0-2)." + Colors.RESET);
                            continue;
                        }
                        if (identity.equals("0")) {
                            backToMenu = true;
                            validIdentity = true;
                            break;
                        }
                        choice = Integer.parseInt(identity);
                        if (choice == 1) {
                            selectedIdentity = "Student";
                        } else if (choice == 2) {
                            selectedIdentity = "Customer";
                        } else {
                            System.out.println("Invalid choice. Enter 0 or 1 or 2 only.");
                            continue;
                        }
                        validIdentity = true;
                    } while (!validIdentity);
                    if (backToMenu) {
                        break;
                    }
                    do {
                        System.out.print("\nEnter username (press '0' to cancel): ");
                        username = scanner.nextLine().trim().toLowerCase();
                        if (username.equals("0")) {
                            backToMenu = true;
                            break;
                        }
                    } while (!regularUsers.validateUsername(username) || regularUsers.isUsernameTaken(username));
                    if (backToMenu) {
                        break;
                    }
                    do {
                        System.out.print("Enter your phone number (EX: 0183298908): ");
                        phoneInput = scanner.nextLine().trim();
                    } while (!regularUsers.validatePhoneNumber(phoneInput) || regularUsers.isPhoneNumberTaken(phoneInput));
                    String verificationCode = null;
                    boolean isEmailVerified = false;
                    int maxVerificationAttempts = 3;
                    int emailSendAttempts = 0;
                    do {
                        System.out.print("Enter your email (EX: siow@gmail.com): ");
                        email = scanner.nextLine().trim();
                    } while (!regularUsers.validateEmail(email) || regularUsers.isEmailTaken(email));
                    while (emailSendAttempts < 2 && !isEmailVerified) {
                        verificationCode = String.format("%06d", new Random().nextInt(1000000));
                        Email emailSender = new Email("siowkaiying06@gmail.com", "bzgb kgpt lobg deor");
                        if (emailSender.sendVerificationCode(email, verificationCode)) {
                            emailSendAttempts++;
                            for (int attempt = 1; attempt <= maxVerificationAttempts; attempt++) {
                                System.out.print("Enter the verification code sent to your email: ");
                                String enteredCode = scanner.nextLine().trim();
                                if (enteredCode.equals(verificationCode)) {
                                    isEmailVerified = true;
                                    break;
                                } else {
                                    System.out.println(color.RED + "Incorrect code. Try again." + color.RESET);
                                }
                            }
                            if (!isEmailVerified && emailSendAttempts < 2) {
                                System.out.println(color.YELLOW + "You have failed to verify. Sending a new verification code..." + color.RESET);
                            }
                        } else {
                            System.out.println(color.RED + "Failed to send email. Registration cancelled." + color.RESET);
                            return;
                        }
                    }
                    if (!isEmailVerified) {
                        System.out.println(color.RED + "Too many failed attempts. Registration cancelled." + color.RESET);
                        break;
                    }
                    do {
                        System.out.println("\nPassword must meet the following requirements:");
                        System.out.println("1. At least 8 characters long.");
                        System.out.println("2. Contain at least one uppercase letter.");
                        System.out.println("3. Contain at least one lowercase letter.");
                        System.out.println("4. Contain at least one digit.");
                        System.out.println("5. Contain at least one special character (e.g., !, @, #, $, %, ^).");
                        System.out.print("Enter password: ");
                        password = scanner.nextLine();
                    } while (!regularUsers.validatePassword(password));
                    do {
                        System.out.print("Confirm password: ");
                        confirmPassword = scanner.nextLine();
                        if (!password.equals(confirmPassword)) {
                            System.out.println(color.RED + "Passwords do not match. Please try again.\n" + color.RESET);
                        }
                    } while (!password.equals(confirmPassword));
                    User newUser = null;
                    if (selectedIdentity.equals("Student")) {
                        newUser = new Student(username, phoneInput, email, password, "Student");
                    } else if (selectedIdentity.equals("Customer")) {
                        newUser = new Customer(username, phoneInput, email, password, "Customer");
                    }
                    if (newUser != null) {
                        regularUsers.addUser(newUser);
                        regularUsers.saveUsersToFile();
                        System.out.println(color.GREEN + "\nUser registered successfully as " + selectedIdentity + "!" + color.RESET);
                    } else {
                        System.out.println(color.RED + "Account already exists. Please login instead." + color.RESET);
                    }
                    break;
                case 2:
                    user = null;
                    boolean back = false;
                    while (!back) {
                        do {
                            System.out.print("\nEnter username (press '0' to cancel): ");
                            username = scanner.nextLine().trim().toLowerCase();
                            if (username.isEmpty()) {
                                System.out.println(color.RED + "Username cannot be empty. Please try again." + color.RESET);
                            }
                            if (username.equals("0")) {
                                back = true;
                                break;
                            }
                            user = regularUsers.findUserByUsername(username);
                            if (user == null) {
                                System.out.println(color.RED + "Invalid username." + color.RESET);
                                continue;
                            }
                            if (time.isUserBlocked(username, false)) {
                                System.out.println(color.RED + "Login cancelled due to failed verification." + color.RESET);
                                time.startCountdownTimer(username, time.getBlockRemaining(username));
                                System.out.println("\n\nPress any key and [Enter] to return to main menu...");
                                scanner.nextLine();
                                Timers.countdownActive = false;
                                back = true;
                                break;
                            }
                        } while (user == null);
                        if (back) {
                            break;
                        }
                        user = regularUsers.findUserByUsername(username);
                        if (user != null) {
                            frame = new JFrame();
                            frame.setAlwaysOnTop(true);
                            passwordField = new JPasswordField();
                            option = JOptionPane.showConfirmDialog(frame, passwordField,
                                    "Enter your password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (option == JOptionPane.OK_OPTION) {
                                char[] passwordChars = passwordField.getPassword();
                                password = new String(passwordChars);
                                if (user.checkPassword(password)) {
                                    System.out.println(color.GREEN + "Login successful! Welcome, " + user.getUsername() + color.RESET);
                                    User.setLoggedInUser(user);
                                    System.out.println("Redirecting to product menu...");
                                    status = user(user.getUsername());
                                    if (!status) {
                                        System.out.println("Returning to main menu...");
                                        break;
                                    }
                                } else {
                                    boolean existAttempt = time.handleFailedLogin(username);
                                    if (existAttempt) {
                                        String sendEmail;
                                        boolean validInput = false;
                                        while (!validInput) {
                                            System.out.print("Forgot password? Send email to verify (Yes/No): ");
                                            sendEmail = scanner.nextLine().trim().toLowerCase();
                                            if (sendEmail.equals("yes") || sendEmail.equals("y")) {
                                                validInput = true;
                                                break;
                                            } else if (sendEmail.equals("no") || sendEmail.equals("n")) {
                                                System.out.println(color.BLUE + "Login cancelled." + color.RESET);
                                                back = true;
                                                break;
                                            } else {
                                                System.out.println(color.RED + "Invalid input. Please enter Yes or No." + color.RESET);
                                            }
                                        }
                                        if (back) {
                                            break;
                                        }
                                        isEmailVerified = false;
                                        emailSendAttempts = 0;
                                        maxVerificationAttempts = 3;
                                        while (emailSendAttempts < 2 && !isEmailVerified) {
                                            verificationCode = String.format("%06d", new Random().nextInt(1000000));
                                            Email emailSender = new Email("siowkaiying06@gmail.com", "bzgb kgpt lobg deor");
                                            if (!emailSender.sendVerificationCode(user.getEmail(), verificationCode)) {
                                                System.out.println(color.RED + "Failed to send email. Login cancelled." + color.RESET);
                                                back = true;
                                                break;
                                            }
                                            emailSendAttempts++;
                                            for (int attempt = 1; attempt <= maxVerificationAttempts; attempt++) {
                                                System.out.print("Enter the verification code sent to your email: ");
                                                String enteredCode = scanner.nextLine().trim();
                                                if (enteredCode.equals(verificationCode)) {
                                                    isEmailVerified = true;
                                                    break;
                                                } else {
                                                    System.out.println(color.RED + "Incorrect code. Try again." + color.RESET);
                                                }
                                            }
                                            if (!isEmailVerified && emailSendAttempts < 2) {
                                                System.out.println(color.YELLOW + "Verification failed. Resending a new code..." + color.RESET);
                                            }
                                        }
                                        if (!isEmailVerified) {
                                            System.out.println(color.RED + "Login cancelled due to failed verification." + color.RESET);
                                            time.startCountdownTimer(username, time.getBlockRemaining(username));
                                            System.out.println("\n\nPress any key and [Enter] to return to main menu...");
                                            scanner.nextLine();
                                            Timers.countdownActive = false;
                                            back = true;
                                            break;
                                        } else {
                                            String newPassword, newConfirmPassword;
                                            do {
                                                System.out.println("\nEnter your new password:");
                                                System.out.println("--Password must meet the following requirements:");
                                                System.out.println("1. At least 8 characters long.");
                                                System.out.println("2. Contain at least one uppercase letter.");
                                                System.out.println("3. Contain at least one lowercase letter.");
                                                System.out.println("4. Contain at least one digit.");
                                                System.out.println("5. Contain at least one special character (e.g., !, @, #, $, %, ^).");
                                                System.out.print("Enter password: ");
                                                newPassword = scanner.nextLine();
                                            } while (!regularUsers.validatePassword(newPassword));
                                            do {
                                                System.out.print("Confirm password: ");
                                                newConfirmPassword = scanner.nextLine();
                                                if (!newPassword.equals(newConfirmPassword)) {
                                                    System.out.println(color.RED + "Passwords do not match. Please try again.\n" + color.RESET);
                                                }
                                            } while (!newPassword.equals(newConfirmPassword));
                                            user.setPassword(newPassword);
                                            regularUsers.saveUsersToFile();
                                            System.out.println(color.GREEN + "\nPassword changed successfully!" + color.RESET);
                                            time.isUserBlocked(username, true);
                                            continue;
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println(color.BLUE + "Login cancelled." + color.RESET);
                        }
                    }
                    break;
                case 3:
                    user = null;
                    back = false;
                    do {
                        System.out.print("\nEnter username (press '0' to cancel): ");
                        username = scanner.nextLine().trim().toLowerCase();
                        if (username.isEmpty()) {
                            System.out.println(color.RED + "Username cannot be empty. Please try again." + color.RESET);
                        }
                        if (username.equals("0")) {
                            back = true;
                            break;
                        }
                        if (time.isUserBlocked(username, false)) {
                            time.startCountdownTimer(username, time.getBlockRemaining(username));
                            System.out.println("\n\nPress any key and [Enter] to return to main menu...");
                            scanner.nextLine();
                            countdownTimer.cancel();
                            Timers.countdownActive = false;
                            countdownTimer.cancel();
                            back = true;
                            break;
                        }
                        user = regularUsers.findAdminByUsername(username);
                        if (user == null) {
                            System.out.println(color.RED + "Invalid username." + color.RESET);
                        }
                    } while (user == null);
                    if (back) {
                        continue;
                    }
                    if (user != null) {
                        frame = new JFrame();
                        frame.setAlwaysOnTop(true);
                        passwordField = new JPasswordField();
                        option = JOptionPane.showConfirmDialog(frame, passwordField,
                                "Enter your password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (option == JOptionPane.OK_OPTION) {
                            char[] passwordChars = passwordField.getPassword();
                            password = new String(passwordChars);
                            if (user.checkPassword(password)) {  // Check password of admin user
                                System.out.println(color.GREEN + "Admin login successful! Welcome, " + user.getUsername() + color.RESET);
                                time.isUserBlocked(username, true);
                                User.setLoggedInUser(user);
                                System.out.println("Redirecting to admin menu...");
                                status = admin(user.getUsername());
                                if (!status) {
                                    System.out.println("Returning to main menu...");
                                    break;
                                }
                            } else {
                                time.handleFailedLogin(username);
                            }
                        } else {
                            System.out.println(color.BLUE + "Login cancelled." + color.RESET);
                        }
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting system...");
                    System.exit(0);
                    break;
                default:
                    System.out.println(color.RED + "Invalid choice. Please try again." + color.RESET);
            }
        }
        scanner.close();
    }

    public static boolean user(String username) {
        Colors color = new Colors();
        Scanner scanner = new Scanner(System.in);
        UserModule userModule = new UserModule();
        User customer = userModule.findUserByUsername(username);
        OrderHistory orderHistory = new OrderHistory(customer.getUsername());
        ShoppingCart cart = new ShoppingCart(customer.getUsername(), orderHistory);
        cart.loadCartItems();
        if (customer instanceof Student) {
            System.out.println("Customer is a Student. Setting cart.");
            ((Student) customer).setCart(cart);
        } else if (customer instanceof Customer) {
            System.out.println("Customer is a Customer. Setting cart.");
            ((Customer) customer).setCart(cart);
        } else {
            System.out.println("Customer is neither Student nor Customer. Cannot set cart.");
        }
        ProductModule menu = new ProductModule();
        CartItem cartItem = new CartItem();
        String productId = "";
        int quantity = 0;
        int type = 0;
        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. View Products");
            System.out.println("2. View Cart");
            System.out.println("3. View Order History");
            System.out.println("4. Get Refund");
            System.out.println("5. Profile");
            System.out.println("6. Real-Time Chat");
            System.out.println("7. Log Out");
            System.out.print("Choose an option: ");
            String menuInput = scanner.nextLine().trim();
            if (!menuInput.matches("\\d+")) {
                System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                continue; // Repeat the menu if input is invalid
            }
            int choice = Integer.parseInt(menuInput);
            if (choice < 1 || choice > 7) {
                System.out.println(color.RED + "Invalid option. Please choose a valid number between 1 and 6." + color.RESET);
                continue;
            }
            switch (choice) {
                case 1:
                    String category = null;
                    boolean stayInCategoryMenu = true;
                    while (stayInCategoryMenu) {
                        System.out.println("\nProduct Category:");
                        System.out.println("1. Computer Components");
                        System.out.println("2. Peripherals & Accessories");
                        System.out.println("3. Networking & Connectivity");
                        System.out.println("4. Back");
//                            System.out.print("Enter your choice: ");
                        System.out.print("Enter your choice: ");
                        String input = scanner.nextLine().trim();

                        if (input.isEmpty()) {
                            System.out.println(color.RED + "Input cannot be empty. Please enter a valid choice." + color.RESET);
                            continue;
                        }
                        int categoryChoice;
                        try {
                            categoryChoice = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println(color.RED + "Invalid input. Please enter a valid number." + color.RESET);
                            continue;
                        }
                        switch (categoryChoice) {
                            case 1:
                                category = "CC";
                                stayInCategoryMenu = false;
                                break;
                            case 2:
                                category = "PA";
                                stayInCategoryMenu = false;
                                break;
                            case 3:
                                category = "NC";
                                stayInCategoryMenu = false;
                                break;
                            case 4:
                                stayInCategoryMenu = false;
                                break;
                            default:
                                System.out.println(color.RED + "Invalid choice. Please enter again." + color.RESET);
                        }
                    }
                    if (category == null) {
                        break;
                    }
                    List<Product> currentProductList = menu.getProductsByCategory(category);
                    boolean stayInProductMenu = true;
                    while (stayInProductMenu) {
                        customer.viewProducts(cart, category, currentProductList);
                        System.out.println("\n1. Add Product to Cart");
                        System.out.println("2. Search Product");
                        System.out.println("3. Sort Products By Price");
                        System.out.println("4. Sort Products By Specific Type");
                        System.out.println("5. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        String input = scanner.nextLine().trim();
                        if (!input.matches("\\d+")) {
                            System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                            continue;
                        }
                        int mainChoice = Integer.parseInt(input);
                        if (mainChoice < 1 || mainChoice > 5) {
                            System.out.println(color.RED + "Please choose a number between 1 and 4." + color.RESET);
                            continue;
                        }
                        switch (mainChoice) {
                            case 1:
                                boolean addToCart = true;
                                while (addToCart) {
                                    System.out.print("Enter Product ID to add to cart (or 0 to go back): ");
                                    productId = scanner.nextLine().trim();
                                    if (productId.equals("0")) {
                                        addToCart = false;
                                        break;
                                    }
                                    if (productId.isEmpty()) {
                                        System.out.println(color.RED + "Product ID cannot be empty." + color.RESET);
                                        continue;
                                    }
                                    final String finalProductId = productId;
                                    boolean productExists = currentProductList.stream()
                                            .anyMatch(p -> p.getProductID().equalsIgnoreCase(finalProductId));
                                    if (!productExists) {
                                        System.out.println(color.RED + "Product not found in this category." + color.RESET);
                                        continue;
                                    }
                                    boolean validQuantity = false;
                                    while (!validQuantity) {
                                        System.out.print("Enter quantity (or 0 to cancel): ");
                                        String qtyInput = scanner.nextLine().trim();
                                        if (qtyInput.equals("0")) {
                                            System.out.println(color.BLUE + "Cancelled adding product to cart." + color.RESET);
                                            validQuantity = true;
                                            break;
                                        }
                                        try {
                                            quantity = Integer.parseInt(qtyInput);
                                            if (quantity <= 0) {
                                                System.out.println(color.RED + "Quantity must be a positive number." + color.RESET);
                                                continue;
                                            }
                                            menu.addToCart(cart, productId, quantity);
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println(color.RED + "Invalid quantity. Please enter a valid number." + color.RESET);
                                        }
                                    }
                                }
                                break;
                            case 2: // Search Product
                                boolean searchingProducts = true;
                                while (searchingProducts) {
                                    System.out.print("Enter product ID or name (0 to go back): ");
                                    String searchQuery = scanner.nextLine().trim();

                                    if (searchQuery.equals("0")) {
                                        break;
                                    }

                                    boolean found = menu.searchProduct(searchQuery, category);

                                    if (!found) {
                                        System.out.println(color.RED + "No products found matching '" + searchQuery + "'." + color.RESET);
                                        System.out.print("Search for another product? (Y/N): ");
                                        if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                                            searchingProducts = false;
                                        }
                                        continue;
                                    }

                                    // Products found - proceed
                                    List<Product> foundProducts = menu.getFoundProducts();

                                    // Display found products
                                    System.out.println(color.GREEN + "\nFound " + foundProducts.size() + " product(s):" + color.RESET);
                                    for (int i = 0; i < foundProducts.size(); i++) {
                                        Product p = foundProducts.get(i);
                                        System.out.printf("%d. %s (ID: %s) - $%.2f\n",
                                                (i+1), p.getName(), p.getProductID(), p.getPrice());
                                    }

                                    // Ask if user wants to add to cart
                                    System.out.print("\nDo you want to add a product to cart? (Y/Other key to cancel): ");
                                    if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                                        System.out.print("Search for another product? (Y/Other key to cancel): ");
                                        if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                                            searchingProducts = false;
                                        }
                                        continue;
                                    }

                                    // Select a product if multiple were found
                                    Product selectedProduct = null;
                                    if (foundProducts.size() > 1) {
                                        boolean validSelection = false;
                                        while (!validSelection) {
                                            System.out.print("Enter product number (1-" + foundProducts.size() + ", or 0 to cancel): ");
                                            String selectionInput = scanner.nextLine().trim();

                                            if (selectionInput.equals("0")) {
                                                validSelection = true;
                                            } else {
                                                try {
                                                    int selection = Integer.parseInt(selectionInput);
                                                    if (selection >= 1 && selection <= foundProducts.size()) {
                                                        selectedProduct = foundProducts.get(selection - 1);
                                                        menu.setSelectedProduct(selectedProduct);
                                                        validSelection = true;
                                                    } else {
                                                        System.out.println(color.RED + "Invalid selection. Please choose between 1 and " +
                                                                foundProducts.size() + "." + color.RESET);
                                                    }
                                                } catch (NumberFormatException e) {
                                                    System.out.println(color.RED + "Please enter a valid number." + color.RESET);
                                                }
                                            }
                                        }

                                        if (selectedProduct == null) {
                                            System.out.print("Search for another product? (Y/Other key to cancel): ");
                                            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                                                searchingProducts = false;
                                            }
                                            continue;
                                        }
                                    } else {
                                        // Only one product found, select it automatically
                                        selectedProduct = foundProducts.get(0);
                                        menu.setSelectedProduct(selectedProduct);
                                        System.out.println("Selected: " + selectedProduct.getName());
                                    }

                                    // Handle quantity input
                                    boolean quantityEntered = false;
                                    while (!quantityEntered) {
                                        System.out.print("Enter quantity (or 0 to cancel): ");
                                        String qtyInput = scanner.nextLine().trim();

                                        if (qtyInput.equals("0")) {
                                            System.out.println(color.BLUE + "Cancelled adding product to cart." + color.RESET);
                                            quantityEntered = true;
                                        } else {
                                            try {
                                                int quantity_search = Integer.parseInt(qtyInput);
                                                if (quantity_search <= 0) {
                                                    System.out.println(color.RED + "Quantity must be a positive number." + color.RESET);
                                                } else {
                                                    // Use the selected product's ID, not the search query
                                                    menu.addToCart(cart, selectedProduct.getProductID(), quantity_search);
                                                    quantityEntered = true;
                                                }
                                            } catch (NumberFormatException e) {
                                                System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                                            }
                                        }
                                    }

                                    // Ask if they want to search for another product
                                    System.out.print("Search for another product? (Y/Other key to cancel): ");
                                    if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                                        searchingProducts = false;
                                    }
                                }
                                break;
                            case 3:
                                boolean validSortChoice = false;
                                while (!validSortChoice) {
                                    System.out.println("\nSort Products By:");
                                    System.out.println("1. Price - Low to High");
                                    System.out.println("2. Price - High to Low");
                                    System.out.print("Choose an option: ");
                                    String sortInput = scanner.nextLine().trim();

                                    if (!sortInput.matches("\\d+")) {
                                        System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                                        continue;
                                    }

                                    int sortChoice = Integer.parseInt(sortInput);
                                    switch (sortChoice) {
                                        case 1:
                                            menu.sortProductsByPrice(currentProductList, true);
                                            validSortChoice = true;
                                            break;
                                        case 2:
                                            menu.sortProductsByPrice(currentProductList, false);
                                            validSortChoice = true;
                                            break;
                                        default:
                                            System.out.println(color.RED + "Invalid choice. Please choose 1 or 2." + color.RESET);
                                    }
                                }
                                break;
                            case 4:
                                boolean validSortTypeChoice = false;
                                while (!validSortTypeChoice) {
                                    System.out.println("\nSort Products By Specific Type:");
                                    System.out.println("1. Type - A to Z");
                                    System.out.println("2. Type - Z to A");
                                    System.out.print("Choose an option: ");
                                    String sortTypeInput = scanner.nextLine().trim();

                                    if (!sortTypeInput.matches("\\d+")) {
                                        System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                                        continue;
                                    }

                                    int sortTypeChoice = Integer.parseInt(sortTypeInput);
                                    switch (sortTypeChoice) {
                                        case 1:
                                            menu.sortProductsByType(currentProductList, true);  // A to Z
                                            validSortTypeChoice = true;
                                            break;
                                        case 2:
                                            menu.sortProductsByType(currentProductList, false); // Z to A
                                            validSortTypeChoice = true;
                                            break;
                                        default:
                                            System.out.println(color.RED + "Invalid choice. Please choose 1 or 2." + color.RESET);
                                    }
                                }
                                break;
                            case 5:
                                stayInProductMenu = false;
                                break;
                        }
                    }
                    break;
                case 2:
                    List<CartItem> selectedItems = new ArrayList<>();
                    boolean hasItem = cart.loadCart();
                    if (!hasItem) {
                        System.out.print("Press Enter to return to main menu...");
                        scanner.nextLine();
                        break;
                    }
                    boolean stayInCartMenu = true;
                    while (stayInCartMenu) {
                        if (customer instanceof Student) {
                            Student student = (Student) customer;
                            student.viewCart(student.getCart()); 
                        } else if (customer instanceof Customer) {
                            Customer cust = (Customer) customer;
                            cust.viewCart(cust.getCart());  
                        } else {
                            System.out.println("Error: Unknown customer type.");
                        }
                        System.out.println("\n1. Remove Product");
                        System.out.println("2. Checkout");
                        System.out.println("3. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        String optionInput = scanner.nextLine().trim();
                        if (!optionInput.matches("\\d+")) {
                            System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                            continue;
                        }
                        choice = Integer.parseInt(optionInput);
                        switch (choice) {
                            case 1:
                                String productIdToRemove = "";
                                boolean keepGo = true;
                                while (keepGo) {
                                    System.out.print("\nEnter Product ID to remove (or 0 to go back): ");
                                    productIdToRemove = scanner.nextLine().trim();
                                    if (productIdToRemove.equals("0")) {
                                        System.out.println(color.BLUE + "Returning to cart menu." + color.RESET);
                                        keepGo = false;
                                        break;
                                    }
                                    if (productIdToRemove.isEmpty() || !productIdToRemove.matches("[a-zA-Z0-9]+")) {
                                        System.out.println(color.RED + "Invalid Product ID. Letters and numbers only, no spaces or symbols." + color.RESET);
                                        continue;
                                    }
                                    break;
                                }
                                if (!keepGo) {
                                    break;
                                }
                                boolean cancel = false;
                                boolean validQty = false;
                                while (!validQty || !cancel) {
                                    System.out.print("Enter quantity to remove (or 0 to go back): ");
                                    String qtyInput = scanner.nextLine().trim();
                                    if (qtyInput.isEmpty()) {
                                        System.out.println(color.RED + "Quantity cannot be empty." + color.RESET);
                                        validQty = false;
                                        continue;
                                    }
                                    if (qtyInput.equals("0")) {
                                        System.out.println(color.BLUE + "Returning to cart menu." + color.RESET);
                                        cancel = true;
                                        break;
                                    }
                                    try {
                                        quantity = Integer.parseInt(qtyInput);
                                        if (quantity < 0) {
                                            System.out.println(color.RED + "Quantity must be a positive number." + color.RESET);
                                            validQty = false;
                                        } else {
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println(color.RED + "Invalid quantity. Please enter a number." + color.RESET);
                                        validQty = false;
                                    }
                                }
                                if (cancel) {
                                    break;
                                }
                                customer.removeFromCart(cartItem, cart, productIdToRemove, quantity);
                                break;
                            case 2:
                                int payment_method = 0;
                                Payment payment;
                                double subtotal = 0;
                                boolean validQuantity = true;
                                boolean validName = true;
                                boolean keepRunning = true;
                                while (keepRunning) {
                                    System.out.print("\nEnter Product ID to checkout (or '0' to go back, 'done' to finish): ");
                                    productId = scanner.nextLine().trim();
                                    if (productId.equals("0")) {
                                        System.out.println(color.BLUE + "Returning to cart menu." + color.RESET);
                                        keepRunning = false;
                                        break;
                                    }
                                    if (productId.equalsIgnoreCase("done")) {
                                        break;
                                    }
                                    if (!validName) {
                                        break;
                                    }

                                    if (!productId.matches("[a-zA-Z0-9]+")) {
                                        System.out.println(color.RED + "Invalid Product ID. Only letters and numbers are allowed." + color.RESET);
                                        continue;
                                    }
                                    final String selectedProductId = productId;
                                    Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                                            .filter(item -> item.getProductId().equalsIgnoreCase(selectedProductId))
                                            .findFirst();
                                    if (!cartItemOpt.isPresent()) {
                                        System.out.println(color.RED + "Product not found in cart." + color.RESET);
                                        continue;
                                    }
                                    cartItem = cartItemOpt.get();
                                    int alreadySelectedQty = selectedItems.stream()
                                            .filter(item -> item.getProductId().equalsIgnoreCase(selectedProductId))
                                            .mapToInt(CartItem::getQuantity)
                                            .sum();
                                    int availableQty = cartItem.getQuantity() - alreadySelectedQty;
                                    if (availableQty <= 0) {
                                        System.out.println(color.RED + "No more available quantity for this product." + color.RESET);
                                        continue;
                                    }

                                    boolean validInput = false;
                                    while (!validInput) {
                                        System.out.print("Enter quantity to checkout (or '0' to go back) (Available: " + availableQty + "): ");
                                        String qtyInput = scanner.nextLine().trim();
                                        try {
                                            int qty = Integer.parseInt(qtyInput);
                                            if (qty == 0) {
                                                System.out.println("Returning to product selection.");
                                                break;
                                            }
                                            if (qty <= 0 || qty > availableQty) {
                                                System.out.println(color.RED + "Invalid quantity. Must be between 1 and " + availableQty + "." + color.RESET);
                                                continue;
                                            }
                                            Optional<CartItem> existingSelectedOpt = selectedItems.stream()
                                                    .filter(item -> item.getProductId().equalsIgnoreCase(selectedProductId))
                                                    .findFirst();
                                            if (existingSelectedOpt.isPresent()) {
                                                CartItem selected = existingSelectedOpt.get();
                                                int newQty = selected.getQuantity() + qty;
                                                selected.setQuantity(newQty);
                                                System.out.println(color.GREEN + "Updated quantity for " + productId + " to " + newQty + color.RESET);
                                            } else {
                                                selectedItems.add(new CartItem(
                                                        cartItem.getProductId(),
                                                        cartItem.getProductName(),
                                                        cartItem.getPrice(),
                                                        qty,
                                                        cartItem.getStatus()
                                                ));
                                                System.out.println(color.GREEN + "Added " + qty + " x " + cartItem.getProductName() + " to checkout." + color.RESET);
                                            }
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println(color.RED + "Invalid input. Please enter a valid number." + color.RESET);
                                        }
                                    }
                                    if (!validName || !validQuantity) {
                                        keepRunning = false;
                                        break;
                                    }
                                }
                                if (!keepRunning) {
                                    selectedItems.clear();
                                    break;
                                }

                                if (selectedItems.isEmpty()) {
                                    System.out.println(color.BLUE + "No items selected for checkout." + color.RESET);
                                    break;
                                }
                                
                                System.out.println("\nEnter your delivery address (e.g., '123 Main St, 12345 City, Johor'):");
                                String addressInput = "";
                                boolean addressValid = false;
                                while (!addressValid) {
                                    // Enter the address line
                                    System.out.print("Enter address (or '0' to cancel): ");
                                    addressInput = scanner.nextLine().trim();
                                    if (addressInput.equals("0")) {
                                        System.out.println(color.BLUE + "Returning to cart menu." + color.RESET);
                                        selectedItems.clear(); 
                                        break;
                                    }
                                    if (addressInput.isEmpty()) {
                                        System.out.println(color.RED + "Address cannot be empty." + color.RESET);
                                        continue;
                                    }
                                    String regex = "^(.+?),\\s*(\\d{5})\\s+([A-Za-z ]+),\\s*([A-Za-z ]+)$";
                                    Pattern pattern = Pattern.compile(regex);
                                    Matcher matcher = pattern.matcher(addressInput);
                                    if (matcher.matches()) {
                                        String addressLine = matcher.group(1).trim();
                                        String postcode = matcher.group(2).trim();
                                        String city = matcher.group(3).trim();
                                        String state = matcher.group(4).trim();    
                                    } else {
                                        System.out.println(color.RED + "Invalid format. Please enter the address in the correct format: Address, Postcode City, State" + color.RESET);
                                        continue;
                                    }
                                    addressValid = true;
                                }
                                
                                if (selectedItems.isEmpty()) {
                                    break;
                                }
                                LocalDate today = LocalDate.now();
                                LocalDate sevenDaysLater = today.plusDays(7);
                                LocalDate maxDate = today.plusDays(7);
                                LocalDate deliveryDate = null;
                                boolean goOut = false;
                                DatePicker.showCalendarOnly();
                                do {
                                    System.out.print("\nEnter delivery month (1-12) or 0 to cancel: ");
                                    String monthInput = scanner.nextLine().trim();
                                    if (monthInput.equals("0")) {
                                        System.out.println(color.BLUE + "Checkout canceled. Items remain in cart." + color.RESET);
                                        selectedItems.clear();
                                        goOut = true;
                                        break;
                                    }
                                    System.out.print("Enter delivery day (1-31) or 0 to cancel: ");
                                    String dayInput = scanner.nextLine().trim();
                                    if (dayInput.equals("0")) {
                                        System.out.println(color.BLUE + "Checkout canceled. Items remain in cart." + color.RESET);
                                        selectedItems.clear();
                                        goOut = true;
                                        break;
                                    }
                                    if (goOut) {
                                        break;
                                    }
                                    try {
                                        int month = Integer.parseInt(monthInput);
                                        int day = Integer.parseInt(dayInput);
                                        int year = today.getYear();
                                        deliveryDate = LocalDate.of(year, month, day);
                                        if (deliveryDate.isBefore(today.plusDays(8))) {
                                            System.out.println(color.RED + "Cannot select a date within the next 7 days. Please choose a later date." + color.RESET);
                                            deliveryDate = null;
                                        } else if (deliveryDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                            System.out.println(color.RED + "No deliveries on Sunday. Please choose another day." + color.RESET);
                                            deliveryDate = null;
                                        } else if (deliveryDate.getMonthValue() > today.plusMonths(1).getMonthValue()
                                                || deliveryDate.getYear() > today.plusMonths(1).getYear()) {
                                            System.out.println(color.RED + "Delivery date must be within this month or next month." + color.RESET);
                                            deliveryDate = null;
                                        } else {
                                            System.out.println(color.GREEN + "Delivery date set for: " + deliveryDate + color.RESET);
                                        }
                                    } catch (DateTimeException | NumberFormatException e) {
                                        System.out.println(color.RED + "Invalid date. Please enter valid numeric values." + color.RESET);
                                        deliveryDate = null;
                                    }
                                } while (deliveryDate == null);
                                if (goOut) {
                                    break;
                                }
                                System.out.println("\nCheckout Summary:");
                                System.out.println("\nSelected Date:" + deliveryDate);
                                for (CartItem item : selectedItems) {
                                    double itemTotal = item.getPrice() * item.getQuantity();
                                    System.out.printf("%s x %d @ $%.2f = $%.2f\n",
                                            item.getProductName(),
                                            item.getQuantity(),
                                            item.getPrice(),
                                            itemTotal);
                                    subtotal += itemTotal;
                                }
                                System.out.println("-----------------");
                                System.out.printf("SUBTOTAL: $%.2f\n", subtotal);
                                double discountRate = 0.0;
                                if (customer instanceof Student) {
                                    discountRate = ((Student) customer).getDiscountRate();
                                    double discountAmount = subtotal * discountRate;
                                    double totalAfterDiscount = subtotal - discountAmount;
                                    System.out.printf("STUDENT DISCOUNT (%.0f%%): -$%.2f\n", discountRate * 100, discountAmount);
                                    System.out.printf("TOTAL: $%.2f\n", totalAfterDiscount);
                                } else {
                                    System.out.printf("TOTAL: $%.2f\n", subtotal);
                                }
                                String confirm = "";
                                boolean validConfirmation = false;
                                while (!validConfirmation) {
                                    System.out.print("\nProceed to payment (yes/no): ");
                                    confirm = scanner.nextLine().trim();
                                    if (confirm.isEmpty()) {
                                        System.out.println(color.RED + "Input cannot be empty. Please enter 'yes' or 'no'." + color.RESET);
                                    } else if (confirm.equalsIgnoreCase("yes")) {
                                        validConfirmation = true;
                                    } else if (confirm.equalsIgnoreCase("no")) {
                                        System.out.println(color.BLUE + "Checkout canceled. Items remain in cart." + color.RESET);
                                        selectedItems.clear();
                                        keepRunning = false;
                                        break;
                                    } else {
                                        System.out.println(color.RED + "Invalid input. Please enter 'yes' or 'no'." + color.RESET);
                                    }
                                    if (!keepRunning) {
                                        break;
                                    }
                                }
                                if (validConfirmation) {
                                    boolean payment_status = false;
                                    while (!payment_status) {
                                        System.out.println("\nPayment Method: ");
                                        System.out.println("[1] Touch n Go");
                                        System.out.println("[2] Online Banking");
                                        System.out.println("[3] Credit Card");
                                        System.out.print("Enter your payment method (0 to cancel - back to cart menu): ");
                                        String paymentInput = scanner.nextLine().trim();
                                        if (paymentInput.isEmpty() || !paymentInput.matches("\\d+")) {
                                            System.out.println(color.RED + "Invalid input. Please enter a valid number (1, 2, or 3)." + color.RESET);
                                            continue;
                                        }
                                        payment_method = Integer.parseInt(paymentInput);
                                        if (payment_method == 0) {
                                            selectedItems.clear();
                                            break;
                                        }
                                        if (payment_method < 1 || payment_method > 3) {
                                            System.out.println(color.RED + "Invalid payment method. Please select 1, 2, or 3." + color.RESET);
                                            continue;
                                        }
                                        switch (payment_method) {
                                            case 1:
                                                String tngPhoneNumber = customer.getPhoneNumber();
                                                System.out.println("Phone Number: +6" + tngPhoneNumber);
                                                System.out.print("Is the phone number correct? (Yes/No(other to go back)): ");
                                                String option = scanner.nextLine();
                                                if (option.equalsIgnoreCase("yes")) {
                                                    boolean valid_tngDigit = false;
                                                    while (!valid_tngDigit) {
                                                        System.out.print("Enter your TNG 6-digits (EX: 090909): ");
                                                        String tngDigit = scanner.nextLine();
                                                        payment = new TngPayment(tngDigit);
                                                        valid_tngDigit = payment.processPayment();
                                                    }
                                                    String verificationCode = "";
                                                    try {
                                                        verificationCode = TngSmsSender.sendVerificationCode(tngPhoneNumber);
                                                    } catch (com.twilio.exception.ApiException e) {
                                                        System.out.println(color.RED + "\n[Error] Unable to send SMS: " + e.getMessage() + color.RESET);
                                                        System.out.print("Press any key to return to payment method selection...");
                                                        scanner.nextLine();
                                                        break;
                                                    }
                                                    int attempts = 3;
                                                    boolean verified = false;
                                                    while (attempts > 0) {
                                                        System.out.print("Enter the verification code: ");
                                                        String userInputCode = scanner.nextLine().trim();
                                                        if (userInputCode.equals(verificationCode)) {
                                                            System.out.println(color.GREEN + "Verification successful!" + color.RESET);
                                                            verified = true;
                                                            break;
                                                        } else {
                                                            attempts--;
                                                            if (attempts > 0) {
                                                                System.out.println(color.RED + "Incorrect code. You have " + attempts + " attempts remaining." + color.RESET);
                                                            }
                                                        }
                                                    }
                                                    if (!verified) {
                                                        System.out.println(color.RED + "Verification failed. Exiting payment." + color.RESET);
                                                        break;
                                                    }
                                                    payment_status = true;
                                                }
                                                else if (option.equalsIgnoreCase("no")) {
                                                    System.out.println("\nPlease check your profile to ensure your phone number is correct.");
                                                    System.out.print("Press any key to go back to the previous page...");
                                                    scanner.nextLine();
                                                    break;
                                                }
                                                break;
                                            case 2:
                                                boolean validBankType = false;
                                                int bankType = 0;
                                                do {
                                                    System.out.println("\nBank Type: ");
                                                    System.out.println("[1] Public Bank");
                                                    System.out.println("[2] Maybank");
                                                    System.out.println("[3] CIMB Bank");
                                                    System.out.println("[4] RHB Bank");
                                                    System.out.println("[5] HONG LEONG Bank");
                                                    System.out.print("Enter your Bank Type (1-5): ");
                                                    String bankTypeInput = scanner.nextLine().trim();
                                                    if (!bankTypeInput.matches("\\d+")) {
                                                        System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                                                        continue;
                                                    }
                                                    bankType = Integer.parseInt(bankTypeInput);
                                                    if (bankType < 1 || bankType > 5) {
                                                        System.out.println(color.RED + "Invalid input. Please enter the number between 1 to 5." + color.RESET);
                                                        continue;
                                                    }
                                                    validBankType = true;
                                                } while (!validBankType);
                                                String bankUserName = "";
                                                String bankPassword = "";
                                                boolean valid = false;
                                                while (!valid) {
                                                    System.out.print("\nEnter your Bank Username: ");
                                                    bankUserName = scanner.nextLine().trim();
                                                    System.out.print("Enter your Bank Password: ");
                                                    bankPassword = scanner.nextLine();
                                                    payment = new BankPayment(bankUserName, bankPassword);
                                                    if (payment.processPayment()) {
                                                        System.out.println("Bank payment details are valid.");
                                                        payment_status = true;
                                                        valid = true;
                                                    } else {
                                                        System.out.println(color.RED + "Payment validation failed. Please check your details and try again." + color.RESET);
                                                    }
                                                }
                                                break;
                                            case 3:
                                                String cardNumber = "";
                                                boolean validCardNumber = false;
                                                boolean valid_cvv;
                                                do {
                                                    System.out.print("Enter your Card Number (16 character): ");
                                                    cardNumber = scanner.nextLine();
                                                    if (cardNumber.isEmpty() || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                                                        System.out.println(color.RED + "Invalid card number. Please enter the correct format of card number." + color.RESET);
                                                    }
                                                    else {
                                                        validCardNumber = true;
                                                    }
                                                } while (!validCardNumber);
                                                do {
                                                    System.out.print("Enter your CVV (3 or 4 digits): ");
                                                    String cvvInput = scanner.nextLine().trim();
                                                    if (!cvvInput.matches("\\d{3,4}")) {
                                                        System.out.println(color.RED + "Invalid CVV format." + color.RESET);
                                                        valid_cvv = false;
                                                        continue;
                                                    }
                                                    int cvv = Integer.parseInt(cvvInput);
                                                    payment = new CreditCardPayment(cvv);
                                                    valid_cvv = payment.processPayment();
                                                } while (!valid_cvv);
                                                payment_status = true;
                                                break;
                                            default:
                                                System.out.println(color.RED + "Invalid input. Please enter a number between 1 and 3." + color.RESET);
                                        }
                                    }
                                    if (payment_status) {
                                        if (payment_method == 1) {
                                            payment = new TngPayment();

                                        } else if (payment_method == 2) {
                                            payment = new BankPayment();
                                        } else {
                                            payment = new CreditCardPayment();
                                        }
                                        payment.setAmount(subtotal);
                                        double fee = payment.calculateFees();
                                        double total = subtotal + fee;
                                        Receipt receipt = new Receipt();
//                                                cart.markSelectedItemsAsCheckedOut(selectedItems);
//                                                receipt.generateReceipt(selectedItems, username);
                                        cart.markSelectedItemsAsCheckedOut(selectedItems, deliveryDate);
                                        receipt.generateReceipt(customer.getUserType() ,selectedItems, username, deliveryDate,total);
                                        System.out.println(color.GREEN + "Checkout successful! Receipt generated." + color.RESET);
                                    } else {
                                        System.out.println(color.GREEN + "Payment failed." + color.RESET);
                                    }
                                }
                                break;
                            case 3:
                                stayInCartMenu = false;
                                break;
                            default:
                                System.out.println(color.RED + "Invalid choice. Please select 1, 2, or 3." + color.RESET);
                        }
                    }
                    break;
                case 3:
                    boolean hasHistory = orderHistory.viewOrderHistory();
                    if (!hasHistory) {
                        System.out.print("Press Enter to return to main menu...");
                        scanner.nextLine();
                        break;
                    }
                    System.out.println("\n1. Cancel Order(s)");
                    System.out.println("2. Change Delivery Date");
                    System.out.println("3. Back to Main Menu");
                    choice = -1;
                    while (choice != 1 && choice != 2 && choice != 3) {
                        System.out.print("Enter your choice: ");
                        if (scanner.hasNextInt()) {
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            if (choice != 1 && choice != 2 && choice != 3) {
                                System.out.println(color.RED + "Invalid option. Please enter 1 or 2 or 3." + color.RESET);
                            }
                        } else {
                            System.out.println(color.GREEN + "Invalid input. Please enter a valid number (1 or 2 or 3)." + color.RESET);
                            scanner.nextLine();
                        }
                    }
                    switch (choice) {
                        case 1:
                            boolean cancelOrder = true;
                            while (cancelOrder) {
                                System.out.print("Enter the order numbers to cancel (comma-separated), or enter 0 to stop: ");
                                String input = scanner.nextLine().trim();
                                if (input.equals("0")) {
                                    System.out.println(color.BLUE + "Exiting cancel order process." + color.RESET);
                                    break;
                                }
                                List<Integer> orderNumbers = new ArrayList<>();
                                try {
                                    for (String num : input.split(",")) {
                                        orderNumbers.add(Integer.parseInt(num.trim()));
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println(color.RED + "Invalid order numbers entered. Please enter only valid integers, separated by commas." + color.RESET);
                                    continue;
                                }
                                orderHistory.cancelOrder(orderNumbers);
                                System.out.println("\nUpdated Order History:");
                                orderHistory.viewOrderHistory();
                                String continueChoice = "";
                                boolean validChoice = false;
                                while (!validChoice) {
                                    System.out.print("Enter 1 to cancel more orders, or 2 to go back to the main menu: ");
                                    continueChoice = scanner.nextLine().trim();
                                    if (continueChoice.isEmpty() || !continueChoice.matches("[12]")) {
                                        System.out.println(color.RED + "Invalid input. Please enter '1' to cancel more orders or '2' to go back to the main menu." + color.RESET);
                                    } else if (continueChoice.equals("2")) {
                                        System.out.println("Returning to the main menu...");
                                        cancelOrder = false;
                                        validChoice = true;
                                        break;
                                    } else if (continueChoice.equals("1")) {
                                        validChoice = true;
                                    }
                                }
                                if (continueChoice.equals("2")) {
                                    break;
                                }
                            }
                            break;
                        case 2:
                            boolean backToMainMenu = false;
                            do {
                                List<Integer> validOrderIndexes = orderHistory.getAvailableChangeDeliveryDate();
                                List<String[]> orderHistoryLines = null;
                                try {
                                    orderHistoryLines = OrderHistoryIOHelper.readOrderHistory(customer.getUsername() + "_orderhistory.txt");
                                } catch (IOException e) {
                                    System.out.println(color.RED + "Error reading order history file: " + e.getMessage() + color.RESET);
                                    break;
                                }
                                boolean hasAvailableOrders = orderHistoryLines.stream().anyMatch(order -> {
                                    if (order.length == 7) {
                                        try {
                                            LocalDate deliveryDate = LocalDate.parse(order[6]);
                                            return deliveryDate.isAfter(LocalDate.now());
                                        } catch (DateTimeParseException e) {
                                            return false;
                                        }
                                    }
                                    return false;
                                });
                                if (!hasAvailableOrders || validOrderIndexes.isEmpty()) {
                                    System.out.println(color.BLUE + "No upcoming delivery dates found for change." + color.RESET);
                                    System.out.print("Enter any key to return to main menu...");
                                    scanner.nextLine();
                                    break;
                                }
                                System.out.print("\nEnter the No. of the order you want to change (or 0 to return to main menu): ");
                                String lineChoice = scanner.nextLine().trim();
                                if (lineChoice.equals("0")) {
                                    backToMainMenu = true;
                                    break;
                                }
                                int selectedLineNumber;
                                try {
                                    selectedLineNumber = Integer.parseInt(lineChoice);
                                    if (selectedLineNumber < 1 || selectedLineNumber > validOrderIndexes.size()) {
                                        System.out.println(color.RED + "Invalid number. Please select a number from the list." + color.RESET);
                                        continue;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println(color.RED + "Invalid input. Please enter a valid number." + color.RESET);
                                    continue;
                                }
                                LocalDate today = LocalDate.now();
                                LocalDate deliveryDate = null;
                                DatePicker.showCalendarOnly();
                                do {
                                    try {
                                        System.out.print("\nEnter delivery month (1-12) or 0 to cancel: ");
                                        String monthInput = scanner.nextLine().trim();
                                        if (monthInput.equals("0")) {
                                            System.out.println(color.BLUE + "Delivery date change canceled." + color.RESET);
                                            break;
                                        }
                                        System.out.print("Enter delivery day (1-31) or 0 to cancel: ");
                                        String dayInput = scanner.nextLine().trim();
                                        if (dayInput.equals("0")) {
                                            System.out.println(color.BLUE + "Delivery date change canceled." + color.RESET);
                                            break;
                                        }
                                        int month = Integer.parseInt(monthInput);
                                        int day = Integer.parseInt(dayInput);
                                        int year = today.getYear();
                                        deliveryDate = LocalDate.of(year, month, day);
                                        if (deliveryDate.isBefore(today.plusDays(8))) {
                                            System.out.println(color.RED + "Cannot select a date within the next 7 days. Please choose a later date." + color.RESET);
                                            deliveryDate = null;
                                        } else if (deliveryDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                            System.out.println(color.RED + "No deliveries on Sunday. Please choose another day." + color.RESET);
                                            deliveryDate = null;
                                        } else if (deliveryDate.getMonthValue() > today.plusMonths(1).getMonthValue()
                                                || deliveryDate.getYear() > today.plusMonths(1).getYear()) {
                                            System.out.println(color.RED + "Delivery date must be within this month or next month." + color.RESET);
                                            deliveryDate = null;
                                        } else {
                                            System.out.println(color.GREEN + "Delivery date set for: " + deliveryDate + color.RESET);
                                        }
                                    } catch (DateTimeException | NumberFormatException e) {
                                        System.out.println(color.RED + "Invalid date. Please enter valid numeric values." + color.RESET);
                                        deliveryDate = null;
                                    }
                                } while (deliveryDate == null);
                                if (deliveryDate != null) {
                                    if (selectedLineNumber > 0 && selectedLineNumber <= validOrderIndexes.size()) {
                                        int realIndexInHistoryFile = validOrderIndexes.get(selectedLineNumber - 1);
                                        System.out.println("Updating order at index: " + realIndexInHistoryFile);
                                        orderHistory.updateDeliveryDate(realIndexInHistoryFile, deliveryDate);
                                    } else {
                                        System.out.println("Invalid order selection.");
                                    }
                                    System.out.println("Valid Order Indexes: " + validOrderIndexes);
                                    System.out.println("Selected Line Number: " + selectedLineNumber);
                                }

                                System.out.print("\nDo you want to change another delivery date? (Y = Yes, any other key = Main Menu): ");
                                String continueChoice = scanner.nextLine().trim();
                                if (!continueChoice.equalsIgnoreCase("Y")) {
                                    backToMainMenu = true;
                                }

                            } while (!backToMainMenu);
                            break;
                        case 3:
                            System.out.println("Returning to the main menu...");
                            break;
                        default:
                            System.out.println(color.RED + "Invalid option. Please enter 1 or 2." + color.RESET);
                    }
                    break;
                case 4:
                    Refund refund = new Refund(username);
                    if (refund.hasPendingRefund(username)) {
                        refund.getRefund();
                        String input = "";
                        boolean validInput = false;
                        while (!validInput) {
                            System.out.print("Do you want to mark all pending refunds as 'Received'? (Y/N): ");
                            input = scanner.nextLine().trim().toLowerCase();
                            if (input.isEmpty()) {
                                System.out.println(color.RED + "Input cannot be empty. Please enter 'Y' for Yes or 'N' for No." + color.RESET);
                            } else if (input.equals("y")) {
                                refund.updateRefund();
                                validInput = true;
                            } else if (input.equals("n")) {
                                System.out.println("Returning to main menu...");
                                validInput = true;
                            } else {
                                System.out.println(color.RED + "Invalid input. Please enter 'Y' for Yes or 'N' for No." + color.RESET);
                            }
                        }
                    } else {
                        System.out.println(color.BLUE + "You have no pending refunds." + color.RESET);
                        System.out.print("Press Enter to return to main menu...");
                        scanner.nextLine();
                    }
                    break;
                case 5:
                    boolean stayInProfile = true;
                    while (stayInProfile) {
                        String changeName, changePhoneNumber, changeEmail, changePassword, changeConfirmPassword;
                        boolean backToMenu = false;
                        System.out.println("\nYour Username: " + customer.getUsername());
                        System.out.println("Your Phone Number: " + customer.getPhoneNumber());
                        System.out.println("Your Email: " + customer.getEmail());
                        System.out.println("Your identity: " + customer.getUserType());
                        System.out.println("\n1. Change username");
                        System.out.println("2. Change phone number");
                        System.out.println("3. Change email");
                        System.out.println("4. Change password");
                        System.out.println("5. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        String optionInput = scanner.nextLine().trim();
                        if (!optionInput.matches("\\d+")) {
                            System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                            continue;
                        }
                        choice = Integer.parseInt(optionInput);
                        ChangeProfile changeProfile = new ChangeProfile(userModule, customer);
                        switch (choice) {
                            case 1:
                                do {
                                    System.out.print("\nEnter your new username (press '0' to cancel): ");
                                    changeName = scanner.nextLine().trim().toLowerCase();
                                    if (changeName.equals("0")) {
                                        backToMenu = true;
                                        break;
                                    }
                                } while (!backToMenu && (!userModule.validateUsername(changeName) || userModule.isUsernameTaken(changeName)));
                                if (backToMenu) {
                                    break;
                                }
                                changeProfile.updateProfile("username", changeName);
                                break;
                            case 2:
                                do {
                                    System.out.print("Enter your phone number (EX: 0183298908) (press '0' to cancel): ");
                                    changePhoneNumber = scanner.nextLine().trim();
                                    if (changePhoneNumber.equals("0")) {
                                        backToMenu = true;
                                        break;
                                    }
                                } while (!backToMenu && (!userModule.validatePhoneNumber(changePhoneNumber) || userModule.isPhoneNumberTaken(changePhoneNumber)));
                                if (backToMenu) {
                                    break;
                                }
                                changeProfile.updateProfile("phoneNumber", changePhoneNumber);
                                break;
                            case 3:
                                String verificationCode = null;
                                boolean isEmailVerified = false;
                                do {
                                    System.out.print("Enter your email (EX: siow@gmail.com) (press '0' to cancel): ");
                                    changeEmail = scanner.nextLine().trim();
                                    if (changeEmail.equals("0")) {
                                        backToMenu = true;
                                        break;
                                    }
                                } while (!backToMenu && (!userModule.validateEmail(changeEmail) || userModule.isEmailTaken(changeEmail)));
                                if (backToMenu) {
                                    break;
                                }
                                verificationCode = String.format("%06d", new Random().nextInt(1000000));
                                Email emailSender = new Email("siowkaiying06@gmail.com", "bzgb kgpt lobg deor");
                                if (emailSender.sendVerificationCode(changeEmail, verificationCode)) {
                                    for (int attempt = 1; attempt <= 3; attempt++) {
                                        System.out.print("Enter the verification code sent to your email (Attempt " + attempt + "/3): ");
                                        String enteredCode = scanner.nextLine().trim();
                                        if (enteredCode.equals(verificationCode)) {
                                            isEmailVerified = true;
                                            break;
                                        } else {
                                            System.out.println(color.RED + "Incorrect code. Try again." + color.RESET);
                                        }
                                    }
                                    if (!isEmailVerified) {
                                        System.out.println(color.RED + "Too many failed attempts." + color.RESET);
                                        System.out.println(color.BLUE + "Registration cancelled." + color.RESET);
                                        break;
                                    }
                                } else {
                                    System.out.println(color.RED + "Failed to send email. Registration cancelled." + color.RESET);
                                    break;
                                }
                                changeProfile.updateProfile("email", changeEmail);
                                break;
                            case 4:
                                String oldPassword;
                                do {
                                    System.out.print("Enter your old password (press 0 to cancel): ");
                                    oldPassword = scanner.nextLine().trim();
                                    if (oldPassword.equals("0")) {
                                        backToMenu = true;
                                        break;
                                    }
                                    if (!oldPassword.equals(customer.getPassword())) {
                                        System.out.println(color.RED + "Incorrect password. Please try again." + color.RESET);
                                    }
                                } while (!oldPassword.equals(customer.getPassword()));
                                if (backToMenu) {
                                    break;
                                }
                                do {
                                    System.out.println("\nPassword must meet the following requirements:");
                                    System.out.println("1. At least 8 characters long.");
                                    System.out.println("2. Contain at least one uppercase letter.");
                                    System.out.println("3. Contain at least one lowercase letter.");
                                    System.out.println("4. Contain at least one digit.");
                                    System.out.println("5. Contain at least one special character (e.g., !, @, #, $, %, ^).");
                                    System.out.print("Enter password: ");
                                    changePassword = scanner.nextLine();
                                } while (!userModule.validatePassword(changePassword));
                                do {
                                    System.out.print("Confirm password: ");
                                    changeConfirmPassword = scanner.nextLine();
                                    if (!changePassword.equals(changeConfirmPassword)) {
                                        System.out.println(color.RED + "Passwords do not match. Please try again.\n" + color.RESET);
                                    }
                                } while (!changePassword.equals(changeConfirmPassword));
                                changeProfile.updateProfile("password", changePassword);
                                break;
                            case 5:
                                stayInProfile = false;
                                break;
                            default:
                                System.out.println(color.RED + "Invalid option. Please enter 1 to 5." + color.RESET);
                        }
                    }
                    break;
                case 6:
                    System.out.println("AI Chat ready. Type your question or 'end' to quit.");
                    AIChat.loadQADatabase();
                    String lastAnswerContext = null;
                    boolean aiChat = true;
                    while (aiChat) {
                        System.out.print("" + "\nYou: ");
                        String input = scanner.nextLine().trim();
                        if (input.equalsIgnoreCase("end")) {
                            System.out.println(color.BLUE + "AI-Chat ended." + color.RESET);
                            aiChat = false;
                            break;
                        }
                        if (input.isEmpty()) {
                            System.out.println(color.RED + "Invalid input, please enter a valid question." + color.RESET);
                            continue;
                        }
                        String previousUserInput = null;
                        String response = AIChat.findClosestAnswer(input, lastAnswerContext);
                        if (response != null) {
                            lastAnswerContext = response;
                            System.out.println(color.PURPLE + "Answer: " + response + color.RESET);
                        } else {
                            response = AIChat.getAnswerFromGemini(input, previousUserInput, lastAnswerContext);
                            if (response != null) {
                                lastAnswerContext = response;
                            }
                        }
                        previousUserInput = input;
                    }
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting Product Menu...");
                    break;
                default:
                    System.out.println(color.RED + "Invalid choice. Please try again." + color.RESET);
            }
        }
        return false;
    }

    public static boolean admin(String username) {
        Colors color = new Colors();
        Scanner scanner = new Scanner(System.in);
        UserModule userModule = new UserModule("users.txt", "admin.txt");
        User admin = userModule.findAdminByUsername(username);
        OrderHistory orderHistory = new OrderHistory();
        ProductModule productModule = new ProductModule();
        AdminModule adminModule = new AdminModule();
        String productId = "";
        String name = "";
        double price = 0;
        int quantity = 0;
        String category = "";
        String specificType = "";
        String specificTypeList = "";
        boolean running = true;
        int choice = 0;
        while (running) {
            boolean validMenuInput = false;
            choice = -1;
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Product List");
            System.out.println("2. Customer Order List");
            System.out.println("3. View Sales Report");
            System.out.println("4. Log Out");
            System.out.print("Choose an option: ");
            String menuInput = scanner.nextLine().trim();
            if (!menuInput.matches("\\d+")) {
                System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                continue;
            } else {
                choice = Integer.parseInt(menuInput);
            }
            if (choice < 1 || choice > 4) {
                System.out.println(color.RED + "Invalid option. Please choose a valid number between 1 and 4." + color.RESET);
                continue;
            }
            switch (choice) {
                case 1:
                    boolean stayInOrderMenu = true;
                    while (stayInOrderMenu) {
                        productModule.loadProductsFromFile();
                        productModule.viewProducts();
                        System.out.println("\n1. Add Product");
                        System.out.println("2. Update Product");
                        System.out.println("3. Remove Product");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Choose an option: ");
                        String optionInput = scanner.nextLine().trim();
                        if (!optionInput.matches("\\d+")) {
                            System.out.println(color.RED + "Invalid input. Please enter a number." + color.RESET);
                            continue;
                        }
                        choice = Integer.parseInt(optionInput);
                        switch (choice) {
                            case 1:
                                boolean addProduct = true;
                                while (addProduct) { //here
                                    System.out.print("\nEnter Product ID you would like to add (Enter 0 back to main): ");
                                    productId = scanner.nextLine().trim();
                                    if (productId.equals("0")) {
                                        System.out.println("Returning to product menu...");
                                        break;
                                    }
                                    if (productId.isEmpty()) {
                                        System.out.println(color.RED + "Invalid Product ID. Cannot be empty." + color.RESET);
                                        continue;
                                    }
                                    if (!productId.matches("P\\d{3}")) {
                                        System.out.println(color.RED + "Invalid Product ID. Format must be 'P' followed by 3 digits (e.g., P001)." + color.RESET);
                                        continue;
                                    }
                                    boolean exists = false;
                                    List<Product> products = productModule.getProducts();
                                    for (Product p : products) {
                                        if (p.getProductID().equalsIgnoreCase(productId)) {
                                            System.out.println(color.RED + "Product already exists. Please add another product." + color.RESET);
                                            exists = true;
                                            break;
                                        }
                                    }
                                    if (exists) {
                                        continue;
                                    }
                                    break;
                                }
                                if (productId.equals("0")) {
                                    break;
                                }
                                boolean validName = false;
                                while (!validName) {
                                    System.out.print("Enter Product Name: ");
                                    name = scanner.nextLine().trim();
                                    if (name.isEmpty()) {
                                        System.out.println(color.RED + "Name cannot be empty." + color.RESET);
                                        continue;
                                    } else {
                                        validName = true;
                                    }
                                    break;
                                }
                                boolean validPrice = false;
                                while (!validPrice) {
                                    System.out.print("Enter Price: ");
                                    String priceInput = scanner.nextLine().trim();
                                    try {
                                        price = Double.parseDouble(priceInput);
                                        if (price < 0) {
                                            System.out.println(color.RED + "Price cannot be negative." + color.RESET);
                                            continue;
                                        } else {
                                            validPrice = true;
                                        }
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println(color.RED + "Invalid price. Please enter a valid number." + color.RESET);
                                    }
                                }
                                boolean validQuantity = false;
                                while (!validQuantity) {
                                    System.out.print("Enter Quantity: ");
                                    String qtyInput = scanner.nextLine().trim();
                                    if (!qtyInput.matches("\\d+")) {
                                        System.out.println(color.RED + "Invalid quantity. Please enter a positive integer." + color.RESET);
                                        continue;
                                    } else {
                                        quantity = Integer.parseInt(qtyInput);
                                        validQuantity = true;
                                    }
                                    break;
                                }
                                boolean validCategory = false;
                                while (!validCategory) {
                                    System.out.print("Enter Category (CC, PA, NC): ");
                                    category = scanner.nextLine().trim().toUpperCase();
                                    if (category.equals("CC") || category.equals("PA") || category.equals("NC")) {
                                        validCategory = true;
                                        break;
                                    } else {
                                        System.out.println(color.RED + "Invalid category. Please enter 'CC', 'PA', or 'NC'." + color.RESET);
                                    }
                                }
                                boolean validSpecificType = false;
                                while (!validSpecificType) {
                                    System.out.print("Enter specific type: ");
                                    specificType = scanner.nextLine().trim();
                                    if (specificType.isEmpty()) {
                                        System.out.println(color.RED + "Invalid type. Cannot be empty." + color.RESET);
                                        continue;
                                    }
                                    if (!specificType.matches("[a-zA-Z0-9 ;]+")) {
                                        System.out.println(color.RED + "Invalid type. Only alphanumeric characters and semicolons are allowed." + color.RESET);
                                        continue;
                                    }
                                    String[] words = specificType.trim().split("\\s+");
                                    if (words.length < 3) {
                                        System.out.println(color.RED + "Invalid type. Must contain at least 3 words." + color.RESET);
                                        continue;
                                    }
                                    validSpecificType = true;
                                }
                                Product newProduct = ProductFactory.createProduct(category, productId, name, price, quantity, specificType);
                                productModule.addProduct(newProduct);
                                productModule.saveNewProductToFile(newProduct);
                                productModule.loadProductsFromFile();
                                System.out.println(color.GREEN + "\nProduct added successfully!" + color.RESET);
                                break;
                            case 2:
                                productId = "";
                                name = "";
                                boolean updateProduct = true;
                                while (updateProduct) {
                                    boolean validProductId = false;
                                    while (!validProductId) {
                                        System.out.print("Enter Product ID to update (or 0 to cancel): ");
                                        productId = scanner.nextLine().trim();
                                        if (productId.equals("0")) {
                                            System.out.println("Returning to product menu...");
                                            break;
                                        }
                                        if (productId.isEmpty() || !productId.matches("P\\d{3}")) {
                                            System.out.println(color.RED + "Invalid Product ID format. Must be 'P' followed by 3 digits (e.g., P001)." + color.RESET);
                                            continue;
                                        }
                                        boolean exists = false;
                                        List<Product> products = productModule.getProducts();
                                        for (Product p : products) {
                                            if (p.getProductID().equalsIgnoreCase(productId)) {
                                                exists = true;
                                                name = p.getName();
                                                break;
                                            }
                                        }
                                        if (!exists) {
                                            System.out.println(color.RED + "Product ID not found. Please enter a valid Product ID." + color.RESET);
                                        } else {
                                            validProductId = true;
                                        }
                                    }
                                    if (productId.equals("0")) {
                                        updateProduct = false;
                                        break;
                                    }
                                    if (!updateProduct) {
                                        break;
                                    }
                                    boolean validNewName = false;
                                    while (!validNewName) {
                                        System.out.print("Enter new name (or press 0 to keep current): ");
                                        String nameInput = scanner.nextLine().trim();
                                        if (nameInput.equals("0")) {
                                            nameInput = name;
                                            validNewName = true;
                                            break;
                                        }
                                        if (nameInput.isEmpty()) {
                                            System.out.println(color.RED + "Name cannot be empty." + color.RESET);
                                            continue;
                                        }
                                        else {
                                            name = nameInput;
                                            validNewName = true;
                                        }
                                    }
                                    boolean validNewPrice = false;
                                    while (!validNewPrice) {
                                        System.out.print("Enter new price (or press 0 to keep current): ");
                                        String priceInput = scanner.nextLine().trim();
                                        if (priceInput.equals("0")) {
                                            price = 0;
                                            validNewPrice = true;
                                            break;
                                        }
                                        try {
                                            price = Double.parseDouble(priceInput);
                                            if (price < 0) {
                                                System.out.println(color.RED + "Price cannot be negative." + color.RESET);
                                                continue;
                                            }
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println(color.RED + "Invalid price. Numbers only (e.g., 12.99)." + color.RESET);
                                        }
                                    }
                                    boolean validNewQuantity = false;
                                    while (!validNewQuantity) {
                                        System.out.print("Enter new quantity (or press 0 to keep current): ");
                                        String qtyInput = scanner.nextLine().trim();
                                        if (qtyInput.equals("0")) {
                                            quantity = 0;
                                            validNewQuantity = true;
                                            break;
                                        }
                                        if (!qtyInput.matches("\\d+")) {
                                            System.out.println(color.RED + "Invalid quantity. Must be a positive integer." + color.RESET);
                                            continue;
                                        }
                                        quantity = Integer.parseInt(qtyInput);
                                        break;
                                    }
                                    category = "0";
                                    specificType = "0";
                                    boolean updated = productModule.updateProduct(productId, name, price, quantity, category, specificType);
                                    if (updated) {
                                        System.out.println(color.GREEN + "Product updated successfully!" + color.RESET);
                                    } else {
                                        System.out.println(color.RED + "Product not found or update failed." + color.RESET);
                                    }
                                    break;
                                }
                                break;
                            case 3:
                                boolean removeProduct = true;
                                while (removeProduct) {
                                    System.out.print("\nEnter Product ID to remove (Enter 0 to go back): ");
                                    productId = scanner.nextLine().trim();
                                    if (productId.equals("0")) {
                                        System.out.println("Returning to product menu...");
                                        removeProduct = false;
                                        break;
                                    }
                                    if (!productId.matches("P\\d{3}")) {
                                        System.out.println(color.RED + "Invalid Product ID format. Must be 'P' followed by 3 digits (e.g., P001)." + color.RESET);
                                        continue;
                                    }
                                    boolean exists = false;
                                    List<Product> products = productModule.getProducts();
                                    for (Product p : products) {
                                        if (p.getProductID().equalsIgnoreCase(productId)) {
                                            exists = true;
                                            name = p.getName();
                                            break;
                                        }
                                    }
                                    if (!exists) {
                                        System.out.println(color.RED + "Product ID not found. Please enter a valid Product ID." + color.RESET);
                                        continue;
                                    }
                                    boolean removed = productModule.removeProduct(productId);
                                    if (removed) {
                                        System.out.println(color.GREEN + "Product removed successfully!" + color.RESET);
                                    } else {
                                        System.out.println(color.RED + "Product removal failed." + color.RESET);
                                    }
                                    break;
                                }
                                break;
                            case 4:
                                stayInOrderMenu = false;
                                break;
                            default:
                                System.out.println(color.RED + "Invalid choice. Please try again." + color.RESET);
                        }
                    }
                    break;
                case 2:
                    boolean stayInHistoryMenu = true;
                    while (stayInHistoryMenu) {
                        orderHistory.viewAllCustomerOrderHistories();
                        System.out.println("\n1. Update Customer Order Status");
                        System.out.println("2. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        if (scanner.hasNextInt()) {
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            if (choice == 1 || choice == 2) {
                                switch (choice) {
                                    case 1:
                                        System.out.println("");
                                        List<String> requestedOrders = AdminModule.displayRequestedOrders();
                                        if (requestedOrders.isEmpty()) {
                                            System.out.println(color.BLUE + "No 'Requested' orders found." + color.RESET);
                                            break;
                                        }

                                        //0 to back
                                        System.out.print("Enter the number(s) to accept (comma-separated): ");
                                        String input = scanner.nextLine().trim();
                                        String[] selections = input.split(",");
                                        boolean valid = true;
                                        for (String selection : selections) {
                                            selection = selection.trim();
                                            if (!selection.matches("\\d+")) { 
                                                valid = false;
                                                break;
                                            }
                                        }
                                        if (valid) {
                                            AdminModule.acceptRequestedOrders(selections);
                                        } else {
                                            System.out.println(color.RED + "Invalid input. Please enter valid order numbers." + color.RESET);
                                        }
                                        break;
                                    case 2:
                                        stayInHistoryMenu = false;
                                        break;
                                    default:
                                        System.out.println(color.RED + "Invalid option, please try again." + color.RESET);
                                }
                            } else {
                                System.out.println(color.RED + "Invalid choice. Please enter 1 or 2." + color.RESET);
                            }
                        } else {
                            System.out.println(color.RED + "Invalid input. Please enter a valid number." + color.RESET);
                            scanner.nextLine();
                        }
                    }
                    break;
                case 3:
                    SalesReport salesReport = new SalesReport();
                    boolean stayInReportMenu = true;
                    while (stayInReportMenu) {
                        System.out.println("\nSales Report Menu:");
                        System.out.println("1. Total Sales Report");
                        System.out.println("2. Sales Trend Report");
                        System.out.println("3. Cancelled Orders Report");
                        System.out.println("4. Back to main menu");
                        System.out.print("Choose an option: ");
                        if (scanner.hasNextInt()) {
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            if (choice >= 1 && choice <= 4) {
                                switch (choice) {
                                    case 1:
                                        salesReport.totalSalesReport();
                                        break;
                                    case 2:
                                        salesReport.salesTrendReport();
                                        break;
                                    case 3:
                                        salesReport.cancelledOrdersReport();
                                        break;
                                    case 4:
                                        System.out.println("Returning to main menu...");
                                        stayInReportMenu = false;
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                System.out.println(color.RED + "Invalid choice. Please enter a number between 1 and 4." + color.RESET);
                            }
                        } else {
                            System.out.println(color.RED + "Invalid input. Please enter a valid number." + color.RESET);
                            scanner.nextLine();
                        }
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting Admin Menu...");
                    break;
                default:
                    System.out.println(color.RED + "Invalid choice. Please try again." + color.RESET);
            }
        }
        return false;
    }
}
