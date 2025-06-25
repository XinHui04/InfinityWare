/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class TngPayment extends Payment {
    private String tngDigit;
    private double amount;
    
    public TngPayment () {}

    public TngPayment(String tngDigit) {
        this.tngDigit = tngDigit;
    }
    
    public double getAmount() {
        return amount;
    }
    
    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean processPayment() {
        if (!tngDigit.matches("\\d{6}")) {
            System.out.println(color.RED + "Invalid input! Please enter exactly 6 digits (e.g., 090909)." + color.RESET);
            return false;
        }
        return true;
    }
    
    @Override
    public double calculateFees() {
        return amount * 0.020;
    }
}
