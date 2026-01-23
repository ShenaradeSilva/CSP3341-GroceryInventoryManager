package com.csp3341.grocery;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        while (true) {
            showMainMenu();
            int choice = readInt("Enter your Choice: ");

            switch (choice) {
                case 1 -> manageProducts();
                case 2 -> manageSuppliers();
                case 3 -> inventoryReports();
                case 4 -> {
                    System.out.println("Exiting...." +
                            "Thank you for using Grocery Inventory Manager!");
                    return;
                }
                default -> System.out.println("Error! Invalid Choice!");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("""
                    WELCOME TO GROCERY INVENTORY MANAGER!
                    
                    MAIN MENU:
                    1. Manage Product
                    2. Manage Suppliers
                    3. Inventory Reports
                    4. Exit
                    """);
    }

    // Manage Products
    private static void manageProducts() {
        boolean running = true;

        while (running) {
            System.out.println("""
            \nMANAGE PRODUCTS:
            1. View All Products
            2. View Expired Products
            3. View Low Stock Products
            4. View Products by Category
            5. Add Products
            6. Update Product Stock
            7. Remove Product
            8. Return to Main Menu
            """);

            int choice = readInt("Enter choice:");

            switch (choice) {
                case 1 -> manager.listAllProducts();
                case 2 -> manager.listExpiredProducts();
                case 3 -> manager.listLowStockProducts();
                case 4 -> filterByCategory();
                case 5 -> addProduct();
                case 6 -> updateProductStock();
                case 7 -> removeProduct();
                case 8 -> {
                    goBack("Main Menu");
                    running = false;
                }
                default -> System.out.println("Error! Invalid Option!");
            }
        }
    }

    // Add Product
    private static void addProduct() {
        boolean running = true;

        while (running) {
            System.out.println("""
            \nSELECT PRODUCT TYPE:
            1. Perishable Products
            2. Non-Perishable Products
            3. Return to Previous Menu
            """);

            int choice = readInt("Enter choice:");

            switch (choice) {
                case 1 -> addPerishableProduct();
                case 2 -> addNonPerishableProduct();
                case 3 -> {
                    goBack("Product Menu");
                    running = false;
                }
                default -> System.out.println("Error! Invalid Option!");
            }
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

        Supplier supplier = selectSupplier();
        if (supplier == null) {
            return;
        }

        String expiryDate = readString("Expiry Date (YYYY-MM-DD): ");

        manager.addProduct(
                new Perishable(id, name, price, quantity, category, supplier, expiryDate)
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

        Supplier supplier = selectSupplier();
        if (supplier == null) {
            return;
        }

        String shelfLife = readString("Shelf Life: ");

        manager.addProduct(
                new NonPerishable(id, name, price, quantity, category, supplier, shelfLife)
        );

        System.out.println("Non-Perishable Product Added Successfully!");
    }

    // Update Product Stock
    private static void updateProductStock() {
        int id = readInt("Enter Product ID: ");
        int quantity = readInt("Enter New Quantity: ");
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

    // Manage Suppliers
    private static void manageSuppliers() {
        boolean running = true;

        while (running) {
            System.out.println("""
                        \n\nMANAGE SUPPLIERS:
                        1. Add Suppliers
                        2. List Suppliers
                        3. Return to Previous Menu
                    """);

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addSupplier();
                case 2 -> manager.listAllSuppliers();
                case 3 -> {
                    goBack("Main Menu");
                    running = false;
                }
                default -> System.out.println("Error! Invalid Type! Please Enter a Valid Option");
            }
        }
    }

    // Add Suppliers
    private static void addSupplier() {
        int id = readInt("Supplier ID: ");
        String name = readString("Supplier Name: ");
        String contact = readString("Contact Info: ");

        manager.addSupplier(new Supplier(id, name, contact));
        System.out.println("Supplier Added Successfully!");
    }

    // Select Suppliers
    private static Supplier selectSupplier() {
        manager.listAllSuppliers();
        int supplierId = readInt("Enter Supplier ID for this product: ");
        Supplier supplier = manager.findSupplier(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found! Please add supplier first.");
        }
        return supplier;
    }

    // Inventory Reports
    private static void inventoryReports() {
        boolean running = true;

        while (running) {
            System.out.println("""
                        \n\nINVENTORY REPORTS:
                        1. Generate Low Stock Reports
                        2. Generate Expired Products Reports
                        3. Generate Category Reports
                        4. Generate Complete Inventory Reports
                        5. Return to Previous Menu
                    """);

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> manager.listLowStockProducts();
                case 2 -> manager.listExpiredProducts();
                case 3 -> filterByCategory();
                case 4 -> manager.listAllProducts();
                case 5 -> {
                    goBack("Main Menu");
                    running = false;
                }
                default -> System.out.println("Error! Invalid Type! Please Enter a Valid Option");
            }
        }
    }

    // Input Helpers
    private static int readInt(String msg) {
        System.out.print(msg + " ");
        return scanner.nextInt();
    }

    private static double readDouble(String msg) {
        System.out.print(msg + " ");
        return scanner.nextDouble();
    }

    private static String readString(String msg) {
        scanner.nextLine();
        System.out.print(msg + " ");
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

    // Exit Helper
    private static void goBack(String menuName) {
        System.out.println("Returning to " + menuName + "...\n\n");
    }
}
