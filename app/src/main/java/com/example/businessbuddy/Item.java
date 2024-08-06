package com.example.businessbuddy;

public class Item {
    private long id;
    private String itemCode;
    private String itemName;
    private String category;
    private double price;
    private int quantity;

    public Item(long id, String itemCode, String itemName, String category, double price, int quantity) {
        this.id = id;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
}
