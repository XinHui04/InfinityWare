/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Refund {
    private String username;
    Colors color = new Colors();

    public Refund(String username) {
        this.username = username;
    }

    public void getRefund() {
        Path refundPath = Paths.get(username + "_refund.txt");
        if (!Files.exists(refundPath)) {
            System.out.println(color.BLUE + "No refund records found." + color.RESET);
            return;
        }
        try {
            List<String> lines = Files.readAllLines(refundPath);
            List<String[]> waitingRefunds = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[6].equals("Waiting")) {
                    waitingRefunds.add(parts);
                }
            }
            if (waitingRefunds.isEmpty()) {
                System.out.println(color.BLUE + "No pending refunds found." + color.RESET);
                return;
            }
            System.out.println("\n=== Pending Refunds ===");
            System.out.printf("%-5s %-40s %-10s %-8s %-25s %-25s %-10s %-15s%n",
                    "No.", "Product", "Price", "Qty", "Order Date", "Accepted Date", "Status", "Delivery Date");
            int i = 1;
            for (String[] parts : waitingRefunds) {
                String truncatedName = parts[0].length() > 40 ? parts[0].substring(0, 37) + "..." : parts[0];
                System.out.printf("%-5d %-40s $%-9s %-8s %-25s %-25s %-10s %-15s%n",
                        i++, truncatedName, parts[1], parts[2], parts[3], parts[4], parts[6], parts[5]);
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error reading refund records: " + e.getMessage() + color.RESET);
        }
    }

    public boolean hasPendingRefund(String username) {
        Path refundPath = Paths.get(username + "_refund.txt");
        if (!Files.exists(refundPath)) {
            return false;
        }
        try {
            List<String> lines = Files.readAllLines(refundPath);
            return lines.stream().anyMatch(line -> line.endsWith("Waiting"));
        } catch (IOException e) {
            System.out.println(color.RED + "Error checking refunds: " + e.getMessage() + color.RESET);
            return false;
        }
    }

    public void updateRefund() {
        Path refundPath = Paths.get(username + "_refund.txt");
        if (!Files.exists(refundPath)) {
            System.out.println(color.BLUE + "No refund records found." + color.RESET);
            return;
        }
        try {
            List<String> lines = Files.readAllLines(refundPath);
            boolean hasWaiting = lines.stream().anyMatch(line -> line.endsWith("Waiting"));
            if (!hasWaiting) {
                System.out.println(color.BLUE + "No 'Waiting' refunds to update." + color.RESET);
                return;
            }
            List<String> updated = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[6].equals("Waiting")) {
                    parts[6] = "Received";
                }
                updated.add(String.join(",", parts));
            }
            Files.write(refundPath, updated);
            System.out.println(color.GREEN + "All refunds updated to 'Received' for " + username + "." + color.RESET);
        } catch (IOException e) {
            System.out.println(color.RED + "Error updating refund records: " + e.getMessage() + color.RESET);
        }
    }
}
