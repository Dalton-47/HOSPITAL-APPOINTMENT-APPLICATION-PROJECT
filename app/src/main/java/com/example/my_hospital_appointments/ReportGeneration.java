package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportGeneration extends AppCompatActivity {

    private final ArrayList<String> myArrayList=new ArrayList<String>();
    private final ArrayList<String> myPatientsArrayList=new ArrayList<String>();
    private final ArrayList<String> myAppointmentsArrayList=new ArrayList<String>();
    DatabaseReference myReport;
    DatabaseReference  myPatients;
    DatabaseReference myAssignments;
    int counter=0;
    int DoctorsCount,PatientsCount,AssignmentCount;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generation);

        progressBar =(ProgressBar)  this.findViewById(R.id.progressBarAdminReports);
        TextView doctors=(TextView) findViewById(R.id.textViewDoctorsRegistered);
        TextView patients=(TextView) findViewById(R.id.textViewPatientsRegistered);
        TextView appointments=(TextView) findViewById(R.id.textViewAppointmentsAtttended);

progressBar.setVisibility(View.VISIBLE);

        // myArrayList.add("Select One");
        myReport = FirebaseDatabase.getInstance().getReference().child("Doctors");
        myReport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    myArrayList.add(snapshot.child("email").getValue().toString());
                }
                 DoctorsCount=myArrayList.size();
                String numberOfDocs=Integer.toString(DoctorsCount);
                doctors.setText(numberOfDocs);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myPatients = FirebaseDatabase.getInstance().getReference().child("Patients");
        myPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    myPatientsArrayList.add(snapshot.child("emailAddress").getValue().toString());
                }
                 PatientsCount=myPatientsArrayList.size();
                String numberOfPatients=Integer.toString(PatientsCount);
                patients.setText(numberOfPatients);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAssignments = FirebaseDatabase.getInstance().getReference().child("AssignedDoctor");
        myAssignments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    myAppointmentsArrayList.add(snapshot.child("email").getValue().toString());
                }
                progressBar.setVisibility(View.GONE);
                 AssignmentCount=myAppointmentsArrayList.size();
                String numberOfAssignments=Integer.toString(AssignmentCount);
                appointments.setText(numberOfAssignments);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                  progressBar.setVisibility(View.GONE);
            }
        });







        ImageView myImage=(ImageView) findViewById(R.id.imageViewReports);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
/*
                doctors.setText(Integer.toString(counter));
                patients.setText(Integer.toString(counter));
                appointments.setText(Integer.toString(counter));

 */

            }
        });

    }
}