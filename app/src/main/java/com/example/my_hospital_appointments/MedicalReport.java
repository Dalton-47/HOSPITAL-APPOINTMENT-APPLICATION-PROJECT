package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;

public class MedicalReport extends AppCompatActivity {

    EditText editTextTitle,editTextDoctorName,editTextResult,editTextConclusion;
    private ArrayList<String> myArrayList=new ArrayList<String>();
    DatabaseReference myReport;
    private String patientEmail;
    DatabaseReference Reports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_report);

        editTextTitle=(EditText) findViewById(R.id.editTextTextTitleMedical);
        editTextDoctorName=(EditText) findViewById(R.id.editTextTextDoctorNameMedical);
        editTextResult=(EditText) findViewById(R.id.editTextTextTestResultMedical);
        editTextConclusion=(EditText) findViewById(R.id.editTextTextConclusionMedical);

        Reports=FirebaseDatabase.getInstance().getReference("MedicalReport");

        myArrayList.add("Select One");
        myReport = FirebaseDatabase.getInstance().getReference().child("Patients");
        myReport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    myArrayList.add(snapshot.child("emailAddress").getValue().toString());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerEmails=(Spinner) findViewById(R.id.spinnerPatientMedical);

        spinnerEmails.setAdapter(adapter1);
        spinnerEmails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String myText=spinnerEmails.getSelectedItem().toString().trim();
                if(myText=="Select One".trim())
                {
                    //Nothing

                }
                else
                {
                    Toast.makeText(MedicalReport.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    patientEmail =myText;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button myReport=(Button) findViewById(R.id.buttonSendReportMedical);
        myReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patientEmail.isEmpty())
                {
                    Toast.makeText(MedicalReport.this, "FIRST SELECT A PATIENT FROM ABOVE", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setReport(patientEmail);
                }

            }
        });
    }

    private void setReport(String myKey)
    {

        String emailKey="";
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

        String title=editTextTitle.getText().toString().trim();
        String doctorName=editTextDoctorName.getText().toString().trim();
        String testResult=editTextResult.getText().toString().trim();
        String  conclusion=editTextConclusion.getText().toString().trim();

        if(title.isEmpty())
        {
            editTextTitle.setError("REPORT TITLE REQUIRED");
            editTextTitle.requestFocus();
        }
        else if(doctorName.isEmpty())
        {
            editTextDoctorName.setError("DOCTOR NAME REQUIRED");
            editTextDoctorName.requestFocus();
        }
        else if(testResult.isEmpty())
        {
            editTextResult.setError("TEST RESULT CANNOT BE BLANK");
            editTextResult.requestFocus();
        }
        else if(conclusion.isEmpty())
        {
            editTextConclusion.setError("CONCLUSION IS REQUIRED");
            editTextConclusion.requestFocus();
        }
        else
        {
            String finalEmailKey = emailKey;
            Reports.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        //String title, String doctorName, String testResult, String conclusion
                        ReportClass myReportsClass=new ReportClass(title,doctorName,testResult,conclusion);
                        Reports.child(finalEmailKey).setValue(myReportsClass);
                        //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MedicalReport.this, "REPORT SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}