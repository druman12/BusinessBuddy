package com.example.businessbuddy;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
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
            SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
            String email=sharedPreferences.getString("email",null);
            int userId= db.getUserId(email);
            ArrayList<HashMap<String, String>> itemList = db.getAllItemsAndSuppliers(userId);

            // Initialize the TableLayout
            TableLayout tableLayout = findViewById(R.id.stock_table);

            for (HashMap<String, String> item : itemList) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                row.setBackgroundResource(R.drawable.table_cell_border);



                // Create an array of the keys to loop through each one
                String[] keys = {"ItemCode", "ItemName", "ItemCategory", "Price", "Quantity", "SupplierName"};
                for (String key : keys) {
                    TextView textView = new TextView(this);
                    textView.setText(item.getOrDefault(key, ""));
                    textView.setTextSize(20);
                    textView.setPadding(20, 20, 20, 20);
                    textView.setTextColor(getColor(R.color.black));
                    textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    textView.setSingleLine(true);
                    textView.setGravity(Gravity.CENTER); // Center the text


                    // Add the TextView to the row
                    row.addView(textView);
                }

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

