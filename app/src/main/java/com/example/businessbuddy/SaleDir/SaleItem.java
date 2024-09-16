package com.example.businessbuddy.SaleDir;

public class SaleItem {
    private String itemCode;
    private int quantity;
    private double totalAmount;

    public SaleItem(String itemCode, int quantity, double totalAmount) {
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

