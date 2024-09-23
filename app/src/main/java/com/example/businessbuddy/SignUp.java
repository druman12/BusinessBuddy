package com.example.businessbuddy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etConfirmPassword, etMobileNo;
    private Button btnSignup;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        TextView changeto = findViewById(R.id.already);
        etName = findViewById(R.id.etname);
        etEmail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etpass);
        etConfirmPassword = findViewById(R.id.etconpass);
        etMobileNo = findViewById(R.id.etphone);
        btnSignup = findViewById(R.id.button);

        dbHelper = new DatabaseHelper(this);



        changeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, LogIn.class));
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }

        });
    }

                private void registerUser() {
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();
                    String mobileNo = etMobileNo.getText().toString();

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_NAME, name);
                    values.put(DatabaseHelper.COLUMN_EMAIL, email);
                    values.put(DatabaseHelper.COLUMN_PASSWORD, password);


                    if (!name.equals("") || !email.equals("") || !mobileNo.equals("")) {

                        if (mobileNo.length() == 10) {


                            if (password.equals(confirmPassword)) {


                                if (isValidEmail(email)) {
                                    if(!isEmailAlreadyRegistered(email)){
                                        ItemDAO itemDAO = new ItemDAO(this);

                                        itemDAO.insertUser(name, email, password, mobileNo);
                                        Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, LogIn.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                else {
                                    Toast.makeText(SignUp.this, "Please enter your valid Email field", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUp.this, "Please enter your valid password", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(SignUp.this, "Please enter your valid phonenumber", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(SignUp.this, "Please enter your valid fields", Toast.LENGTH_SHORT).show();
                    }



                }

    private boolean isEmailAlreadyRegistered(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_REGISTER,
                new String[]{DatabaseHelper.COLUMN_EMAIL},
                DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{email}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }




    private boolean isValidEmail(String email) {


        if (!email.contains("@")) {
            return false;
        }

        if (!email.endsWith("gmail.com")) {
            return false;
        }

        return true;
    }
}