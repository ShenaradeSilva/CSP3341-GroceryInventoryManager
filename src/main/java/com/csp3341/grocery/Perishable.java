package com.csp3341.grocery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Perishable extends Product {
    private LocalDate expiryDate;

    public Perishable(int id, String name, double price, int quantity, Category category, Supplier supplier, String expiryDate) {
        super(id, name, price, quantity, category, supplier);
        setExpiryDate(expiryDate);
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        try {
            this.expiryDate = LocalDate.parse(expiryDate, DateTimeFormatter.ISO_DATE);
        }
        catch (DateTimeParseException e) {
            System.out.println("Invalid Date Format! Use the format YYYY-MM-DD");
            this.expiryDate = LocalDate.now();
        }
    }

    @Override
    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return expiryDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return super.toString() + " | Expiry: " + expiryDate;
    }
}
