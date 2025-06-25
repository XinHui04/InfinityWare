/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class OrderRecord {
    String username;
    String dateTime;
    String productId;
    String productName;
    double price;
    int qty; // -1 if not available
    String status;
    String deliveryDate;

    public OrderRecord(String username, String dateTime, String productId,
                       String productName, double price, int qty, String status, String deliveryDate) {
        this.username = username;
        this.dateTime = dateTime;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.qty = qty;
        this.status = status;
        this.deliveryDate = deliveryDate;
    }

    public OrderRecord(String username, String dateTime, String productId,
                       String productName, double price, int qty, String status) {
        this(username, dateTime, productId, productName, price, qty, status, "-");
    }
}
