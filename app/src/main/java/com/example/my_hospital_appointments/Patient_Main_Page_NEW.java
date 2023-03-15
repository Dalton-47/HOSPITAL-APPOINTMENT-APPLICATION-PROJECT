package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Patient_Main_Page_NEW extends AppCompatActivity {
   ImageView userProfile;
    String userID="";
    DatabaseReference myReference,patientRefAppointment;
    String firstname;
    TextView welcomeUser,descriptionTextView,dateTextView,timeTextView,ageTextView,textViewDate;
    String description,date,time,age;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    View view60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_page_new);

        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        textViewDate = (TextView)  this.findViewById(R.id.textViewPatientMainDate_NEW);

        Date currentDate= new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd, MMMM, yyyy");
        String formattedDate= dateFormat.format(currentDate);

        textViewDate.setText(formattedDate);

        view60 = (View)  this.findViewById(R.id.view60);

       // progressBar.setVisibility(View.INVISIBLE);
        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
       //  Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();

         welcomeUser =(TextView)  this.findViewById(R.id.textViewPatientMainPageWelcome);


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

        myReference =FirebaseDatabase.getInstance().getReference("Patients").child(userID);
        patientRefAppointment =FirebaseDatabase.getInstance().getReference("Appointments").child(userID);

        userProfile =(ImageView)  findViewById(R.id.imageViewPatientProfileNew);


        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_Main_Page_NEW.this,Patient_User_Profile_Activity.class) );
            }
        });

        descriptionTextView=(TextView) this.findViewById(R.id.textViewPatientDescriptionMainset);
        dateTextView =(TextView)  this.findViewById(R.id.textViewPatientDateset);
        timeTextView =(TextView)  this.findViewById(R.id.textViewPatientTimeset);
        ageTextView =(TextView)  this.findViewById(R.id.textViewPatientAgeset);

        if(firebaseUser!=null)
        {
            // Set User DP (After user has uploaded)
            Uri uri = firebaseUser.getPhotoUrl();

            if(uri!=null)
            {
                // ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
                view60.setBackground(getResources().getDrawable(R.drawable.white_background_circle));
                Picasso.get().load(uri)
                        .transform(new RoundedTransformation())
                        .into(userProfile);
            }
            else
            {
                view60.setBackground(getResources().getDrawable(R.drawable.white_background_circle));

                userProfile.setBackground(getResources().getDrawable(R.drawable.user_error));

            }


        }
        else
        {
            view60.setBackground(getResources().getDrawable(R.drawable.white_background_circle));
            userProfile.setBackground(getResources().getDrawable(R.drawable.user_error));


        }


        readUserName(); //set userName to the main page for personalization
        readAppointment();//set upcoming appointment to users screen

       Button bookAppointments=(Button) this.findViewById(R.id.button_Book_Appointments_New);

        bookAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(Patient_Main_Page_NEW.this,Patient_Appointment_Booking_New.class);
               Bundle bundle = new Bundle();
               bundle.putString("firstname",firstname);
               bundle.putString("userID",userID);
                myIntent.putExtras(bundle);
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

        //button to open Patient's History
        String checkUser = "Patient";
        Button btnPatientHistory =(Button) this.findViewById(R.id.buttonPatientHistory_NEW);
        btnPatientHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(Patient_Main_Page_NEW.this,Doctor_History_New.class);
                myIntent.putExtra("checkUser",checkUser);
                startActivity(myIntent);
            }
        });


        Button btnPatientReports=(Button)  this.findViewById(R.id.buttonPatientReportsNew);
        btnPatientReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_Main_Page_NEW.this, Patient_Report_Activity_New.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            // Set User DP (After user has uploaded)
            Uri uri = firebaseUser.getPhotoUrl();

            // ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
            Picasso.get().load(uri)
                    .transform(new RoundedTransformation())
                    .into(userProfile);
        }
        else
        {
            Picasso.get()
                    .load(R.drawable.user_error)
                    .transform(new RoundedTransformation())
                    .into(userProfile);

        }

    }

    void readUserName()
    {
        myReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    DataSnapshot thisDataSnapshot=task.getResult();
                     firstname=String.valueOf(thisDataSnapshot.child("firstName").getValue());
                   // Toast.makeText(Patient_Main_Page_NEW.this," FIRSTNAME "+firstname ,Toast.LENGTH_SHORT).show();
                    if(firstname.equals("null"))
                    {
                        welcomeUser.setText("Kindly Register With Us.");
                    }
                    else
                    {
                        welcomeUser.setText("Hello "+firstname+".");
                    }


                }
                else
                {
                    welcomeUser.setText("Hello User Check Network!");
                }

            }
        });
    }

    void readAppointment()
    {
        patientRefAppointment.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {

                    DataSnapshot thisDataSnapshot=task.getResult();
                     description=String.valueOf(thisDataSnapshot.child("description").getValue());
                     date=String.valueOf(thisDataSnapshot.child("date").getValue());
                     time=String.valueOf(thisDataSnapshot.child("time").getValue());
                     age=String.valueOf(thisDataSnapshot.child("age").getValue());

                     if(Objects.equals(description, "null"))
                     {

                     }
                     else
                     {
                         descriptionTextView.setText(description);
                         dateTextView.setText("Date : "+date);
                         timeTextView.setText("Time : "+time);
                         ageTextView.setText("Age :  "+age+" Years");
                     }


                }
            }
        });
    }
}