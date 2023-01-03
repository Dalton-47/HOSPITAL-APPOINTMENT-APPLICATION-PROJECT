package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientMedicalReport extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        getReport(myUsersEmail);
    }

    TextView textViewTitle,textViewDoctorName,textViewResult,textViewConclusion;
    String myUsersEmail;
    DatabaseReference myDatabaseReports;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_report);

        myDatabaseReports= FirebaseDatabase.getInstance().getReference("MedicalReport");
        Intent intent =getIntent();
        myUsersEmail=intent.getExtras().getString("usersEmail");

        textViewTitle=(TextView) findViewById(R.id.textViewTitleMedicalPATIENT);
        textViewDoctorName=(TextView) findViewById(R.id.textViewDoctorNameMedicalPATIENT);
        textViewResult=(TextView) findViewById(R.id.textViewtestResultMedicalPATIENT);
        textViewConclusion=(TextView) findViewById(R.id.textViewConclusionMedicalPATIENT);


    }

    private void getReport(String myKey)
    {
        String emailKey ="";
        int Counter=myKey.length();

        for(int a=0; a<Counter; a++)
        {
            if(myKey.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+myKey.charAt(a);

            }
        }

        emailKey=emailKey.trim();


       myDatabaseReports.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {

               if(task.isSuccessful())
               {
                   if(task.getResult().exists())
                   {
                       DataSnapshot thisDataSnapshot=task.getResult();
                       String title=String.valueOf(thisDataSnapshot.child("title").getValue());
                       String doctorName=String.valueOf(thisDataSnapshot.child("doctorName").getValue());
                       String testResult=String.valueOf(thisDataSnapshot.child("testResult").getValue());
                       String conclusion=String.valueOf(thisDataSnapshot.child("conclusion").getValue());

                       textViewDoctorName.setText(doctorName);
                       textViewTitle.setText(title);
                       textViewResult.setText(testResult);
                       textViewConclusion.setText(conclusion);

                   }
                   else
                   {
                       String notYet="NOT AVAILABLE";
                       textViewDoctorName.setText(notYet);
                       textViewTitle.setText(notYet);
                       textViewResult.setText(notYet);
                       textViewConclusion.setText(notYet);
                   }
               }
           }
       });
    }
}