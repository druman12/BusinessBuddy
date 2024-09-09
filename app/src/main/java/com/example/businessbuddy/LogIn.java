package com.example.businessbuddy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    EditText email,password;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        TextView signup=findViewById(R.id.signup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LogIn.this,SignUp.class));
            }
        });
        email=(EditText)findViewById(R.id.Email);
        password=(EditText)findViewById(R.id.Password);
        button=(Button)findViewById(R.id.LogIn);

        ItemDAO itemDAO=new ItemDAO(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = email.getText().toString();
                String pass =password.getText().toString();
                if(email1.equals("")||pass.equals(""))
                {
                    Toast.makeText(LogIn.this,"please enter all fileds",Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkuserpass=itemDAO.checkusernamepassword(email1,pass);

                    if(checkuserpass)
                    {
                        Toast.makeText(LogIn.this, "login successfuly", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LogIn.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LogIn.this, "invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });


    }
}