package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class Patient_Appointment_Booking_New extends AppCompatActivity {
   RecyclerView myRecyclerView;
   DatabaseReference userRef,assignedDoc;
    String myUserID;
   private ArrayList<appointmentsNew> appointmentsList=new ArrayList<>();
    Patients_Appointments_Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_booking_new);

        Intent intent =getIntent();
         myUserID=intent.getExtras().getString("userID");
        Toast.makeText(this,"Your ID is "+myUserID ,Toast.LENGTH_SHORT).show();


        userRef= FirebaseDatabase.getInstance().getReference("Appointments").child(myUserID);
        assignedDoc=FirebaseDatabase.getInstance().getReference("AssignedDoctor").child(myUserID);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setStackFromEnd(true);//to set the recyclerView items to the last position
        myRecyclerView = (RecyclerView)  this.findViewById(R.id.recyclerViewPatientAppointments);
        myRecyclerView.setLayoutManager(layoutManager);
        myAdapter=new Patients_Appointments_Adapter(appointmentsList) ;
        myRecyclerView.setAdapter(myAdapter);

        Button newAppt = (Button)  this.findViewById(R.id.button_Add_New_Appointment_New);
        newAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(Patient_Appointment_Booking_New.this,GetAppointmentDetails.class);
                myIntent.putExtra("userID",myUserID);
                startActivity(myIntent);
            }
        });


        // getAppointmentDetails();



    }

    void getAppointmentDetails()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapShot: snapshot.getChildren())
                {
                    appointmentsNew app= childSnapShot.getValue(appointmentsNew.class);
                    appointmentsList.add(app);
                }

                myAdapter.setData(appointmentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Patient_Appointment_Booking_New.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}