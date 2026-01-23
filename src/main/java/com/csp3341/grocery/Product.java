package com.csp3341.grocery;

public abstract class Product {
    protected int id;
    protected String name;
    protected double price;
    protected int quantity;
    protected Category category;
    protected Supplier supplier;
    protected int lowStockThreshold = 5;

    public Product(int id, String name, double price, int quantity, Category category, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
    }

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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isLowStock() {
        return quantity <= lowStockThreshold;
    }

    public abstract boolean isExpired();

    @Override
    public String toString() {
        String status = "";

        if (isLowStock()) {
            status += " [LOW STOCK]";
        }

        if (isExpired()) {
            status += " [EXPIRED]";
        }

        return id + " | " + name + " | LKR " + price + " | Qty: " + quantity + " | " +
                category + " | Supplier: " + supplier.getSupplierName() + status;
    }
}
