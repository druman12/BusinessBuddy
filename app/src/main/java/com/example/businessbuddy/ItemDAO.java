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
    public void addItem(String itemCode,String itemName , String Catagory , int quntity , double price){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEMCODE, itemCode);
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY, Catagory);
        values.put(DatabaseHelper.COLUMN_ITEM_PRICE, price);
        values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, quntity);


        database.insert(TABLE_ITEM, null, values);

    }
    // Add a new supplier
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

        // Update item quantity in the item table
        updateItemQuantityFromSupplier(itemCode, totalQuantity);
    }



    // Update item information
    public int updateItem(String itemCode, String itemName, String category, double price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_ITEM_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_ITEM_PRICE, price);
        values.put(DatabaseHelper.COLUMN_ITEM_QUANTITY, quantity);

        // Update the item based on itemCode
        return database.update(TABLE_ITEM, values, DatabaseHelper.COLUMN_ITEMCODE + " = ?", new String[]{itemCode});
    }


    // Update supplier information
    public int updateSupplier(long id, String itemCode, String supplierName, String contactNumber, String paymentDate, String paymentType, int totalQuantity, double totalBillAmount) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ITEMCODE, itemCode);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_NO, contactNumber);
        values.put(DatabaseHelper.COLUMN_PAYMENT_DATE, paymentDate);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_PAYMENT_TYPE, paymentType);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_QUANTITY, totalQuantity);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT, totalBillAmount);

        int rowsUpdated = database.update(DatabaseHelper.TABLE_SUPPLIER, values, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)});

        // Update item quantity in the item table
        updateItemQuantityFromSupplier(itemCode, totalQuantity);

        return rowsUpdated;
    }

    // Update item quantity when supplier adds more items
    public void updateItemQuantityFromSupplier(String itemCode, int additionalQuantity) {
        database.execSQL("UPDATE " + TABLE_ITEM + " SET " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " = " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " + ? WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?",
                new Object[]{additionalQuantity, itemCode});
    }

    // Update item quantity when customer buys items
    public void updateItemQuantityFromSale(String itemCode, int quantitySold) {
        database.execSQL("UPDATE " + TABLE_ITEM + " SET " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " = " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " - ? WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?",
                new Object[]{quantitySold, itemCode});
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

    // Record a sale transaction
    public void recordSale(int customerId, String itemCode, int quantitySold, double totalPrice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SALE_CUSTOMER_ID, customerId);
        values.put(DatabaseHelper.COLUMN_SALE_ITEMCODE, itemCode);
        values.put(DatabaseHelper.COLUMN_SALE_QUANTITY, quantitySold);
        values.put(DatabaseHelper.COLUMN_TOTAL_PRICE, totalPrice);

        database.insert(DatabaseHelper.TABLE_SALES, null, values);

        // Update item quantity in the item table
        updateItemQuantityFromSale(itemCode, quantitySold);
    }

    // Delete an item
    public void deleteItem(long id) {
        database.delete(TABLE_ITEM, DatabaseHelper.COLUMN_ITEMCODE + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete a supplier
    public void deleteSupplier(long id) {
        database.delete(DatabaseHelper.TABLE_SUPPLIER, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)});
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

    // Get a single supplier by ID
    public Supplier getSupplier(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_SUPPLIER, null, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Supplier supplier = new Supplier(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ITEMCODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_NO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAYMENT_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_PAYMENT_TYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_QUANTITY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT))
            );
            cursor.close();
            return supplier;
        }
        return null;
    }

    // Get all items
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.query(TABLE_ITEM, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Item item = new Item(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEMCODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_QUANTITY))
                );
                items.add(item);
            }
            cursor.close();
        }
        return items;
    }

    // Get all suppliers
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_SUPPLIER, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Supplier supplier = new Supplier(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ITEMCODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_CONTACT_NO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAYMENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_PAYMENT_TYPE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_TOTAL_BILL_AMOUNT))
                );
                suppliers.add(supplier);
            }
            cursor.close();
        }
        return suppliers;
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

    public static String getItemName(String itemCode) {
         database = dbHelper.getReadableDatabase();
        String query = "SELECT name FROM " + TABLE_ITEM  + " WHERE itemcode = ?";
        Cursor cursor = database.rawQuery(query, new String[]{itemCode});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("name"));
            cursor.close();
            return itemName;
        }
        cursor.close();
        return null;
    }

    public static double getPrice(String itemCode) {
        double price = 0.0;
        Cursor cursor = null;

        try {
            String query = "SELECT price FROM item WHERE itemcode = ?";
            cursor = database.rawQuery(query, new String[]{itemCode});

            if (cursor.moveToFirst()) {
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return price;
    }

    // Close the database connection when done
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }

    }
}