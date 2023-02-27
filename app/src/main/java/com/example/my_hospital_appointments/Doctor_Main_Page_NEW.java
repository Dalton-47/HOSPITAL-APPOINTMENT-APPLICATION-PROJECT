package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Doctor_Main_Page_NEW extends AppCompatActivity {
   Button btnDocAppointments, btnDocConsult, btnDocHistory, btnDocReports;
   DatabaseReference docRef;
    String emailID,userName;
    TextView textViewDocName,textViewDate;
    ImageView docImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_page_new);

        textViewDocName = (TextView)  this.findViewById(R.id.textViewDocUserName_NEW);
        docImage =(ImageView)  this.findViewById(R.id.imageViewDocProfile_MAIN);


        //let's picasso the imageView
        Picasso.get()
                .load(R.drawable.blackfemaledoctor)
                .transform(new RoundedTransformation() )
                .into(docImage);
        docImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Doctor_Main_Page_NEW.this ,Doc_User_Profile.class  ));
            }
        });

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
        Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();

        String [] parts= myUsersEmail.split("@"); //split the email to get users ID
         emailID=parts[0];

        docRef = FirebaseDatabase.getInstance().getReference("Doctors").child(emailID);
        getUserName();

        textViewDate = (TextView)  this.findViewById(R.id.textViewDocDate_NEW);

        Date currentDate= new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd, MMMM, yyyy");
        String formattedDate= dateFormat.format(currentDate);

        textViewDate.setText(formattedDate);


        btnDocAppointments = (Button)  this.findViewById(R.id.buttonDocAppointments_MAN);
        btnDocConsult = (Button)  this.findViewById(R.id.buttonDocConsultation_MAN);
        btnDocReports = (Button)  this.findViewById(R.id.buttonDocReports_MAN);
        btnDocHistory = (Button)  this.findViewById(R.id.buttonDocHistory_MAN);


        btnDocAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent myIntent=new Intent(MainPageDoctor.this,DoctorAppointments.class);
                Intent myIntent=new Intent(Doctor_Main_Page_NEW.this,Doctor_View_Appointments_NEW.class);
                myIntent.putExtra("docEmailId",emailID);
                startActivity(myIntent);
            }
        });

        btnDocConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent myIntent=new Intent(MainPageDoctor.this,DoctorConsultation.class);
                Intent myIntent=new Intent(Doctor_Main_Page_NEW.this,Messages_From_Patients.class);
                myIntent.putExtra("doctorName",userName);
                startActivity(myIntent);
            }
        });

    }

    void getUserName()
    {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    DataSnapshot snapshot=task.getResult();
                     userName=String.valueOf(snapshot.child("userName").getValue());
                     textViewDocName.setText("Hello Dr."+userName);
                }
                else
                {
                    textViewDocName.setText("CHECK YOUR NETWORK CONNECTION");
                }
            }
        });
    }
}