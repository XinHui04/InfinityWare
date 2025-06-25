/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Receipt {
    Colors color = new Colors();

    public void generateReceipt(String type, List<CartItem> items, String username, LocalDate deliveryDate, double priceAfterFee) {
        StringBuilder receipt = new StringBuilder();
        int receiptWidth = 60;
        receipt.append("\n").append(repeatChar('=', receiptWidth)).append("\n");
        receipt.append("Customer: ").append(username).append("\n");
        receipt.append("Date: ").append(new Date()).append("\n");
        receipt.append("Delivery Date: ").append(deliveryDate).append("\n");
        receipt.append(repeatChar('-', receiptWidth)).append("\n");
        receipt.append(String.format("%-4s %-30s %12s %12s%n", "QTY", "ITEM", "PRICE (RM)", "TOTAL (RM)"));
        receipt.append(repeatChar('-', receiptWidth)).append("\n");
        double subtotal = 0;
        for (CartItem item : items) {
            double itemTotal = item.getPrice() * item.getQuantity();
            receipt.append(String.format("%-4d %-30s %12.2f %12.2f%n",
                    item.getQuantity(),
                    item.getProductName(),
                    item.getPrice(),
                    itemTotal));
            subtotal += itemTotal;
        }
        double discount = 0.0;
        String discountRate = "0%";
        if (subtotal >= 5000) {
            discount = subtotal * 0.7;
            discountRate = "70%";
        } else if (subtotal >= 4000) {
            discount = subtotal * 0.5;
            discountRate = "50%";
        } else if (subtotal >= 3000) {
            discount = subtotal * 0.3;
            discountRate = "30%";
        } else if (subtotal >= 2000) {
            discount = subtotal * 0.15;
            discountRate = "15%";
        }
        double discountedTotal = subtotal - discount;
        double tax = discountedTotal * 0.08;
        double afterTax = discountedTotal + tax;
        double processingFee = priceAfterFee - subtotal;
        double totalWithFee = afterTax + processingFee;
        double studentDiscount = 0;
        if (type.equalsIgnoreCase("Student")) {
            studentDiscount = subtotal * 0.10;
        }
        double finalTotal = totalWithFee - studentDiscount;
        receipt.append(repeatChar('-', receiptWidth)).append("\n");
        receipt.append(String.format("%-44s RM%10.2f%n", "SUBTOTAL:", subtotal));
        if (!discountRate.equalsIgnoreCase("0%")) {
            receipt.append(String.format("%-44s RM%10.2f%n", discountRate + " DISCOUNT:", discount));
        }
        receipt.append(String.format("%-44s RM%10.2f%n", "TAX (8%):", tax));
        receipt.append(String.format("%-44s RM%10.2f%n", "PROCESSING FEE:", processingFee));
        if (studentDiscount > 0) {
            receipt.append(String.format("%-44s RM%10.2f%n", "10% Student Discount:", studentDiscount));
        }
        receipt.append(String.format("%-44s RM%10.2f%n", "TOTAL:", finalTotal));
        receipt.append(repeatChar('=', receiptWidth)).append("\n");
        String thankYouMessage = "Thank you!";
        int padding = (receiptWidth - thankYouMessage.length()) / 2;
        String paddedThankYou = repeatChar(' ', padding) + thankYouMessage + repeatChar(' ', padding);
        if (paddedThankYou.length() < receiptWidth) {
            paddedThankYou += " ";
        }
        receipt.append(paddedThankYou).append("\n");
        receipt.append(repeatChar('=', receiptWidth)).append("\n");
        String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String filename = username + "receipt" + dateString + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(receipt.toString());
        } catch (IOException e) {
            System.out.println(color.RED + "Error writing receipt to file: " + e.getMessage() + color.RESET);
        }
    }

   private String repeatChar(char ch, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
