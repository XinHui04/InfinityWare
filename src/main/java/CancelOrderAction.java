/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class CancelOrderAction extends OrderAction {
    Colors color = new Colors();
    private String username;
    private String deliveryDate; // Add a delivery date field

    public CancelOrderAction(String[] orderParts, Path filePath, int lineIndex, String username, String deliveryDate) {
        super(orderParts, filePath, lineIndex, Collections.singletonList(username));
        this.username = username;
        this.deliveryDate = deliveryDate; // Initialize the delivery date
    }

    @Override
    public void execute() throws IOException {
        String productName = orderParts[2];
        String price = orderParts[3];
        String quantity = orderParts[4];
        String orderDateTime = orderParts[0];
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String adminAcceptDateTime = java.time.LocalDateTime.now().format(formatter);

        // Include the delivery date in the refund info
        String refundInfo = String.join(",", productName, price, quantity, orderDateTime, adminAcceptDateTime, deliveryDate, "Waiting");

        Path refundPath = Paths.get(username + "_refund.txt");
        Files.write(refundPath, Collections.singletonList(refundInfo), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println(color.GREEN + "Refund info written for username: " + username + color.RESET);
    }
}
