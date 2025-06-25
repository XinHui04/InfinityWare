/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String username;
    protected String phoneNumber;
    protected String email;
    protected String password;
    protected final String USER_TYPE;
    private static User loggedInUser;

    public User(String username, String phoneNumber, String email, String password, String userType) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.USER_TYPE = userType;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return USER_TYPE;
    }

    public abstract boolean isCustomer();
    public abstract boolean isAdmin();
    public abstract boolean isStudent();
    
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void viewProducts(ShoppingCart cart, String category, List<Product> products) {
        ProductModule catalog = new ProductModule();
        catalog.viewProductsWithOptions(cart, category, products);
    }

    public void viewCart(ShoppingCart cart) {
        if (cart != null) {
            cart.viewCart();
        } else {
            System.out.println(Colors.RED + "Cart is not initialized." + Colors.RESET);
        }
    }

    public void addToCart(ShoppingCart cart, Product product, int quantity) {
        if (cart != null) {
            cart.addProduct(product, quantity);
        } else {
            System.out.println(Colors.RED + "Cart is not initialized." + Colors.RESET);
        }
    }

    public void removeFromCart(CartItem cartItem, ShoppingCart cart, String productID, int quantity) {
        if (cart != null) {
            cart.removeProductById(productID, quantity);
            List<CartItem> updated = cart.loadCartItems();
            if (updated != null) {
                cart.setCartItems(updated);
            }
        } else {
            System.out.println(Colors.RED + "Cart is not initialized." + Colors.RESET);
        }
    }

    public void generateReceipt(Receipt receipt, List<CartItem> selectedItems, LocalDate deliveryDate, double total) {
        if (receipt != null) {
            receipt.generateReceipt(USER_TYPE, selectedItems, username, deliveryDate,total);
        } else {
            System.out.println(Colors.RED + "Receipt generator not initialized." + Colors.RESET);
        }
    }

    public void viewOrderHistory(OrderHistory orderHistory) {
        if (orderHistory != null && !orderHistory.viewOrderHistory()) {
            System.out.println(Colors.BLUE + "No order history found." + Colors.RESET);
        } else if (orderHistory == null) {
            System.out.println(Colors.RED + "Order history viewer not initialized." + Colors.RESET);
        }
    }
    
    public static User createUser(String userType, String username, String phoneNumber, String email, String password) {
       switch (userType.toLowerCase()) {
           case "admin":
               return new Admin(username, phoneNumber, email, password, userType);
           case "student":
               return new Student(username, phoneNumber, email, password, userType);
           case "customer":
           default:
               return new Customer(username, phoneNumber, email, password, userType);
       }
   }
    
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public abstract String toString();
}