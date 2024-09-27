package com.example.businessbuddy;

import static com.example.businessbuddy.DatabaseHelper.COLUMN_SUPPLIER_ID;
import static com.example.businessbuddy.DatabaseHelper.TABLE_ITEM;
import static com.example.businessbuddy.DatabaseHelper.TABLE_REGISTER;
import static com.example.businessbuddy.DatabaseHelper.TABLE_SUPPLIER;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemDAO {

    static SQLiteDatabase database;
    private static DatabaseHelper dbHelper;

    public ItemDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Add Item into db
    public void addItem(int userId, String itemCode, String itemName, String category, int quantity, double price, String supplierName) {
        Log.d("add Item come data", price + " , " + quantity);

        // Get or add the supplier and retrieve the supplier ID
        int supplierId =getSupplierId(supplierName);

        Cursor cursor = database.query(TABLE_ITEM, null, DatabaseHelper.COLUMN_ITEMCODE + "=? AND " + DatabaseHelper.COLUMN_REGISTER_ID + "=?",
                new String[]{itemCode, String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Item exists, update the quantity
            @SuppressLint("Range") int existingQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_QUANTITY));
            int newQuantity = existingQuantity + quantity;

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
            values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY, category);
            values.put(DatabaseHelper.COLUMN_ITEM_PRICE, price);
            values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, newQuantity);
            values.put(DatabaseHelper.COLUMN_ITEM_SUPPLIER_ID, supplierId); // Set the supplier ID

            database.update(TABLE_ITEM, values, DatabaseHelper.COLUMN_ITEMCODE + "=? AND " + DatabaseHelper.COLUMN_REGISTER_ID + "=?",
                    new String[]{itemCode, String.valueOf(userId)});
        } else {
            // Item does not exist, insert a new entry
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_REGISTER_ID, userId);
            values.put(DatabaseHelper.COLUMN_ITEMCODE, itemCode);
            values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
            values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY, category);
            values.put(DatabaseHelper.COLUMN_ITEM_PRICE, price);
            values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, quantity);
            values.put(DatabaseHelper.COLUMN_ITEM_SUPPLIER_ID, supplierId); // Set the supplier ID

            database.insert(TABLE_ITEM, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
    }
    private int getSupplierId(String supplierName) {
        // Check if the supplier already exists
        Cursor cursor = database.query(TABLE_SUPPLIER, new String[]{COLUMN_SUPPLIER_ID}, "supplier_name=?",
                new String[]{supplierName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int supplierId = cursor.getInt(cursor.getColumnIndex(COLUMN_SUPPLIER_ID));
            cursor.close();
            return supplierId;
        }
        return -1;

    }

    // Add new supplier
    public void addSupplier(int userId, String itemCode, String supplierName, String contactNumber, String paymentDate, String paymentType, int totalQuantity, double totalBillAmount) {
        Log.d("come from entry to...", supplierName + " " + contactNumber + " " + paymentDate);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_REGISTER_ID, userId);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ITEMCODE, itemCode);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_NO, contactNumber);
        values.put(DatabaseHelper.COLUMN_PAYMENT_DATE, paymentDate);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_PAYMENT_TYPE, paymentType);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_QUANTITY, totalQuantity);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT, totalBillAmount);

        // Insert supplier information without updating the item quantity
        database.insert(TABLE_SUPPLIER, null, values);
    }

    @SuppressLint("Range")
    public double getItemPrice(int userId, String itemCode) {
        double price = 0.0;

        // Query to fetch the price from the item table for the specific user
        String query = "SELECT " + DatabaseHelper.COLUMN_ITEM_PRICE + " FROM " + TABLE_ITEM +
                " WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ? AND " + DatabaseHelper.COLUMN_REGISTER_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{itemCode, String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_PRICE));
            cursor.close();
        }

        return price;
    }

    public void updateItemQuantity(int userId, String itemCode, int quantitySold) {
        // First, get the current quantity of the item for the specific user
        int currentQuantity = getItemQuantity(userId, itemCode);

        // Calculate the new quantity
        int newQuantity = currentQuantity - quantitySold;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, newQuantity);

        database.update(TABLE_ITEM, values, DatabaseHelper.COLUMN_ITEMCODE + " = ? AND " + DatabaseHelper.COLUMN_REGISTER_ID + " = ?",
                new String[]{itemCode, String.valueOf(userId)});
    }

    @SuppressLint("Range")
    public static int getItemQuantity(int userId, String itemCode) {
        int quantity = 0;
        String query = "SELECT " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " FROM " + TABLE_ITEM +
                " WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ? AND " + DatabaseHelper.COLUMN_REGISTER_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{itemCode, String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_QUANTITY));
            cursor.close();
        }

        return quantity;
    }

    public Boolean checkUsernamePassword(String email, String password) {
        Cursor cursor = database.rawQuery("SELECT email,password FROM " + TABLE_REGISTER +
                " WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void insertUser(String name, String email, String password, String mobileNo) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_EMAIL, email);
        contentValues.put(DatabaseHelper.COLUMN_PASSWORD, password);
        contentValues.put(DatabaseHelper.COLUMN_MOBILE_NO, mobileNo);

        database.insert(TABLE_REGISTER, null, contentValues);
    }
}
