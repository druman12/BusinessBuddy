package com.example.businessbuddy;

import static com.example.businessbuddy.DatabaseHelper.TABLE_SALES;
import static com.example.businessbuddy.ItemDAO.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    private static SQLiteDatabase db;
    private static DatabaseHelper dbHelper;

    public SaleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert a new sale
    public long insertSale(long customerId, SaleItem saleItem) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SALE_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.COLUMN_SALE_ITEMCODE, saleItem.getItemCode());
        values.put(DatabaseHelper.COLUMN_SALE_QUANTITY, saleItem.getQuantity());
        values.put(DatabaseHelper.COLUMN_TOTAL_PRICE, saleItem.getAmount());

        return db.insert(TABLE_SALES, null, values);
    }

    public List<Sale> getAllSales() {
        List<Sale> salesList = new ArrayList<>();
         db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SALES + " INNER JOIN customer ON " + TABLE_SALES + ".customer_id = customer.customer_id";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int saleId = cursor.getInt(cursor.getColumnIndex("sale_id"));
                @SuppressLint("Range") int customerId = cursor.getInt(cursor.getColumnIndex("customer_id"));
                @SuppressLint("Range") String contactNumber = cursor.getString(cursor.getColumnIndex("contact_no"));
                @SuppressLint("Range") double totalBill = cursor.getDouble(cursor.getColumnIndex("total_bill"));
                @SuppressLint("Range") String paymentType = cursor.getString(cursor.getColumnIndex("payment_type"));

                Sale sale = new Sale(saleId,customerId, contactNumber, totalBill, paymentType);
                salesList.add(sale);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return salesList;
    }

    // Method to get items for a specific sale
    public static List<SaleItem> getItemsForSale(int saleId) {
        List<SaleItem> saleItemsList = new ArrayList<>();
         db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SALES + " WHERE sale_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(saleId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String itemCode = cursor.getString(cursor.getColumnIndex("itemcode"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("total_price"));

                SaleItem saleItem = new SaleItem(itemCode, quantity, amount);
                saleItemsList.add(saleItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return saleItemsList;
    }
    private String getCustomerContact(int customerId) {
        String contactNumber = "";
        Cursor cursor = null;

        try {
            String query = "SELECT contact_no FROM customer WHERE customer_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(customerId)});

            if (cursor.moveToFirst()) {
                contactNumber = cursor.getString(cursor.getColumnIndexOrThrow("contact_no"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contactNumber;
    }

    private String getPaymentType(int customerId) {
        String paymentType = "";
        Cursor cursor = null;

        try {
            String query = "SELECT payment_type FROM customer WHERE customer_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(customerId)});

            if (cursor.moveToFirst()) {
                paymentType = cursor.getString(cursor.getColumnIndexOrThrow("payment_type"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return paymentType;
    }
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }


}
