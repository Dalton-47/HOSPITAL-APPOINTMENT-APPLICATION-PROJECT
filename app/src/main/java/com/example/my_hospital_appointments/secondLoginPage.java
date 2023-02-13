package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class secondLoginPage extends AppCompatActivity {

    public Button adminBtn,btnPatient,btnDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login_page);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
       // Toast.makeText(secondLoginPage.this,"Your Email = "+myUsersEmail,Toast.LENGTH_SHORT).show();
       // Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();
        adminBtn=(Button) findViewById(R.id.buttonAdminCategory);
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent=new Intent(secondLoginPage.this,admincodepage.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

        btnPatient=(Button) findViewById(R.id.buttonPatientCategory);
        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent myIntent=new Intent(secondLoginPage.this,MainPage.class);
                Intent myIntent=new Intent(secondLoginPage.this,Patient_Main_Page_NEW.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

        btnDoctor=(Button) findViewById(R.id.buttonDoctorCategory);
        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent myIntent=new Intent(secondLoginPage.this,DoctorWaitingPage.class);
                Intent myIntent=new Intent(secondLoginPage.this,Doctor_Main_Page_NEW.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

    }
}