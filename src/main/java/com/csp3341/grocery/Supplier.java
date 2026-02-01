package com.csp3341.grocery;

import java.util.Objects;

/**
 * Represents a supplier in the inventory system.
 * Implements equals() and hashCode() based on supplier ID.
 */
public class Supplier {
    private final int supplierId;
    private String supplierName;
    private String contact;

    public Supplier(int supplierId, String supplierName, String contact) {
        validateConstructorArgs(supplierId, supplierName, contact);
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
    }

    private void validateConstructorArgs(int supplierId, String supplierName, String contact) {
        if (supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID must be positive");
        }
        if (supplierName == null || supplierName.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name cannot be null or empty");
        }
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact information cannot be null or empty");
        }
    }

    // Getters
    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getContact() {
        return contact;
    }

    // Setters with validation
    public void setSupplierName(String supplierName) {
        if (supplierName == null || supplierName.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name cannot be null or empty");
        }
        this.supplierName = supplierName;
    }

    public void setContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact information cannot be null or empty");
        }
        this.contact = contact;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s", supplierId, supplierName, contact);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Supplier other = (Supplier) obj;
        return supplierId == other.supplierId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierId);
    }
}