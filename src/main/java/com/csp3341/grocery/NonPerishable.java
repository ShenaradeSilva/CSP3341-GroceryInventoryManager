package com.csp3341.grocery;

/**
 * Represents a non-perishable product with shelf life information.
 */
public class NonPerishable extends Product {
    private String shelfLife;

    public NonPerishable(int id, String name, double price, int quantity,
                         Category category, Supplier supplier, String shelfLife) {
        super(id, name, price, quantity, category, supplier);
        setShelfLife(shelfLife);
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        if (shelfLife == null || shelfLife.trim().isEmpty()) {
            throw new IllegalArgumentException("Shelf life cannot be null or empty for non-perishable products");
        }
        this.shelfLife = shelfLife.trim();
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " | Shelf Life: " + shelfLife;
    }
}