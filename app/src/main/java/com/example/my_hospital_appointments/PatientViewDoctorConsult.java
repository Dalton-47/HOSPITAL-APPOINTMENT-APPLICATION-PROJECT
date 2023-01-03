package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientViewDoctorConsult extends AppCompatActivity {
    private ArrayList<String> myArrayList=new ArrayList<String>();
    DatabaseReference myDoctorsData;
    Spinner spinnerDoctorEmails;
    String doctorEmail;
    TextView textViewDoctorName,textViewEmail,textViewPhone,textViewDepartment;
    private String myUsersEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_doctor_consult);

        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");
       // Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();

        textViewDoctorName=(TextView)  findViewById(R.id.textViewDoctorConsultName3);
        textViewEmail=(TextView)  findViewById(R.id.textViewDoctorConsultEmail3);
        textViewPhone=(TextView)  findViewById(R.id.textViewDoctorConsultPhone3);
        textViewDepartment=(TextView)  findViewById(R.id.textViewDoctorConsultDepartment3);
        myArrayList.add("Select One");

        myDoctorsData=FirebaseDatabase.getInstance().getReference().child("Doctors");

        myDoctorsData.addValueEventListener(new ValueEventListener() {
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

        spinnerDoctorEmails =(Spinner) findViewById(R.id.spinnerDoctorEmails3);


        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myArrayList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDoctorEmails.setAdapter(adapter2);
        spinnerDoctorEmails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String myText2= spinnerDoctorEmails.getSelectedItem().toString().trim();
                if(myText2=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                    Toast.makeText(PatientViewDoctorConsult.this,"You have Selected "+myText2,Toast.LENGTH_SHORT).show();
                    doctorEmail=myText2;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button viewDetails=(Button) findViewById(R.id.buttonViewDoctorDetails3);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDoctorDetails(doctorEmail);
            }
        });

        Button consultDoctor=(Button) findViewById(R.id.buttonConsultDoctor);
        consultDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent=new Intent(PatientViewDoctorConsult.this, PatientConsultDoctor.class);
                Bundle extras = new Bundle();
                extras.putString("usersEmail",myUsersEmail);
                extras.putString("doctorEmail",doctorEmail);
                myIntent.putExtras(extras);
                startActivity(myIntent);
            }
        });
    }

    private void getDoctorDetails(String doctorKey)
    {

        String emailKey="";
        int Counter=doctorKey.length();
        for(int a=0; a<Counter; a++)
        {
            if(doctorKey.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+doctorKey.charAt(a);
            }
        }
        emailKey=emailKey.trim();

        if(emailKey.isEmpty())
        {
            Toast.makeText(PatientViewDoctorConsult.this,"KINDLY SELECT AN EMAIL TO VIEW THE DOCTOR'S DATA",Toast.LENGTH_SHORT ).show();

        }
        else
        {
          //  Toast.makeText(PatientAppointmentAdmin.this,"YOUR KEY = "+doctorKey,Toast.LENGTH_SHORT ).show();

            myDoctorsData.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot myDataSnapshot =task.getResult();
                            String doctorName=String.valueOf(myDataSnapshot.child("userName").getValue());
                            String doctorEmail=String.valueOf(myDataSnapshot.child("email").getValue());
                            String doctorPhone=String.valueOf(myDataSnapshot.child("phoneNumber").getValue());
                            String doctorDepartment=String.valueOf(myDataSnapshot.child("department").getValue());

                            // TextView textViewDoctorName,textViewEmail,textViewPhone,textViewDepartment;
                            textViewDoctorName .setText(doctorName);
                            textViewEmail.setText(doctorEmail);
                            textViewPhone.setText(doctorPhone);
                            textViewDepartment.setText(doctorDepartment);



                        }
                        else
                        {

                            Toast.makeText(PatientViewDoctorConsult.this,"ERROR!!! EMAIL DOESN'T EXIST",Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else
                    {

                        Toast.makeText(PatientViewDoctorConsult.this,"ERROR WHILE READING THE SELECTED DOCTOR'S EMAIL",Toast.LENGTH_SHORT ).show();

                    }
                }
            });
        }

    }
}