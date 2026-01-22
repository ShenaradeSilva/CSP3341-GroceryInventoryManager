package com.csp3341.grocery;

public abstract class Product {
    protected int id;
    protected String name;
    protected double price;
    protected int quantity;
    protected Category category;

    public Product(int id, String name, double price, int quantity, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isLowStock() {
        return quantity < 5;
    }

    public abstract boolean isExpired();

    @Override
    public String toString() {
        return id + " | " + name + " | LKR " + price + " | " + quantity + " | " + category;
    }
}
