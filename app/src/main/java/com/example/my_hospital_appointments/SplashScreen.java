package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    Handler myHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent=new Intent(SplashScreen.this,Launcher_Screen.class);
                startActivity(myIntent);
            }
        },4000);


    }
}