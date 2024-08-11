package com.example.businessbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

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


        database.insert(DatabaseHelper.TABLE_ITEM, null, values);

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
        return database.update(DatabaseHelper.TABLE_ITEM, values, DatabaseHelper.COLUMN_ITEMCODE + " = ?", new String[]{itemCode});
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
        database.execSQL("UPDATE " + DatabaseHelper.TABLE_ITEM + " SET " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " = " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " + ? WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?",
                new Object[]{additionalQuantity, itemCode});
    }

    // Update item quantity when customer buys items
    public void updateItemQuantityFromSale(String itemCode, int quantitySold) {
        database.execSQL("UPDATE " + DatabaseHelper.TABLE_ITEM + " SET " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " = " + DatabaseHelper.COLUMN_ITEM_QUANTITY + " - ? WHERE " + DatabaseHelper.COLUMN_ITEMCODE + " = ?",
                new Object[]{quantitySold, itemCode});
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
        database.delete(DatabaseHelper.TABLE_ITEM, DatabaseHelper.COLUMN_ITEMCODE + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete a supplier
    public void deleteSupplier(long id) {
        database.delete(DatabaseHelper.TABLE_SUPPLIER, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Get a single item by ID
    public Item getItem(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEM, null, DatabaseHelper.COLUMN_ITEMCODE + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Item item = new Item(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEMCODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CATEGORY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_QUANTITY))
            );
            cursor.close();
            return item;
        }
        return null;
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
        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEM, null, null, null, null, null, null);
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

}
