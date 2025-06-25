/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.util.*;

public class SalesReport {
    Colors color = new Colors();

    public void totalSalesReport() {
        double totalSales = 0;
        int totalOrders = 0;
        try {
            File folder = new File(".");
            File[] orderFiles = folder.listFiles((dir, name) -> name.endsWith("_orderhistory.txt"));
            if (orderFiles != null) {
                for (File orderFile : orderFiles) {
                    BufferedReader reader = new BufferedReader(new FileReader(orderFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String status = parts[5].trim();
                        if (status.equalsIgnoreCase("CheckedOut") || status.equalsIgnoreCase("Delivered")) {
                            double price = Double.parseDouble(parts[3].trim());
                            int quantity = Integer.parseInt(parts[4].trim());
                            totalSales += price * quantity;
                            totalOrders++;
                        }
                    }
                    reader.close();
                }
                System.out.println("\n\nGenerating Total Sales Report...");
                System.out.println("\n======= Total Sales Report =======");
                System.out.printf("| %-20s: $%.2f\n", "Total Sales", totalSales);
                System.out.printf("| %-20s: %d\n", "Total Orders", totalOrders);
                System.out.printf("| %-20s: $%.2f\n", "Average Order Amount", (totalOrders == 0 ? 0 : totalSales / totalOrders));
                System.out.println("==================================");
            } else {
                System.out.println(color.BLUE + "No order history files found." + color.RESET);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading order history files: " + e.getMessage() + color.RESET);
        }
    }

    public void salesTrendReport() {
        Map<String, Integer> productSales = new HashMap<>();
        try {
            File folder = new File(".");
            File[] orderFiles = folder.listFiles((dir, name) -> name.endsWith("_orderhistory.txt"));
            if (orderFiles != null) {
                for (File orderFile : orderFiles) {
                    BufferedReader reader = new BufferedReader(new FileReader(orderFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String status = parts[5].trim();
                        if (status.equalsIgnoreCase("CheckedOut") || status.equalsIgnoreCase("Delivered")) {
                            String productId = parts[1].trim();
                            int quantity = Integer.parseInt(parts[4].trim());
                            productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
                        }
                    }
                    reader.close();
                }
                List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productSales.entrySet());
                sortedProducts.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
                System.out.println("\n\nGenerating Sales Trend Report...");
                System.out.println("\nTop 5 Products by Quantity Sold:");
                int topLimit = Math.min(5, sortedProducts.size());
                for (int i = 0; i < topLimit; i++) {
                    Map.Entry<String, Integer> entry = sortedProducts.get(i);
                    String productId = entry.getKey();
                    int quantity = entry.getValue();

                    String bar = new String(new char[quantity]).replace('\0', '█');
                    if (quantity > 50) bar = bar.substring(0, 50) + "...";
                    System.out.printf("Product ID: %-10s - Quantity Sold: %-3d | %s\n", productId, quantity, bar);
                }
            } else {
                System.out.println(color.BLUE + "No order history files found." + color.RESET);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading order history files: " + e.getMessage() + color.RESET);
        }
    }

    public void cancelledOrdersReport() {
        int cancelledOrders = 0;
        try {
            File folder = new File(".");
            File[] orderFiles = folder.listFiles((dir, name) -> name.endsWith("_orderhistory.txt"));
            if (orderFiles != null) {
                System.out.println("\n\nGenerating Cancelled Orders Report...");
                System.out.println("\n+---------------------+--------+---------------------------------------------+--------+----------+-----------+-------------------+");
                System.out.printf("| %-19s | %-6s | %-43s | %-6s | %-8s | %-9s | %-17s |\n",
                        "Date & Time", "PID", "Product", "Price", "Quantity", "Status", "Delivery Date");
                System.out.println("+---------------------+--------+---------------------------------------------+--------+----------+-----------+-------------------+");
                for (File orderFile : orderFiles) {
                    BufferedReader reader = new BufferedReader(new FileReader(orderFile));
                    String line;
                    boolean foundCancelled = false;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 7) {
                            String status = parts[5].trim();
                            if (status.equalsIgnoreCase("Cancelled")) {
                                foundCancelled = true;
                                cancelledOrders++;
                                String deliveryDate = parts[6];
                                String productName = parts[2].trim();
                                if (productName.length() > 35) {
                                    productName = productName.substring(0, 35) + "...";
                                }

                                System.out.printf("| %-19s | %-6s | %-43s | %-6s | %-8s | %-9s | %-17s |\n",
                                        parts[0].trim(), parts[1].trim(), productName,
                                        parts[3].trim(), parts[4].trim(), parts[5].trim(), deliveryDate);
                            }
                        }
                    }
                    reader.close();
                }
                System.out.println("+---------------------+--------+---------------------------------------------+--------+----------+-----------+-------------------+");
                System.out.println(color.GREEN + "Total Cancelled Orders: " + cancelledOrders + color.RESET);
            } else {
                System.out.println(color.BLUE + "No order history files found." + color.RESET);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading order history files: " + e.getMessage() + color.RESET);
        }
    }
}
