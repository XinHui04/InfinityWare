/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public abstract class Product {
    private String productID;
    private String name;
    private double price;
    private int quantity;

    // Default constructor
    public Product() {}

    // Constructor with fields (without category parameter)
    public Product(String productID, String name, double price, int quantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Setters
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }

    // Abstract method that subclasses must implement
    public abstract String getCategory();
}