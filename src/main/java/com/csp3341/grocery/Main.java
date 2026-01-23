package com.csp3341.grocery;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> updateProductStock();
                case 3 -> removeProduct();
                case 4 -> manager.listExpiredProducts();
                case 5 -> manager.listLowStockProducts();
                case 6 -> filterByCategory();
                case 7 -> manager.generateInventoryReport();
                case 8 -> {
                    System.out.println("Exiting...." +
                            "Thank you for using Grocery Inventory Manager!");
                    return;
                }
                default -> System.out.println("Error! Invalid Choice!");
            }
        }
    }

    private static void showMenu() {
        System.out.println("""
                    WELCOME TO GROCERY INVENTORY MANAGER!
                    
                    MAIN MENU:
                    1. Add Product
                    2. Update Product Stock
                    3. Remove Product
                    4. View Expired Products
                    5. View Low Stock Products
                    6. Filter Products by Category
                    7. Generate Inventory Report
                    8. Exit
                    """);
    }

    // Add Product
    private static void addProduct() {
        System.out.println("""
                    SELECT PRODUCT TYPE:
                    1. Perishable Products
                    2. Non-Perishable Products
                """);

        int type = scanner.nextInt();
        scanner.nextLine();

        switch (type) {
            case 1 -> addPerishableProduct();
            case 2 -> addNonPerishableProduct();
            default -> System.out.println("Error! Invalid Type! Please Enter (1 or 2)");
        }
    }

    // Add Perishable Products
    private static void addPerishableProduct() {
        System.out.println("\nADD PERISHABLE PRODUCT: ");

        int id = readInt("Product ID: ");
        String name = readString("Product Name: ");
        double price = readDouble("Price (LKR): ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();
        String expiryDate = readString("Expiry Date (YYYY-MM-DD): ");

        manager.addProduct(
                new Perishable(id, name, price, quantity, category, expiryDate)
        );

        System.out.println("Perishable Product Added Successfully!");
    }

    // Add Non-Perishable Products
    private static void addNonPerishableProduct() {
        System.out.println("\nADD NON-PERISHABLE PRODUCT: ");

        int id = readInt("Product ID: ");
        String name = readString("Product Name: ");
        double price = readDouble("Price: LKR ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();
        String shelfLife = readString("Shelf Life: ");

        manager.addProduct(
                new NonPerishable(id, name, price, quantity, category, shelfLife)
        );

        System.out.println("Non-Perishable Product Added Successfully!");
    }

    // Update Product Stock
    private static void updateProductStock() {
        int id = readInt("Product ID: ");
        int quantity = readInt("Quantity: ");
        manager.updateStock(id, quantity);
        System.out.println("Stock Updated Successfully!");
    }

    // Remove Product
    private static void removeProduct() {
        int id = readInt("Enter the Product ID to be removed: ");
        manager.removeProduct(id);
        System.out.println("Product Removed Successfully!");
    }

    // Filter Products by Category
    private static void filterByCategory() {
        Category category = readCategory();
        manager.listProductsByCategory(category);
    }

    // Input Helpers
    private static int readInt(String msg) {
        System.out.println(msg);
        return scanner.nextInt();
    }

    private static double readDouble(String msg) {
        System.out.println(msg);
        return scanner.nextDouble();
    }

    private static String readString(String msg) {
        scanner.nextLine();
        System.out.println(msg);
        return scanner.nextLine();
    }

    private static Category readCategory() {
        System.out.println("Available Categories: ");

        for (Category c: Category.values()) {
            System.out.println("- " + c);
        }

        System.out.println("Select Category: ");
        return Category.valueOf(scanner.nextLine().toUpperCase());
    }
}
