package com.example.businessbuddy;

import static com.example.businessbuddy.DatabaseHelper.COLUMN_EMAIL;
import static com.example.businessbuddy.DatabaseHelper.COLUMN_REGISTER_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout4;
    String email;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        button5 = (Button) findViewById(R.id.button5);
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
        linearLayout = findViewById(R.id.linearLayout2);
        linearLayout1=findViewById(R.id.linearLayout3);
        linearLayout4=findViewById(R.id.linearLayout4);

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Editprofile.class);
                startActivity(intent);
            }
        });

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDeleteConfirmationDialog();
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        dbHelper = new DatabaseHelper(this);


        // Retrieve profile information from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "#");
        Log.d("fetched data from shred pref", email);

        // Display profile information on the screen

        profileEmailTextView.setText(email);

        String name = dbHelper.getName(email);
        String phone = dbHelper.getPhone(email);

        profileNameTextView.setText(name);
        profilephoneTextView.setText(phone);

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the confirmation dialog
        builder.create().show();
    }




    private void deleteAccount() {
        // Log the email for debugging
        Log.d("DeleteAccount", "Attempting to delete account with email: " + email);

        boolean isDeleted = dbHelper.deleteUser(email);
        if (isDeleted) {
            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Profile.this, SignUp.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
        }
    }


}





