/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryIOHelper {

    public static List<String[]> readOrderHistory(String historyFile) throws IOException {
        List<String[]> orderHistoryLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orderHistoryLines.add(line.split(","));
            }
        }
        return orderHistoryLines;
    }

    public static void writeOrderHistory(String historyFile, List<String[]> orderHistoryLines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))) {
            for (String[] order : orderHistoryLines) {
                writer.write(String.join(",", order) + "\n");
            }
        }
    }
}

