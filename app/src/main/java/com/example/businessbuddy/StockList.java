package com.example.businessbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class StockList extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        DatabaseHelper db = new DatabaseHelper(this);

        try {
            ArrayList<HashMap<String, String>> itemList = db.getAllItemsAndSuppliers();

            // Initialize the TableLayout
            TableLayout tableLayout = findViewById(R.id.stock_table);

            for (HashMap<String, String> item : itemList) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                TextView itemCode = new TextView(this);
                if (item.containsKey("ItemCode")) {
                    itemCode.setText(item.get("ItemCode"));
                } else {
                    itemCode.setText("");
                }
                itemCode.setPadding(20, 20, 20, 20);
                itemCode.setTextSize(16);
                itemCode.setTextColor(getColor(R.color.black));
                itemCode.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                itemCode.setSingleLine(true);

                TextView itemName = new TextView(this);
                if (item.containsKey("ItemName")) {
                    itemName.setText(item.get("ItemName"));
                } else {
                    itemName.setText("");
                }
                itemName.setPadding(20, 20, 20, 20);
                itemName.setTextSize(16);
                itemName.setTextColor(getColor(R.color.black));
                itemName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                itemName.setSingleLine(true);

                TextView category = new TextView(this);
                if (item.containsKey("ItemCategory")) {
                    category.setText(item.get("ItemCategory"));
                } else {
                    category.setText("");
                }
                category.setPadding(20, 20, 20, 20);
                category.setTextSize(16);
                category.setTextColor(getColor(R.color.black));
                category.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                category.setSingleLine(true);

                TextView price = new TextView(this);
                if (item.containsKey("Price")) {
                    price.setText(item.get("Price"));
                } else {
                    price.setText("");
                }
                price.setPadding(20, 20, 20, 20);
                price.setTextSize(16);
                price.setTextColor(getColor(R.color.black));
                price.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                price.setSingleLine(true);

                TextView quantity = new TextView(this);
                if (item.containsKey("Quantity")) {
                    quantity.setText(item.get("Quantity"));
                } else {
                    quantity.setText("");
                }
                quantity.setPadding(20, 20, 20, 20);
                quantity.setTextSize(16);
                quantity.setTextColor(getColor(R.color.black));
                quantity.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                quantity.setSingleLine(true);

                TextView supplierName = new TextView(this);
                if (item.containsKey("SupplierName")) {
                    supplierName.setText(item.get("SupplierName"));
                } else {
                    supplierName.setText("");
                }
                supplierName.setPadding(20, 20, 20, 20);
                supplierName.setTextSize(16);
                supplierName.setTextColor(getColor(R.color.black));
                supplierName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                supplierName.setSingleLine(true);

                // Add all TextViews to the row
                row.addView(itemCode);
                row.addView(itemName);
                row.addView(category);
                row.addView(price);
                row.addView(quantity);
                row.addView(supplierName);

                // Add row to the TableLayout
                tableLayout.addView(row);
            }

            tableLayout.requestLayout(); // Update the layout
        } catch (Exception e) {
            // Handle the error, e.g., display an error message to the user
        }

        // Button to go back to home
        Button back = findViewById(R.id.BackHome);
        Button btnAddItem = findViewById(R.id.Add_Items);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StockList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Button to add new items
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(StockList.this, PurchaseEntry.class);
                startActivity(intent);
            }
        });
    }
}