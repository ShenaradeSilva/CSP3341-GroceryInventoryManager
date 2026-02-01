package com.csp3341.grocery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a perishable product with an expiry date.
 */
public class Perishable extends Product {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private LocalDate expiryDate;

    public Perishable(int id, String name, double price, int quantity,
                      Category category, Supplier supplier, String expiryDate) {
        super(id, name, price, quantity, category, supplier);
        setExpiryDate(expiryDate);
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Expiry date cannot be null or empty");
        }

        try {
            LocalDate parsedDate = LocalDate.parse(expiryDate.trim(), DATE_FORMATTER);

            if (parsedDate.isBefore(LocalDate.now())) {
                System.out.println("Warning: Expiry date " + parsedDate + " is in the past. Product may be expired.");
            }

            this.expiryDate = parsedDate;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date format: '" + expiryDate + "'. Expected format: YYYY-MM-DD", e);
        }
    }

    @Override
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        String baseString = super.toString();
        String expiryInfo;

        if (isExpired()) {
            expiryInfo = " [Expired " + expiryDate + "]";
        } else {
            expiryInfo = " | Expiry: " + expiryDate;
        }

        return baseString + expiryInfo;
    }
}