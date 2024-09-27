package com.example.businessbuddy;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CustomerDAO {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CustomerDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert a new customer or update if exists
    @SuppressLint("Range")
    public Integer insertCustomer(String contactNo, String paymentType, double totalBill, Integer userId) {
        // Validate inputs
        if (contactNo == null || paymentType == null || userId == null) {
            Log.e("CustomerDAO", "Invalid input: contactNo, paymentType, and userId cannot be null.");
            return null; // Handle null values appropriately
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUSTOMER_CONTACT_NO, contactNo);
        values.put(DatabaseHelper.COLUMN_PAYMENT_TYPE, paymentType);
        values.put(DatabaseHelper.COLUMN_TOTAL_BILL, totalBill);
        values.put(DatabaseHelper.COLUMN_CUSTOMER_USER_ID, userId);

        Log.d("Customer data by druman", contactNo + " , " + paymentType + " , " + totalBill);

        Cursor cursor = null;
        Integer customerId = null;

        try {
            // Check if the customer already exists
            cursor = db.query(DatabaseHelper.TABLE_CUSTOMER,
                    new String[]{DatabaseHelper.COLUMN_CUSTOMER_ID},
                    DatabaseHelper.COLUMN_CUSTOMER_CONTACT_NO + "=?",
                    new String[]{contactNo}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Update existing customer
                customerId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CUSTOMER_ID));
                int rowsAffected = db.update(DatabaseHelper.TABLE_CUSTOMER, values,
                        DatabaseHelper.COLUMN_CUSTOMER_ID + "=?", new String[]{String.valueOf(customerId)});

                if (rowsAffected > 0) {
                    Log.d("CustomerDAO", "Customer updated successfully: " + customerId);
                } else {
                    Log.e("CustomerDAO", "Failed to update customer: " + customerId);
                }
            } else {
                // Insert new customer
                customerId = Math.toIntExact((db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values)));
                if (customerId != -1) {
                    Log.d("CustomerDAO", "Customer inserted successfully: " + customerId);
                } else {
                    Log.e("CustomerDAO", "Failed to insert customer.");
                }
            }
        } catch (Exception e) {
            Log.e("CustomerDAO", "Error while inserting/updating customer: " + e.getMessage());
        } finally {
            // Ensure cursor is closed
            if (cursor != null) {
                cursor.close();
            }
        }

        return customerId;
    }
}
