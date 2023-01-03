package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPageDoctor extends AppCompatActivity {

    String emailKey="";
    DatabaseReference myDoctorData;
    TextView helloUser,greetingUser;
    Button doctorAppointments,btnDoctorConsult,btnMedicalReports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_doctor);

        helloUser=(TextView)  findViewById(R.id.textViewHelloDr);
        greetingUser=(TextView)  findViewById(R.id.textViewHowArYouDr);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");

        myDoctorData= FirebaseDatabase.getInstance().getReference("Doctors");

        getDoctorName(myUsersEmail);

        doctorAppointments=(Button) findViewById(R.id.buttonDoctorAppointments1);
        doctorAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(MainPageDoctor.this,DoctorAppointments.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

        btnDoctorConsult=(Button) findViewById(R.id.buttonDoctorConsultancy);
        btnDoctorConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(MainPageDoctor.this,DoctorConsultation.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

        btnMedicalReports=(Button) findViewById(R.id.buttonLabReportsDoctor);
        btnMedicalReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(MainPageDoctor.this,MedicalReport.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

    }

    private void getDoctorName(String doctorEmail)
    {
        int Counter=doctorEmail.length();

        for(int a=0; a<Counter; a++)
        {
            if(doctorEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+doctorEmail.charAt(a);

            }
        }

        emailKey=emailKey.trim();

        myDoctorData.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        DataSnapshot thisDataSnapshot=task.getResult();
                        String doctorName=String.valueOf(thisDataSnapshot.child("userName").getValue());
                        if(!doctorName.isEmpty()) {
                            String Hello = "Hello Dr. "+doctorName;
                            String day="How Are You Doing Today?";
                            helloUser.setText(Hello);
                            greetingUser.setText(day);
                        }
                    }
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
        getDoctorName(myUsersEmail);
    }
}