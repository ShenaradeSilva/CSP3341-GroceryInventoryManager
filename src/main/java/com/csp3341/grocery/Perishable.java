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
            LocalDate parsedDate = LocalDate.parse(expiryDate, DateTimeFormatter.ISO_DATE);
            if (parsedDate.isBefore(LocalDate.now())) {
                System.out.println("Warning! Expiry Date is in the past! Product may be Expired.");
            }
            this.expiryDate = parsedDate;
        }
        catch (DateTimeParseException e) {
            System.out.println("Error! Invalid Date Format! Use the format YYYY-MM-DD");
            throw new IllegalArgumentException("Invalid date format: " + expiryDate);
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
        String expiryStatus = "";
        if (isExpired()) {
            expiryStatus = " [Expired " + expiryDate + "]";
        } else {
            expiryStatus = " | Expiry: " + expiryDate;
        }
        return super.toString() + expiryStatus;
    }
}
