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
        int nextID = manager.getNextProductId();
        System.out.println("Product will be assigned to ID: " + nextID);

        String name = readString("Product Name: ");
        double price = readDouble("Price (LKR): ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();

        Supplier supplier = selectSupplier();
        if (supplier == null) {
            System.out.println("Product Addition Cancelled");
            return;
        }

        String expiryDate = readString("Expiry Date (YYYY-MM-DD): ");

        manager.addProduct(
                new Perishable(nextID, name, price, quantity, category, supplier, expiryDate)
        );

        System.out.println("Perishable Product Added Successfully!");
    }

    // Add Non-Perishable Products
    private static void addNonPerishableProduct() {
        System.out.println("\nADD NON-PERISHABLE PRODUCT: ");
        int nextId = manager.getNextProductId();
        System.out.println("Product will be assigned to ID: " + nextId);

        String name = readString("Product Name: ");
        double price = readDouble("Price: LKR ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();

        Supplier supplier = selectSupplier();
        if (supplier == null) {
            System.out.println("Product Addition Cancelled");
            return;
        }

        String shelfLife = readString("Shelf Life: ");

        manager.addProduct(
                new NonPerishable(nextId, name, price, quantity, category, supplier, shelfLife)
        );

        System.out.println("Non-Perishable Product Added Successfully!");
    }

    // Update Product Stock
    private static void updateProductStock() {
        System.out.println("\nUPDATE PRODUCT STOCK: ");
        manager.listAllProducts();

        if (!manager.hasProducts()) {
            System.out.println("No Products Available to Update!");
            return;
        }

        int id = readInt("Enter Product ID to Update: ");
        Product product = manager.findProduct(id);

        if (product == null) {
            System.out.println("Product with ID " + id + " Not Found!");
        }
        else  {
            System.out.println("Current Stock for '" + product.getName() + "' : " + product.getQuantity());
            int quantity = readInt("Enter New Quantity: ");
            manager.updateStock(id, quantity);
            System.out.println("Stock Updated Successfully!");
        }
    }

    // Remove Product
    private static void removeProduct() {
        System.out.println("\nREMOVE PRODUCT: ");
        manager.listAllProducts();

        if (!manager.hasProducts()) {
            System.out.println("No Products Available to Remove!");
            return;
        }

        int id = readInt("Enter the Product ID to be removed: ");
        manager.removeProduct(id);
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
                        3. Remove Supplier
                        4. Return to Previous Menu
                    """);

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addSupplier();
                case 2 -> manager.listAllSuppliers();
                case 3 -> removeSupplier();
                case 4 -> {
                    goBack("Main Menu");
                    running = false;
                }
                default -> System.out.println("Error! Invalid Type! Please Enter a Valid Option");
            }
        }
    }

    // Add Suppliers
    private static void addSupplier() {
        System.out.println("\nADD SUPPLIER: ");
        System.out.println("Supplier will be assigned ID: " + manager.getNextSupplierId());

        String name = readString("Supplier Name: ");
        String contact = readString("Contact Info: ");

        manager.addSupplier(name, contact);
    }

    // Select Suppliers
    private static Supplier selectSupplier() {
        System.out.println("\nAVAILABLE SUPPLIERS: ");
        manager.listAllSuppliers();

        if (!manager.hasSuppliers()) {
            System.out.println("\nNo Suppliers Available!");
            System.out.println("\nYou need to Add a Supplier first.");

            int choice = readInt("\n1. Add new supplier" +
                    "\n2. Cancel product addition" +
                    "\nEnter choice: ");
            if (choice == 1) {
                addSupplier();
                return selectSupplier(); // Retry after adding
            }
            else {
                return null;
            }
        }

        int supplierId = readInt("\nEnter Supplier ID for this product: ");
        Supplier supplier = manager.findSupplier(supplierId);

        if (supplier == null) {
            System.out.println("Supplier not found!");
            System.out.println("Do you want to:");
            System.out.println("1. Try another supplier ID");
            System.out.println("2. Add a new supplier");
            System.out.println("3. Cancel product addition");

            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> {
                    return selectSupplier();
                }
                case 2 -> {
                    addSupplier();
                    return selectSupplier();
                }
                case 3 -> {
                    System.out.println("Product addition cancelled.");
                    return null;
                }
                default -> {
                    System.out.println("Invalid choice. Cancelling product addition.");
                    return null;
                }
            }
        }
        return supplier;
    }

    // Remove Suppliers
    private  static void removeSupplier() {
        System.out.println("\nREMOVE SUPPLIER: ");
        manager.listAllSuppliers();

        if (!manager.hasSuppliers()) {
            System.out.println("No Suppliers Available to Remove!");
            return;
        }

        int id = readInt("\nEnter the Supplier ID to be Removed: ");
        manager.removeSupplier(id);
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
        while (true) {
            try {
                System.out.print(msg + " ");
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg + " ");
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static String readString(String msg) {
        System.out.print(msg + " ");
        return scanner.nextLine();
    }

    private static Category readCategory() {
        Category[] categories = Category.values();
        System.out.println("Available Categories: ");
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        while (true) {
            int choice = readInt("Select Category (1-" + categories.length + "): ");
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];
            } else {
                System.out.println("Invalid choice. Please select a number between 1 and " + categories.length + ".");
            }
        }
    }

    // Exit Helper
    private static void goBack(String menuName) {
        System.out.println("Returning to " + menuName + "...\n\n");
    }
}
