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
    private static final DateTimeFormatter REPORT_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int REPORT_SEPARATOR_LENGTH = 60;

    private final List<Product> products;
    private final List<Supplier> suppliers;
    private int nextProductId;
    private int nextSupplierId;

    public InventoryManager() {
        this.products = new ArrayList<>();
        this.suppliers = new ArrayList<>();
        this.nextProductId = 1;
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
        Supplier supplier = new Supplier(nextSupplierId, supplierName, contact);
        suppliers.add(supplier);
        System.out.printf("Supplier '%s' added with ID: %d%n", supplierName, supplier.getSupplierId());
        nextSupplierId++;
    }

    /**
     * Adds an existing supplier to the inventory.
     *
     * @param supplier the supplier to add
     * @throws IllegalArgumentException if supplier is null
     */
    public void addSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        suppliers.add(supplier);
        nextSupplierId = Math.max(nextSupplierId, supplier.getSupplierId() + 1);
    }

    /**
     * Finds a supplier by ID.
     *
     * @param supplierId the ID of the supplier to find
     * @return Optional containing the supplier if found, empty otherwise
     */
    public Optional<Supplier> findSupplier(int supplierId) {
        return suppliers.stream()
                .filter(s -> s.getSupplierId() == supplierId)
                .findFirst();
    }

    /**
     * Removes a supplier if no products are associated with it.
     *
     * @param supplierId the ID of the supplier to remove
     */
    public void removeSupplier(int supplierId) {
        Optional<Supplier> supplierOpt = findSupplier(supplierId);

        if (supplierOpt.isEmpty()) {
            System.out.printf("Supplier with ID %d not found!%n", supplierId);
            return;
        }

        Supplier supplier = supplierOpt.get();

        if (hasProductsForSupplier(supplierId)) {
            System.out.printf("Cannot remove supplier '%s'! There are products associated with this supplier.%n",
                    supplier.getSupplierName());
            return;
        }

        suppliers.removeIf(s -> s.getSupplierId() == supplierId);
        System.out.printf("Supplier '%s' with ID: %d removed successfully!%n",
                supplier.getSupplierName(), supplierId);
    }

    private boolean hasProductsForSupplier(int supplierId) {
        return products.stream()
                .anyMatch(p -> p.getSupplier().getSupplierId() == supplierId);
    }

    // Product Management

    /**
     * Adds a product to the inventory.
     *
     * @param product the product to add
     * @throws IllegalArgumentException if product is null
     */
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        products.add(product);
        System.out.printf("Product '%s' added with ID: %d%n", product.getName(), product.getId());
        nextProductId = Math.max(nextProductId, product.getId() + 1);
    }

    /**
     * Removes a product from the inventory.
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
        products.removeIf(p -> p.getId() == productId);
        System.out.printf("Product '%s' with ID: %d removed successfully!%n",
                product.getName(), productId);
    }

    /**
     * Finds a product by ID.
     *
     * @param productId the ID of the product to find
     * @return Optional containing the product if found, empty otherwise
     */
    public Optional<Product> findProduct(int productId) {
        return products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst();
    }

    /**
     * Updates the stock quantity of a product.
     *
     * @param productId the ID of the product to update
     * @param quantity the new quantity
     */
    public void updateStock(int productId, int quantity) {
        Optional<Product> productOpt = findProduct(productId);
        productOpt.ifPresentOrElse(
                product -> product.setQuantity(quantity),
                () -> System.out.printf("Product with ID %d not found!%n", productId)
        );
    }

    // Console Reporting Methods

    public void listAllProducts() {
        System.out.println("PRODUCT LIST:");
        printProductsOrMessage(products, "No products found!");
    }

    public void listExpiredProducts() {
        System.out.println("EXPIRED PRODUCT LIST:");
        List<Product> expiredProducts = products.stream()
                .filter(Product::isExpired)
                .toList();
        printProductsOrMessage(expiredProducts, "No expired products found!");
    }

    public void listLowStockProducts() {
        System.out.println("LOW STOCK PRODUCT LIST:");
        List<Product> lowStockProducts = products.stream()
                .filter(Product::isLowStock)
                .toList();
        printProductsOrMessage(lowStockProducts, "No low stock products found!");
    }

    public void listProductsByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        System.out.printf("PRODUCTS IN CATEGORY: %s%n", category);
        List<Product> categoryProducts = products.stream()
                .filter(p -> p.getCategory() == category)
                .toList();
        printProductsOrMessage(categoryProducts, "No products found in this category!");
    }

    public void listAllSuppliers() {
        System.out.println("SUPPLIER LIST:");
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found!");
        } else {
            suppliers.forEach(System.out::println);
        }
    }

    /**
     * Generates a complete inventory report to console.
     * Includes supplier details if requested.
     *
     * @param includeSupplierDetails whether to include supplier details in the report
     */
    public void generateCompleteReport(boolean includeSupplierDetails) {
        String separator = createSeparator('=', REPORT_SEPARATOR_LENGTH);

        System.out.println("\n" + separator);
        System.out.println("COMPLETE INVENTORY REPORT");
        System.out.println(separator);

        if (includeSupplierDetails) {
            System.out.println("\nSUPPLIER DETAILS:");
            System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            listAllSuppliers();
        }

        System.out.println("\nPRODUCT SUMMARY:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        System.out.println("Total Products: " + getProductCount());
        System.out.println("Total Suppliers: " + getSupplierCount());

        System.out.println("\nALL PRODUCTS:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        listAllProducts();

        System.out.println("\nEXPIRED PRODUCTS:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        listExpiredProducts();

        System.out.println("\nLOW STOCK PRODUCTS:");
        System.out.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
        listLowStockProducts();

        System.out.println("\n" + separator);
        System.out.println("REPORT COMPLETE");
        System.out.println(separator);
    }

    // File Reporting Methods

    public void saveLowStockReportToFile(String filename) {
        List<Product> lowStockProducts = products.stream()
                .filter(Product::isLowStock)
                .toList();

        saveProductReportToFile(filename, "LOW STOCK PRODUCTS REPORT",
                "LOW STOCK PRODUCTS", lowStockProducts);
    }

    public void saveExpiredProductsReportToFile(String filename) {
        List<Product> expiredProducts = products.stream()
                .filter(Product::isExpired)
                .toList();

        saveProductReportToFile(filename, "EXPIRED PRODUCTS REPORT",
                "EXPIRED PRODUCTS", expiredProducts);
    }

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
     * Includes supplier details if requested.
     *
     * @param filename the name of the file to save to
     * @param includeSupplierDetails whether to include supplier details
     */
    public void saveCompleteReportToFile(String filename, boolean includeSupplierDetails) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String timestamp = LocalDateTime.now().format(REPORT_TIMESTAMP_FORMATTER);

            // Report Header
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println("COMPLETE INVENTORY REPORT");
            writer.println("Generated: " + timestamp);
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println();

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
                writer.println();
            }

            // Product Summary
            writer.println("PRODUCT SUMMARY:");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));
            writer.println("Total Products: " + getProductCount());
            writer.println("Total Suppliers: " + getSupplierCount());
            writer.println("Expired Products: " + countExpiredProducts());
            writer.println("Low Stock Products: " + countLowStockProducts());
            writer.println();

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
            writer.println();

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
            writer.println();

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
            System.out.printf("Error saving report to file '%s': %s%n", filename, e.getMessage());
        }
    }

    /**
     * Saves a general inventory report to a file.
     * This is a simplified version for compatibility with the original interface.
     *
     * @param filename the name of the file to save to
     * @param includeSupplierDetails whether to include supplier details
     */
    public void saveReportToFile(String filename, boolean includeSupplierDetails) {
        saveCompleteReportToFile(filename, includeSupplierDetails);
    }

    private void saveProductReportToFile(String filename, String reportTitle,
                                         String sectionTitle, List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String timestamp = LocalDateTime.now().format(REPORT_TIMESTAMP_FORMATTER);

            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println(reportTitle);
            writer.println("Generated: " + timestamp);
            writer.println(createSeparator('=', REPORT_SEPARATOR_LENGTH));
            writer.println();

            writer.println(sectionTitle + " (" + products.size() + "):");
            writer.println(createSeparator('-', REPORT_SEPARATOR_LENGTH));

            if (products.isEmpty()) {
                writer.println("No products found!");
            } else {
                for (Product product : products) {
                    writer.println(product.toString());
                }
            }

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

    private void printProductsOrMessage(List<Product> products, String emptyMessage) {
        if (products.isEmpty()) {
            System.out.println(emptyMessage);
        } else {
            products.forEach(System.out::println);
        }
    }

    private String createSeparator(char character, int length) {
        return String.valueOf(character).repeat(length);
    }

    private long countExpiredProducts() {
        return products.stream()
                .filter(Product::isExpired)
                .count();
    }

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
     *
     * @return an unmodifiable list of all products
     */
    public List<Product> getAllProducts() {
        return List.copyOf(products);
    }

    /**
     * Gets all suppliers in the inventory.
     *
     * @return an unmodifiable list of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return List.copyOf(suppliers);
    }
}