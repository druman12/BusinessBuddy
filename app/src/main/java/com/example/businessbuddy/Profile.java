package com.example.businessbuddy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Profile extends AppCompatActivity {

    private Button button5;
    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private TextView profilephoneTextView;
    private DatabaseHelper dbHelper;
    private LinearLayout logoutlinearLayout;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout4;
    String email;
    Integer userId;

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
                finish();
            }
        });

        profileNameTextView = findViewById(R.id.textView2);
        profileEmailTextView = findViewById(R.id.textView3);
        profilephoneTextView = findViewById(R.id.textView4);
        logoutlinearLayout = findViewById(R.id.linearLayout2);
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

        logoutlinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showLogoutDialog();
            }
        });

        dbHelper = new DatabaseHelper(this);

        // Retrieve profile information from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "#");
        profileEmailTextView.setText(email);
        String name = dbHelper.getName(email);
        String phone = dbHelper.getPhone(email);
        userId=dbHelper.getUserId(email);
        profileNameTextView.setText(name);
        profilephoneTextView.setText(phone);
    }
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Go to the Login page and clear the back stack
                        Intent intent = new Intent(Profile.this, LogIn.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
                finish();
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
        boolean isDeleted = dbHelper.deleteUser(userId);
        if (isDeleted) {
            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
            clearSession();
            Intent intent = new Intent(Profile.this, SignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}