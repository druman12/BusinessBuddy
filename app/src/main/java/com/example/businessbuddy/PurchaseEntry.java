package com.example.businessbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.text.TextUtils;
import android.widget.Toast;


public class PurchaseEntry extends Activity {

    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private int userId;

    private TableLayout tableItemDetails;
    private Button btnAddMore;
    private Button btnSubmit;
    private Button btnHome;
    private Button btnSeeStock;
    private TextView textTotalBillAmount;
    private RadioGroup radioGroupPaymentMode;
    private EditText supplierName, supplierContact;
    private EditText editDate;

    private List<TableRow> itemRows = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parchase_entry);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences=getSharedPreferences("login_session", MODE_PRIVATE);
        String email=sharedPreferences.getString("email",null);
        userId= dbHelper.getUserId(email);


        tableItemDetails = findViewById(R.id.tableItemDetails);
        btnAddMore = findViewById(R.id.btnAddMore);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnHome = findViewById(R.id.btnHome);
        btnSeeStock = findViewById(R.id.btnSeeStock);
        textTotalBillAmount = findViewById(R.id.textTotalBillAmount);
        radioGroupPaymentMode = findViewById(R.id.radioGroupPaymentMode);

        supplierName=findViewById(R.id.editSupplierName);
        supplierContact=findViewById(R.id.editContactNumber);
        editDate = findViewById(R.id.editDate);




        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PurchaseEntry.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set date in EditText in the format you want
                                editDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItemRow();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(supplierName.getText().toString().equals("") && supplierContact.getText().toString().equals("") && supplierContact.getText().toString().length() !=10 ){
                    Toast.makeText(PurchaseEntry.this, "Please fill all fields correctly !!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(supplierContact.getText().toString().length() ==10){
                        submitData();
                        Toast.makeText(PurchaseEntry.this, "Parchase Entry done sucessfully", Toast.LENGTH_SHORT).show();
                        clearForm();
                    }
                    else{
                        Toast.makeText(PurchaseEntry.this, "Error , Please check Mobile Number !!", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseEntry.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSeeStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseEntry.this, StockList.class);
                startActivity(intent);
            }
        });

        // Add the initial item row
        addNewItemRow();
    }

    private void addNewItemRow() {
        TableRow newRow = new TableRow(this);
        newRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // Create and add EditText and TextView to the new row
        EditText itemCode = new EditText(this);
        itemCode.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        itemCode.setHint("Item Code");
        itemCode.setHintTextColor(getColor(R.color.black));
        itemCode.setTextColor(getColor(R.color.black));
        itemCode.setTextSize(13);
        itemCode.setSingleLine(true);

        EditText itemName = new EditText(this);
        itemName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        itemName.setHint("Item Name");
        itemName.setTextSize(13);
        itemName.setHintTextColor(getColor(R.color.black));
        itemName.setTextColor(getColor(R.color.black));
        itemName.setSingleLine(true);

        EditText category = new EditText(this);
        category.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        category.setHint("Category");
        category.setHintTextColor(getColor(R.color.black));
        category.setTextColor(getColor(R.color.black));
        category.setTextSize(14);

        EditText price = new EditText(this);
        price.setHint("Price");
        price.setTextSize(14);
        price.setHintTextColor(getColor(R.color.black));
        price.setTextColor(getColor(R.color.black));
        price.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        EditText quantity = new EditText(this);
        quantity.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        quantity.setHint("Quantity");
        quantity.setTextSize(14);
        quantity.setHintTextColor(getColor(R.color.black));
        quantity.setTextColor(getColor(R.color.black));
        quantity.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        TextView totalAmount = new TextView(this);
        totalAmount.setTextColor(getColor(R.color.black));
        totalAmount.setTextColor(getColor(R.color.black));
        totalAmount.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        totalAmount.setText("0.00");

        // Add TextWatchers to price and quantity
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotalAmountForRow(newRow);
                updateTotalBillAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        price.addTextChangedListener(textWatcher);
        quantity.addTextChangedListener(textWatcher);

        // Add views to the row
        newRow.addView(itemCode);
        newRow.addView(itemName);
        newRow.addView(category);
        newRow.addView(price);
        newRow.addView(quantity);
        newRow.addView(totalAmount);

        tableItemDetails.addView(newRow);
        itemRows.add(newRow);

        // Update total bill amount when new row is added
        updateTotalBillAmount();
    }

    private void updateTotalAmountForRow(TableRow row) {
        EditText price = (EditText) row.getChildAt(3);
        EditText quantity = (EditText) row.getChildAt(4);
        TextView totalAmount = (TextView) row.getChildAt(5);

        String priceText = price.getText().toString();
        String quantityText = quantity.getText().toString();

        try {
            double priceValue = TextUtils.isEmpty(priceText) ? 0.0 : Double.parseDouble(priceText);
            int quantityValue = TextUtils.isEmpty(quantityText) ? 0 : Integer.parseInt(quantityText);
            double totalAmountValue = priceValue * quantityValue;
            totalAmount.setText(df.format(totalAmountValue));
        } catch (NumberFormatException e) {
            totalAmount.setText("0.00");
        }
    }

    private void updateTotalBillAmount() {
        double totalBillAmount = 0.0;
        for (TableRow row : itemRows) {
            TextView totalAmount = (TextView) row.getChildAt(5);
            try {
                totalBillAmount += Double.parseDouble(totalAmount.getText().toString());
            } catch (NumberFormatException e) {
                // Handle parse exception
            }
        }
        textTotalBillAmount.setText("Total Bill Amount: " + df.format(totalBillAmount));
    }

    private void submitData() {
        // Initialize ItemDAO
        ItemDAO itemDAO = new ItemDAO(this);

        // Get selected payment mode
        int selectedId = radioGroupPaymentMode.getCheckedRadioButtonId();
        RadioButton selectedPaymentMode = findViewById(selectedId);
        String paymentMode = selectedPaymentMode != null ? selectedPaymentMode.getText().toString() : "cash";

        // Fetch supplier data
        String supplierNameValue = supplierName.getText().toString().trim();
        String supplierContactValue = supplierContact.getText().toString().trim();
        String paymentDateValue = editDate.getText().toString();



        // Loop through itemRows to gather data and add to database
        for (TableRow row : itemRows) {
            EditText itemCode = (EditText) row.getChildAt(0);
            EditText itemName = (EditText) row.getChildAt(1);
            EditText category = (EditText) row.getChildAt(2);
            EditText price = (EditText) row.getChildAt(3);
            EditText quantity = (EditText) row.getChildAt(4);
            TextView totalAmount = (TextView) row.getChildAt(5);

            // Gather item data
            String code = itemCode.getText().toString().trim();
            String name = itemName.getText().toString().trim();
            String cat = category.getText().toString().trim();
            double priceValue = Double.parseDouble(price.getText().toString().trim());
            int quantityValue = Integer.parseInt(quantity.getText().toString().trim());
            double totalAmountValue = Double.parseDouble(totalAmount.getText().toString().trim());

            Log.d("Suppier data",supplierNameValue +" "+ supplierContactValue +" "+paymentDateValue);
            itemDAO.addSupplier(userId,code, supplierNameValue, supplierContactValue, paymentDateValue, paymentMode, quantityValue, totalAmountValue);
            // Add item to the database
            itemDAO.addItem(userId,code, name, cat, quantityValue, priceValue,supplierNameValue);

        }
    }
    private void clearForm() {

        supplierName.setText("");
        supplierContact.setText("");
        editDate.setText("");
        textTotalBillAmount.setText("Total Bill Amount: 0.00");

        tableItemDetails.removeViews(1, tableItemDetails.getChildCount() - 1);
        itemRows.clear();

    }

}