package com.csp3341.grocery;

public class Supplier {
    private final int supplierId;
    private String supplierName;
    private String contact;

    public Supplier(int supplierId, String supplierName, String contact) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getContact() {
        return contact;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return supplierId + " | " + supplierName + " | " + contact;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Supplier other = (Supplier) obj;
        return supplierId == other.supplierId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(supplierId);
    }
}
