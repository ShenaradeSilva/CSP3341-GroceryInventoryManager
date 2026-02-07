/**
 * This file defines the Category enumeration for a grocery inventory system.
 * Enums are used to represent a fixed set of constants - in this case,
 * product categories available in the grocery store.
 */

package com.csp3341.grocery;

/**
 * Represents product categories in the grocery inventory system.
 * Provides a formatted string representation for display.
 */
public enum Category {
    // Enum constants representing the available product categories
    DAIRY,
    PRODUCE,
    MEAT,
    BEVERAGES,
    CANNED_FOOD,
    DRIED_FOOD;

    /**
     * Overrides the default toString() method to provide a human-readable,
     * formatted representation of the category.
     *
     * The default enum name() method returns the constant name as declared
     * (e.g., "DRIED_FOOD"). This custom toString() converts it to a more
     * readable format (e.g., "Dried food").
     *
     * Example transformations:
     *   "DRIED_FOOD" → "Dried food"
     *   "CANNED_FOOD" → "Canned food"
     *   "DAIRY" → "Dairy"
     *
     * @return A formatted, human-readable string representation of the category
     */
    @Override
    public String toString() {
        // Replace underscores with spaces and convert to lowercase
        String formattedName = name().replace("_", " ").toLowerCase();

        // Capitalise only the first character
        return formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1);
    }
}