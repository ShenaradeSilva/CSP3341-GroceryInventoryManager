package com.csp3341.grocery;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private final List<Product> products;

    public InventoryManager() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(int productId) {
        products.removeIf(p -> p.getId() == productId);
    }

    public Product findProduct(int productId) {
        return products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
    }

    public void updateStock(int productId, int quantity) {
        Product p = findProduct(productId);
        if (p != null) {
            p.setQuantity(quantity);
        }
    }

    public void listAllProducts() {
        System.out.println("PRODUCT LIST: ");
        products.forEach(System.out::println);
    }

    public void listExpiredProducts() {
        System.out.println("EXPIRED PRODUCT LIST: ");
        products.stream().filter(Product::isExpired).forEach(System.out::println);
    }

    public void listLowStockProducts() {
        System.out.println("LOW STOCK PRODUCT LIST:");
        products.stream().filter(Product::isLowStock).forEach(System.out::println);
    }

    public void listProductsByCategory(Category category) {
        // TODO: Implement
    }

    public void generateInventoryReport() {
        System.out.println("INVENTORY REPORT: ");
        listAllProducts();
        listExpiredProducts();
        listLowStockProducts();
    }
}
