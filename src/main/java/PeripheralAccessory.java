/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class PeripheralAccessory extends Product {
    private String deviceType; // Mouse, Keyboard, Monitor, etc.

    public PeripheralAccessory(String productID, String name, double price, int quantity, String deviceType) {
        super(productID, name, price, quantity);
        this.deviceType = deviceType;
    }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    @Override
    public String getCategory() {
        return "PA"; // Category code for Peripherals & Accessories
    }
}