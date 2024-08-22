package com.example.businessbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SaleDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public SaleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insertSale(int customerId, SaleItem saleItem) {
        ContentValues values = new ContentValues();
        values.put("customer_id", customerId);
        values.put("itemcode", saleItem.getItemCode());
        values.put("quantity", saleItem.getQuantity());
        values.put("total_price", saleItem.getTotalAmount());

        return database.insert("sale_details", null, values);
    }


}

