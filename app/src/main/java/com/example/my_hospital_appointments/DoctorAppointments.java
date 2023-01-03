package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorAppointments extends AppCompatActivity {

    private ArrayList<String> myArrayList=new ArrayList<String>();
    DatabaseReference myDoctorsData;
    DatabaseReference attendedAppointmentsData;
    Spinner spinnerEmails;
    public String patientEmail="";
    public TextView textViewPatientName, textViewPatientDescription, textViewPatientDate, textViewPatientTime;
    Button btnViewAppointments,btnCancelAppointments,btnAttendAppointments,backHomeAppointments;
    private String patientName,patientDescription,patientDate,patientTime;
    RelativeLayout myLayoutAttend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        myLayoutAttend=(RelativeLayout)  findViewById(R.id.relativeLayoutViewAttend);
        attendedAppointmentsData=FirebaseDatabase.getInstance().getReference("AttendedAppointments");

        textViewPatientName =(TextView)  findViewById(R.id.textViewPatientNameDoctorAppointments2);
        textViewPatientDescription =(TextView)  findViewById(R.id.textViewPatientDescriptionDoctorAppointments2);
        textViewPatientDate =(TextView)  findViewById(R.id.textViewPatientDateDoctorAppointments2);
        textViewPatientTime =(TextView)  findViewById(R.id.textViewPatientTimeDoctorAppointments2);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");

        spinnerEmails =(Spinner) findViewById(R.id.spinnerDoctorAppointments);

        myArrayList.add("Select One");

        myDoctorsData = FirebaseDatabase.getInstance().getReference().child("AssignedPatient");
        myDoctorsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    myArrayList.add(snapshot.child("email").getValue().toString());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEmails.setAdapter(adapter1);
        spinnerEmails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String myText=spinnerEmails.getSelectedItem().toString().trim();
                if(myText=="Select One".trim())
                {
                    //Nothing

                }
                else
                {
                    Toast.makeText(DoctorAppointments.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    patientEmail=myText;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnViewAppointments=(Button) findViewById(R.id.buttonDoctorViewAppointments);
        btnViewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAppointment(myUsersEmail);
            }
        });

        btnAttendAppointments=(Button) findViewById(R.id.buttonApointmentAttendedDoctor);
        btnAttendAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendedAppointments(myUsersEmail);
            }
        });

        btnCancelAppointments=(Button) findViewById(R.id.buttonCancelAppointmentDoctor);
        btnCancelAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDoctorsData.child(myUsersEmail).removeValue();
                Toast.makeText(DoctorAppointments.this,"APPOINTMENT CANCELLED SUCCESSSFULLY",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ViewAppointment(String doctorEmail)
    {

        String emailKey="";
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

        myDoctorsData.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        // public TextView patientName,patientDescription,patientDate,patientTime;
                        DataSnapshot thisDataSnapshot=task.getResult();
                         patientName=String.valueOf(thisDataSnapshot.child("name").getValue());
                         patientDescription=String.valueOf(thisDataSnapshot.child("description").getValue());
                         patientDate=String.valueOf(thisDataSnapshot.child("date").getValue());
                         patientTime=String.valueOf(thisDataSnapshot.child("time").getValue());

                        textViewPatientName.setText(patientName);
                        textViewPatientDate.setText(patientDate);
                        textViewPatientDescription.setText(patientDescription);
                        textViewPatientTime.setText(patientTime);
                    }
                }
            }
        });

    }

    public void AttendedAppointments(String doctorEmail)
    {

        String emailKey="";
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

        //String id=databaseAppointments.push().getKey(); //unique path that identifies the location of the data
        String id=attendedAppointmentsData.push().getKey();
        //patientName,patientDescription,patientDate,patientTime
        //String patientEmail, String doctorEmail, String description, String time, String date
        AppointmentsAttended myAppointments=new AppointmentsAttended(patientEmail,doctorEmail,patientDescription,patientTime,patientDate);
        String finalEmailKey = emailKey;
        attendedAppointmentsData.child(id).setValue(myAppointments).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    myDoctorsData.child(finalEmailKey).removeValue();
                    btnViewAppointments.setVisibility(View.GONE);
                    btnAttendAppointments.setVisibility(View.GONE);
                    btnCancelAppointments.setVisibility(View.GONE);
                    myLayoutAttend.setVisibility(View.VISIBLE);
                    backHomeAppointments=(Button) findViewById(R.id.buttonBackAfterAppointments);
                    backHomeAppointments.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent=new Intent(DoctorAppointments.this,MainPageDoctor.class);
                            startActivity(myIntent);
                        }
                    });
                }
            }
        });
    }
}