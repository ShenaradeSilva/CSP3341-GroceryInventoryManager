/**
 * This file defines the InventoryManager class for the grocery inventory management system.
 * It serves as the central controller that manages all products and suppliers, providing
 * comprehensive inventory operations, reporting capabilities, and file export functionality.
 * This is the main business logic layer of the application.
 */

package com.csp3341.grocery;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages inventory operations including products and suppliers.
 * Provides comprehensive reporting capabilities.
 */
public class InventoryManager {
    // Format for report timestamps: "2026-02-04 14:30:45"
    private static final DateTimeFormatter REPORT_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Length of separator lines in reports (60 characters)
    private static final int REPORT_SEPARATOR_LENGTH = 60;

    // Core data storage - use ArrayList for fast random access
    private final List<Product> products;           // All products in inventory
    private final List<Supplier> suppliers;         // All suppliers in system

    // ID counters - tracks next available ID for auto-increment
    private int nextProductId;
    private int nextSupplierId;

    /**
     * Constructor - initialises empty inventory with starting IDs
     */
    public InventoryManager() {
        this.products = new ArrayList<>();
        this.suppliers = new ArrayList<>();
        this.nextProductId = 1;             // Start IDs at 1 (positive)
        this.nextSupplierId = 1;
    }

    // Supplier Management

    /**
     * Adds a new supplier with auto-generated ID.
     *
     * @param supplierName the name of the supplier
     * @param contact the contact information
     */
    public void addSupplier(String supplierName, String contact) {
        // Create supplier with auto-incremented ID
        Supplier supplier = new Supplier(nextSupplierId, supplierName, contact);
        suppliers.add(supplier);
        System.out.printf("Supplier '%s' added with ID: %d%n", supplierName, supplier.getSupplierId());
        nextSupplierId++;         // Increment for next supplier
    }

    /**
     * Adds an existing supplier to the inventory.
     * Useful for loading pre-existing suppliers from database/file.
     * Updates nextSupplierId to prevent ID conflicts.
     *
     * @param supplier the supplier to add
     * @throws IllegalArgumentException if supplier is null
     */
    public void addSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        suppliers.add(supplier);

        // Ensure nextSupplierId is higher than any existing ID
        nextSupplierId = Math.max(nextSupplierId, supplier.getSupplierId() + 1);
    }

    /**
     * Finds a supplier by ID.
     * Uses Java Streams for functional-style filtering
     *
     * @param supplierId the ID of the supplier to find
     * @return Optional containing the supplier if found, empty otherwise
     */
    public Optional<Supplier> findSupplier(int supplierId) {
        // Match supplier ID and return first match or empty
        return suppliers.stream()
                .filter(s -> s.getSupplierId() == supplierId)
                .findFirst();
    }

    /**
     * Removes a supplier if no products are associated with it.
     * Business rule: Cannot delete supplier with existing products (referential integrity).
     *
     * @param supplierId the ID of the supplier to remove
     */
    public void removeSupplier(int supplierId) {
        // Find supplier first
        Optional<Supplier> supplierOpt = findSupplier(supplierId);

        if (supplierOpt.isEmpty()) {
            System.out.printf("Supplier with ID %d not found!%n", supplierId);
            return;             // Early exit - supplier doesn't exist
        }

        Supplier supplier = supplierOpt.get();

        // Check if supplier has any products
        if (hasProductsForSupplier(supplierId)) {
            System.out.printf("Cannot remove supplier '%s'! There are products associated with this supplier.%n",
                    supplier.getSupplierName());
            return;             // Prevent removal - products depend on this supplier
        }

        // Safe to remove - no dependent products
        suppliers.removeIf(s -> s.getSupplierId() == supplierId);
        System.out.printf("Supplier '%s' with ID: %d removed successfully!%n",
                supplier.getSupplierName(), supplierId);
    }

    /**
     * Checks if any products reference the given supplier.
     * Private helper method for referential integrity check.
     *
     * @param supplierId supplier ID to check
     * @return true if any products use this supplier, false otherwise
     */
    private boolean hasProductsForSupplier(int supplierId) {
        return products.stream()
                .anyMatch(p -> p.getSupplier().getSupplierId() == supplierId);
    }

    // Product Management

    /**
     * Adds a product to the inventory.
     * Updates nextProductId to prevent ID conflicts.
     *
     * @param product the product to add (must not be null)
     * @throws IllegalArgumentException if product is null
     */
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        products.add(product);
        System.out.printf("Product '%s' added with ID: %d%n", product.getName(), product.getId());

        // Ensure nextProductId is higher than any existing ID
        nextProductId = Math.max(nextProductId, product.getId() + 1);
    }

    /**
     * Removes a product from the inventory.
     * No referential checks needed - products don't have dependencies.
     *
     * @param productId the ID of the product to remove
     */
    public void removeProduct(int productId) {
        Optional<Product> productOpt = findProduct(productId);

        if (productOpt.isEmpty()) {
            System.out.printf("Product with ID %d not found!%n", productId);
            return;
        }

        Product product = productOpt.get();

        // Remove product by ID predicate
        products.removeIf(p -> p.getId() == productId);
        System.out.printf("Product '%s' with ID: %d removed successfully!%n",
                product.getName(), productId);
    }

    /**
     * Finds a product by ID.
     * Uses Java Streams for functional-style filtering.
     *
     * @param productId the ID of the product to find
     * @return Optional containing the product if found, empty Optional otherwise
     */
    public Optional<Product> findProduct(int productId) {
        // Match product ID and return first match or empty
        return products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst();
    }

    /**
     * Updates the stock quantity of a product.
     * Uses Optional.ifPresentOrElse for concise null-safe handling.
     *
     * @param productId the ID of the product to update
     * @param quantity the new quantity (must be non-negative - validated by Product.setQuantity)
     */
    public void updateStock(int productId, int quantity) {
        Optional<Product> productOpt = findProduct(productId);
        // Update if found or else display error
        productOpt.ifPresentOrElse(
                product -> product.setQuantity(quantity),
                () -> System.out.printf("Product with ID %d not found!%n", productId)
        );
    }

    // Console Reporting Methods
    // These methods display information directly to console

    /**
     * Lists all products in inventory to console.
     */
    public void listAllProducts() {
        System.out.println("PRODUCT LIST:");
        printProductsOrMessage(products, "No products found!");
    }

    /**
     * Lists expired products to console.
     * Uses Product.isExpired() method (polymorphic - calls subclass implementation).
     */
    public void listExpiredProducts() {
        System.out.println("EXPIRED PRODUCT LIST:");
        List<Product> expiredProducts = products.stream()
                .filter(Product::isExpired)         // Method reference to isExpired()
                .toList();
        printProductsOrMessage(expiredProducts, "No expired products found!");
    }

    /**
     * Lists low stock products to console.
     * Uses Product.isLowStock() method.
     */
    public void listLowStockProducts() {
        System.out.println("LOW STOCK PRODUCT LIST:");
        List<Product> lowStockProducts = products.stream()
                .filter(Product::isLowStock)        // Method reference to isLowStock()
                .toList();
        printProductsOrMessage(lowStockProducts, "No low stock products found!");
    }

    /**
     * Lists products by specific category to console.
     *
     * @param category the category to filter by (must not be null)
     * @throws IllegalArgumentException if category is null
     */
    public void listProductsByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        System.out.printf("PRODUCTS IN CATEGORY: %s%n", category);
        List<Product> categoryProducts = products.stream()
                .filter(p -> p.getCategory() == category)       // Compare enum references
                .toList();
        printProductsOrMessage(categoryProducts, "No products found in this category!");
    }

    /**
     * Lists all suppliers to console.
     */
    public void listAllSuppliers() {
        System.out.println("SUPPLIER LIST:");
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found!");
        } else {
            suppliers.forEach(System.out::println);     // Print each supplier using toString()
        }
    }

    /**
     * Generates a complete inventory report to console.
     * Includes formatted sections with separators.
     *
     * @param includeSupplierDetails whether to include supplier details in the report
     */
    public void generateCompleteReport(boolean includeSupplierDetails) {
        String separator = createSeparator('=', REPORT_SEPARATOR_LENGTH);

        System.out.println("\n" + separator);
        System.out.println("COMPLETE INVENTORY REPORT");
        System.out.println(separator);

        // Optional supplier section
        if (includeSupplierDetails) {
            System.out.println("\nSUPPLIER DETAILS:");
            System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            listAllSuppliers();
        }

        // Product summary section
        System.out.println("\nPRODUCT SUMMARY:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        System.out.println("Total Products: " + getProductCount());
        System.out.println("Total Suppliers: " + getSupplierCount());

        // All products section
        System.out.println("\nALL PRODUCTS:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        listAllProducts();

        // Expired products section
        System.out.println("\nEXPIRED PRODUCTS:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        listExpiredProducts();

        // Low stock products section
        System.out.println("\nLOW STOCK PRODUCTS:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        listLowStockProducts();

        // Report footer
        System.out.println("\n" + separator);
        System.out.println("REPORT COMPLETE");
        System.out.println(separator);
    }

    // File Reporting Methods
    // These methods save reports to external files

    /**
     * Saves low stock products report to a file.
     *
     * @param filename the name/path of the file to save to
     */
    public void saveLowStockReportToFile(String filename) {
        List<Product> lowStockProducts = products.stream()
                .filter(Product::isLowStock)
                .toList();

        saveProductReportToFile(filename, "LOW STOCK PRODUCTS REPORT",
                "LOW STOCK PRODUCTS", lowStockProducts);
    }

    /**
     * Saves expired products report to a file.
     *
     * @param filename the name/path of the file to save to
     */
    public void saveExpiredProductsReportToFile(String filename) {
        List<Product> expiredProducts = products.stream()
                .filter(Product::isExpired)
                .toList();

        saveProductReportToFile(filename, "EXPIRED PRODUCTS REPORT",
                "EXPIRED PRODUCTS", expiredProducts);
    }

    /**
     * Saves category-specific products report to a file.
     *
     * @param filename the name/path of the file to save to
     * @param category the category to filter by (must not be null)
     * @throws IllegalArgumentException if category is null
     */
    public void saveCategoryReportToFile(String filename, Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        List<Product> categoryProducts = products.stream()
                .filter(p -> p.getCategory() == category)
                .toList();

        saveProductReportToFile(filename,
                String.format("CATEGORY REPORT: %s", category),
                String.format("PRODUCTS IN CATEGORY: %s", category),
                categoryProducts);
    }

    /**
     * Saves a complete inventory report to a file.
     * Creates a comprehensive report with multiple sections.
     *
     * @param filename the name/path of the file to save to
     * @param includeSupplierDetails whether to include supplier details
     */
    public void saveCompleteReportToFile(String filename, boolean includeSupplierDetails) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Try-with-resources ensures writer is closed automatically
            String timestamp = LocalDateTime.now().format(REPORT_TIMESTAMP_FORMATTER);

            // Report Header
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println("COMPLETE INVENTORY REPORT");
            writer.println("Generated: " + timestamp);
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println();       // Blank line

            // Supplier Section (if requested)
            if (includeSupplierDetails) {
                writer.println("SUPPLIER DETAILS:");
                writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
                if (suppliers.isEmpty()) {
                    writer.println("No suppliers found!");
                } else {
                    for (Supplier supplier : suppliers) {
                        writer.println(supplier.toString());
                    }
                }
                writer.println();       // Blank line
            }

            // Product Summary
            writer.println("PRODUCT SUMMARY:");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            writer.println("Total Products: " + getProductCount());
            writer.println("Total Suppliers: " + getSupplierCount());
            writer.println("Expired Products: " + countExpiredProducts());
            writer.println("Low Stock Products: " + countLowStockProducts());
            writer.println();           // Blank line

            // All Products Section
            writer.println("ALL PRODUCTS:");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            if (products.isEmpty()) {
                writer.println("No products found!");
            } else {
                for (Product product : products) {
                    writer.println(product.toString());
                }
            }
            writer.println();           // Blank line

            // Expired Products Section
            writer.println("EXPIRED PRODUCTS:");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            List<Product> expiredProducts = products.stream()
                    .filter(Product::isExpired)
                    .toList();
            if (expiredProducts.isEmpty()) {
                writer.println("No expired products found!");
            } else {
                for (Product product : expiredProducts) {
                    writer.println(product.toString());
                }
            }
            writer.println();           // Blank line

            // Low Stock Products Section
            writer.println("LOW STOCK PRODUCTS:");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            List<Product> lowStockProducts = products.stream()
                    .filter(Product::isLowStock)
                    .toList();
            if (lowStockProducts.isEmpty()) {
                writer.println("No low stock products found!");
            } else {
                for (Product product : lowStockProducts) {
                    writer.println(product.toString());
                }
            }

            // Report Footer
            writer.println("\n" + createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println("REPORT END");
            writer.println("Generated by Grocery Inventory Manager");
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));

            System.out.printf("Complete inventory report saved to: %s%n", filename);
        } catch (IOException e) {
            // Graceful error handling - don't crash, just inform user
            System.out.printf("Error saving report to file '%s': %s%n", filename, e.getMessage());
        }
    }

    /**
     * Saves a general inventory report to a file.
     * Backward compatibility method - delegates to saveCompleteReportToFile.
     *
     * @param filename the name/path of the file to save to
     * @param includeSupplierDetails whether to include supplier details
     */
    public void saveReportToFile(String filename, boolean includeSupplierDetails) {
        saveCompleteReportToFile(filename, includeSupplierDetails);
    }

    /**
     * Private helper to save a product list report to a file.
     * Generic method used by specialized report methods.
     *
     * @param filename target file name
     * @param reportTitle main report title
     * @param sectionTitle section heading
     * @param products list of products to include in report
     */
    private void saveProductReportToFile(String filename, String reportTitle,
                                         String sectionTitle, List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String timestamp = LocalDateTime.now().format(REPORT_TIMESTAMP_FORMATTER);

            // Report header
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println(reportTitle);
            writer.println("Generated: " + timestamp);
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println();

            // Product section with count
            writer.println(sectionTitle + " (" + products.size() + "):");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));

            if (products.isEmpty()) {
                writer.println("No products found!");
            } else {
                for (Product product : products) {
                    writer.println(product.toString());
                }
            }

            // Report footer
            writer.println("\n" + createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println("REPORT END");
            writer.println("Generated by Grocery Inventory Manager");
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));

            System.out.printf("Report saved to: %s%n", filename);
        } catch (IOException e) {
            System.out.printf("Error saving to file '%s': %s%n", filename, e.getMessage());
        }
    }

    // Helper Methods

    /**
     * Helper: Prints products list or message if list is empty.
     * Used by console display methods for consistent empty handling.
     *
     * @param products list of products to print
     * @param emptyMessage message to display if list is empty
     */
    private void printProductsOrMessage(List<Product> products, String emptyMessage) {
        if (products.isEmpty()) {
            System.out.println(emptyMessage);
        } else {
            products.forEach(System.out::println);      // Method reference for concise code
        }
    }

    /**
     * Helper: Creates visual separator line for reports.
     * Repeats character to specified length for consistent formatting.
     *
     * @param character character to repeat (e.g., '=', '-')
     * @param length number of characters in separator
     * @return separator string
     */
    private String createSeparator(char character, int length) {
        return String.valueOf(character).repeat(length);
    }

    /**
     * Helper: Counts expired products.
     * Uses stream count() for efficient counting.
     *
     * @return number of expired products
     */
    private long countExpiredProducts() {
        return products.stream()
                .filter(Product::isExpired)
                .count();       // Terminal operation - returns count
    }

    /**
     * Helper: Counts low stock products.
     * Uses stream count() for efficient counting.
     *
     * @return number of low stock products
     */
    private long countLowStockProducts() {
        return products.stream()
                .filter(Product::isLowStock)
                .count();
    }

    // Getters

    public int getNextProductId() {

        return nextProductId;
    }

    public int getNextSupplierId() {

        return nextSupplierId;
    }

    public boolean hasProducts() {

        return !products.isEmpty();
    }

    public boolean hasSuppliers() {
        return !suppliers.isEmpty();
    }

    public int getProductCount() {
        return products.size();
    }

    public int getSupplierCount() {
        return suppliers.size();
    }

    /**
     * Gets all products in the inventory.
     * Returns an unmodifiable copy to prevent external modification.
     *
     * @return an unmodifiable list of all products
     */
    public List<Product> getAllProducts() {
        return List.copyOf(products);       // Defensive copy - immutable
    }

    /**
     * Gets all suppliers in the inventory.
     * Returns an unmodifiable copy to prevent external modification.
     *
     * @return an unmodifiable list of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return List.copyOf(suppliers);      // Defensive copy - immutable
    }
}