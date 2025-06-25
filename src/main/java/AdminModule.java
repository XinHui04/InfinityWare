/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Collections;

public class AdminModule {
    private static final String PRODUCT_FILE_NAME = "products.txt";
    private static Map<Integer, String[]> indexMap = new HashMap<>();
    private List<Product> products = new ArrayList<>();
    private static Product selectedProduct;
    static Colors color = new Colors();

    static {
        selectedProduct = null;
    }

    public static List<String> displayRequestedOrders() {
        indexMap.clear();
        List<String> displayList = new ArrayList<>();
        int index = 1;
        File dir = new File(".");
        File[] orderFiles = dir.listFiles((d, name) -> name.endsWith("_orderhistory.txt"));
        System.out.println("\nRequested Orders:");
        System.out.printf("%-5s %-15s %-20s %-5s %-35s %-13s %-10s %-12s %-15s%n",
                "No.", "Username", "Date and Time", "ID", "Name", "Price", "Qty", "Status", "Delivery Date");
        for (File file : orderFiles) {
            String fileName = file.getName();
            String username = fileName.replace("_orderhistory.txt", "");
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    String[] parts = line.split(",");
                    if (parts.length >= 7 && parts[5].equalsIgnoreCase("Requested")) {
                        String dateTime = parts[0];
                        String productId = parts[1];
                        String productName = parts[2];
                        double productPrice = Double.parseDouble(parts[3]);
                        int quantity = Integer.parseInt(parts[4]);
                        String status = parts[5];
                        String deliveryDate = parts[6];
                        System.out.printf("%-5d %-15s %-20s %-5s %-35s RM%-11.2f %-10d %-12s %-15s%n",
                                index, username, dateTime, productId, productName, productPrice, quantity, status, deliveryDate);
                        indexMap.put(index, new String[]{file.getPath(), String.valueOf(i)});
                        displayList.add(line);
                        index++;
                    }
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error reading file: " + file.getName() + color.RESET);
            }
        }
        if (index == 1) {
            System.out.println(color.BLUE + "No orders with 'Requested' status found." + color.RESET);
        }
        return displayList;
    }

    public static void acceptRequestedOrders(String[] selectedNumbers) {
        Map<String, Integer> productStockMap = new HashMap<>();
        for (String numStr : selectedNumbers) {
            try {
                int choice = Integer.parseInt(numStr.trim());
                if (indexMap.containsKey(choice)) {
                    String[] info = indexMap.get(choice);
                    Path filePath = Paths.get(info[0]);
                    int lineIndex = Integer.parseInt(info[1]);
                    List<String> lines = Files.readAllLines(filePath);
                    String[] parts = lines.get(lineIndex).split(",");
                    if (parts.length < 6) {
                        System.out.println(color.RED + "Invalid order format at index " + choice + ", skipping." + color.RESET);
                        continue;
                    }
                    String status = parts[5].trim();
                    if ("Requested".equalsIgnoreCase(status)) {
                        int requestedQuantity = Integer.parseInt(parts[4].trim());
                        String productId = parts[1].trim();
                        String productName = parts[2].trim();
                        String price = parts[3].trim();
                        String orderDateTime = parts[0].trim();
                        String deliveryDate = parts[6].trim();
                        String fileName = filePath.getFileName().toString();
                        String username = fileName.replace("_orderhistory.txt", "");
                        productStockMap.put(productId, productStockMap.getOrDefault(productId, 0) + requestedQuantity);
                        parts[5] = "Cancelled";
                        lines.set(lineIndex, String.join(",", parts));
                        Files.write(filePath, lines);
                        OrderAction action = new CancelOrderAction(parts, filePath, lineIndex, username, deliveryDate);
                        action.execute();
                        System.out.println(color.GREEN + "Order [" + choice + "] status updated to Cancelled." + color.RESET);
                    } else {
                        System.out.println(color.RED + "Order [" + choice + "] was not in 'Requested' status, no action taken." + color.RESET);
                    }
                } else {
                    System.out.println(color.RED + "Invalid index: " + choice + color.RESET);
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println(color.RED + "Error updating order: " + e.getMessage() + color.RESET);
            }
        }
        updateProductStockBatch(productStockMap);
    }

    private static void updateProductStockBatch(Map<String, Integer> stockUpdates) {
        File productFile = new File(PRODUCT_FILE_NAME);
        List<String[]> productLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(productFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String productId = parts[0];
                    if (stockUpdates.containsKey(productId)) {
                        int existingStock = Integer.parseInt(parts[4]);
                        int updateAmount = stockUpdates.get(productId);
                        parts[4] = String.valueOf(existingStock + updateAmount);
                        System.out.println(color.GREEN + "Updated stock for product: " + productId + " to " + parts[4] + color.RESET);
                    }
                }
                productLines.add(parts);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(productFile))) {
                for (String[] lineParts : productLines) {
                    writer.write(String.join(",", lineParts));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error updating product stock: " + e.getMessage() + color.RESET);
        }
    }
}