/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class ShoppingCart {

    private String username;
    private String cartFile;
    private List<CartItem> selectedItems = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();
    private OrderHistory orderHistory;
    Colors color = new Colors();

    public ShoppingCart() {}

    public ShoppingCart(String username, OrderHistory orderHistory) {
        this.username = username;
        this.orderHistory = orderHistory;
        this.cartFile = "cart_" + username + ".txt";
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> updatedItems) {
        if (updatedItems != null) {
            this.cartItems = updatedItems;
        }
    }

    public CartItem getCartItemByProductId(String productId) {
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    public void addProduct(Product product, int quantity) {
        File file = new File(cartFile);
        List<String[]> cartLines = new ArrayList<>();
        boolean updated = false;
        List<String[]> productLines = loadProductFile();
        if (productLines == null) {
            return;
        }
        boolean stockUpdated = updateStockInMemory(productLines, product.getProductID(), quantity);
        if (!stockUpdated) {
            return;
        }
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5 && parts[0].equals(product.getProductID()) && parts[4].equals("Added")) {
                        try {
                            int existingQty = Integer.parseInt(parts[3]);
                            parts[3] = String.valueOf(existingQty + quantity);
                            updated = true;
                        } catch (NumberFormatException e) {
                            System.out.println(color.RED + "Error parsing quantity: " + e.getMessage() + color.RESET);
                            System.out.println("Problematic cart line: " + line);
                        }
                    }
                    cartLines.add(parts);
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error reading cart file: " + e.getMessage() + color.RESET);
                return;
            }
        }
        if (!updated) {
            String[] newProductLine = {
                    product.getProductID(),
                    product.getName(),
                    String.valueOf(product.getPrice()),
                    String.valueOf(quantity),
                    "Added"
            };
            cartLines.add(newProductLine);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] lineParts : cartLines) {
                writer.write(String.join(",", lineParts));
                writer.newLine();
            }
            System.out.println(quantity + " " + product.getName() + "(s) added to cart.");
        } catch (IOException e) {
            System.out.println(color.RED + "Error writing to cart file: " + e.getMessage() + color.RESET);
            return;
        }
        saveProductFile(productLines);
    }

    private List<String[]> loadProductFile() {
        List<String[]> productLines = new ArrayList<>();
        File productFile = new File("products.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(productFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                productLines.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading product file: " + e.getMessage() + color.RESET);
            return null;
        }
        return productLines;
    }

    private boolean updateStockInMemory(List<String[]> productLines, String productId, int quantity) {
        for (String[] parts : productLines) {
            if (parts.length == 6 && parts[0].equals(productId)) {
                int stock = Integer.parseInt(parts[4]);
                if (stock < quantity) {
                    System.out.println(color.RED + "Error: Not enough stock for product " + productId + "." + color.RESET);
                    return false;
                }
                return true;
            }
        }
        System.out.println(color.RED + "Error: Product ID " + productId + " not found." + color.RESET);
        return false;
    }

    private void saveProductFile(List<String[]> productLines) {
        File productFile = new File("products.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productFile))) {
            for (String[] parts : productLines) {
                writer.write(String.join(",", parts));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error writing to product file: " + e.getMessage() + color.RESET);
        }
    }

    public void removeProductById(String productId, int quantity) {
        if (quantity < 0) {
            System.out.println(color.RED + "Invalid quantity. Please enter a positive number." + color.RESET);
            return;
        }
        File file = new File(cartFile);
        if (!file.exists()) {
            System.out.println(color.BLUE + "Your cart is empty." + color.RESET);
            return;
        }
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        boolean fullyRemoved = false;
        int removedQuantity = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equalsIgnoreCase(productId) && parts[4].equals("Added")) {
                    int currentQuantity = Integer.parseInt(parts[3]);
                    if (currentQuantity > quantity) {
                        removedQuantity = quantity;
                        line = parts[0] + "," + parts[1] + "," + parts[2] + "," + (currentQuantity - quantity) + ",Added";
                    } else if (currentQuantity == quantity) {
                        removedQuantity = quantity;
                        fullyRemoved = true;
                        found = true;
                        continue;
                    }
                    found = true;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading cart for removal: " + e.getMessage() + color.RESET);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cartFile))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error updating cart after removal: " + e.getMessage() + color.RESET);
            return;
        }
        if (found && removedQuantity > 0) {
            List<String> updatedProductLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5 && parts[0].equalsIgnoreCase(productId)) {
                        int stock = Integer.parseInt(parts[3]);
                        stock += removedQuantity;
                        line = parts[0] + "," + parts[1] + "," + parts[2] + "," + stock + "," + parts[4];
                    }
                    updatedProductLines.add(line);
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error reading products.txt for stock update: " + e.getMessage() + color.RESET);
                return;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
                for (String updatedLine : updatedProductLines) {
                    writer.write(updatedLine + "\n");
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error updating products.txt after stock update: " + e.getMessage() + color.RESET);
            }
        }
        if (removedQuantity > 0) {
            if (fullyRemoved) {
                System.out.println(color.GREEN + "Product fully removed from cart." + color.RESET);
            } else {
                System.out.println(color.GREEN + String.valueOf(removedQuantity) + " item(s) removed from cart." + color.RESET);
            }
        } else if (found) {
            System.out.println(color.RED + "Not enough quantity in cart to remove " + quantity + "." + color.RESET);
        } else {
            System.out.println(color.RED + "Product not found in cart." + color.RESET);
        }
    }

    public void viewCart() {
        File file = new File(cartFile);
        if (!file.exists()) {
            System.out.println(color.BLUE + "Your cart is empty." + color.RESET);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
            System.out.println("\nShopping Cart:");
            System.out.printf("%-5s %-40s %-10s %-10s %-10s%n", "ID", "Name", "Price", "Quantity", "Status");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[4].equals("Added")) {
                    String truncatedName = parts[1].length() > 40 ? parts[1].substring(0, 37) + "..." : parts[1];
                    System.out.printf("%-5s %-40s $%-9.2f %-10s %-10s%n",
                            parts[0], truncatedName, Double.parseDouble(parts[2]), parts[3], parts[4]);
                }
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading cart: " + e.getMessage() + color.RESET);
        }
    }

    public List<CartItem> loadCartItems() {
        List<CartItem> items = new ArrayList<>();
        File file = new File(cartFile);
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int qty = Integer.parseInt(parts[3].trim());
                    String status = parts[4].trim();
                    if (status.equals("Added") && qty > 0) {
                        items.add(new CartItem(id, name, price, qty, status));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading cart file: " + e.getMessage() + color.RESET);
            return null;
        } catch (NumberFormatException e) {
            System.out.println(color.RED + "Error parsing cart file data: " + e.getMessage() + color.RESET);
            return null;
        }
        return items;
    }

    public boolean loadCart() {
        List<CartItem> items = loadCartItems();
        if (items == null || items.isEmpty()) {
            System.out.println(color.BLUE + "Your cart is empty. Nothing to checkout." + color.RESET);
            return false;
        }
        this.cartItems = items;
        return true;
    }

    public void markSelectedItemsAsCheckedOut(List<CartItem> selectedItems, LocalDate deliveryDate) {
        List<String[]> fileLines = new ArrayList<>();
        List<String[]> updatedLines = new ArrayList<>();
        List<String[]> orderHistoryLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cartFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading cart for checkout: " + e.getMessage() + color.RESET);
            return;
        }
        System.out.println("Cart before update:");
        fileLines.forEach(line -> System.out.println(Arrays.toString(line)));
        Map<String, Integer> productsToUpdate = new HashMap<>();

        for (String[] parts : fileLines) {
            if (parts.length != 5) {
                continue;
            }
            String productId = parts[0];
            int qtyInCart = Integer.parseInt(parts[3]);
            boolean matched = false;
            for (CartItem selectedItem : selectedItems) {
                if (selectedItem.getProductId().equals(productId)) {
                    matched = true;
                    int checkoutQty = selectedItem.getQuantity();
                    productsToUpdate.put(productId, checkoutQty);

                    if (qtyInCart > checkoutQty) {
                        parts[3] = String.valueOf(qtyInCart - checkoutQty);
                        updatedLines.add(parts);
                        orderHistoryLines.add(new String[]{parts[0], parts[1], parts[2], String.valueOf(checkoutQty), "CheckedOut", deliveryDate.toString()});
                    } else {
                        orderHistoryLines.add(new String[]{parts[0], parts[1], parts[2], String.valueOf(checkoutQty), "CheckedOut", deliveryDate.toString()});
                    }
                    break;
                }
            }
            if (!matched) {
                updatedLines.add(parts);
            }
        }
        System.out.println("Cart after update:");
        updatedLines.forEach(line -> System.out.println(Arrays.toString(line)));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cartFile))) {
            for (String[] lineParts : updatedLines) {
                writer.write(String.join(",", lineParts));
                writer.newLine();
            }
            System.out.println(color.GREEN + "Cart file successfully updated." + color.RESET);
        } catch (IOException e) {
            System.out.println(color.RED + "Error writing to cart file: " + e.getMessage() + color.RESET);
        }
        updateProductStock(productsToUpdate);
        orderHistory.saveToOrderHistoryFile(orderHistoryLines);
    }

    private void updateProductStock(Map<String, Integer> productsToUpdate) {
        if (productsToUpdate.isEmpty()) {
            return;
        }

        Path inventoryPath = Paths.get("products.txt"); // Adjust filename as needed

        try {

            List<String> lines = Files.readAllLines(inventoryPath);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    updatedLines.add(line);
                    continue;
                }

                String productId = parts[0];

                if (productsToUpdate.containsKey(productId)) {
                    int currentStock = Integer.parseInt(parts[4]);
                    int quantityToDeduct = productsToUpdate.get(productId);
                    int newStock = Math.max(0, currentStock - quantityToDeduct);

                    parts[4] = String.valueOf(newStock);

                    updatedLines.add(String.join(",", parts));
                    System.out.println("Updated stock for product " + productId
                            + " from " + currentStock + " to " + newStock);
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(inventoryPath, updatedLines);
            System.out.println("Product inventory updated successfully.");

        } catch (IOException e) {
            System.out.println("Error updating product inventory: " + e.getMessage());
        }
    }
}