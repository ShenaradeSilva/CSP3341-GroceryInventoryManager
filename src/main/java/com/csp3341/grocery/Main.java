/**
 * This file defines the Main class which serves as the console user interface
 * for the Grocery Inventory Manager system. It provides a text-based menu system
 * that allows users to interact with all inventory management features through
 * a command-line interface. This class orchestrates the interaction between
 * the user and the InventoryManager business logic layer.
 */

package com.csp3341.grocery;

import java.util.Optional;
import java.util.Scanner;

/**
 * Console interface for the Grocery Inventory Manager.
 * Implements the main application loop and user interaction logic.
 */
public class Main {
    // Shared Scanner for reading user input from console
    private static final Scanner scanner = new Scanner(System.in);

    // Core inventory manager instance - handles all business logic
    private static final InventoryManager manager = new InventoryManager();

    /**
     * Enumeration of report types for type-safe report generation.
     * Used to parameterize the save report functionality.
     */
    private enum ReportType {
        LOW_STOCK,
        EXPIRED_PRODUCTS,
        CATEGORY,
        COMPLETE_INVENTORY
    }

    /**
     * Main entry point of the application.
     * Sets up the application and starts the main menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        displayWelcomeMessage();

        // Main application loop - runs until user chooses to exit
        while (true) {
            displayMainMenu();
            int choice = readInt("Enter your choice: ");

            // Process user's main menu choice
            switch (choice) {
                case 1 -> manageProducts();
                case 2 -> manageSuppliers();
                case 3 -> inventoryReports();
                case 4 -> exitApplication();
                default -> System.out.println("Error! Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the welcome banner when application starts.
     */
    private static void displayWelcomeMessage() {
        System.out.println("=".repeat(60));     // Decorative separator
        System.out.println("WELCOME TO GROCERY INVENTORY MANAGER");
        System.out.println("=".repeat(60));
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void displayMainMenu() {
        System.out.println("\nMAIN MENU:");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Suppliers");
        System.out.println("3. Inventory Reports");
        System.out.println("4. Exit");
    }

    /**
     * Gracefully exits the application.
     * Closes resources and displays exit message.
     */
    private static void exitApplication() {
        System.out.println("\nExiting... Thank you for using Grocery Inventory Manager!");
        scanner.close();            // Close scanner to prevent resource leak
        System.exit(0);       // Terminate JVM with status code 0 (success)
    }

    // Product Management

    /**
     * Handles the product management submenu.
     * Allows user to perform various product-related operations.
     */
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

            // Process product menu choice
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
                    return;     // Exit this menu, return to main menu
                }
                default -> System.out.println("Error! Invalid option.");
            }
        }
    }

    /**
     * Displays product type selection menu and routes to appropriate add method.
     */
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

    /**
     * Guides user through adding a perishable product.
     * Perishable products have an expiry date.
     */
    private static void addPerishableProduct() {
        System.out.println("\nADD PERISHABLE PRODUCT:");
        int nextId = manager.getNextProductId();
        System.out.println("Product will be assigned ID: " + nextId);

        // Collect product information from user
        String name = readString("Product Name: ");
        double price = readDouble("Price (LKR): ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();

        // Supplier selection (may return empty if user cancels)
        Optional<Supplier> supplierOpt = selectSupplier();
        if (supplierOpt.isEmpty()) {
            System.out.println("Product addition cancelled.");
            return;         // User cancelled supplier selection
        }

        String expiryDate = readString("Expiry Date (YYYY-MM-DD): ");

        try {
            // Create Perishable product with collected data
            Product product = new Perishable(nextId, name, price, quantity,
                    category, supplierOpt.get(), expiryDate);
            manager.addProduct(product);        // Add to inventory
            System.out.println("Perishable product added successfully!");
        } catch (IllegalArgumentException e) {
            // Handle validation errors from product creation
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Guides user through adding a non-perishable product.
     * Non-perishable products have shelf life instead of expiry date.
     */
    private static void addNonPerishableProduct() {
        System.out.println("\nADD NON-PERISHABLE PRODUCT:");
        int nextId = manager.getNextProductId();
        System.out.println("Product will be assigned ID: " + nextId);

        // Collect product information from user
        String name = readString("Product Name: ");
        double price = readDouble("Price (LKR): ");
        int quantity = readInt("Quantity: ");
        Category category = readCategory();

        // Supplier selection
        Optional<Supplier> supplierOpt = selectSupplier();
        if (supplierOpt.isEmpty()) {
            System.out.println("Product addition cancelled.");
            return;
        }

        // Shelf life is descriptive string (e.g., "2 years", "Indefinite")
        String shelfLife = readString("Shelf Life (e.g., 2 years): ");

        try {
            // Create NonPerishable product with collected data
            Product product = new NonPerishable(nextId, name, price, quantity,
                    category, supplierOpt.get(), shelfLife);
            manager.addProduct(product);
            System.out.println("Non-perishable product added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Updates the stock quantity of an existing product.
     */
    private static void updateProductStock() {
        System.out.println("\nUPDATE PRODUCT STOCK:");
        manager.listAllProducts();          // Show products so user can choose

        if (!manager.hasProducts()) {
            System.out.println("No products available to update!");
            return;             // Early exit if no products exist
        }

        int productId = readInt("Enter Product ID to update: ");
        Optional<Product> productOpt = manager.findProduct(productId);

        if (productOpt.isEmpty()) {
            System.out.printf("Product with ID %d not found!%n", productId);
            return;         // Product doesn't exist
        }

        Product product = productOpt.get();
        System.out.printf("Current stock for '%s': %d%n", product.getName(), product.getQuantity());

        int newQuantity = readInt("Enter new quantity: ");
        manager.updateStock(productId, newQuantity);        // Update via InventoryManager
        System.out.println("Stock updated successfully!");
    }

    /**
     * Removes a product from the inventory.
     */
    private static void removeProduct() {
        System.out.println("\nREMOVE PRODUCT:");
        manager.listAllProducts();          // Show products for user selection

        if (!manager.hasProducts()) {
            System.out.println("No products available to remove!");
            return;
        }

        int productId = readInt("Enter the Product ID to remove: ");
        manager.removeProduct(productId);       // Delegate removal to InventoryManager
    }

    /**
     * Filters and displays products by selected category.
     */
    private static void filterProductsByCategory() {
        // Get category from user
        Category category = readCategory();

        // Display filtered products
        manager.listProductsByCategory(category);
    }

    // Supplier Management

    /**
     * Handles the supplier management submenu.
     */
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

    /**
     * Guides user through adding a new supplier.
     */
    private static void addSupplier() {
        System.out.println("\nADD SUPPLIER:");
        System.out.println("Supplier will be assigned ID: " + manager.getNextSupplierId());

        String name = readString("Supplier Name: ");
        String contact = readString("Contact Information: ");

        manager.addSupplier(name, contact);     // Delegate to InventoryManager
    }

    /**
     * Guides user through selecting a supplier for a product.
     * Handles cases where no suppliers exist.
     *
     * @return Optional containing selected supplier, or empty if user cancels
     */
    private static Optional<Supplier> selectSupplier() {
        System.out.println("\nAVAILABLE SUPPLIERS:");
        manager.listAllSuppliers();         // Show available suppliers

        if (!manager.hasSuppliers()) {
            System.out.println("\nNo suppliers available!");
            System.out.println("You need to add a supplier first.");

            // Provide options when no suppliers exist
            System.out.println("\n1. Add new supplier");
            System.out.println("2. Cancel product addition");

            int initialChoice = readInt("Enter choice: ");
            if (initialChoice == 1) {
                addSupplier();              // Add supplier first
                return selectSupplier();    // Recursive call to select again
            } else {
                return Optional.empty();    // User cancelled
            }
        }

        // Loop until valid supplier selected or user cancels
        while (true) {
            int supplierId = readInt("\nEnter Supplier ID for this product (0 to cancel): ");

            if (supplierId == 0) {
                System.out.println("Product addition cancelled.");
                return Optional.empty();        // User chose to cancel
            }

            Optional<Supplier> supplierOpt = manager.findSupplier(supplierId);

            if (supplierOpt.isPresent()) {
                return supplierOpt;             // Valid supplier found
            } else {
                System.out.printf("Supplier with ID %d not found!%n", supplierId);

                // Provide recovery options when supplier not found
                System.out.println("\n1. Try another supplier ID");
                System.out.println("2. Add a new supplier");
                System.out.println("3. Cancel product addition");

                int choice = readInt("Enter choice: ");
                switch (choice) {
                    case 1 -> {
                        continue;       // Try again with different ID
                    }
                    case 2 -> {
                        addSupplier();
                        return selectSupplier();        // Select from updated list
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

    /**
     * Removes a supplier from the system.
     * Note: Supplier can only be removed if no products reference it.
     */
    private static void removeSupplier() {
        System.out.println("\nREMOVE SUPPLIER:");
        manager.listAllSuppliers();         // Show suppliers for selection

        if (!manager.hasSuppliers()) {
            System.out.println("No suppliers available to remove!");
            return;
        }

        int supplierId = readInt("Enter the Supplier ID to remove: ");
        manager.removeSupplier(supplierId);     // Delegate to InventoryManager
    }

    // Inventory Reports

    /**
     * Handles the inventory reporting submenu.
     */
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

    /**
     * Generates and displays low stock report.
     */
    private static void generateLowStockReport() {
        System.out.println("\n" + "=".repeat(60));      // Report header
        System.out.println("LOW STOCK REPORT");
        System.out.println("=".repeat(60));
        manager.listLowStockProducts();                       // Display report content
        System.out.println("=".repeat(60));             // Report footer

        // Ask if user wants to save to file
        askToSaveReport("low_stock_report.txt", ReportType.LOW_STOCK, null);
    }

    /**
     * Generates and displays expired products report.
     */
    private static void generateExpiredProductsReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EXPIRED PRODUCTS REPORT");
        System.out.println("=".repeat(60));
        manager.listExpiredProducts();
        System.out.println("=".repeat(60));

        askToSaveReport("expired_products_report.txt", ReportType.EXPIRED_PRODUCTS, null);
    }

    /**
     * Generates and displays category-specific report.
     */
    private static void generateCategoryReport() {
        Category category = readCategory();
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("CATEGORY REPORT: %s%n", category);
        System.out.println("=".repeat(60));
        manager.listProductsByCategory(category);
        System.out.println("=".repeat(60));

        // Generate filename based on category (e.g., "category_report_dairy.txt")
        askToSaveReport("category_report_" + category.name().toLowerCase() + ".txt",
                ReportType.CATEGORY, category);
    }

    /**
     * Generates and displays complete inventory report.
     */
    private static void generateCompleteInventoryReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("COMPLETE INVENTORY REPORT");
        System.out.println("=".repeat(60));

        // Multiple report sections
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

    /**
     * Asks user if they want to save a report to file, and handles the save process.
     *
     * @param defaultFilename suggested filename for the report
     * @param reportType type of report being saved
     * @param category category for category reports (null for other report types)
     */
    private static void askToSaveReport(String defaultFilename, ReportType reportType, Category category) {
        System.out.println("\nWould you like to save this report to a file?");
        boolean saveToFile = askYesNo("Save report to file? (yes/no): ");

        if (!saveToFile) {
            System.out.println("Report not saved. Displayed on console only.");
            return;         // User doesn't want to save
        }

        // Get filename from user (use default if blank)
        System.out.print("Enter filename (default: " + defaultFilename + "): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = defaultFilename;
        }

        // Call appropriate save method based on report type
        switch (reportType) {
            case LOW_STOCK -> manager.saveLowStockReportToFile(filename);
            case EXPIRED_PRODUCTS -> manager.saveExpiredProductsReportToFile(filename);
            case CATEGORY -> manager.saveCategoryReportToFile(filename, category);
            case COMPLETE_INVENTORY -> {
                // Ask additional question for complete report
                boolean includeSuppliers = askYesNo("Include supplier details in the report? (yes/no): ");
                manager.saveCompleteReportToFile(filename, includeSuppliers);
            }
        }

        System.out.println("Report saved to: " + filename);
    }

    // Helper Methods

    /**
     * Prompts user for yes/no response and validates input.
     *
     * @param question the yes/no question to ask
     * @return true for yes, false for no
     */
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

    /**
     * Reads and validates an integer from user input.
     *
     * @param prompt message to display to user
     * @return valid integer entered by user
     */
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

    /**
     * Reads and validates a double from user input.
     *
     * @param prompt message to display to user
     * @return valid double entered by user
     */
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

    /**
     * Reads a string from user input and trims whitespace.
     *
     * @param prompt message to display to user
     * @return trimmed string entered by user
     */
    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Displays available categories and reads user's category selection.
     *
     * @return selected Category enum value
     */
    private static Category readCategory() {
        Category[] categories = Category.values();

        System.out.println("\nAvailable Categories:");
        // Display categories with numbered options (1-based)
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("%d. %s%n", i + 1, categories[i]);
        }

        while (true) {
            int choice = readInt(String.format("Select Category (1-%d): ", categories.length));
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];      // Convert to 0-based index
            }
            System.out.printf("Invalid choice. Please enter a number between 1 and %d.%n", categories.length);
        }
    }
}