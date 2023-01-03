package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generation);

       // myArrayList.add("Select One");
        myReport = FirebaseDatabase.getInstance().getReference().child("Doctors");
        myReport.addValueEventListener(new ValueEventListener() {
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

        myPatients = FirebaseDatabase.getInstance().getReference().child("Patients");
        myPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    myPatientsArrayList.add(snapshot.child("emailAddress").getValue().toString());
                }

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

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        int AssignmentCount=myAppointmentsArrayList.size();
        int PatientsCount=myPatientsArrayList.size();
        int DoctorsCount=myArrayList.size();
        TextView doctors=(TextView) findViewById(R.id.textViewDoctorsRegistered);
        TextView patients=(TextView) findViewById(R.id.textViewPatientsRegistered);
        TextView appointments=(TextView) findViewById(R.id.textViewAppointmentsAtttended);
        doctors.setText(Integer.toString(DoctorsCount));
        patients.setText(Integer.toString(PatientsCount));
        appointments.setText(Integer.toString(AssignmentCount));

        ImageView myImage=(ImageView) findViewById(R.id.imageViewReports);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;

                doctors.setText(Integer.toString(counter));
                patients.setText(Integer.toString(counter));
                appointments.setText(Integer.toString(counter));

            }
        });

    }
}