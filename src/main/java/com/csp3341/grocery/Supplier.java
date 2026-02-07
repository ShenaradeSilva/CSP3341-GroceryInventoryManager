/**
 * This file defines a Supplier class for the grocery inventory management system.
 * It represents vendors or companies that provide products to the grocery store,
 * with unique identification, contact information, and proper equality semantics
 * for use in collections.
 */
package com.csp3341.grocery;

import java.util.Objects;

/**
 * Represents a supplier in the inventory system.
 * Implements equals() and hashCode() based on supplier ID.
 */
public class Supplier {
    // Immutable field - supplier ID should not change
    private final int supplierId;

    // Mutable fields - can be updated
    private String supplierName;
    private String contact;

    /**
     * Constructor with validation for all arguments.
     */
    public Supplier(int supplierId, String supplierName, String contact) {
        validateConstructorArgs(supplierId, supplierName, contact);
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
    }

    // Private Validation
    /**
     * Validates constructor arguments for data integrity.
     */
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

    // String Representation
    /**
     * Returns formatted supplier information.
     * Format: "ID | Name | Contact"
     */
    @Override
    public String toString() {
        return String.format("%d | %s | %s", supplierId, supplierName, contact);
    }

    // Equality and Hashing
    /**
     * Suppliers are equal if they have the same supplier ID.
     * ID is assumed to be unique across all suppliers.
     */
    @Override
    public boolean equals(Object obj) {
        // Same object reference
        if (this == obj) return true;

        // Null or different class
        if (obj == null || getClass() != obj.getClass()) return false;

        // Compare by supplier ID
        Supplier other = (Supplier) obj;
        return supplierId == other.supplierId;
    }

    /**
     * Hash code based on supplier ID for consistency with equals().
     * Required for proper functioning in hash-based collections.
     */
    @Override
    public int hashCode() {
        return Objects.hash(supplierId);
    }
}