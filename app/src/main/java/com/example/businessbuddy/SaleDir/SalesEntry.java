package com.example.businessbuddy.SaleDir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.businessbuddy.CustomerDAO;
import com.example.businessbuddy.DatabaseHelper;
import com.example.businessbuddy.ItemDAO;
import com.example.businessbuddy.MainActivity;
import com.example.businessbuddy.R;

public class SalesEntry extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private int userId;

    private EditText etContactNumber;
    private RadioGroup rgPaymentType;
    private TextView tvTotalBill;
    private Button btnHome, btnCheckHistory, btnSubmit, btnAddRow;

    private LinearLayout itemTableLayout;
    private double totalBill = 0.0;

    private ItemDAO itemDAO;
    private CustomerDAO customerDAO;
    private SaleDAO saleDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_entry);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences=getSharedPreferences("login_session", MODE_PRIVATE);
        String email=sharedPreferences.getString("email",null);
        userId= dbHelper.getUserId(email);

        etContactNumber = findViewById(R.id.et_contact_number);
        rgPaymentType = findViewById(R.id.rg_payment_type);
        tvTotalBill = findViewById(R.id.tv_total_bill);
        btnHome = findViewById(R.id.btn_home);
        btnCheckHistory = findViewById(R.id.btn_check_history);
        btnSubmit = findViewById(R.id.btn_submit);
        btnAddRow = findViewById(R.id.addRowButton);

        itemTableLayout = findViewById(R.id.itemTableLayout);

        itemDAO = new ItemDAO(this);
        customerDAO = new CustomerDAO(this);
        saleDAO = new SaleDAO(this);



        // Add initial row
        addNewRow();

        // Add more rows when the "Add More" button is clicked
        btnAddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow();
            }
        });

        // Submit button listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etContactNumber.getText().toString().equals("")){
                    Toast.makeText(SalesEntry.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    submitSale();
                }

            }
        });

        // Home button listener (define its functionality as needed)
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SalesEntry.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Check History button listener (define its functionality as needed)
        btnCheckHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SalesEntry.this, SaleHistory.class);
                startActivity(intent);
            }
        });
    }

    private void addNewRow() {
        LinearLayout newRow = new LinearLayout(this);
        newRow.setOrientation(LinearLayout.HORIZONTAL);

        // Item Code/EditText
        EditText itemCodeEditText = new EditText(this);
        itemCodeEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        itemCodeEditText.setHint("Item Code");
        itemCodeEditText.setTextColor(getColor(R.color.black));
        itemCodeEditText.setHintTextColor(getColor(R.color.text_color));
        itemCodeEditText.addTextChangedListener(new ItemCodeTextWatcher(itemCodeEditText));

        // Quantity/EditText
        EditText quantityEditText = new EditText(this);
        quantityEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        quantityEditText.setHint("Quantity");
        quantityEditText.setTextColor(getColor(R.color.black));
        quantityEditText.setHintTextColor(getColor(R.color.text_color));
        quantityEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        quantityEditText.addTextChangedListener(new QuantityTextWatcher(quantityEditText, itemCodeEditText));

        // Amount/TextView
        TextView amountTextView = new TextView(this);
        amountTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        amountTextView.setText("0.00");
        amountTextView.setTextColor(getColor(R.color.black));
        amountTextView.setHintTextColor(getColor(R.color.text_color));
        amountTextView.setPadding(20,0,0,0);
        amountTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);


        // Add the views to the row
        newRow.addView(itemCodeEditText);
        newRow.addView(quantityEditText);
        newRow.addView(amountTextView);

        // Add the row to the table layout
        itemTableLayout.addView(newRow);
    }

    private class ItemCodeTextWatcher implements TextWatcher {
        private final EditText itemCodeEditText;

        public ItemCodeTextWatcher(EditText itemCodeEditText) {
            this.itemCodeEditText = itemCodeEditText;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private class QuantityTextWatcher implements TextWatcher {
        private final EditText quantityEditText;
        private final EditText itemCodeEditText;

        public QuantityTextWatcher(EditText quantityEditText, EditText itemCodeEditText) {
            this.quantityEditText = quantityEditText;
            this.itemCodeEditText = itemCodeEditText;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Calculate amount whenever quantity changes
            calculateAmount(itemCodeEditText, quantityEditText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private void calculateAmount(EditText itemCodeEditText, EditText quantityEditText) {
        String itemCode = itemCodeEditText.getText().toString();
        String quantityStr = quantityEditText.getText().toString();

        if (!itemCode.isEmpty() && !quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);
            double price = itemDAO.getItemPrice(userId, itemCode); // Ensure item price is fetched correctly
            double amount = price * quantity;

            // Update the amount TextView for the same row
            LinearLayout parentRow = (LinearLayout) quantityEditText.getParent();
            TextView amountTextView = (TextView) parentRow.getChildAt(2); // Get the amount TextView in the same row
            amountTextView.setText(String.format("%.2f", amount));
            amountTextView.setTextColor(getColor(R.color.black));

            // Recalculate total bill
            updateTotalBill();
        }
    }

    private void updateTotalBill() {
        totalBill = 0.0;
        // Iterate through all rows in the itemTableLayout
        for (int i = 0; i < itemTableLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) itemTableLayout.getChildAt(i);
            TextView amountTextView = (TextView) row.getChildAt(2);
            String amountStr = amountTextView.getText().toString();

            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                totalBill += amount;
            }
        }
        tvTotalBill.setText(String.format("%.2f", totalBill));
        tvTotalBill.setTextColor(getColor(R.color.black));
    }

    private void submitSale() {
        String contactNo = etContactNumber.getText().toString();
        String paymentType = getSelectedPaymentType();

        // Insert customer and get customer ID
        Integer customerId = customerDAO.insertCustomer(contactNo, paymentType, totalBill, userId);

        // Iterate over all rows and insert sales data
        for (int i = 0; i < itemTableLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) itemTableLayout.getChildAt(i);
            EditText itemCodeEditText = (EditText) row.getChildAt(0);
            EditText quantityEditText = (EditText) row.getChildAt(1);
            TextView amountTextView = (TextView) row.getChildAt(2);

            String itemCode = itemCodeEditText.getText().toString();
            String quantityStr = quantityEditText.getText().toString();
            String amountStr = amountTextView.getText().toString();

            if (!itemCode.isEmpty() && !quantityStr.isEmpty() && !amountStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                double amount = Double.parseDouble(amountStr);

                int currentQuantity = itemDAO.getItemQuantity(userId, itemCode);

                if (currentQuantity >= quantity) {
                    // Create SaleItem and insert into SaleDAO
                    SaleItem saleItem = new SaleItem(itemCode, quantity, amount);
                    saleDAO.insertSale(userId, customerId, saleItem);

                    // Update item quantity
                    itemDAO.updateItemQuantity(userId, itemCode, quantity);
                } else {
                    Toast.makeText(this, "Sale quantity is higher than available stock!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Toast.makeText(this, "Sale completed!", Toast.LENGTH_SHORT).show();
        clearForm();
    }


    private String getSelectedPaymentType() {
        int selectedId = rgPaymentType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }

    private void clearForm() {
        etContactNumber.setText("");
        itemTableLayout.removeAllViews();
        addNewRow(); // Add a new empty row after clearing
        tvTotalBill.setText("0.00");
        tvTotalBill.setTextColor(getColor(R.color.black));
        totalBill = 0.0;
    }
}
