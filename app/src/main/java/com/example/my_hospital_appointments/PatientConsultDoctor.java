package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class PatientConsultDoctor extends AppCompatActivity {

    private ArrayList<String> myArrayList=new ArrayList<String>();
    String myDoctorEmail;
    DatabaseReference messageDatabase;
    TextView textViewPatient,textViewDoctor;
    EditText editTextPatientMessage;
    private String myUsersEmail;
    private String doctorEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_consult_doctor);
        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        myUsersEmail=extras.getString("usersEmail");
        doctorEmail=extras.getString("doctorEmail");


        editTextPatientMessage =(EditText)  findViewById(R.id.editTextTextDoctorMessage);

       // Toast.makeText(this,"YOU SELECTED "+doctorEmail+" TO CONSULT",Toast.LENGTH_SHORT).show();
        textViewPatient=(TextView)  findViewById(R.id.textViewPatientActivityPatientConsultationPATIENT);
        textViewDoctor=(TextView)  findViewById(R.id.textViewPatientActivityConsultationMessageDOCTOR);
        myArrayList.add("Select One");

        // messageDatabase = FirebaseDatabase.getInstance().getReference().child("PatientMessage");

        messageDatabase = FirebaseDatabase.getInstance().getReference().child("DoctorMessage");
        messageDatabase.addValueEventListener(new ValueEventListener() {
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

        Spinner spinnerEmails=(Spinner) findViewById(R.id.spinnerDoctorEmailsMessagePATIENT);

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
                    Toast.makeText(PatientConsultDoctor.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    myDoctorEmail =myText;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button sendMessage=(Button) findViewById(R.id.buttonSendMessagePATIENT);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPatientMessage(myUsersEmail);
            }
        });

        Button getMessage=(Button) findViewById(R.id.btnChatConsultationPATIENT);
        getMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDoctorMessage(myDoctorEmail);
            }
        });

    }

    private void getDoctorMessage(String myKey)
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
            Toast.makeText(PatientConsultDoctor.this,"KINDLY SELECT AN EMAIL TO VIEW THE PATIENTS MESSAGE",Toast.LENGTH_SHORT ).show();

        }
        else
        {
            messageDatabase.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot myDataSnapshot =task.getResult();
                            String patientMessage=String.valueOf(myDataSnapshot.child("message").getValue());
                            if(patientMessage.isEmpty())
                            {
                                textViewDoctor.setText("NO MESSAGE SENT");
                            }
                            else
                            {
                                textViewDoctor.setText(patientMessage);
                            }


                        }
                    }
                }
            });
        }
    }

    private void sendPatientMessage(String patientEmail)
    {
        //boilerplatecode
        String myText= editTextPatientMessage.getText().toString().trim();
        String emailKey="";
        int Counter=patientEmail.length();
        for(int a=0; a<Counter; a++)
        {
            if(patientEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+patientEmail.charAt(a);
            }
        }
        emailKey=emailKey.trim();

        if(myText.isEmpty())
        {
            editTextPatientMessage.setError("TYPE A MESSAGE");
            editTextPatientMessage.requestFocus();
        }
        else
        {
            DatabaseReference myPatientDatabase;
            myPatientDatabase=FirebaseDatabase.getInstance().getReference("PatientMessage");
            String finalEmailKey = emailKey;
            myPatientDatabase.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        doctorMessages myMessage=new doctorMessages(myText,myUsersEmail);
                        myPatientDatabase.child(finalEmailKey).setValue(myMessage);
                        editTextPatientMessage.setText("");
                        textViewPatient.setText(myText);
                    }
                }
            });
        }
    }
}