/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class BankPayment extends Payment {
    private String bankUserName;
    private String bankPassword;
    private double amount;
    
    public BankPayment () {}

    public BankPayment(String bankUserName, String bankPassword) {
        this.bankUserName = bankUserName;
        this.bankPassword = bankPassword;
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
        if (bankUserName == null || bankUserName.isEmpty()) {
            System.out.println(color.RED + "Username cannot be empty." + color.RESET);
            return false;
        }
        if (!bankUserName.matches("[a-zA-Z]{3,20}")) {
            System.out.println(color.RED + "Invalid username! It must contain only letters and be 3 to 20 characters long." + color.RESET);
            return false;
        }
        if (bankPassword.length() < 6) {
            System.out.println(color.RED + "Password must be at least 6 characters long." + color.RESET);
            return false;
        }
        return true; 
    }
    
    @Override
    public double calculateFees() {
        return amount * 0.030;
    }
}
