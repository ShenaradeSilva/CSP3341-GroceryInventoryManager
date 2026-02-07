/**
 * This file defines the abstract base class for all products in a grocery
 * inventory management system. It provides common functionality and
 * attributes that all product types share, while allowing specific
 * implementations through abstraction.
 */

package com.csp3341.grocery;

/**
 * Abstract base class for all products in the inventory system.
 */
public abstract class Product {
    // Default low stock warning threshold
    protected static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    // Immutable fields (set once at creation)
    protected final int id;
    protected final String name;
    protected final Category category;
    protected final Supplier supplier;

    // Mutable fields (can be updated)
    protected double price;
    protected int quantity;
    protected int lowStockThreshold;

    /**
     * Constructor with validation for all arguments.
     */
    public Product(int id, String name, double price, int quantity,
                   Category category, Supplier supplier) {
        // Validate all arguments before object creation
        validateConstructorArgs(id, name, price, quantity, category, supplier);
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
        this.lowStockThreshold = DEFAULT_LOW_STOCK_THRESHOLD;
    }

    /**
     * Validates constructor arguments to ensure data integrity.
     */
    private void validateConstructorArgs(int id, String name, double price,
                                         int quantity, Category category, Supplier supplier) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    // Setters with validation
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        if (lowStockThreshold < 0) {
            throw new IllegalArgumentException("Low stock threshold cannot be negative");
        }
        this.lowStockThreshold = lowStockThreshold;
    }

    // Business Logic

    /**
     * Checks if current stock is at or below threshold.
     */
    public boolean isLowStock() {
        return quantity <= lowStockThreshold;
    }

    /**
     * Abstract method - subclasses must define expiration logic.
     */
    public abstract boolean isExpired();

    // String Representation
    /**
     * Returns formatted product info with status indicators.
     * Format: "ID | Name | Price | Quantity | Category | Supplier [Status]"
     */
    @Override
    public String toString() {
        StringBuilder statusBuilder = new StringBuilder();

        if (isLowStock()) {
            statusBuilder.append(" [LOW STOCK]");
        }

        if (isExpired()) {
            statusBuilder.append(" [EXPIRED]");
        }

        return String.format("%d | %s | LKR %.2f | Qty: %d | %s | Supplier: %s%s",
                id, name, price, quantity, category, supplier.getSupplierName(), statusBuilder);
    }
}