/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class ProductFactory {
    public static Product createProduct(String category, String productID, String name, double price, int quantity, String specificType) {
        switch(category) {
            case "CC":
                return new ComputerComponent(productID, name, price, quantity, specificType);
            case "PA":
                return new PeripheralAccessory(productID, name, price, quantity, specificType);
            case "NC":
                return new NetworkingProduct(productID, name, price, quantity, specificType);
            default:
                throw new IllegalArgumentException("Invalid product category: " + category);
        }
    }
}