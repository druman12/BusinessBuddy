package com.example.businessbuddy;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.businessbuddy.ItemDAO;
import com.example.businessbuddy.R;
import com.example.businessbuddy.Sale;
import com.example.businessbuddy.SaleDAO;
import com.example.businessbuddy.SaleItem;

import java.util.List;

public class SalesAdapter extends ArrayAdapter<Sale> {

    private Context context;
    private List<Sale> salesList;
    private ItemDAO itemDAO;

    public SalesAdapter(Context context, List<Sale> salesList) {
        super(context, R.layout.item_sales, salesList);
        this.context = context;
        this.salesList = salesList;
        itemDAO = new ItemDAO(context);
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
                displayItemDetails(tlItemDetails, sale.getSaleId());
            } else {
                tlItemDetails.setVisibility(View.GONE);
                btnShowDetails.setText("Show Item Details");
            }
        });

        return convertView;
    }

    private void displayItemDetails(TableLayout tlItemDetails, int saleId) {
        tlItemDetails.removeAllViews();

        TableRow headerRow = new TableRow(context);
        String[] headers = {"Item Code", "Item Name", "Price", "Quantity"};
        for (String header : headers) {
            TextView textView = new TextView(context);
            textView.setText(header);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(8, 8, 8, 8);
            headerRow.addView(textView);
        }
        tlItemDetails.addView(headerRow);

        SaleDAO saleDAO = new SaleDAO(context);
        List<SaleItem> saleItems = saleDAO.getItemsForSale(saleId);

        for (SaleItem item : saleItems) {
            // Fetch the price for each item from the database
            double price = itemDAO.getItemPrice(item.getItemCode());
            item.setPrice(price);

            TableRow row = new TableRow(context);

            TextView itemCode = new TextView(context);
            itemCode.setText(item.getItemCode());
            itemCode.setPadding(8, 8, 8, 8);
            row.addView(itemCode);

            TextView itemName = new TextView(context);
            itemName.setText(itemDAO.getItemName(item.getItemCode()));
            itemName.setPadding(8, 8, 8, 8);
            row.addView(itemName);

            TextView priceView = new TextView(context);
            priceView.setText(String.valueOf(item.getPrice()));
            priceView.setPadding(8, 8, 8, 8);
            row.addView(priceView);

            TextView quantity = new TextView(context);
            quantity.setText(String.valueOf(item.getQuantity()));
            quantity.setPadding(8, 8, 8, 8);
            row.addView(quantity);

            tlItemDetails.addView(row);
        }

        saleDAO.close();
        itemDAO.close();
    }
}
