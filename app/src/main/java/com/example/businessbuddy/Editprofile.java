package com.example.businessbuddy;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Editprofile extends AppCompatActivity {

    private EditText editName, editEmail, editMobileNo, editPassword;
    private Button buttonSave;
    private Button buttonhome;
    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private TextView profilephoneTextView;
    private DatabaseHelper dbHelper;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editprofile);


        editName = findViewById(R.id.edit_name);
        editMobileNo = findViewById(R.id.edit_mobile_no);
        editPassword = findViewById(R.id.edit_password);
        buttonSave = findViewById(R.id.btnSubmit);
        buttonhome=findViewById(R.id.btnHome);
        profileNameTextView = findViewById(R.id.textView2);
        profileEmailTextView = findViewById(R.id.textView3);
        profilephoneTextView = findViewById(R.id.textView4);

        dbHelper = new DatabaseHelper(this);

        buttonhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editprofile.this, Profile.class);
                startActivity(intent);
            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        dbHelper = new DatabaseHelper(this);

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

    private void saveChanges() {
        String name = editName.getText().toString();
        String mobileNo = editMobileNo.getText().toString();
        String password = editPassword.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "#");

        if (name.isEmpty() || mobileNo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_MOBILE_NO, mobileNo);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        int rowsAffected = db.update(DatabaseHelper.TABLE_REGISTER, values,
                DatabaseHelper.COLUMN_EMAIL + " = ?", new String[]{email}); // Use email instead of userId

        if (rowsAffected > 0) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Editprofile.this, Profile.class);

            finish();
            startActivity(i);
        } else {
            Toast.makeText(this, "Profile update failed or email not found", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }


}

