package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientRegistration extends AppCompatActivity {
    private FirebaseAuth  patientAuth;
    private EditText patientAge,patientCounty,patientFirstName, patientSecondName,patientPhoneNumber,patientEmailAddress,patientPassword;
    public String firstName,secondName,phoneNumber,emailAddress,password,age,county;
    private Button registerPatient;
    DatabaseReference patientDatabase;
    private String patientKey="";
;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patientDatabase= FirebaseDatabase.getInstance().getReference("Patients");

        patientAuth = FirebaseAuth.getInstance();

        patientAge =(EditText)  findViewById(R.id.editTextTextPatientAge);
        patientCounty =(EditText)  findViewById(R.id.editTextTextPatientCounty);
        patientFirstName =(EditText)  findViewById(R.id.editTextPatientFName);
        patientSecondName =(EditText) findViewById(R.id.editTextTextPatientSName);
        patientPhoneNumber =(EditText) findViewById(R.id.editTextPatientPhoneNumber);
        patientEmailAddress=(EditText) findViewById(R.id.editTextTextPatientEmailAddress);
        patientPassword=(EditText) findViewById(R.id.editTextTextPatientPassword);
        registerPatient=(Button) findViewById(R.id.btnRegisterPatient);
        registerPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPatient();
            }
        });


    }

    private void registerPatient()
    {
        firstName=patientFirstName.getText().toString().trim();
        secondName=patientSecondName.getText().toString().trim();
        phoneNumber=patientPhoneNumber.getText().toString().trim();
        emailAddress=patientEmailAddress.getText().toString().trim();
        password=patientPassword.getText().toString().trim();
        age=patientAge.getText().toString().trim();
        county=patientCounty.getText().toString().trim();



        if(firstName.isEmpty())
        {
            patientFirstName.setError("Cannot be blank");
            patientFirstName.requestFocus();
        }
        else if(secondName.isEmpty())
        {
            patientSecondName.setError("Cannot be Blank");
            patientSecondName.requestFocus();
        }
        else if(age.isEmpty())
        {
            patientAge.setError("Cannot Be Blank");
            patientAge.requestFocus();
        }
        else if(county.isEmpty())
        {
            patientCounty.setError("Cannot Be Blank");
            patientCounty.requestFocus();
        }
        else if(phoneNumber.isEmpty())
        {
            patientPhoneNumber.setError("Cannot Be Blank");
            patientPhoneNumber.requestFocus();
        }
        else if(emailAddress.isEmpty())
        {
            patientEmailAddress.setError("Cannot Be Blank");
            patientEmailAddress.requestFocus();
        }
        else if(password.isEmpty())
        {
            patientPassword.setError("Cannot Be Blank");
            patientPassword.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            patientEmailAddress.setError("Please Provide a Valid Email Address");
            patientEmailAddress.requestFocus();
        }
        else if(password.length()<8)
        {
            patientPassword.setError("Minimum Password Characters Should Be 8");
            patientPassword.requestFocus();
        }
        else
        {
            patientAuth.createUserWithEmailAndPassword(emailAddress,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {//  public String firstName,secondName,phoneNumber,emailAddress,password;
                        int Counter=emailAddress.length();
                        for(int a=0; a<Counter; a++)
                        {
                            if(emailAddress.charAt(a)=='@')
                            {
                                break;
                            }
                            else
                            {
                                patientKey=patientKey+emailAddress.charAt(a);
                            }
                        }
                        patientKey=patientKey.trim();
                        Patients myPatients=new Patients(firstName,secondName,phoneNumber,emailAddress,age,county);
                        patientDatabase.child(patientKey).setValue(myPatients);

                        Toast.makeText(PatientRegistration.this,"REGISTRATION WAS SUCCESSFUL",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(PatientRegistration.this,"!!!!!ERROR DURING REGISTRATION",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}