/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class NetworkingProduct extends Product {
    private String networkType; // Router, Switch, Cable, etc.

    public NetworkingProduct(String productID, String name, double price, int quantity, String networkType) {
        super(productID, name, price, quantity);
        this.networkType = networkType;
    }

    public String getNetworkType() { return networkType; }
    public void setNetworkType(String networkType) { this.networkType = networkType; }

    @Override
    public String getCategory() {
        return "NC"; // Category code for Networking & Connectivity
    }
}