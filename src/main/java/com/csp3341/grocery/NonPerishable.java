/**
 * This file defines a NonPerishable product class that extends the Product base class
 * for a grocery inventory management system. It represents products that don't expire
 * in the traditional sense but have recommended shelf life durations.
 * These products never expire in the system.
 */

package com.csp3341.grocery;

/**
 * Represents a non-perishable product with shelf life information.
 */
public class NonPerishable extends Product {
    private String shelfLife;

    /**
     * Constructor for non-perishable products.
     *
     * @param shelfLife Recommended shelf life duration
     */
    public NonPerishable(int id, String name, double price, int quantity,
                         Category category, Supplier supplier, String shelfLife) {
        // Call parent constructor first
        super(id, name, price, quantity, category, supplier);

        // Set shelf life with validation
        setShelfLife(shelfLife);
    }

    // Getter
    public String getShelfLife() {
        return shelfLife;
    }

    // Setter
    /**
     * Sets the recommended shelf life duration.
     *
     * @param shelfLife Cannot be null or empty (trimmed before storage)
     * @throws IllegalArgumentException if shelfLife is null or empty
     */
    public void setShelfLife(String shelfLife) {
        // Check for null or empty input
        if (shelfLife == null || shelfLife.trim().isEmpty()) {
            throw new IllegalArgumentException("Shelf life cannot be null or empty for non-perishable products");
        }
        this.shelfLife = shelfLife.trim();
    }

    // Overridden Methods
    /**
     * Non-perishable products never expire in this system.
     * Always returns false regardless of shelf life.
     *
     * @return false (non-perishable products don't expire)
     */
    @Override
    public boolean isExpired() {
        return false;
    }

    /**
     * Returns product info with shelf life appended.
     *
     * @return Formatted string: "[parent toString()] | Shelf Life: [shelfLife]"
     */
    @Override
    public String toString() {
        return super.toString() + " | Shelf Life: " + shelfLife;
    }
}