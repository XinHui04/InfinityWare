/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.nio.file.*;

public class ChangeProfile {
    private UserModule userModule;
    private User customer;
    Colors color = new Colors();

    public ChangeProfile(UserModule userModule, User customer) {
        this.userModule = userModule;
        this.customer = customer;
    }

    public void updateProfile(String field, String newValue) {
        File file = new File("users.txt");
        File tempFile = new File("users_temp.txt");
        String oldUsername = customer.getUsername();

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userFields = line.split(",");
                if (userFields[0].equals(customer.getUsername())) {
                    switch (field) {
                        case "username":
                            userFields[0] = newValue;
                            customer.setUsername(newValue);
                            break;
                        case "phoneNumber":
                            userFields[1] = newValue;
                            customer.setPhoneNumber(newValue);
                            break;
                        case "email":
                            userFields[2] = newValue;
                            customer.setEmail(newValue);
                            break;
                        case "password":
                            userFields[3] = newValue;
                            customer.setPassword(newValue);
                            break;
                    }
                }
                writer.write(String.join(",", userFields));
                writer.newLine();
            }
            writer.flush();
            reader.close();
            writer.close();
            try {
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println(color.RED + "Error renaming temporary file: " + e.getMessage() + color.RESET);
                return;
            }

            if (field.equals("username")) {
                renameFile("cart_" + oldUsername + ".txt", "cart_" + newValue + ".txt");
                renameFile(oldUsername + "_orderhistory.txt", newValue + "_orderhistory.txt");
                renameFile(oldUsername + "_refund.txt", newValue + "_refund.txt");
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error updating profile: " + e.getMessage() + color.RESET);
        }
    }

    private void renameFile(String oldName, String newName) {
        File oldFile = new File(oldName);
        File newFile = new File(newName);
        if (!oldFile.exists()) {
            System.out.println(color.YELLOW + "Warning: File " + oldName + " does not exist." + color.RESET);
            return;
        }
        if (newFile.exists()) {
            System.out.println(color.YELLOW + "Warning: File " + newName + " already exists. Renaming failed." + color.RESET);
            return;
        }
        if (!oldFile.renameTo(newFile)) {
            System.out.println(color.YELLOW + "Warning: Could not rename file " + oldName + " to " + newName + "." + color.RESET);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!oldFile.renameTo(newFile)) {
                System.out.println(color.RED + "Error: Could not rename file even after retrying." + color.RESET);
            }
        } else {
            System.out.println(color.GREEN + "Successfully renamed " + oldName + " to " + newName + "." + color.RESET);
        }
    }
}