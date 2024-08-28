package com.example.businessbuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version and Name
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "BusinessBuddy.db";

    // Table names
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_SALES = "sales";
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String TABLE_REGISTER="register";

    // Item Table Columns
    public static final String COLUMN_ITEMCODE = "itemcode";
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_CATEGORY = "category";
    public static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_QUANTITY = "quantity";

    // Customer Table Columns
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_CUSTOMER_CONTACT_NO = "contact_no";
    public static final String COLUMN_PAYMENT_TYPE = "payment_type";
    public static final String COLUMN_TOTAL_BILL = "total_bill";

    // Sales Table Columns
    public static final String COLUMN_SALE_ID = "sale_id";
    public static final String COLUMN_SALE_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_SALE_ITEMCODE = "itemcode";
    public static final String COLUMN_SALE_QUANTITY = "quantity";
    public static final String COLUMN_TOTAL_PRICE = "total_price";

    // Supplier Table Columns
    public static final String COLUMN_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_SUPPLIER_ITEMCODE = "itemcode";
    public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_SUPPLIER_CONTACT_NO = "contact_no";
    public static final String COLUMN_PAYMENT_DATE = "payment_date";
    public static final String COLUMN_SUPPLIER_PAYMENT_TYPE = "payment_type";
    public static final String COLUMN_SUPPLIER_TOTAL_QUANTITY = "total_quantity";
    public static final String COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT = "total_bill_amount";


    //register table columns
    public static final String COLUMN_REGISTER_ID="user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_MOBILE_NO = "mobile_no";



    // SQL Create Table Statements
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE " + TABLE_ITEM + " ("
            + COLUMN_ITEMCODE + " TEXT PRIMARY KEY, "
            + COLUMN_ITEM_NAME + " TEXT NOT NULL, "
            + COLUMN_ITEM_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_ITEM_PRICE + " REAL NOT NULL, "
            + COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL);";

    private static final String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + " ("
            + COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CUSTOMER_CONTACT_NO + " TEXT NOT NULL, "
            + COLUMN_PAYMENT_TYPE + " TEXT CHECK (" + COLUMN_PAYMENT_TYPE + " IN ('cash', 'online')) NOT NULL, "
            + COLUMN_TOTAL_BILL + " REAL NOT NULL);";

    private static final String CREATE_TABLE_SALES = "CREATE TABLE " + TABLE_SALES + " ("
            + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SALE_CUSTOMER_ID + " INTEGER NOT NULL, "
            + COLUMN_SALE_ITEMCODE + " TEXT NOT NULL, "
            + COLUMN_SALE_QUANTITY + " INTEGER NOT NULL CHECK (" + COLUMN_SALE_QUANTITY + " > 0), "
            + COLUMN_TOTAL_PRICE + " REAL NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_SALE_CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMER + "(" + COLUMN_CUSTOMER_ID + "), "
            + "FOREIGN KEY(" + COLUMN_SALE_ITEMCODE + ") REFERENCES " + TABLE_ITEM + "(" + COLUMN_ITEMCODE + "));";

    private static final String CREATE_TABLE_SUPPLIER = "CREATE TABLE " + TABLE_SUPPLIER + " ("
            + COLUMN_SUPPLIER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SUPPLIER_ITEMCODE + " TEXT NOT NULL, "
            + COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
            + COLUMN_SUPPLIER_CONTACT_NO + " TEXT NOT NULL, "
            + COLUMN_PAYMENT_DATE + " DATE NOT NULL, "
            + COLUMN_SUPPLIER_PAYMENT_TYPE + " TEXT CHECK (" + COLUMN_SUPPLIER_PAYMENT_TYPE + " IN ('cash', 'debit')) NOT NULL, "
            + COLUMN_SUPPLIER_TOTAL_QUANTITY + " INTEGER NOT NULL CHECK (" + COLUMN_SUPPLIER_TOTAL_QUANTITY + " > 0), "
            + COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT + " REAL NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_SUPPLIER_ITEMCODE + ") REFERENCES " + TABLE_ITEM + "(" + COLUMN_ITEMCODE + "));";


    private static final String CREATE_TABLE_REGISTER="CREATE TABLE " + TABLE_REGISTER+ "("
            + COLUMN_REGISTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_MOBILE_NO + " TEXT"
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the tables
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_SALES);
        db.execSQL(CREATE_TABLE_SUPPLIER);
        db.execSQL(CREATE_TABLE_REGISTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_REGISTER);
        // Create tables again
        onCreate(db);
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getAllItemsAndSuppliers() {
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to join Items and Suppliers tables
        String query = "SELECT i." + COLUMN_ITEMCODE + ", "
                + "i." + COLUMN_ITEM_NAME + ", "
                + "i." + COLUMN_ITEM_CATEGORY + ", "
                + "i." + COLUMN_ITEM_PRICE + ", "
                + "i." + COLUMN_ITEM_QUANTITY + ", "
                + "s." + COLUMN_SUPPLIER_NAME + " "
                + "FROM " + TABLE_ITEM + " i "
                + "LEFT JOIN " + TABLE_SUPPLIER + " s "
                + "ON i." + COLUMN_ITEMCODE + " = s." + COLUMN_SUPPLIER_ITEMCODE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> item = new HashMap<>();
                item.put("ItemCode", String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_ITEMCODE))));
                item.put("ItemName", cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                item.put("ItemCategory", cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY)));
                item.put("Price", String.valueOf(cursor.getDouble(cursor.getColumnIndex(COLUMN_ITEM_PRICE))));
                item.put("Quantity", String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY))));
                item.put("SupplierName", cursor.getString(cursor.getColumnIndex(COLUMN_SUPPLIER_NAME)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return itemList;
    }

}
