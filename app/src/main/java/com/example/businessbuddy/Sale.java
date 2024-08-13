package com.example.businessbuddy;

public class Sale {
    private int saleId;
    private int customerId;
    private String contactNumber;
    private double totalBill;
    private String paymentType;

    public Sale(int saleId, int customerId, String contactNumber, double totalBill, String paymentType) {
        this.saleId=saleId;
        this.customerId=customerId;
        this.contactNumber=contactNumber;
        this.totalBill=totalBill;
        this.paymentType=paymentType;
    }

    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}


