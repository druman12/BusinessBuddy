package com.example.businessbuddy;

import android.database.Cursor;

public class SaleItem {
    private String itemCode;
    private int quantity;
    private double price;

    public SaleItem(String itemCode, int quantity, double amount) {
        this.itemCode=itemCode;
        this.quantity=quantity;

    }

    // Getters and Setters
    public String getItemCode() {
        return itemCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getAmount() {
        return price * quantity;
    }
}