package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admincodepage extends AppCompatActivity {

     private Button adminProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincodepage);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
        //Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();

        EditText adminCode=(EditText)  findViewById(R.id.editTextTextPasswordADMIN);


        adminProceed=(Button) findViewById(R.id.buttonProceedAdmin);
        adminProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password=adminCode.getText().toString().trim();
                if(password.isEmpty())
                {
                    adminCode.setError("ENTER ADMIN CODE");
                    adminCode.requestFocus();
                }
                else
                {
                    Intent myIntent=new Intent(admincodepage.this,AdminMainPage.class);
                    startActivity(myIntent);
                    adminCode.setText("");
                    finish();
                }

                /*
                myIntent.putExtra("usersEmail",myUsersEmail);
                 */



            }
        });
    }
}