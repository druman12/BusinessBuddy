package com.example.businessbuddy.SaleDir;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessbuddy.DatabaseHelper;

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

        return database.insert("sales", null, values);
    }


}

