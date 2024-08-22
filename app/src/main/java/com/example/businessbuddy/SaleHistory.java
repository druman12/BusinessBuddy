package com.example.businessbuddy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SaleHistory extends AppCompatActivity {

    private TableLayout saleHistoryTable;
    private SQLiteDatabase database;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_history);

        saleHistoryTable = findViewById(R.id.saleHistoryTable);
        btnBack = findViewById(R.id.btnBack);

        // Initialize the database
        database = new DatabaseHelper(this).getReadableDatabase();

        // Load sale history data
        loadSaleHistory();

        // Set up the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the current activity and return to the previous one
            }
        });
    }

    private void loadSaleHistory() {
        // Correct the SQL query to match your database schema
        String query = "SELECT customer.customer_id, customer.contact_no, customer.payment_type, customer.total_bill, " +
                "GROUP_CONCAT(sales.itemcode, ',') AS items_purchased " +
                "FROM sales " +
                "JOIN customer ON sales.customer_id = customer.customer_id " +
                "GROUP BY customer.customer_id";

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(query, null);

            // Check if the cursor is empty
            if (cursor == null || !cursor.moveToFirst()) {
                Toast.makeText(this, "No sale history found.", Toast.LENGTH_SHORT).show();
                return;
            }

            do {
                TableRow row = new TableRow(this);

                // Create TextViews for each column and ensure proper error handling
                try {
                    TextView customerId = new TextView(this);
                    customerId.setText(cursor.getString(cursor.getColumnIndexOrThrow("customer_id")));
                    row.addView(customerId);

                    TextView contactNo = new TextView(this);
                    contactNo.setText(cursor.getString(cursor.getColumnIndexOrThrow("contact_no")));
                    row.addView(contactNo);

                    TextView paymentType = new TextView(this);
                    paymentType.setText(cursor.getString(cursor.getColumnIndexOrThrow("payment_type")));
                    row.addView(paymentType);

                    TextView totalBill = new TextView(this);
                    totalBill.setText(cursor.getString(cursor.getColumnIndexOrThrow("total_bill")));
                    row.addView(totalBill);

                    TextView itemsPurchased = new TextView(this);
                    itemsPurchased.setText(cursor.getString(cursor.getColumnIndexOrThrow("items_purchased")));
                    row.addView(itemsPurchased);
                } catch (Exception e) {
                    Toast.makeText(this, "Error parsing row data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    continue; // Skip this row and proceed with the next one
                }

                // Add the row to the table layout
                saleHistoryTable.addView(row);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            // Handle the general exception case
            Toast.makeText(this, "Error loading sale history: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            // Ensure the cursor is closed to avoid memory leaks
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure the database is closed when the activity is destroyed
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
