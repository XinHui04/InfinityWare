/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class ProductModule {

    private static final String PRODUCT_FILE_NAME = "products.txt";
    private static List<Product> products = new ArrayList<>();
    private static Product selectedProduct;
    Colors color = new Colors();
    private List<Product> foundProducts = new ArrayList<>();

    static {
        selectedProduct = null;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> updatedProducts) {
        products = updatedProducts;
    }

    public void viewCartWithOptions(ShoppingCart cart) {
        cart.viewCart();
    }

    public void displayProductsWithStudentDiscount(double discountRate) {
        loadProductsFromFile();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-45s | %-20s | %-10s | %-10s | %-25s |\n",
                    "ID", "Name", "Type", "Price", "Stock", "Category");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");

            for (Product product : products) {
                // Determine the full category name
                String fullCategory;
                switch (product.getCategory()) {
                    case "CC":
                        fullCategory = "Computer Components";
                        break;
                    case "PA":
                        fullCategory = "Peripherals & Accessories";
                        break;
                    case "NC":
                        fullCategory = "Networking & Connectivity";
                        break;
                    default:
                        fullCategory = "Unknown";
                        break;
                }

                String specificType = "";
                if (product instanceof ComputerComponent) {
                    specificType = ((ComputerComponent) product).getComponentType();
                } else if (product instanceof PeripheralAccessory) {
                    specificType = ((PeripheralAccessory) product).getDeviceType();
                } else if (product instanceof NetworkingProduct) {
                    specificType = ((NetworkingProduct) product).getNetworkType();
                }

                double studentPrice = product.getPrice() - (product.getPrice() * discountRate);
                System.out.printf("| %-5s | %-45s | %-20s | $%-9.2f | %-10d | %-25s |\n",
                        product.getProductID(),
                        product.getName(),
                        specificType,
                        studentPrice,
                        product.getQuantity(),
                        fullCategory);

                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
    }

    public void viewProducts() {
        loadProductsFromFile();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-45s | %-20s | %-10s | %-10s | %-25s |\n",
                    "ID", "Name", "Type", "Price", "Stock", "Category");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");

            for (Product product : products) {
                // Determine the full category name
                String fullCategory;
                switch (product.getCategory()) {
                    case "CC":
                        fullCategory = "Computer Components";
                        break;
                    case "PA":
                        fullCategory = "Peripherals & Accessories";
                        break;
                    case "NC":
                        fullCategory = "Networking & Connectivity";
                        break;
                    default:
                        fullCategory = "Unknown";
                        break;
                }

                String specificType = "";
                if (product instanceof ComputerComponent) {
                    specificType = ((ComputerComponent) product).getComponentType();
                } else if (product instanceof PeripheralAccessory) {
                    specificType = ((PeripheralAccessory) product).getDeviceType();
                } else if (product instanceof NetworkingProduct) {
                    specificType = ((NetworkingProduct) product).getNetworkType();
                }

                System.out.printf("| %-5s | %-45s | %-20s | $%-9.2f | %-10d | %-25s |\n",
                        product.getProductID(),
                        product.getName(),
                        specificType, 
                        product.getPrice(),
                        product.getQuantity(),
                        fullCategory);

                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
    }

    public List<Product> getProductsByCategory(String category) {
        loadProductsFromFile();
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public void sortProductsByPrice(List<Product> productList, boolean ascending) {
        productList.sort((p1, p2) -> {
            if (ascending) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            } else {
                return Double.compare(p2.getPrice(), p1.getPrice());
            }
        });
    }

    public void sortProductsByType(List<Product> productList, boolean ascending) {
        productList.sort((p1, p2) -> {
            String type1 = "";
            String type2 = "";

            if (p1 instanceof ComputerComponent) {
                type1 = ((ComputerComponent) p1).getComponentType();
            } else if (p1 instanceof PeripheralAccessory) {
                type1 = ((PeripheralAccessory) p1).getDeviceType();
            } else if (p1 instanceof NetworkingProduct) {
                type1 = ((NetworkingProduct) p1).getNetworkType();
            }

            if (p2 instanceof ComputerComponent) {
                type2 = ((ComputerComponent) p2).getComponentType();
            } else if (p2 instanceof PeripheralAccessory) {
                type2 = ((PeripheralAccessory) p2).getDeviceType();
            } else if (p2 instanceof NetworkingProduct) {
                type2 = ((NetworkingProduct) p2).getNetworkType();
            }

            return ascending ? type1.compareTo(type2) : type2.compareTo(type1);
        });
    }

    public void viewProductsSortedBySpecificType(String category) {
        loadProductsFromFile();

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        // Filter products by category (CC, PA, NC)
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty()) {
            System.out.println("No products found for the specified category.");
            return;
        }

        // Use the existing sortProductsByType method to sort by type in ascending order
        sortProductsByType(filteredProducts, true);

        // Display header
        System.out.println("\n----------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-45s | %-20s | %-10s | %-10s |\n", "ID", "Name", "Type", "Price", "Stock");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // Display each product
        for (Product product : filteredProducts) {
            String specificType = "N/A";

            if (product instanceof ComputerComponent) {
                specificType = ((ComputerComponent) product).getComponentType();
            } else if (product instanceof PeripheralAccessory) {
                specificType = ((PeripheralAccessory) product).getDeviceType();
            } else if (product instanceof NetworkingProduct) {
                specificType = ((NetworkingProduct) product).getNetworkType();
            }

            System.out.printf("| %-5s | %-45s | %-20s | $%-9.2f | %-10d |\n",
                    product.getProductID(),
                    product.getName(),
                    specificType,
                    product.getPrice(),
                    product.getQuantity());
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
    }

    public void viewProductsWithOptions(ShoppingCart cart, String category, List<Product> products) {
        System.out.println("\n----------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-45s | %-20s | %-10s | %-10s |\n", "ID", "Name", "Type", "Price", "Stock");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        for (Product product : products) {
            String specificType = "";
            if (product instanceof ComputerComponent) {
                specificType = ((ComputerComponent) product).getComponentType();
            } else if (product instanceof PeripheralAccessory) {
                specificType = ((PeripheralAccessory) product).getDeviceType();
            } else if (product instanceof NetworkingProduct) {
                specificType = ((NetworkingProduct) product).getNetworkType();
            }
            System.out.printf("| %-5s | %-45s | %-20s | $%-9.2f | %-10d |\n",
                    product.getProductID(),
                    product.getName(),
                    specificType,
                    product.getPrice(),
                    product.getQuantity());
        }
        System.out.println("----------------------------------------------------------------------------------------------------------");
    }

    public boolean searchProduct(String searchQuery, String category) {
        foundProducts.clear(); 

        String query = searchQuery.toLowerCase();

        for (Product product : products) {
            // Skip if category is specified and doesn't match
            if (category != null && !category.equals("All") && !product.getCategory().equals(category)) {
                continue;
            }

            if (product.getProductID().toLowerCase().contains(query)
                    || product.getName().toLowerCase().contains(query)) {
                foundProducts.add(product);

                System.out.println("Product found:");
                System.out.printf("ID: %s | Name: %s | Category: %s | Price: $%.2f | Stock: %d\n",
                        product.getProductID(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity());
            }
        }

        if (foundProducts.size() == 1) {
            selectedProduct = foundProducts.get(0);
        } else if (foundProducts.size() > 1) {
            System.out.println("\nMultiple products found.");
        } else {
            System.out.println(color.YELLOW + "No products found matching '" + searchQuery + "'" + color.RESET);
        }

        return !foundProducts.isEmpty();
    }
    
    public List<Product> getFoundProducts() {
        return foundProducts;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
    }


    public void addToCart(ShoppingCart cart, String productId, int quantity) {
        boolean found = false;
        for (Product product : products) {
            if (product.getProductID().equalsIgnoreCase(productId)) {
                found = true;

                // Calculate total requested quantity (existing in cart + new quantity)
                CartItem existingItem = cart.getCartItemByProductId(productId);
                int totalRequestedQuantity = quantity;

                if (existingItem != null) {
                    totalRequestedQuantity += existingItem.getQuantity();
                }

                // Check against available stock
                if (product.getQuantity() >= totalRequestedQuantity) {
                    // Only add if sufficient stock
                    cart.addProduct(product, quantity);
                    System.out.println(color.GREEN + "Product added to cart successfully." + color.RESET);
                } else {
                    System.out.println(color.RED + "Not enough stock available. " +
                            "You already have " + (existingItem != null ? existingItem.getQuantity() : 0) +
                            " of this item in your cart. Available stock: " + product.getQuantity() + color.RESET);
                }
                break;
            }
        }
        if (!found) {
            System.out.println(color.RED + "Product not found." + color.RESET);
        }
    }



    public static void loadProductsFromFile() {
        products.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 6);  

                if (parts.length >= 6) {
                    String productId = parts[0];         // Product ID (e.g., P001)
                    String name = parts[1];              // Product name (e.g., Intel Core i7)
                    String category = parts[2];          // Product category (CC, PA, NC)

                    try {
                        double price = Double.parseDouble(parts[3]);  // Product price
                        int quantity = Integer.parseInt(parts[4]);    // Product quantity
                        String specificType = parts[5];      // Type of product

                        Product product = ProductFactory.createProduct(
                                category, productId, name, price, quantity, specificType
                        );

                        // Add product to the list
                        products.add(product);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing product data: " + line);
                        System.out.println("Specific error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading products file: " + e.getMessage());
        }
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void saveNewProductToFile(Product product) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE_NAME, true))) {
            StringBuilder line = new StringBuilder();
            line.append(product.getProductID()).append(",")
                    .append(product.getName()).append(",")
                    .append(product.getCategory()).append(",")
                    .append(product.getPrice()).append(",")
                    .append(product.getQuantity()).append(",");

            String type = "";
            if (product instanceof ComputerComponent) {
                type = ((ComputerComponent) product).getComponentType();
            } else if (product instanceof PeripheralAccessory) {
                type = ((PeripheralAccessory) product).getDeviceType();
            } else if (product instanceof NetworkingProduct) {
                type = ((NetworkingProduct) product).getNetworkType();
            }

            line.append(type);
            writer.write(line.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println(color.RED + "Error saving product: " + e.getMessage() + color.RESET);
        }
    }

    public boolean updateProduct(String productId, String name, double price, int quantity, String category, String specificTypeStr) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getProductID().equalsIgnoreCase(productId)) {
                if (!name.equals("0")) product.setName(name);
                if (price != 0) product.setPrice(price);
                if (quantity != 0) product.setQuantity(quantity);

                if (!"0".equals(specificTypeStr)) {
                   if (product instanceof ComputerComponent) {
                        ((ComputerComponent) product).setComponentType(specificTypeStr);
                    } else if (product instanceof PeripheralAccessory) {
                       ((PeripheralAccessory) product).setDeviceType(specificTypeStr);
                    } else if (product instanceof NetworkingProduct) {
                        ((NetworkingProduct) product).setNetworkType(specificTypeStr);
                    }
                }
                saveAllProductsToFile();
                return true;
           }
        }
       return false;
    }        

    public boolean removeProduct(String productId) {
        boolean removed = products.removeIf(product -> product.getProductID().equalsIgnoreCase(productId));
        if (removed) {
            saveAllProductsToFile();
        }
        return removed;
    }

    public void saveAllProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE_NAME, false))) {
            for (Product product : products) {
                StringBuilder line = new StringBuilder();
                line.append(product.getProductID()).append(",")
                        .append(product.getName()).append(",")
                        .append(product.getCategory()).append(",")
                        .append(product.getPrice()).append(",")
                        .append(product.getQuantity()).append(",");

                String type = "";
                if (product instanceof ComputerComponent) {
                    type = ((ComputerComponent) product).getComponentType();
                } else if (product instanceof PeripheralAccessory) {
                    type = ((PeripheralAccessory) product).getDeviceType();
                } else if (product instanceof NetworkingProduct) {
                    type = ((NetworkingProduct) product).getNetworkType();
                }

                line.append(type);
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(color.RED + "Error saving products: " + e.getMessage() + color.RESET);
        }
    }
}
