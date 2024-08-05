package com.example.businessbuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BusinessBuddy.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_ITEMS = "Items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM_CODE = "item_code";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY="quantity";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEM_CODE + " TEXT, " +
                    COLUMN_ITEM_NAME + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_QUANTITY + " INTEGER" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + COLUMN_QUANTITY + " INTEGER DEFAULT 0");
        }
    }
}
