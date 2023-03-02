package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Patient_Report_Activity_New extends AppCompatActivity {

TextView textViewPatientName,textViewPatientAge,textViewAppointmentDate,textViewAppointmentDescription;
TextView textViewReportDate,textViewReportTitle,textViewReportContent,textViewDocName;
DatabaseReference reportRef;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_report_new);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        String userEmail= firebaseUser.getEmail();
        String[] parts =userEmail.split("@");
        String emailID=parts[0];

        reportRef= FirebaseDatabase.getInstance().getReference("PatientReport").child(emailID);

        textViewPatientName = (TextView)  this.findViewById(R.id.textViewPatientName);
        textViewPatientAge = (TextView)  this.findViewById(R.id.textViewPatientAge);
        textViewAppointmentDate = (TextView)  this.findViewById(R.id.textViewAppointmentDate);
        textViewAppointmentDescription = (TextView)  this.findViewById(R.id.textViewAppointmentDescription);
        textViewDocName = (TextView)  this.findViewById(R.id.textViewDocName);
        textViewReportDate = (TextView)  this.findViewById(R.id.textViewReportDate);
        textViewReportTitle = (TextView)  this.findViewById(R.id.textViewReportTitle);
        textViewReportContent = (TextView)  this.findViewById(R.id.textViewReportContent);

        getAppointmentReport();

    }

    void getAppointmentReport()
    {
     reportRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DataSnapshot> task) {
             if(task.isSuccessful())
             {
                 if(task.getResult().exists())
                 {
                     DataSnapshot thisDataSnapshot=task.getResult();
                     textViewPatientName.setText(String.valueOf(thisDataSnapshot.child("patientName").getValue()));
                     textViewPatientAge.setText(String.valueOf(thisDataSnapshot.child("patientAge").getValue()));
                     textViewAppointmentDate.setText(String.valueOf(thisDataSnapshot.child("appointmentDate").getValue()));
                     textViewAppointmentDescription.setText(String.valueOf(thisDataSnapshot.child("appointmentDescription").getValue()));
                     textViewDocName.setText(String.valueOf(thisDataSnapshot.child("docName").getValue()));
                     textViewReportDate.setText(String.valueOf(thisDataSnapshot.child("reportDate").getValue()));
                     textViewReportTitle.setText(String.valueOf(thisDataSnapshot.child("reportTitle").getValue()));
                     textViewReportContent.setText(String.valueOf(thisDataSnapshot.child("reportContent").getValue()));


                 }
             }
         }
     });
    }
}