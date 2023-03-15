package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Doctor_History_New extends AppCompatActivity {

    DatabaseReference appointmentsHistoryRef_Attended,appointmentsHistoryRef_Cancelled;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewAttended, txtViewCancelled;
    String userEmail,thisUserType;
    int attendedCounter=0;
    int cancelledCounter=0;
    ArrayList <Appointment_Data_Class> appointmentList = new ArrayList<>();
    Doctor_Appointments_History_Adapter historyAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_history_new);

        Intent intent =getIntent();
         thisUserType=intent.getExtras().getString("checkUser");

           txtViewAttended =(TextView)  this.findViewById(R.id.textViewAppointmentsAttended_HISTORY);
           txtViewCancelled = (TextView)  this.findViewById(R.id.textViewAppointmentsCancelled_HISTORY);
           recyclerView = (RecyclerView)  this.findViewById(R.id.recyclerViewDocHistory);


        historyAdapter = new Doctor_Appointments_History_Adapter(appointmentList);
        recyclerView.setAdapter(historyAdapter);
        LinearLayoutManager myLayout=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayout);

           firebaseAuth =FirebaseAuth.getInstance();
           firebaseUser = firebaseAuth.getCurrentUser();


        if(firebaseUser!=null)
        {
             userEmail= firebaseUser.getEmail();

        }
        else {
            userEmail="Null";
        }

        if(Objects.equals(thisUserType, "Patient"))
        {
            checkPatientAppointments();
            getPatientAttendedAppointments();
        }
        else if(Objects.equals(thisUserType, "Doctor"))
        {
            checkDoctorAppointments();
            getDoctorAttendedAppointments();
        }


    }



    void checkDoctorAppointments()
    {


        if(!Objects.equals(userEmail, "Null"))
        {

            String [] parts= userEmail.split("@");
            String docEmailID= parts[0];

            appointmentsHistoryRef_Attended =  FirebaseDatabase.getInstance().getReference("Doctor Appointments History").child("Attended").child(docEmailID);
            appointmentsHistoryRef_Attended.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                       attendedCounter++;
                    }
                    String attended=Integer.toString(attendedCounter);
                    txtViewAttended.setText(attended);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //we now check the number of cancelled appointments
            appointmentsHistoryRef_Attended =  FirebaseDatabase.getInstance().getReference("Doctor Appointments History").child("Cancelled").child(docEmailID);
            appointmentsHistoryRef_Attended.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        cancelledCounter++;
                    }
                    String attended=Integer.toString(cancelledCounter);
                    txtViewCancelled.setText(attended);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
        else {
            Toast.makeText(Doctor_History_New.this, "CHECK NETWORK CONNECTION", Toast.LENGTH_SHORT).show();
        }

    }

    void getDoctorAttendedAppointments()
    {
        if(!Objects.equals(userEmail, "Null")) {
            String [] parts= userEmail.split("@");
            String docEmailID= parts[0];
            appointmentsHistoryRef_Attended =  FirebaseDatabase.getInstance().getReference("Doctor Appointments History").child("Attended").child(docEmailID);

               appointmentsHistoryRef_Attended.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot thisDataSnapshot : snapshot.getChildren())
                    {
                        Appointment_Data_Class  appointments=thisDataSnapshot.getValue(Appointment_Data_Class.class);
                        appointmentList.add(appointments);
                    }


                    historyAdapter.setData(appointmentList);
                    if(appointmentList.size()==0)
                    {
                        //relativeLayoutNoAppointments.setVisibility(View.VISIBLE);
                        Toast.makeText(Doctor_History_New.this, "No Appointments Booked ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    //
    void checkPatientAppointments()
    {
        if(!Objects.equals(userEmail, "Null"))
        {

            String [] parts= userEmail.split("@");
            String patientEmailID= parts[0];

            appointmentsHistoryRef_Attended =  FirebaseDatabase.getInstance().getReference("Patient Appointments History").child("Attended").child(patientEmailID);
            appointmentsHistoryRef_Attended.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        attendedCounter++;
                    }
                    String attended=Integer.toString(attendedCounter);
                    txtViewAttended.setText(attended);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //we now check the number of cancelled appointments
            appointmentsHistoryRef_Attended =  FirebaseDatabase.getInstance().getReference("Patient Appointments History").child("Cancelled").child(patientEmailID);
            appointmentsHistoryRef_Attended.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        cancelledCounter++;
                    }
                    String attended=Integer.toString(cancelledCounter);
                    txtViewCancelled.setText(attended);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
        else {
            Toast.makeText(Doctor_History_New.this, "CHECK NETWORK CONNECTION", Toast.LENGTH_SHORT).show();
        }

    }
    //

    void getPatientAttendedAppointments()
    {
        if(!Objects.equals(userEmail, "Null")) {
            String [] parts= userEmail.split("@");
            String patientEmailID= parts[0];
            appointmentsHistoryRef_Attended =  FirebaseDatabase.getInstance().getReference("Patient Appointments History").child("Attended").child(patientEmailID);

            appointmentsHistoryRef_Attended.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot thisDataSnapshot : snapshot.getChildren())
                    {
                        Appointment_Data_Class  appointments=thisDataSnapshot.getValue(Appointment_Data_Class.class);
                        appointmentList.add(appointments);
                    }


                    historyAdapter.setData(appointmentList);
                    if(appointmentList.size()==0)
                    {
                        //relativeLayoutNoAppointments.setVisibility(View.VISIBLE);
                        Toast.makeText(Doctor_History_New.this, "No Appointments Booked ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

}