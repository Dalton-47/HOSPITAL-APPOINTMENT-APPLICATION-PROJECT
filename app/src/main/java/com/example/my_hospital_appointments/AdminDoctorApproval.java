package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminDoctorApproval extends AppCompatActivity {
    private Spinner spinnerEmails;
    private ArrayList<String> myArrayList=new ArrayList<String>();
    DatabaseReference myDoctorsData;
    DatabaseReference doctorDatabase;
    public String patientEmail="";
    private TextView textViewName,textViewDepartment,textViewTime,textViewIDNumber,textViewPhoneNumber,textViewEmployeeNumber;

    public String doctorName,doctorEmployeeNumber,doctorDepartment,doctorTime,doctorID,doctorPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doctor_approval);

        doctorDatabase=FirebaseDatabase.getInstance().getReference("Doctors");

        textViewName=(TextView)  findViewById(R.id.textViewPatientTimeDoctorAppointments2);
        textViewDepartment=(TextView)  findViewById(R.id.textViewApprovalDepartment);
        textViewTime=(TextView)  findViewById(R.id.textViewApprovalTimeAvailable);
        textViewIDNumber=(TextView)  findViewById(R.id.textViewPatientNameDoctorAppointments2);
        textViewPhoneNumber=(TextView)  findViewById(R.id.textViewPatientDescriptionDoctorAppointments2);
        textViewEmployeeNumber=(TextView)  findViewById(R.id.textViewPatientDateDoctorAppointments2);

        spinnerEmails=(Spinner) findViewById(R.id.spinnerDoctorAppointments);

        myArrayList.add("Select One");

        myDoctorsData = FirebaseDatabase.getInstance().getReference().child("PendingDoctors");
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

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                    Toast.makeText(AdminDoctorApproval.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    patientEmail=myText;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button myView=(Button) findViewById(R.id.buttonDoctorViewAppointments);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!patientEmail.isEmpty()) {
                    doctorData(patientEmail) ;
                }
                else
                {
                    Toast.makeText(AdminDoctorApproval.this,"You have not selected any email",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button AcceptDoctor =(Button) findViewById(R.id.buttonApointmentAttendedDoctor);
        AcceptDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!patientEmail.isEmpty()) {
                    approveDoctor(patientEmail) ;
                }
                else
                {
                    Toast.makeText(AdminDoctorApproval.this,"You have not selected any email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void doctorData(String myKey)
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


        if(emailKey.isEmpty())
        {
            Toast.makeText(AdminDoctorApproval.this,"KINDLY SELECT AN EMAIL TO VIEW THE PATIENTS DATA",Toast.LENGTH_SHORT ).show();

        }
        else
        {

            //Toast.makeText(AdminDoctorApproval.this,"YOUR KEY = "+emailKey,Toast.LENGTH_SHORT ).show();

            myDoctorsData.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot myDataSnapshot =task.getResult();
                             doctorName=String.valueOf(myDataSnapshot.child("userName").getValue());
                             doctorEmployeeNumber=String.valueOf(myDataSnapshot.child("employeeNumber").getValue());
                             doctorDepartment=String.valueOf(myDataSnapshot.child("department").getValue());
                             doctorTime=String.valueOf(myDataSnapshot.child("time").getValue());
                             doctorID=String.valueOf(myDataSnapshot.child("id").getValue());
                             doctorPhoneNumber=String.valueOf(myDataSnapshot.child("phoneNumber").getValue());

                            textViewName.setText(doctorName);
                            textViewDepartment.setText(doctorDepartment);
                            textViewIDNumber.setText(doctorID);
                            textViewTime.setText(doctorTime);
                            textViewPhoneNumber.setText(doctorPhoneNumber);
                            textViewEmployeeNumber.setText(doctorEmployeeNumber);

                        }
                        else
                        {
                            Toast.makeText(AdminDoctorApproval.this,"ERROR!!! EMAIL DOESN'T EXIST",Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(AdminDoctorApproval.this,"ERROR WHILE READING THE SELECTED DOCTOR'S EMAIL",Toast.LENGTH_SHORT ).show();

                    }
                }
            });
        }
    }

    public void approveDoctor(String myKey)
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


        Doctors myDoctor=new Doctors (doctorID,doctorPhoneNumber,doctorEmployeeNumber,doctorName,myKey,doctorTime,doctorDepartment) ;
        String finalEmailKey = emailKey;
        doctorDatabase.child(emailKey).setValue(myDoctor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    myDoctorsData.child(finalEmailKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference doctorApprovalData;
                                doctorApprovalData= FirebaseDatabase.getInstance().getReference("DoctorApproval");
                                String Status="APPROVED";
                                DoctorApproval myDoctorApproval=new DoctorApproval(Status);
                                doctorApprovalData.child(finalEmailKey).setValue(myDoctorApproval);
                                Toast.makeText(AdminDoctorApproval.this,"DOCTOR APPROVED SUCCESSFULLY",Toast.LENGTH_SHORT ).show();
                            }
                        }
                    });

                }
            }
        });


    }

}