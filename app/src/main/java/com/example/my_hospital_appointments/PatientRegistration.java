package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PatientRegistration extends AppCompatActivity {
    private FirebaseAuth  patientAuth;
    private EditText patientAge,patientCounty,patientFirstName, patientSecondName,patientPhoneNumber,patientEmailAddress,patientPassword;
    public String firstName,secondName,phoneNumber,emailAddress,password,age,county;
    private Button btnRegPatients;
    DatabaseReference patientsRef;
    private String patientKey="";
     ImageView patientImageSet;
     TextView txtViewSetMessage;
     Button btnProceed;
     ProgressBar progressBar;
     RelativeLayout relativeLayout;

;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patientsRef = FirebaseDatabase.getInstance().getReference("Patients");

        patientAuth = FirebaseAuth.getInstance();

        patientImageSet=(ImageView)  this.findViewById(R.id.imageViewPatientRegistration_NEW);
        txtViewSetMessage = (TextView)  this.findViewById(R.id.textViewPatientPatientRegistration_NEW);
        btnProceed = (Button)  this.findViewById(R.id.buttonPatientRegistration_NEW);
        progressBar =(ProgressBar)  this.findViewById(R.id.progressBarPatientReg_NEW);
        relativeLayout =(RelativeLayout)  this.findViewById(R.id.relativeLayoutPatient_NEW);


        patientAge =(EditText)  findViewById(R.id.editTextTextPatientAge);
        patientCounty =(EditText)  findViewById(R.id.editTextTextPatientCounty);
        patientFirstName =(EditText)  findViewById(R.id.editTextPatientFName);
        patientSecondName =(EditText) findViewById(R.id.editTextTextPatientSName);
        patientPhoneNumber =(EditText) findViewById(R.id.editTextPatientPhoneNumber);
        patientEmailAddress=(EditText) findViewById(R.id.editTextTextPatientEmailAddress);
        patientPassword=(EditText) findViewById(R.id.editTextTextPatientPassword);


        btnRegPatients =(Button) findViewById(R.id.btnRegisterPatient);
        btnRegPatients.setOnClickListener(new View.OnClickListener() {
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
            progressBar.setVisibility(View.VISIBLE);

            patientAuth.createUserWithEmailAndPassword(emailAddress,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Query query = patientsRef.orderByChild("emailAddress").equalTo(emailAddress);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email already exists in database

                                btnRegPatients.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                patientImageSet.setImageResource(R.drawable.computer);
                                txtViewSetMessage.setText("Error 105:User Email Found in Database!");
                                btnProceed.setText("RETRY");
                                relativeLayout.setVisibility(View.VISIBLE);
                                btnProceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        relativeLayout.setVisibility(View.GONE);
                                        btnRegPatients.setVisibility(View.VISIBLE);
                                        patientEmailAddress.setText("");
                                        patientEmailAddress.requestFocus();

                                    }
                                });



                            } else {
                                // Email does not exist in database, proceed with registration

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
                                    patientsRef.child(patientKey).setValue(myPatients);

                                    btnRegPatients.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    patientImageSet.setImageResource(R.drawable.check);
                                    txtViewSetMessage.setText("DATA SAVED SUCCESSFULLY!");
                                    btnProceed.setText("PROCEED");
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    btnProceed.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent myIntent=new Intent(PatientRegistration.this,secondLoginPage.class);
                                            myIntent.putExtra("usersEmail",emailAddress);
                                            startActivity(myIntent);
                                            finish();
                                        }
                                    });

                                  //  Toast.makeText(PatientRegistration.this,"REGISTRATION WAS SUCCESSFUL",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    btnRegPatients.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    patientImageSet.setImageResource(R.drawable.computer);
                                    txtViewSetMessage.setText("Error 107:Error Saving Data, Check NETWORK!!");
                                    btnProceed.setText("RETRY");
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    btnProceed.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            relativeLayout.setVisibility(View.GONE);
                                            btnRegPatients.setVisibility(View.VISIBLE);

                                        }
                                    });

                                    Toast.makeText(PatientRegistration.this,"!!!!!ERROR DURING REGISTRATION",Toast.LENGTH_SHORT).show();
                                }



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors here
                            Toast.makeText(PatientRegistration.this, "Error Checking Email, CHECK NETWORK!", Toast.LENGTH_SHORT).show();
                        }
                    });






                }
            });
        }
    }
}