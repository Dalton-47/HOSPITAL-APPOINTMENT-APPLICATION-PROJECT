package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Doctor_View_Appointments_NEW extends AppCompatActivity {
    ArrayList <myPatient> patientAppointmentsList=new ArrayList<myPatient>();
    DatabaseReference appointmentsRef;
    Doctor_Appointments_Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_appointments_new);



        Intent intent =getIntent();
        String userID=intent.getExtras().getString("docEmailId");
        Toast.makeText(this, "DOC ID : "+userID, Toast.LENGTH_SHORT).show();

        appointmentsRef = FirebaseDatabase.getInstance().getReference("AssignedPatient").child(userID);

        getPatientData();
        RecyclerView myRecyclerView=(RecyclerView)  this.findViewById(R.id.recyclerViewDoctorAppointments);

         myAdapter=new Doctor_Appointments_Adapter(patientAppointmentsList);
        myRecyclerView.setAdapter(myAdapter);

        LinearLayoutManager myLayout=new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayout);


    }

    void getPatientData()
    {
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    myPatient appointments=childSnapshot.getValue(myPatient.class);
                    patientAppointmentsList.add(appointments);
                }
                myAdapter.setData(patientAppointmentsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}