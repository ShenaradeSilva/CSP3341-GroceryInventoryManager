package com.csp3341.grocery;

public class NonPerishable extends Product {
    private String shelfLife;

    public NonPerishable(int id, String name, double price, int quantity, Category category, Supplier supplier, String shelfLife) {
        super(id, name, price, quantity, category, supplier);
        if (shelfLife == null || shelfLife.trim().isEmpty()) {
            System.out.println("Error! Shelf life cannot be empty for non-perishable products!");
            throw new IllegalArgumentException("Shelf life required for non-perishable products");
        }

        this.shelfLife = shelfLife.trim();
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        if (shelfLife == null || shelfLife.trim().isEmpty()) {
            throw new IllegalArgumentException("Shelf life cannot be empty");
        }
        this.shelfLife = shelfLife.trim();
    }

    public boolean hasShelfLife() {
        return shelfLife != null && !shelfLife.isEmpty();
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String toString() {
        String shelfInfo = hasShelfLife() ? " | Shelf Life: " + shelfLife : " | No Shelf Life Info";
        return super.toString() + shelfInfo;
    }
}
