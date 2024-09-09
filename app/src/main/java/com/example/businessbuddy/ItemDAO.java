package com.example.businessbuddy;

import static com.example.businessbuddy.DatabaseHelper.TABLE_ITEM;
import static com.example.businessbuddy.DatabaseHelper.TABLE_REGISTER;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    static SQLiteDatabase database;
    private static DatabaseHelper dbHelper;

    public ItemDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }
    //add Item into db
    public void addItem(String itemCode, String itemName, String category, int quantity, double price) {
        // Check if the item already exists
        Cursor cursor = database.query(TABLE_ITEM, null, DatabaseHelper.COLUMN_ITEMCODE + "=?", new String[]{itemCode}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Item exists, update the quantity
            @SuppressLint("Range") int existingQuantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_QUANTITY));
            int newQuantity = existingQuantity + quantity;

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
            values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY, category);
            values.put(DatabaseHelper.COLUMN_ITEM_PRICE, price);
            values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, newQuantity);

            database.update(TABLE_ITEM, values, DatabaseHelper.COLUMN_ITEMCODE + "=?", new String[]{itemCode});
        } else {
            // Item does not exist, insert a new entry
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ITEMCODE, itemCode);
            values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
            values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY, category);
            values.put(DatabaseHelper.COLUMN_ITEM_PRICE, price);
            values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, quantity);

            database.insert(TABLE_ITEM, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Add new supplier
    public void addSupplier(String itemCode, String supplierName, String contactNumber, String paymentDate, String paymentType, int totalQuantity, double totalBillAmount) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ITEMCODE, itemCode);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_NO, contactNumber);
        values.put(DatabaseHelper.COLUMN_PAYMENT_DATE, paymentDate);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_PAYMENT_TYPE, paymentType);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_QUANTITY, totalQuantity);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT, totalBillAmount);

        database.insert(DatabaseHelper.TABLE_SUPPLIER, null, values);


        updateItemQuantityFromSupplier(itemCode, totalQuantity);
    }



    // Update item quantity when supplier adds more items
    public void updateItemQuantityFromSupplier(String itemCode, int additionalQuantity) {
        database.execSQL("UPDATE " + TABLE_ITEM + " SET " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " = " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " + ? WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?",
                new Object[]{additionalQuantity, itemCode});
    }


    @SuppressLint("Range")
    public double getItemPrice(String itemCode) {
        double price = 0.0;

        // Query to fetch the price from the item table
        String query = "SELECT " + DatabaseHelper.COLUMN_ITEM_PRICE + " FROM " + TABLE_ITEM +
                " WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{itemCode});

        if (cursor != null && cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_PRICE));
            cursor.close();
        }

        return price;
    }

    public void updateItemQuantity(String itemCode, int quantitySold) {
        // First, get the current quantity of the item
        int currentQuantity = getItemQuantity(itemCode);

        // Calculate the new quantity
        int newQuantity = currentQuantity - quantitySold;

        // Update the item table with the new quantity
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, newQuantity);

        database.update(TABLE_ITEM, values, DatabaseHelper.COLUMN_ITEMCODE + " = ?", new String[]{itemCode});
    }
    @SuppressLint("Range")
    private int getItemQuantity(String itemCode) {
        int quantity = 0;
        String query = "SELECT " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " FROM " + TABLE_ITEM +
                " WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{itemCode});

        if (cursor != null && cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_QUANTITY));
            cursor.close();
        }

        return quantity;
    }




    public Boolean checkusernamepassword(String email,String password)
    {
//        Cursor cursor = database.query(DatabaseHelper.TABLE_SUPPLIER, null, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        Cursor cursor = database.rawQuery("SELECT email,password FROM "+ TABLE_REGISTER+" WHERE email = ? AND password = ?", new String[]{email, password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
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