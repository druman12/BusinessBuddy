package com.example.businessbuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BusinessBuddy.db";
    private static final int DATABASE_VERSION = 3;  // Incremented version for new table
    public static final String TABLE_ITEMS = "Items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM_CODE = "item_code";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";

    public static final String TABLE_SUPPLIERS = "SupplierTable";
    public static final String COLUMN_SUPPLIER_ID = "_supplier_id";
    public static final String COLUMN_SUPPLIER_ITEM_CODE = "item_code";  // Foreign key
    public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_ADDRESS = "address";

    private static final String TABLE_ITEMS_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEM_CODE + " TEXT, " +
                    COLUMN_ITEM_NAME + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_QUANTITY + " INTEGER" +
                    ");";

    private static final String TABLE_SUPPLIERS_CREATE =
            "CREATE TABLE " + TABLE_SUPPLIERS + " (" +
                    COLUMN_SUPPLIER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SUPPLIER_ITEM_CODE + " TEXT, " +
                    COLUMN_SUPPLIER_NAME + " TEXT, " +
                    COLUMN_CONTACT_NUMBER + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_SUPPLIER_ITEM_CODE + ") REFERENCES " + TABLE_ITEMS + "(" + COLUMN_ITEM_CODE + ")" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ITEMS_CREATE);
        db.execSQL(TABLE_SUPPLIERS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + COLUMN_QUANTITY + " INTEGER DEFAULT 0");
        }
        if (oldVersion < 3) {
            db.execSQL(TABLE_SUPPLIERS_CREATE);
        }
    }

    public ArrayList<HashMap<String, String>> getAllItemsAndSuppliers() {
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "i." + COLUMN_ITEM_NAME + ", " +
                "s." + COLUMN_SUPPLIER_NAME + ", " +
                "i." + COLUMN_ITEM_CODE + ", " +
                "i." + COLUMN_PRICE + ", " +
                "i." + COLUMN_CATEGORY + ", " +
                "i." + COLUMN_QUANTITY +
                " FROM " + TABLE_ITEMS + " i" +
                " LEFT JOIN " + TABLE_SUPPLIERS + " s" +
                " ON i." + COLUMN_ITEM_CODE + " = s." + COLUMN_SUPPLIER_ITEM_CODE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> item = new HashMap<>();
                item.put("ItemName", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME)));
                item.put("SupplierName", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPLIER_NAME)));
                item.put("ItemCode", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_CODE)));
                item.put("Price", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                item.put("ItemCategory", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                item.put("Quantity", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }
}