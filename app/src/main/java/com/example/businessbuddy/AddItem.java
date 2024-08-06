package com.example.businessbuddy;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItem extends AppCompatActivity {

    private EditText editTextItemCode;
    private EditText editTextItemName;
    private EditText editTextItemPrice;
    private EditText editTextCategory;
    private Button buttonAddItem;
    private EditText editTextItemQuantity;
    private Button buttonHome;
    private Button buttonSeeItem;

    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemDAO = new ItemDAO(this);

        editTextItemCode = findViewById(R.id.editTextItemCode);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemPrice = findViewById(R.id.editTextItemPrice);
        editTextCategory = findViewById(R.id.editTextCategory);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        editTextItemQuantity=findViewById(R.id.editTextItemQuantity);
        buttonHome = findViewById(R.id.Home);
        buttonSeeItem=findViewById(R.id.SeeItem);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddItem.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonSeeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddItem.this, StockList.class);
                startActivity(intent);
            }
        });

    }

    private void addItem() {
        String itemCode = editTextItemCode.getText().toString().trim();
        String itemName = editTextItemName.getText().toString().trim();
        String itemPrice = editTextItemPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String itemquantity=editTextItemQuantity.getText().toString().trim();

        if (itemCode.isEmpty() || itemName.isEmpty() || itemPrice.isEmpty() || category.isEmpty() || itemquantity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert item price to double
        double price;
        int quantity;
        try {
            price = Double.parseDouble(itemPrice);
            quantity=Integer.parseInt(itemquantity);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the item in SQLite database
        itemDAO.addItem(itemCode, itemName, category, price,quantity);

        Toast.makeText(this, "Item added: " + itemCode + ", " + itemName + ", " + category + ", ₹" + price + ", Quantity: " + quantity , Toast.LENGTH_LONG).show();

        // Clear fields
        editTextItemCode.setText("");
        editTextItemName.setText("");
        editTextItemPrice.setText("");
        editTextCategory.setText("");
    }

}