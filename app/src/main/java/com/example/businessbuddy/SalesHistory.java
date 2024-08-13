package com.example.businessbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SalesHistory extends AppCompatActivity {

    private ListView lvSalesHistory;
    private SalesAdapter salesAdapter;
    private List<Sale> salesList;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        lvSalesHistory = findViewById(R.id.lv_sales_history);
        btn_home=findViewById(R.id.btn_sale_home);
        // Fetch sales data from the database
        SaleDAO saleDAO = new SaleDAO(this);
        salesList = saleDAO.getAllSales();

        salesAdapter = new SalesAdapter(this, R.layout.item_sales, salesList);
        lvSalesHistory.setAdapter((ListAdapter) salesAdapter);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SalesHistory.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



    private class SalesAdapter extends ArrayAdapter<Sale> {

        public SalesAdapter(Context context, int resource, List<Sale> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sales, parent, false);
            }

            Sale sale = getItem(position);

            TextView tvCustomerId = convertView.findViewById(R.id.tv_customer_id);
            TextView tvContactNumber = convertView.findViewById(R.id.tv_contact_number);
            TextView tvTotalBill = convertView.findViewById(R.id.tv_total_bill);
            TextView tvPaymentType = convertView.findViewById(R.id.tv_payment_type);
            Button btnShowDetails = convertView.findViewById(R.id.btn_show_details);
            TableLayout tlItemDetails = convertView.findViewById(R.id.tl_item_details);

            tvCustomerId.setText(String.valueOf(sale.getCustomerId()));
            tvContactNumber.setText(sale.getContactNumber());
            tvTotalBill.setText(String.valueOf(sale.getTotalBill()));
            tvPaymentType.setText(sale.getPaymentType());

            btnShowDetails.setOnClickListener(v -> {
                if (tlItemDetails.getVisibility() == View.GONE) {
                    tlItemDetails.setVisibility(View.VISIBLE);
                    btnShowDetails.setText("Hide Item Details");
                    displayItemDetails(sale.getSaleId(), tlItemDetails); // Display item details for the clicked sale
                } else {
                    tlItemDetails.setVisibility(View.GONE);
                    btnShowDetails.setText("Show Item Details");
                }
            });

            return convertView;
        }

        private void displayItemDetails(int saleId, TableLayout tlItemDetails) {
            tlItemDetails.removeAllViews();

            // Add header row
            TableRow headerRow = new TableRow(getContext());
            String[] headers = {"Item Code", "Item Name", "Price", "Quantity"};
            for (String header : headers) {
                TextView textView = new TextView(getContext());
                textView.setText(header);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(8, 8, 8, 8);
                headerRow.addView(textView);
            }
            tlItemDetails.addView(headerRow);

            // Fetch items for the given saleId
            SaleDAO saleDAO = new SaleDAO(getContext());
            List<SaleItem> saleItems = saleDAO.getItemsForSale(saleId);
            for (SaleItem item : saleItems) {
                TableRow row = new TableRow(getContext());

                TextView itemCode = new TextView(getContext());
                itemCode.setText(item.getItemCode());
                itemCode.setPadding(8, 8, 8, 8);
                row.addView(itemCode);

                TextView itemName = new TextView(getContext());
                itemName.setText(ItemDAO.getItemName(item.getItemCode()));
                itemName.setPadding(8, 8, 8, 8);
                row.addView(itemName);

                TextView price = new TextView(getContext());
                price.setText(String.valueOf(ItemDAO.getPrice(item.getItemCode())));
                price.setPadding(8, 8, 8, 8);
                row.addView(price);

                TextView quantity = new TextView(getContext());
                quantity.setText(String.valueOf(item.getQuantity()));
                quantity.setPadding(8, 8, 8, 8);
                row.addView(quantity);

                tlItemDetails.addView(row);
            }
        }
    }
}
