package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Objects;

public class Doctor_History_New extends AppCompatActivity {

    DatabaseReference appointmentsHistoryRef_Attended,appointmentsHistoryRef_Cancelled;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewAttended, txtViewCancelled;
    String userEmail;
    int attendedCounter=0;
    int cancelledCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_history_new);

           txtViewAttended =(TextView)  this.findViewById(R.id.textViewAppointmentsAttended_HISTORY);
           txtViewCancelled = (TextView)  this.findViewById(R.id.textViewAppointmentsCancelled_HISTORY);

           firebaseAuth =FirebaseAuth.getInstance();
           firebaseUser = firebaseAuth.getCurrentUser();


        if(firebaseUser!=null)
        {
             userEmail= firebaseUser.getEmail();
        }
        else {
            userEmail="Null";
        }

        checkAppointments();



    }

    void checkAppointments()
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
}