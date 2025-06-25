/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.util.List;

public class CartItem {
    private String productId, productName, status;
    private double price;
    private int quantity;

    public CartItem() {}

    public CartItem(String productId, String productName, double price, int quantity, String status) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setStatus(String status) {
        this.status = status;
    }
}

