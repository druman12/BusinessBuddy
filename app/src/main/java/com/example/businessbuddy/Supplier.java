package com.example.businessbuddy;

public class Supplier {
    private long id;
    private String itemCode;
    private String supplierName;
    private String contactNumber;
    private String address;

    public Supplier(long id, String itemCode, String supplierName, String contactNumber, String address) {
        this.id = id;
        this.itemCode = itemCode;
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // Getters and setters
}
