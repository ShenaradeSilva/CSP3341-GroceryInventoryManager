package com.csp3341.grocery;

import java.util.Optional;
import java.util.Scanner;

/**
 * Console interface for the Grocery Inventory Manager.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryManager manager = new InventoryManager();

    private enum ReportType {
        LOW_STOCK,
        EXPIRED_PRODUCTS,
        CATEGORY,
        COMPLETE_INVENTORY
    }

    public static void main(String[] args) {
        displayWelcomeMessage();

        while (true) {
            displayMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> manageProducts();
                case 2 -> manageSuppliers();
                case 3 -> inventoryReports();
                case 4 -> exitApplication();
                default -> System.out.println("Error! Invalid choice. Please try again.");
            }
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println("=".repeat(60));
        System.out.println("WELCOME TO GROCERY INVENTORY MANAGER");
        System.out.println("=".repeat(60));
    }

    private static void displayMainMenu() {
        System.out.println("\nMAIN MENU:");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Suppliers");
        System.out.println("3. Inventory Reports");
        System.out.println("4. Exit");
    }

    private static void exitApplication() {
        System.out.println("\nExiting... Thank you for using Grocery Inventory Manager!");
        scanner.close();
        System.exit(0);
    }

    // Product Management

    private static void manageProducts() {
        while (true) {
            System.out.println("\nMANAGE PRODUCTS:");
            System.out.println("1. View All Products");
            System.out.println("2. View Expired Products");
            System.out.println("3. View Low Stock Products");
            System.out.println("4. View Products by Category");
            System.out.println("5. Add Product");
            System.out.println("6. Update Product Stock");
            System.out.println("7. Remove Product");
            System.out.println("8. Return to Main Menu");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> manager.listAllProducts();
                case 2 -> manager.listExpiredProducts();
                case 3 -> manager.listLowStockProducts();
                case 4 -> filterProductsByCategory();
                case 5 -> addProduct();
                case 6 -> updateProductStock();
                case 7 -> removeProduct();
                case 8 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Error! Invalid option.");
            }
        }
    }

    private static void addProduct() {
        System.out.println("\nSELECT PRODUCT TYPE:");
        System.out.println("1. Perishable Product");
        System.out.println("2. Non-Perishable Product");
        System.out.println("3. Return to Previous Menu");

        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1 -> addPerishableProduct();
            case 2 -> addNonPerishableProduct();
            case 3 -> System.out.println("Returning to Product Menu...");
            default -> System.out.println("Error! Invalid option.");
        }
    }

    private static void addPerishableProduct() {
        System.out.println("\nADD PERISHABLE PRODUCT:");
        int nextId = manager.getNextProductId();
        System.out.println("Product will be assigned ID: " + nextId);

        String name = readString("Product Name: ");
        double price = readDouble("Price (LKR): ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();

        Optional<Supplier> supplierOpt = selectSupplier();
        if (supplierOpt.isEmpty()) {
            System.out.println("Product addition cancelled.");
            return;
        }

        String expiryDate = readString("Expiry Date (YYYY-MM-DD): ");

        try {
            Product product = new Perishable(nextId, name, price, quantity,
                    category, supplierOpt.get(), expiryDate);
            manager.addProduct(product);
            System.out.println("Perishable product added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addNonPerishableProduct() {
        System.out.println("\nADD NON-PERISHABLE PRODUCT:");
        int nextId = manager.getNextProductId();
        System.out.println("Product will be assigned ID: " + nextId);

        String name = readString("Product Name: ");
        double price = readDouble("Price (LKR): ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();

        Optional<Supplier> supplierOpt = selectSupplier();
        if (supplierOpt.isEmpty()) {
            System.out.println("Product addition cancelled.");
            return;
        }

        String shelfLife = readString("Shelf Life (e.g., 2 years): ");

        try {
            Product product = new NonPerishable(nextId, name, price, quantity,
                    category, supplierOpt.get(), shelfLife);
            manager.addProduct(product);
            System.out.println("Non-perishable product added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateProductStock() {
        System.out.println("\nUPDATE PRODUCT STOCK:");
        manager.listAllProducts();

        if (!manager.hasProducts()) {
            System.out.println("No products available to update!");
            return;
        }

        int productId = readInt("Enter Product ID to update: ");
        Optional<Product> productOpt = manager.findProduct(productId);

        if (productOpt.isEmpty()) {
            System.out.printf("Product with ID %d not found!%n", productId);
            return;
        }

        Product product = productOpt.get();
        System.out.printf("Current stock for '%s': %d%n", product.getName(), product.getQuantity());

        int newQuantity = readInt("Enter new quantity: ");
        manager.updateStock(productId, newQuantity);
        System.out.println("Stock updated successfully!");
    }

    private static void removeProduct() {
        System.out.println("\nREMOVE PRODUCT:");
        manager.listAllProducts();

        if (!manager.hasProducts()) {
            System.out.println("No products available to remove!");
            return;
        }

        int productId = readInt("Enter the Product ID to remove: ");
        manager.removeProduct(productId);
    }

    private static void filterProductsByCategory() {
        Category category = readCategory();
        manager.listProductsByCategory(category);
    }

    // Supplier Management

    private static void manageSuppliers() {
        while (true) {
            System.out.println("\nMANAGE SUPPLIERS:");
            System.out.println("1. Add Supplier");
            System.out.println("2. List Suppliers");
            System.out.println("3. Remove Supplier");
            System.out.println("4. Return to Main Menu");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addSupplier();
                case 2 -> manager.listAllSuppliers();
                case 3 -> removeSupplier();
                case 4 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Error! Invalid option.");
            }
        }
    }

    private static void addSupplier() {
        System.out.println("\nADD SUPPLIER:");
        System.out.println("Supplier will be assigned ID: " + manager.getNextSupplierId());

        String name = readString("Supplier Name: ");
        String contact = readString("Contact Information: ");

        manager.addSupplier(name, contact);
    }

    private static Optional<Supplier> selectSupplier() {
        System.out.println("\nAVAILABLE SUPPLIERS:");
        manager.listAllSuppliers();

        if (!manager.hasSuppliers()) {
            System.out.println("\nNo suppliers available!");
            System.out.println("You need to add a supplier first.");

            System.out.println("\n1. Add new supplier");
            System.out.println("2. Cancel product addition");

            int initialChoice = readInt("Enter choice: ");
            if (initialChoice == 1) {
                addSupplier();
                return selectSupplier();
            } else {
                return Optional.empty();
            }
        }

        while (true) {
            int supplierId = readInt("\nEnter Supplier ID for this product (0 to cancel): ");

            if (supplierId == 0) {
                System.out.println("Product addition cancelled.");
                return Optional.empty();
            }

            Optional<Supplier> supplierOpt = manager.findSupplier(supplierId);

            if (supplierOpt.isPresent()) {
                return supplierOpt;
            } else {
                System.out.printf("Supplier with ID %d not found!%n", supplierId);

                System.out.println("\n1. Try another supplier ID");
                System.out.println("2. Add a new supplier");
                System.out.println("3. Cancel product addition");

                int choice = readInt("Enter choice: ");
                switch (choice) {
                    case 1 -> {
                        continue;
                    }
                    case 2 -> {
                        addSupplier();
                        return selectSupplier();
                    }
                    case 3 -> {
                        System.out.println("Product addition cancelled.");
                        return Optional.empty();
                    }
                    default -> {
                        System.out.println("Invalid choice. Cancelling product addition.");
                        return Optional.empty();
                    }
                }
            }
        }
    }

    private static void removeSupplier() {
        System.out.println("\nREMOVE SUPPLIER:");
        manager.listAllSuppliers();

        if (!manager.hasSuppliers()) {
            System.out.println("No suppliers available to remove!");
            return;
        }

        int supplierId = readInt("Enter the Supplier ID to remove: ");
        manager.removeSupplier(supplierId);
    }

    // Inventory Reports

    private static void inventoryReports() {
        while (true) {
            System.out.println("\nINVENTORY REPORTS:");
            System.out.println("1. Low Stock Report");
            System.out.println("2. Expired Products Report");
            System.out.println("3. Category Report");
            System.out.println("4. Complete Inventory Report");
            System.out.println("5. Return to Main Menu");

            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> generateLowStockReport();
                case 2 -> generateExpiredProductsReport();
                case 3 -> generateCategoryReport();
                case 4 -> generateCompleteInventoryReport();
                case 5 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Error! Invalid option.");
            }
        }
    }

    private static void generateLowStockReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("LOW STOCK REPORT");
        System.out.println("=".repeat(60));
        manager.listLowStockProducts();
        System.out.println("=".repeat(60));

        askToSaveReport("low_stock_report.txt", ReportType.LOW_STOCK, null);
    }

    private static void generateExpiredProductsReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EXPIRED PRODUCTS REPORT");
        System.out.println("=".repeat(60));
        manager.listExpiredProducts();
        System.out.println("=".repeat(60));

        askToSaveReport("expired_products_report.txt", ReportType.EXPIRED_PRODUCTS, null);
    }

    private static void generateCategoryReport() {
        Category category = readCategory();
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("CATEGORY REPORT: %s%n", category);
        System.out.println("=".repeat(60));
        manager.listProductsByCategory(category);
        System.out.println("=".repeat(60));

        askToSaveReport("category_report_" + category.name().toLowerCase() + ".txt",
                ReportType.CATEGORY, category);
    }

    private static void generateCompleteInventoryReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("COMPLETE INVENTORY REPORT");
        System.out.println("=".repeat(60));

        System.out.println("\nALL PRODUCTS:");
        System.out.println("-".repeat(60));
        manager.listAllProducts();

        System.out.println("\nEXPIRED PRODUCTS:");
        System.out.println("-".repeat(60));
        manager.listExpiredProducts();

        System.out.println("\nLOW STOCK PRODUCTS:");
        System.out.println("-".repeat(60));
        manager.listLowStockProducts();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("REPORT COMPLETE");
        System.out.println("=".repeat(60));

        askToSaveReport("complete_inventory_report.txt", ReportType.COMPLETE_INVENTORY, null);
    }

    private static void askToSaveReport(String defaultFilename, ReportType reportType, Category category) {
        System.out.println("\nWould you like to save this report to a file?");
        boolean saveToFile = askYesNo("Save report to file? (yes/no): ");

        if (!saveToFile) {
            System.out.println("Report not saved. Displayed on console only.");
            return;
        }

        System.out.print("Enter filename (default: " + defaultFilename + "): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = defaultFilename;
        }

        switch (reportType) {
            case LOW_STOCK -> manager.saveLowStockReportToFile(filename);
            case EXPIRED_PRODUCTS -> manager.saveExpiredProductsReportToFile(filename);
            case CATEGORY -> manager.saveCategoryReportToFile(filename, category);
            case COMPLETE_INVENTORY -> {
                boolean includeSuppliers = askYesNo("Include supplier details in the report? (yes/no): ");
                manager.saveCompleteReportToFile(filename, includeSuppliers);
            }
        }

        System.out.println("Report saved to: " + filename);
    }

    // Helper Methods

    private static boolean askYesNo(String question) {
        while (true) {
            System.out.print(question);
            String response = scanner.nextLine().trim().toLowerCase();

            switch (response) {
                case "yes", "y" -> {
                    return true;
                }
                case "no", "n" -> {
                    return false;
                }
                default -> System.out.println("Please answer 'yes' or 'no'.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static Category readCategory() {
        Category[] categories = Category.values();

        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("%d. %s%n", i + 1, categories[i]);
        }

        while (true) {
            int choice = readInt(String.format("Select Category (1-%d): ", categories.length));
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];
            }
            System.out.printf("Invalid choice. Please enter a number between 1 and %d.%n", categories.length);
        }
    }
}