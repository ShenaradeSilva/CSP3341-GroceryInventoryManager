package com.csp3341.grocery;

public class NonPerishable extends Product {
    private String shelfLife;

    public NonPerishable(int id, String name, double price, int quantity, Category category, String shelfLife) {
        super(id, name, price, quantity, category);
        this.shelfLife = shelfLife;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
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
