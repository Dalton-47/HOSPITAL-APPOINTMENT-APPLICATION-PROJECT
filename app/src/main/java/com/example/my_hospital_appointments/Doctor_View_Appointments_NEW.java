package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Doctor_View_Appointments_NEW extends AppCompatActivity {
    ArrayList <myPatient> patientAppointmentsList=new ArrayList<myPatient>();
    DatabaseReference appointmentsRef;
    Doctor_Appointments_Adapter myAdapter;
    RelativeLayout relativeLayout;
    EditText editTextReportTitle,editTextReportContent;
    Button btnSendReport;
     String  reportDate,docName,titleOriginal,reportContentOriginal;
     DatabaseReference docRef;
     ProgressBar progressBar;
     TextView textViewPatientName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_appointments_new);


           relativeLayout = (RelativeLayout)  this.findViewById(R.id.relativeLayoutDocReport_NEW);
           editTextReportTitle =(EditText)  this.findViewById(R.id.editTextTextReportTitle_NEW);
           editTextReportContent = (EditText)  this.findViewById(R.id.editTextTextrReportContent_NEW);
           btnSendReport = (Button)  this.findViewById(R.id.buttonSendReport_NEW);
           progressBar = (ProgressBar)  this.findViewById(R.id.progressBarSaveReportsDOC);
           textViewPatientName = (TextView)  this.findViewById(R.id.textViewReportPatientName);


        Intent intent =getIntent();
        String userID=intent.getExtras().getString("docEmailId");
        Toast.makeText(this, "DOC ID : "+userID, Toast.LENGTH_SHORT).show();


        docRef = FirebaseDatabase.getInstance().getReference("Doctors").child(userID);
        appointmentsRef = FirebaseDatabase.getInstance().getReference("AssignedPatient").child(userID);

        //let us get the doctor's name
        getUserName();

        //let us get current Date
        Date currentDate= new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd, MMMM, yyyy");
        reportDate = dateFormat.format(currentDate);



        //we get the patients data
        getPatientData();
        RecyclerView myRecyclerView=(RecyclerView)  this.findViewById(R.id.recyclerViewDoctorAppointments);

         myAdapter=new Doctor_Appointments_Adapter(this,textViewPatientName,patientAppointmentsList,relativeLayout,userID,editTextReportTitle,editTextReportContent,titleOriginal,reportContentOriginal);
        myRecyclerView.setAdapter(myAdapter);

        LinearLayoutManager myLayout=new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayout);

         titleOriginal= editTextReportTitle.getText().toString().trim();
         reportContentOriginal=editTextReportContent.getText().toString().trim();
        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 myAdapter.setReportData(progressBar,editTextReportTitle,editTextReportContent,titleOriginal,reportContentOriginal,docName,reportDate);

            }
        });

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

    private void showErrorText() {
        Toast.makeText(Doctor_View_Appointments_NEW.this, "Data Not Saved!", Toast.LENGTH_SHORT).show();
    }



    void getUserName()
    {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    DataSnapshot snapshot=task.getResult();
                    docName =String.valueOf(snapshot.child("userName").getValue());

                }
                else
                {
                    Toast.makeText(Doctor_View_Appointments_NEW.this, "Err DOC : CHECK YOUR NETWORK CONNECTION", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}