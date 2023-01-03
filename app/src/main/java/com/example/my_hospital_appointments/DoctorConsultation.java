package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class DoctorConsultation extends AppCompatActivity {

    private ArrayList<String> myArrayList=new ArrayList<String>();
    String patientEmail;
    DatabaseReference messageDatabase;
    TextView textViewPatient,textViewDoctor;
    EditText editTextDoctorMessage;
    private String myUsersEmail;
    DatabaseReference myDoctorMessageDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_consultation);


        myDoctorMessageDatabase=FirebaseDatabase.getInstance().getReference("DoctorMessage");
        Intent intent =getIntent();
        myUsersEmail=intent.getExtras().getString("usersEmail");
        editTextDoctorMessage=(EditText)  findViewById(R.id.editTextTextDoctorMessage);

        textViewPatient=(TextView)  findViewById(R.id.textViewPatientConsultation);
        textViewDoctor=(TextView)  findViewById(R.id.textViewDoctorConsultation);
        myArrayList.add("Select One");

        messageDatabase = FirebaseDatabase.getInstance().getReference().child("PatientMessage");
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

        Spinner spinnerEmails=(Spinner) findViewById(R.id.spinnerPatientMessage);

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
                    Toast.makeText(DoctorConsultation.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    patientEmail=myText;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button myChat=(Button) findViewById(R.id.buttonChatConsultationDOCTOR);
        myChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPatientMessage(patientEmail);
            }
        });

        Button doctorSend=(Button) findViewById(R.id.buttonSendMessageDOCTOR);
        doctorSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDoctorMessage(myUsersEmail);
            }
        });

    }

    private void getPatientMessage(String myKey)
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
            Toast.makeText(DoctorConsultation.this,"KINDLY SELECT AN EMAIL TO VIEW THE PATIENTS MESSAGE",Toast.LENGTH_SHORT ).show();

        }
        else
        {
            messageDatabase.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot myDataSnapshot =task.getResult();
                            String patientMessage=String.valueOf(myDataSnapshot.child("message").getValue());
                            textViewPatient.setText(patientMessage);

                        }
                    }
                }
            });
        }
    }

    private void sendDoctorMessage(String doctorEmail)
    {
        String myText=editTextDoctorMessage.getText().toString().trim();
        String emailKey="";
        int Counter=doctorEmail.length();
        for(int a=0; a<Counter; a++)
        {
            if(doctorEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+doctorEmail.charAt(a);
            }
        }
        emailKey=emailKey.trim();

        if(myText.isEmpty())
        {
            editTextDoctorMessage.setError("TYPE A MESSAGE");
            editTextDoctorMessage.requestFocus();
        }
        else
        {

            String finalEmailKey = emailKey;
            myDoctorMessageDatabase.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        doctorMessages myMessage=new doctorMessages(myText,myUsersEmail);
                        myDoctorMessageDatabase.child(finalEmailKey).setValue(myMessage);
                        editTextDoctorMessage.setText("");
                        textViewDoctor.setText(myText);
                    }
                }
            });
        }
    }
}