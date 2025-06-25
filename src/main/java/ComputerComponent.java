/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class ComputerComponent extends Product {
    private String componentType; // CPU, GPU, RAM, etc.

    public ComputerComponent(String productID, String name, double price, int quantity, String componentType) {
        super(productID, name, price, quantity);
        this.componentType = componentType;
    }

    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }

    @Override
    public String getCategory() {
        return "CC"; // Category code for Computer Components
    }
}