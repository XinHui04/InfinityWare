/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public abstract class Payment {
    protected Colors color = new Colors();
    
    public abstract void setAmount(double amount);

    public abstract boolean processPayment();
    public abstract double calculateFees();
}