package com.example.businessbuddy;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.businessbuddy.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardItem = findViewById(R.id.carditem);
        CardView cardStock = findViewById(R.id.cardstock);
        CardView cardBill = findViewById(R.id.cardbill);
        CardView cardSupplyerInfo = findViewById(R.id.cardsupplyerinfo);
        CardView cardProfile = findViewById(R.id.cardProfile);
        CardView cardLogout = findViewById(R.id.cardlogout);

        cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PurchaseEntry.class);
                startActivity(intent);
            }
        });

        cardStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StockList.class);
                startActivity(intent);
            }
        });

        cardBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesEntry.class);
                startActivity(intent);
            }
        });

        cardSupplyerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesHistory.class);
                startActivity(intent);
            }
        });

//        cardProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogIn.class);
                startActivity(intent);
            }
        });
    }
}