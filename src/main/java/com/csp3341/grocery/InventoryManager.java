package com.csp3341.grocery;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryManager {
    private final List<Product> products;
    private final List<Supplier> suppliers;
    private int nextProductId;
    private int nextSupplierId;

    public InventoryManager() {
        products = new ArrayList<>();
        suppliers = new ArrayList<>();
        nextProductId = 1;
        nextSupplierId = 1;
    }

    // Supplier Management
    public void addSupplier(String supplierName, String contact) {
        Supplier supplier = new Supplier(nextSupplierId, supplierName, contact);
        suppliers.add(supplier);
        nextSupplierId++;
        System.out.println("Supplier '" + supplierName + "' added with ID: " + supplier.getSupplierId());
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
        if (supplier.getSupplierId() >= nextSupplierId) {
            nextSupplierId = supplier.getSupplierId() + 1;
        }
    }

    public Supplier findSupplier(int supplierId) {
        return suppliers.stream().filter(s -> s.getSupplierId() == supplierId)
                .findFirst().orElse(null);
    }

    public void removeSupplier(int supplierId)
    {
        Supplier supplier = findSupplier(supplierId);
        if (supplier != null) {
            boolean hasProducts = products.stream().anyMatch(p -> p.getSupplier().getSupplierId() == supplierId);
            if (hasProducts) {
                System.out.println("Cannot Remove Supplier '" + supplier.getSupplierName() + "'! There are products associated with this Supplier.");
                return;
            }

            suppliers.removeIf(s -> s.getSupplierId() == supplierId);
            System.out.println("Supplier '" + supplier.getSupplierName() + "'with ID: " + supplierId + " Removed Successfully!");

            reassignSupplierIds();
        }
        else {
            System.out.println("Supplier with ID " + supplierId + " Not Found!");
        }
    }

    private void reassignSupplierIds() {
        suppliers.sort((s1, s2) -> Integer.compare(s1.getSupplierId(), s2.getSupplierId()));
        nextSupplierId = 1;

        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            Supplier newSupplier = new Supplier(
                    nextSupplierId,
                    supplier.getSupplierName(),
                    supplier.getContact()
            );
            suppliers.set(i, newSupplier);

            for (Product product : products) {
                if (product.getSupplier().equals(supplier)) {
                    updateProductSupplier(product, newSupplier);
                }
            }

            nextSupplierId++;
        }
    }

    private void updateProductSupplier(Product oldProduct, Supplier newSupplier) {
        int index = products.indexOf(oldProduct);
        if (index != -1) {
            Product product = products.get(index);

            if (product instanceof Perishable) {
                Perishable perishable = (Perishable) product;
                Product newProduct = new Perishable(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getCategory(),
                        newSupplier,
                        perishable.getExpiryDate().toString()
                );
                products.set(index, newProduct);
            }
            else if (product instanceof NonPerishable) {
                NonPerishable nonPerishable = (NonPerishable) product;
                Product newProduct = new NonPerishable(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getCategory(),
                        newSupplier,
                        nonPerishable.getShelfLife()
                );
                products.set(index, newProduct);
            }
        }
    }

    // Product Management
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product '" + product.getName() + "' added with ID: " + product.getId());

        if (product.getId() >= nextProductId) {
            nextProductId = product.getId() + 1;
        }
    }

    public void removeProduct(int productId) {
        Product product = findProduct(productId);
        if (product != null) {
            products.removeIf(p -> p.getId() == productId);
            System.out.println("Product '" + product.getName() + "' with ID: " + productId + " Removed Successfully!");
            reassignProductIds();
        }
        else {
            System.out.println("Product with ID " + productId + " not found!");
        }
    }

    private void reassignProductIds(){
        products.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
        nextProductId = 1;

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (product instanceof Perishable) {
                Perishable perishable = (Perishable) product;
                Product newProduct = new Perishable(
                        nextProductId,
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getCategory(),
                        product.getSupplier(),
                        perishable.getExpiryDate().toString()
                );
                products.set(i, newProduct);
            }
            else if (product instanceof NonPerishable) {
                NonPerishable nonPerishable = (NonPerishable) product;
                Product newProduct = new NonPerishable(
                        nextProductId,
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getCategory(),
                        product.getSupplier(),
                        nonPerishable.getShelfLife()
                );
                products.set(i, newProduct);
            }
            nextProductId++;
        }
    }

    public Product findProduct(int productId) {
        return products.stream().filter(p -> p.getId() == productId)
                .findFirst().orElse(null);
    }

    public void updateStock(int productId, int quantity) {
        Product p = findProduct(productId);
        if (p != null) {
            p.setQuantity(quantity);
        }
    }

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

    // Listing and Reporting
    public void listAllProducts() {
        System.out.println("PRODUCT LIST: ");
        if (products.isEmpty()) {
            System.out.println("No Products Found!");
        }
        else {
            products.forEach(System.out::println);
        }
    }

    public void listExpiredProducts() {
        System.out.println("EXPIRED PRODUCT LIST: ");
        List<Product> expiredProduct = products.stream().filter(Product::isExpired).toList();

        if (expiredProduct.isEmpty()) {
            System.out.println("No Expired Products Found!");
        }
        else {
            expiredProduct.forEach(System.out::println);
        }
    }

    public void listLowStockProducts() {
        System.out.println("LOW STOCK PRODUCT LIST:");
        List<Product> lowStockProducts = products.stream().filter(Product::isLowStock).toList();

        if (lowStockProducts.isEmpty()) {
            System.out.println("No Low Stock Products Found!");
        }
        else {
            lowStockProducts.forEach(System.out::println);
        }
    }

    public void listProductsByCategory(Category category) {
        System.out.println("PRODUCTS IN CATEGORY:" + category);
        List<Product> categoryProducts = products.stream().filter(p -> p.getCategory() == category)
                .toList();

        if (categoryProducts.isEmpty()) {
            System.out.println("No Products Found in Category!");
        }
        else {
            categoryProducts.forEach(System.out::println);
        }
    }

    public void listAllSuppliers() {
        System.out.println("SUPPLIER LIST: ");
        if (suppliers.isEmpty()) {
            System.out.println("No Suppliers Found!");
        }
        else {
            suppliers.forEach(System.out::println);
        }
    }

    public void generateCompleteReport() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("COMPLETE INVENTORY REPORT");
        System.out.println("=".repeat(80));

        System.out.println("\nALL PRODUCTS:");
        System.out.println("-".repeat(80));
        listAllProducts();

        System.out.println("\nEXPIRED PRODUCTS:");
        System.out.println("-".repeat(80));
        listExpiredProducts();

        System.out.println("\nLOW STOCK PRODUCTS:");
        System.out.println("-".repeat(80));
        listLowStockProducts();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("REPORT COMPLETE");
        System.out.println("=".repeat(80));
    }

    public void saveLowStockReportToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String separator = createSeparator('=', 60);
            String subSeparator = createSeparator('-', 60);

            writer.println(separator);
            writer.println("LOW STOCK PRODUCTS REPORT");
            writer.println("Generated: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println(separator);
            writer.println();

            List<Product> lowStockProducts = products.stream()
                    .filter(Product::isLowStock)
                    .toList();

            writer.println("LOW STOCK PRODUCTS (" + lowStockProducts.size() + "):");
            writer.println(subSeparator);

            if (lowStockProducts.isEmpty()) {
                writer.println("No Low Stock Products Found!");
            } else {
                for (Product p : lowStockProducts) {
                    writer.println(p.toString());
                }
            }

            System.out.println("Low stock report saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void saveExpiredProductsReportToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String separator = createSeparator('=', 60);
            String subSeparator = createSeparator('-', 60);

            writer.println(separator);
            writer.println("EXPIRED PRODUCTS REPORT");
            writer.println("Generated: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println(separator);
            writer.println();

            List<Product> expiredProducts = products.stream()
                    .filter(Product::isExpired)
                    .toList();

            writer.println("EXPIRED PRODUCTS (" + expiredProducts.size() + "):");
            writer.println(subSeparator);

            if (expiredProducts.isEmpty()) {
                writer.println("No Expired Products Found!");
            } else {
                for (Product p : expiredProducts) {
                    writer.println(p.toString());
                }
            }

            System.out.println("Expired products report saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void saveCategoryReportToFile(String filename, Category category) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String separator = createSeparator('=', 60);
            String subSeparator = createSeparator('-', 60);

            writer.println(separator);
            writer.println("CATEGORY REPORT: " + category);
            writer.println("Generated: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println(separator);
            writer.println();

            List<Product> categoryProducts = products.stream()
                    .filter(p -> p.getCategory() == category)
                    .toList();

            writer.println("PRODUCTS IN CATEGORY: " + category + " (" + categoryProducts.size() + ")");
            writer.println(subSeparator);

            if (categoryProducts.isEmpty()) {
                writer.println("No Products Found in Category!");
            } else {
                for (Product p : categoryProducts) {
                    writer.println(p.toString());
                }
            }

            System.out.println("Category report saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void saveReportToFile(String filename, boolean includeSupplierDetails) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            String separator = createSeparator('=', 60);
            String subSeparator = createSeparator('-', 60);

            writer.println(separator);
            writer.println("INVENTORY REPORT");
            writer.println("Generated: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println(separator);
            writer.println();

            if (includeSupplierDetails) {
                writer.println("SUPPLIER DETAILS:");
                writer.println(subSeparator);
                if (suppliers.isEmpty()) {
                    writer.println("No Suppliers Found!");
                } else {
                    for (Supplier s : suppliers) {
                        writer.println(s.toString());
                    }
                }
                writer.println();
            }

            writer.println("PRODUCT LIST:");
            writer.println(subSeparator);
            if (products.isEmpty()) {
                writer.println("No Products Found!");
            } else {
                for (Product p : products) {
                    writer.println(p.toString());
                }
            }

            writer.println("\n" + separator);
            writer.println("REPORT END");
            writer.println("Total Products: " + getProductCount());
            writer.println("Total Suppliers: " + getSupplierCount());

            System.out.println("Report saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private String createSeparator(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void saveConsoleReport(boolean includeSupplierDetails) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("CONSOLE INVENTORY REPORT");
        System.out.println("=".repeat(80));

        if (includeSupplierDetails) {
            System.out.println("\nSUPPLIER DETAILS:");
            System.out.println("-".repeat(80));
            listAllSuppliers();
        }

        System.out.println("\nPRODUCT LIST:");
        System.out.println("-".repeat(80));
        listAllProducts();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("REPORT END");
        System.out.println("Total Products: " + getProductCount());
        System.out.println("Total Suppliers: " + getSupplierCount());
        System.out.println("=".repeat(80));
    }
}