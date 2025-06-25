/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;

public class OrderHistory implements OrderManageable {
    private String username;
    private String historyFile;
    static Colors color = new Colors();

    public OrderHistory() {}

    public OrderHistory(String username) {
        this.username = username;
        this.historyFile = username + "_orderhistory.txt";
    }
    
    public static void updateOrderStatusIfDelivered() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        boolean anyUpdated = false;
        File dir = new File(".");
        File[] orderFiles = dir.listFiles((d, name) -> name.endsWith("_orderhistory.txt"));
        if (orderFiles == null || orderFiles.length == 0) {
            System.out.println(color.RED + "No *_orderhistory.txt files found in the current directory." + color.RESET);
            return;
        }
        for (File file : orderFiles) {
            List<String> updatedLines = new ArrayList<>();
            boolean fileUpdated = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 7) {
                        updatedLines.add(line);
                        continue;
                    }
                    String status = parts[5].trim();
                    String deliveryDateStr = parts[6].trim();
                    try {
                        LocalDate deliveryDate = LocalDate.parse(deliveryDateStr, formatter);
                        if (status.equalsIgnoreCase("CheckedOut") && deliveryDate.isBefore(today)) {
                            parts[5] = "Delivered";
                            updatedLines.add(String.join(",", parts));
                            fileUpdated = true;
                            anyUpdated = true;
                        } else {
                            updatedLines.add(line);
                        }
                    } catch (Exception e) {
                        System.out.println(color.RED + "Invalid date in file: " + file.getName() + " -> " + deliveryDateStr + color.RESET);
                        updatedLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error reading file: " + file.getName() + " - " + e.getMessage() + color.RESET);
                continue;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
                if (fileUpdated) {
                    System.out.println(color.GREEN + "Updated orders in: " + file.getName() + color.RESET);
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error writing file: " + file.getName() + " - " + e.getMessage() + color.RESET);
            }
        }
        if (!anyUpdated) {
            System.out.println(color.GREEN + "All order statuses are already up to date." + color.RESET);
        }
    }


    @Override
    public boolean viewOrderHistory() {
        File file = new File(historyFile);
        if (!file.exists()) {
            System.out.println(color.BLUE + "No order history found." + color.RESET);
            return false;
        }
        try {
            List<String[]> orderHistoryLines = OrderHistoryIOHelper.readOrderHistory(historyFile);
            if (orderHistoryLines.isEmpty()) {
                System.out.println(color.BLUE + "No order history found." + color.RESET);
                return false;
            }
            System.out.println("\nOrder History:");
            System.out.printf("%-5s %-20s %-5s %-40s %-10s %-10s %-12s %-15s%n",
                    "No.", "Date and Time", "ID", "Name", "Price", "Qty", "Status", "Delivery Date");
            int orderNumber = 1;
            for (String[] order : orderHistoryLines) {
                if (order.length == 7) {
                    String dateTime = order[0];
                    String productId = order[1];
                    String productName = order[2];
                    double productPrice = Double.parseDouble(order[3]);
                    int quantity = Integer.parseInt(order[4]);
                    String status = order[5];
                    String deliveryDate = order[6];
                    String truncatedName = productName.length() > 40 ? productName.substring(0, 37) + "..." : productName;
                    System.out.printf("%-5d %-20s %-5s %-40s $%-9.2f %-10d %-12s %-15s%n",
                            orderNumber++, dateTime, productId, truncatedName, productPrice, quantity, status, deliveryDate);
                } else if (order.length == 6) {
                    String dateTime = order[0];
                    String productId = order[1];
                    String productName = order[2];
                    double productPrice = Double.parseDouble(order[3]);
                    int quantity = Integer.parseInt(order[4]);
                    String status = order[5];

                    String truncatedName = productName.length() > 40 ? productName.substring(0, 37) + "..." : productName;
                    System.out.printf("%-5d %-20s %-5s %-40s $%-9.2f %-10d %-10s %-15s%n",
                            orderNumber++, dateTime, productId, truncatedName, productPrice, quantity, status, "N/A");
                } else {
                    System.out.println(color.RED + "Invalid order format detected. Skipping." + color.RESET);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading order history file: " + e.getMessage() + color.RESET);
            return false;
        }
    }

    public void saveToOrderHistoryFile(List<String[]> historyItems) {
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String status = "CheckedOut";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true))) {
            for (String[] item : historyItems) {
                String[] record;
                if (item.length == 6) {
                    record = new String[7];
                    record[0] = currentDateTime;
                    System.arraycopy(item, 0, record, 1, 5);
                    record[6] = item[5];
                } else {
                    record = item;
                }
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error writing order history: " + e.getMessage() + color.RESET);
        }
    }

    public List<Integer> getAvailableChangeDeliveryDate() {
        File file = new File(historyFile);
        List<Integer> validIndexes = new ArrayList<>();
        if (!file.exists()) {
            System.out.println(color.BLUE + "No order history found." + color.RESET);
            return null;
        }
        try {
            List<String[]> orderHistoryLines = OrderHistoryIOHelper.readOrderHistory(historyFile);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.println("\nAvailable for Delivery Date Change:");
            System.out.printf("%-5s %-20s %-5s %-40s %-15s%n",
                    "No.", "Date and Time", "ID", "Name", "Delivery Date");
            int orderNumber = 1;
            LocalDate today = LocalDate.now();
            for (int i = 0; i < orderHistoryLines.size(); i++) {
                String[] order = orderHistoryLines.get(i);
                if (order.length == 7) {
                    String dateTime = order[0];
                    String productId = order[1];
                    String productName = order[2];
                    String status = order[5];
                    String deliveryDateStr = order[6];
                    if ("CheckedOut".equalsIgnoreCase(status)) {
                        try {
                            LocalDate deliveryDate = LocalDate.parse(deliveryDateStr, formatter);
                            if (deliveryDate.isAfter(today)) {
                                String truncatedName = productName.length() > 40 ? productName.substring(0, 37) + "..." : productName;
                                System.out.printf("%-5d %-20s %-5s %-40s %-15s%n",
                                        orderNumber++, dateTime, productId, truncatedName, deliveryDateStr);
                                validIndexes.add(i); 
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println(color.RED + "Invalid delivery date format: " + deliveryDateStr + color.RESET);
                        }
                    }
                }
            }
            if (orderNumber == 1) {
                System.out.println(color.BLUE + "No upcoming delivery dates found." + color.RESET);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading order history file: " + e.getMessage() + color.RESET);
        }
        return validIndexes;
    }

    public void updateDeliveryDate(int actualOrderIndex, LocalDate newDeliveryDate) {
        File file = new File(historyFile);
        if (!file.exists()) {
            System.out.println(color.BLUE + "No order history found." + color.RESET);
            return;
        }
        try {
            List<String[]> orderHistoryLines = OrderHistoryIOHelper.readOrderHistory(historyFile);
            if (actualOrderIndex < 0 || actualOrderIndex >= orderHistoryLines.size()) {
                System.out.println(color.RED + "Invalid selection. No update performed." + color.RESET);
                return;
            }
            orderHistoryLines.get(actualOrderIndex)[6] = newDeliveryDate.toString();
            OrderHistoryIOHelper.writeOrderHistory(historyFile, orderHistoryLines);
            System.out.println(color.GREEN + "Delivery date updated successfully!" + color.RESET);
        } catch (IOException e) {
            System.out.println(color.RED + "Error processing order history file: " + e.getMessage() + color.RESET);
        }
    }

    @Override
    public void cancelOrder(List<Integer> orderNumbers) {
        try {
            List<String[]> orderHistoryLines = OrderHistoryIOHelper.readOrderHistory(historyFile);
            boolean cancelled = false;
            for (int orderNumber : orderNumbers) {
                if (orderNumber < 1 || orderNumber > orderHistoryLines.size()) {
                    System.out.println(color.RED + "Invalid order number " + orderNumber + ", skipping." + color.RESET);
                    continue;
                }
                String[] selectedOrder = orderHistoryLines.get(orderNumber - 1);
                if (selectedOrder.length >= 7) {
                    String status = selectedOrder[5].trim();
                    if (status.equalsIgnoreCase("Cancelled") || status.equalsIgnoreCase("Requested")) {
                        System.out.println(color.RED + "Order " + orderNumber + " is already '" + status + "', skipping." + color.RESET);
                        continue;
                    }
                    selectedOrder[5] = "Requested";
                    cancelled = true;
                    System.out.println(color.GREEN + "Order " + orderNumber + " has been updated to 'Requested'." + color.RESET);
                } else {
                    System.out.println(color.RED + "Order " + orderNumber + " has an invalid format (less than 7 fields). Skipping." + color.RESET);
                }
            }
            if (cancelled) {
                OrderHistoryIOHelper.writeOrderHistory(historyFile, orderHistoryLines);
                System.out.println(color.GREEN + "Order history updated successfully." + color.RESET);
            } else {
                System.out.println(color.BLUE + "No orders were cancelled." + color.RESET);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading or writing order history: " + e.getMessage() + color.RESET);
        }
    }
    
    public void viewAllCustomerOrderHistories() {
        File currentDir = new File(".");
        File[] orderFiles = currentDir.listFiles((dir, name) -> name.endsWith("_orderhistory.txt"));
        if (orderFiles == null || orderFiles.length == 0) {
            System.out.println(color.BLUE + "No order history files found." + color.RESET);
            return;
        }
        List<OrderRecord> allOrders = new ArrayList<>();
        for (File file : orderFiles) {
            String filename = file.getName();
            String username = filename.replace("_orderhistory.txt", "");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        allOrders.add(new OrderRecord(
                                username,
                                parts[0],                      // dateTime
                                parts[1],                      // productId
                                parts[2],                      // productName
                                Double.parseDouble(parts[3]),  // price
                                Integer.parseInt(parts[4]),    // qty
                                parts[5],                      // status
                                parts[6]                       // deliveryDate
                        ));
                    } else {
                        System.out.println(color.RED + "Invalid record in " + filename + ", skipping." + color.RESET);
                    }
                }
            } catch (IOException e) {
                System.out.println(color.RED + "Error reading file " + filename + ": " + e.getMessage() + color.RESET);
            }
        }
        allOrders.sort(Comparator.comparing(o -> o.dateTime));
        System.out.println("\n=== All Customer Orders ===");
        System.out.printf("%-5s %-15s %-20s %-8s %-35s %-13s %-8s %-12s %-15s%n",
                "No.", "Username", "Date & Time", "ID", "Name", "Price", "Qty", "Status", "Delivery Date");
        int orderNumber = 1;
        for (OrderRecord o : allOrders) {
            String qtyDisplay = (o.qty >= 0) ? String.valueOf(o.qty) : "-";
            System.out.printf("%-5d %-15s %-20s %-8s %-35s RM%-11.2f %-8s %-12s %-15s%n",
                    orderNumber++, o.username, o.dateTime, o.productId, o.productName, o.price, qtyDisplay, o.status, o.deliveryDate);
        }
    }
}
