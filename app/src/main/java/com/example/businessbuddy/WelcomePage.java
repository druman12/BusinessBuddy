package com.example.businessbuddy;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        getWindow() .setFlags (WindowManager.LayoutParams. FLAG_FULLSCREEN,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        Intent ihome=new Intent(WelcomePage.this, LogIn.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(ihome);
                finish();
            }
        },3500);

    }
}