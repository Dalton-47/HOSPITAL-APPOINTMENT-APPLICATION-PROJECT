package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class askuseroccupation extends AppCompatActivity {

    public Button myDoctor;
    public Button myPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askuseroccupation);

        myDoctor=(Button) findViewById(R.id.buttonDoctor);
        myDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myIntent=new Intent(askuseroccupation.this, doctorRegistration.class);
                startActivity(myIntent);
                finish();
            }
        });

        myPatient=(Button) findViewById(R.id.buttonPatientAskUser);
        myPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(askuseroccupation.this,PatientRegistration.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}