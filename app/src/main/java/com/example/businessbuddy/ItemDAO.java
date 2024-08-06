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

    public void addItem(String itemCode, String itemName, String category, double price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_CODE, itemCode);
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);

        database.insert(DatabaseHelper.TABLE_ITEMS, null, values);
    }

    public void addSupplier(String itemCode, String supplierName, String contactNumber, String address) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ITEM_CODE, itemCode);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(DatabaseHelper.COLUMN_CONTACT_NUMBER, contactNumber);
        values.put(DatabaseHelper.COLUMN_ADDRESS, address);

        database.insert(DatabaseHelper.TABLE_SUPPLIERS, null, values);
    }

    public int updateItem(long id, String itemCode, String itemName, String category, double price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM_CODE, itemCode);
        values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);

        return database.update(DatabaseHelper.TABLE_ITEMS, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int updateSupplier(long id, String itemCode, String supplierName, String contactNumber, String address) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUPPLIER_ITEM_CODE, itemCode);
        values.put(DatabaseHelper.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(DatabaseHelper.COLUMN_CONTACT_NUMBER, contactNumber);
        values.put(DatabaseHelper.COLUMN_ADDRESS, address);

        return database.update(DatabaseHelper.TABLE_SUPPLIERS, values, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(long id) {
        database.delete(DatabaseHelper.TABLE_ITEMS, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteSupplier(long id) {
        database.delete(DatabaseHelper.TABLE_SUPPLIERS, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Item getItem(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, null, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Item item = new Item(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY))
            );
            cursor.close();
            return item;
        }
        return null;
    }

    public Supplier getSupplier(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_SUPPLIERS, null, DatabaseHelper.COLUMN_SUPPLIER_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Supplier supplier = new Supplier(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ITEM_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))
            );
            cursor.close();
            return supplier;
        }
        return null;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Item item = new Item(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY))
                );
                items.add(item);
            }
            cursor.close();
        }
        return items;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_SUPPLIERS, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Supplier supplier = new Supplier(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_ITEM_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUPPLIER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))
                );
                suppliers.add(supplier);
            }
            cursor.close();
        }
        return suppliers;
    }


}
