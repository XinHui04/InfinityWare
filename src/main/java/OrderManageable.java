/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.List;

public interface OrderManageable {
    void cancelOrder(List<Integer> orderNumbers);
    boolean viewOrderHistory();
}

