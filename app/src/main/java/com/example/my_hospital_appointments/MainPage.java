package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPage extends AppCompatActivity {
 String userID="";
 DatabaseReference patientDetails;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

           Intent intent =getIntent();
           String myUsersEmail=intent.getExtras().getString("usersEmail");
          // Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();

           patientDetails= FirebaseDatabase.getInstance().getReference("Patients");

           int Counter=myUsersEmail.length();

           for(int a=0; a<Counter; a++)
           {
               if(myUsersEmail.charAt(a)=='@')
               {
                   break;
               }
               else
               {
                  // emailFirstAppt=emailFirstAppt+myUsersEmail.charAt(a);
                   userID=userID+myUsersEmail.charAt(a);
               }
           }

           TextView myHello=(TextView)  findViewById(R.id.textViewMainPageHello );
           patientDetails.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
               @SuppressLint("SetTextI18n")
               @Override
               public void onComplete(@NonNull Task<DataSnapshot> task) {
                   if (task.isSuccessful()) {

                       DataSnapshot thisDataSnapshot = task.getResult();
                       String Name = String.valueOf(thisDataSnapshot.child("secondName").getValue());
                       myHello.setText("HELLO "+Name+",");
                   }
               }
               });

           ImageButton myContacts=(ImageButton)  findViewById(R.id.imageButtonContacts);
           myContacts.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent myIntent=new Intent(MainPage.this,Contacts.class);
                   myIntent.putExtra("usersEmail",myUsersEmail);
                   startActivity(myIntent);
               }
           });

           ImageButton bookAppointment = (ImageButton) findViewById(R.id.imageButtonBookAppointment);
        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(MainPage.this,AppointmentBooking.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });

        ImageButton consultDoctor=(ImageButton) findViewById(R.id.imageButtonPatientConsultDoctor);
        consultDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent myIntent=new Intent(MainPage.this,PatientViewDoctorConsult.class);
               // myIntent.putExtra("usersEmail",myUsersEmail);
                Intent myIntent=new Intent(MainPage.this, Patient_Check_Doctors_to_Consult_New.class);
                myIntent.putExtra("userEmailID",userID);
                startActivity(myIntent);
            }
        });

        ImageButton medicalReports=(ImageButton) findViewById(R.id.imageButtonPatientMedicalReports);
        medicalReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent myIntent=new Intent(MainPage.this,PatientMedicalReport.class);
                Intent myIntent=new Intent(MainPage.this,Patient_Main_Page_Activity.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
            }
        });
    }
}