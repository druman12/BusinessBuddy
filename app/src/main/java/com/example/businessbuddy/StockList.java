package com.example.businessbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
        ArrayList<HashMap<String, String>> itemList = db.getAllItemsAndSuppliers();
        ListView lv = (ListView) findViewById(R.id.user_list);

        ListAdapter adapter = new SimpleAdapter(
                StockList.this,
                itemList,
                R.layout.list_row,
                new String[]{"ItemName", "SupplierName", "ItemCode", "Price", "ItemCategory", "Quantity"},
                new int[]{R.id.ItemName, R.id.SupplierName, R.id.ItemCode, R.id.Price, R.id.ItemCategory, R.id.Quantity}
        );

        lv.setAdapter(adapter);

        Button back = (Button)findViewById(R.id.BackHome);
        Button btnAddItem =findViewById(R.id.Add_Items);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StockList.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(StockList.this, PurchaseEntry.class);
            }
        });
    }
}
