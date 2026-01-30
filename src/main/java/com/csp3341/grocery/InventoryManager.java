package com.csp3341.grocery;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private final List<Product> products;
    private final List<Supplier> suppliers;

    public InventoryManager() {
        products = new ArrayList<>();
        suppliers = new ArrayList<>();
    }

    // Supplier Management
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public Supplier findSupplier(int supplierId) {
        return suppliers.stream().filter(s -> s.getSupplierId() == supplierId)
                .findFirst().orElse(null);
    }

    // Product Management
    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(int productId) {
        products.removeIf(p -> p.getId() == productId);
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

    public void generateInventoryReport() {
        System.out.println("INVENTORY REPORT: ");
        listAllProducts();
        listExpiredProducts();
        listLowStockProducts();
    }
}