package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class secondLoginPage extends AppCompatActivity {

    public Button adminBtn,btnPatient,btnDoctor;
    DatabaseReference docRef,patientRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login_page);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");

        String parts[]=myUsersEmail.split("@"); //store the split part before @ character at the first index of the array parts
        String emailID=parts[0];

        docRef=FirebaseDatabase.getInstance().getReference("Doctors").child(emailID);
        patientRef=FirebaseDatabase.getInstance().getReference("Patients").child(emailID);



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

                patientRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Patients patients=snapshot.getValue(Patients.class); //get the value from the doctors database
                        if(patients==null)
                        {
                            //If  details aren't found access is denied
                            Toast.makeText(secondLoginPage.this, "Register As a Patient!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // Intent myIntent=new Intent(secondLoginPage.this,MainPage.class);
                            Intent myIntent=new Intent(secondLoginPage.this,Patient_Main_Page_NEW.class);
                            myIntent.putExtra("usersEmail",myUsersEmail);
                            startActivity(myIntent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(secondLoginPage.this, "USER NOT FOUND, CHECK NETWORK CONNECTION!", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        btnDoctor=(Button) findViewById(R.id.buttonDoctorCategory);
        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctors doctors=snapshot.getValue(Doctors.class); //get the value from the doctors database
                        if(doctors==null)
                        {
                            //If  details aren't found access is denied
                            Toast.makeText(secondLoginPage.this, "ACCESS DENIED!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //  Intent myIntent=new Intent(MainPageDoctor.this,DoctorConsultation.class);
                            Intent myIntent=new Intent(secondLoginPage.this,Doctor_Main_Page_NEW.class);
                            myIntent.putExtra("usersEmail",myUsersEmail);
                            startActivity(myIntent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(secondLoginPage.this, "USER NOT FOUND, CHECK NETWORK CONNECTION!", Toast.LENGTH_SHORT).show();
                    }
                });

                //here
            }
        });

    }
}