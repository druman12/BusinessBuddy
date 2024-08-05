package com.example.businessbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ItemDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ItemDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void addItem(String itemCode, String itemName, String category, double price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_CODE, itemCode);
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);

        database.insert(DatabaseHelper.TABLE_ITEMS, null, values);
    }

    // Add other CRUD operations if needed (update, delete, fetch)
}
