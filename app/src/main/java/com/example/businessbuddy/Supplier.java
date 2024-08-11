package com.example.businessbuddy;

public class Supplier {
    private long id;
    private String itemCode;
    private String supplierName;
    private String contactNumber;
    private String paymentDate;
    private String paymentType;
    private Double totalBillAmount;
    private Integer suppliedQuantities;


    public Supplier(long id, String itemCode, String supplierName, String contactNumber, String paymentDate,String paymentType,Integer suppliedQuantities, Double totalBillAmount) {
        this.id = id;
        this.itemCode = itemCode;
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
       this.paymentDate= paymentDate;
       this.paymentType=paymentType;
       this.totalBillAmount=totalBillAmount;
       this.suppliedQuantities=suppliedQuantities;
    }

    // Getters and setters
}
