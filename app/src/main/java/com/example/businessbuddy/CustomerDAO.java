package com.example.businessbuddy;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CustomerDAO {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CustomerDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert a new customer or update if exists
    @SuppressLint("Range")
    public Integer insertCustomer(String contactNo, String paymentType, double totalBill) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUSTOMER_CONTACT_NO, contactNo);
        values.put(DatabaseHelper.COLUMN_PAYMENT_TYPE, paymentType);
        values.put(DatabaseHelper.COLUMN_TOTAL_BILL, totalBill);

        // Check if the customer already exists
        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER,
                new String[]{DatabaseHelper.COLUMN_CUSTOMER_ID},
                DatabaseHelper.COLUMN_CUSTOMER_CONTACT_NO + "=?",
                new String[]{contactNo}, null, null, null);

        Integer customerId;

        if (cursor != null && cursor.moveToFirst()) {
            // Update existing customer
            customerId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CUSTOMER_ID));
            db.update(DatabaseHelper.TABLE_CUSTOMER, values, DatabaseHelper.COLUMN_CUSTOMER_ID + "=?",
                    new String[]{String.valueOf(customerId)});
        } else {
            // Insert new customer
            customerId = Math.toIntExact((db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values)));
        }

        if (cursor != null) {
            cursor.close();
        }

        return customerId;
    }

    // Other potential methods like getCustomerById, deleteCustomer, etc.
}
