/**
 * This file defines a Perishable product class that extends the Product base class
 * for a grocery inventory management system. It adds expiration date functionality
 * specifically for products that spoil over time, implementing the abstract
 * expiration checking required by the parent class.
 */

package com.csp3341.grocery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a perishable product with an expiry date.
 * Extends Product class with expiration date functionality.
 */
public class Perishable extends Product {
    // ISO date format: YYYY-MM-DD (2026-02-04)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private LocalDate expiryDate;

    /**
     * Constructor for perishable products.
     *
     * @param expiryDate Date in YYYY-MM-DD format
     */
    public Perishable(int id, String name, double price, int quantity,
                      Category category, Supplier supplier, String expiryDate) {
        // Call parent constructor first
        super(id, name, price, quantity, category, supplier);

        // Set expiry date with validation
        setExpiryDate(expiryDate);
    }

    // Getter
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    // Setter
    /**
     * Sets expiry date with validation.
     *
     * @param expiryDate Must be valid YYYY-MM-DD format
     * @throws IllegalArgumentException for invalid or empty dates
     */
    public void setExpiryDate(String expiryDate) {
        // Check for null or empty input
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Expiry date cannot be null or empty");
        }

        try {
            // Parse date from string
            LocalDate parsedDate = LocalDate.parse(expiryDate.trim(), DATE_FORMATTER);

            // Warn if date is already past
            if (parsedDate.isBefore(LocalDate.now())) {
                System.out.println("Warning: Expiry date " + parsedDate + " is in the past. Product may be expired.");
            }

            this.expiryDate = parsedDate;
        } catch (DateTimeParseException e) {
            // Provide helpful error message for format issues
            throw new IllegalArgumentException(
                    "Invalid date format: '" + expiryDate + "'. Expected format: YYYY-MM-DD", e);
        }
    }

    // Overridden Methods
    /**
     * Checks if product is expired.
     *
     * @return true if expiry date exists and is before today
     */
    @Override
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Returns string with product info + expiry details.
     * Formats expired products differently.
     */
    @Override
    public String toString() {
        // Get parent's formatted string
        String baseString = super.toString();

        String expiryInfo;

        if (isExpired()) {
            // Show as expired with date
            expiryInfo = " [Expired " + expiryDate + "]";
        } else {
            // Show upcoming expiry date
            expiryInfo = " | Expiry: " + expiryDate;
        }

        return baseString + expiryInfo;
    }
}