package com.csp3341.grocery;

/**
 * Represents product categories in the grocery inventory system.
 * Provides a formatted string representation for display.
 */
public enum Category {
    DAIRY,
    PRODUCE,
    MEAT,
    BEVERAGES,
    CANNED_FOOD,
    DRIED_FOOD;

    /**
     * Returns a formatted string representation of the category.
     * Example: "DRIED_FOOD" becomes "Dried food"
     *
     * @return formatted category name
     */
    @Override
    public String toString() {
        String formattedName = name().replace("_", " ").toLowerCase();
        return formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1);
    }
}