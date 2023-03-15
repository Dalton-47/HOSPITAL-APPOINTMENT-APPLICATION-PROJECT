package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Patient_Appointment_Booking_New extends AppCompatActivity {
   RecyclerView myRecyclerView;
   DatabaseReference userRef,assignedDoc;
    String myUserID;
   private ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();
   private ArrayList<myDoctor> assignedDoctorList=new ArrayList<>();
    Patients_Appointments_Adapter myAdapter;
    String firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_booking_new);

        Intent intent =getIntent();
         myUserID=getIntent().getStringExtra("userID");
          firstname=getIntent().getStringExtra("firstname");

       // Toast.makeText(this,"Your ID is "+myUserID ,Toast.LENGTH_SHORT).show();


        userRef= FirebaseDatabase.getInstance().getReference("Appointments");
        assignedDoc=FirebaseDatabase.getInstance().getReference("AssignedDoctor").child(myUserID);

        TextView textViewUserName_new=(TextView)  this.findViewById(R.id.textViewUserName_new);
        textViewUserName_new.setText(firstname+" Appointments");


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
                if(appointmentsList.size()==0)
                {
                    Intent myIntent=new Intent(Patient_Appointment_Booking_New.this,GetAppointmentDetails.class);
                    myIntent.putExtra("userID",myUserID);
                    startActivity(myIntent);
                }
                else if(Objects.equals(firstname, "null"))
                {
                    Toast.makeText(Patient_Appointment_Booking_New.this, "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Patient_Appointment_Booking_New.this, "You Have a Pending Appointment", Toast.LENGTH_SHORT).show();

                }

            }
        });


         getAppointmentDetails();


             getAssignedDoctor();




    }

    void getAppointmentDetails()
    {

       userRef.child(myUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PatientAppointmentData app= snapshot.getValue(PatientAppointmentData.class);
                assert app!=null;
                appointmentsList.add(app);


                myAdapter.setData(appointmentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Patient_Appointment_Booking_New.this, "Error Loading Data", Toast.LENGTH_SHORT).show();

            }
        });



    }

    void getAssignedDoctor()
    {
        assignedDoc.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DataSnapshot> task) {
                  if(task.isSuccessful())
                  {
                      if(task.getResult().exists())
                      {
                          DataSnapshot childSnapshot=task.getResult();
                          myDoctor doc=childSnapshot.getValue(myDoctor.class);
                          //assert doc != null;
                          // assignedDoctorList.add(doc);
                          myAdapter.setDoc(doc.getName(),doc.getEmail(),doc.getPhoneNumber());
                      }
                      else
                      {
                          myAdapter.setDoc("null","null","null");
                      }

                  }
                  else
                  {
                      assignedDoctorList=null;
                  }

                 // myAdapter.setDoc(assignedDoctorList);
              }
          });
    }

}