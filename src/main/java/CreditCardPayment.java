/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class CreditCardPayment extends Payment {
    private int cvv;
    private double amount;
    
    public CreditCardPayment () {}

    public CreditCardPayment(int cvv) {
        this.cvv = cvv;
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
        if (cvv < 100 || cvv > 9999) {
            System.out.println(color.RED + "Invalid CVV! It must be 3 or 4 digits." + color.RESET);
            return false;
        }
        return true;
    }
    
    @Override
    public double calculateFees() {
        return amount * 0.025;
    }
}
