package com.example.businessbuddy.SaleDir;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.businessbuddy.DatabaseHelper;
import com.example.businessbuddy.R;

public class SaleHistory extends AppCompatActivity {

    private TableLayout saleHistoryTable;
    private SQLiteDatabase database;
    private Button btnBack;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_history);

        saleHistoryTable = findViewById(R.id.saleHistoryTable);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences=getSharedPreferences("login_session", MODE_PRIVATE);
        String email=sharedPreferences.getString("email",null);
        userId= dbHelper.getUserId(email);

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
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        String query = "SELECT customer.customer_id, customer.contact_no, customer.payment_type, customer.total_bill, " +
                "GROUP_CONCAT(sales.itemcode, ',') AS items_purchased " +
                "FROM sales " +
                "JOIN customer ON sales.customer_id = customer.customer_id " +
                "JOIN register ON sales.user_id = register.user_id " +
                "WHERE sales.user_id = ? " +
                "GROUP BY customer.customer_id";

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor == null || !cursor.moveToFirst()) {
                Toast.makeText(this, "No sale history found.", Toast.LENGTH_SHORT).show();
                return;
            }

            do {
                TableRow row = new TableRow(this);

                // Set the background border for each row
                row.setBackgroundResource(R.drawable.table_cell_border);  // Apply the border drawable

                // Set layout parameters for the row
                TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,  // Width: match parent
                        TableRow.LayoutParams.WRAP_CONTENT   // Height: wrap content
                );
                row.setLayoutParams(rowLayoutParams);

                // Add TextViews for each column and apply layout parameters
                addTextViewToRow(row, cursor.getString(cursor.getColumnIndexOrThrow("customer_id")));
                addTextViewToRow(row, cursor.getString(cursor.getColumnIndexOrThrow("contact_no")));
                addTextViewToRow(row, cursor.getString(cursor.getColumnIndexOrThrow("payment_type")));
                addTextViewToRow(row, cursor.getString(cursor.getColumnIndexOrThrow("total_bill")));
                addTextViewToRow(row, cursor.getString(cursor.getColumnIndexOrThrow("items_purchased")));

                // Add the row to the table layout
                saleHistoryTable.addView(row);
                saleHistoryTable.requestLayout();
            } while (cursor.moveToNext());

        } catch (Exception e) {
            Toast.makeText(this, "Error loading sale history: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void addTextViewToRow(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(getColor(R.color.black));
        textView.setTextSize(20);
        textView.setPadding(16,16,16,16);
        textView.setBackgroundResource(R.drawable.table_cell_border);
        textView.setGravity(Gravity.CENTER);

        // Set layout parameters for each TextView
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,  // Width
                TableRow.LayoutParams.WRAP_CONTENT   // Height
        );
        layoutParams.setMargins(1, 1, 1, 1);  // Set margins to avoid gaps between borders
        textView.setLayoutParams(layoutParams);

        row.addView(textView);
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
