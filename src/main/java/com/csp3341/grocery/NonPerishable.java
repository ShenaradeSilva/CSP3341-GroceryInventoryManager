package com.csp3341.grocery;

public class NonPerishable extends Product {
    private String shelfLife;

    public NonPerishable(int id, String name, double price, int quantity, Category category, Supplier supplier, String shelfLife) {
        super(id, name, price, quantity, category, supplier);
        this.shelfLife = shelfLife;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
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
        String shelfInfo = hasShelfLife() ? " ! Shelf Life: " + shelfLife: "";
        return super.toString() + shelfInfo;
    }
}
