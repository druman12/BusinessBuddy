package com.example.businessbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class Profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 2;
    private Button button5;
    private static final int SELECT_PHOTO = 1;
    private ImageView profileImageView;
    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private TextView profilephoneTextView;
    private DatabaseHelper dbHelper;
    private LinearLayout linearLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        button5=(Button)findViewById(R.id.button5);
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        profileNameTextView = findViewById(R.id.textView2);
        profileEmailTextView = findViewById(R.id.textView3);
        profilephoneTextView = findViewById(R.id.textView4);
        linearLayout=findViewById(R.id.linearLayout2);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, LogIn.class);
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(this);


        // Retrieve profile information from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "#");
        Log.d("fetched data from shred pref",email);

        // Display profile information on the screen

        profileEmailTextView.setText(email);

        String name = dbHelper.getName(email);
        String phone = dbHelper.getPhone(email);

        profileNameTextView.setText(name);
        profilephoneTextView.setText(phone);



        profileImageView = (ImageView) findViewById(R.id.pro);
        profileImageView.setOnClickListener(v -> openGallery());

        profileImageView.setOnClickListener(v -> openGallery());

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}