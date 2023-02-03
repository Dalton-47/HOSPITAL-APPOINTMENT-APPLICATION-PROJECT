package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

public class Patient_Main_Page_NEW extends AppCompatActivity {
   ImageView userProfile;
    String userID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_page_new);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
         Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();

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

        userProfile =(ImageView)  findViewById(R.id.imageViewPatientProfileNew);

        Picasso.get()
                .load(R.drawable.happydoctor)
                .transform(new RoundedTransformation() )
                .into(userProfile);

       Button bookAppointments=(Button) this.findViewById(R.id.button_Book_Appointments_New);

        bookAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(Patient_Main_Page_NEW.this,Patient_Appointment_Booking_New.class);
                myIntent.putExtra("userID",userID);
                startActivity(myIntent);
            }
        });

        Button consultDoctor=(Button)  this.findViewById(R.id.buttonPatientConsultDocNew);
        consultDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(Patient_Main_Page_NEW.this,Patient_Check_Doctors_to_Consult_New.class);
                myIntent.putExtra("userID",userID);
                startActivity(myIntent);
            }
        });


    }
}