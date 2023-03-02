package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.util.Objects;

public class doctorRegistration extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progressBar;
    RelativeLayout myRelativeLayout;
    Button doneButton;
    TextView verificationText;
    ImageView myImageView;
    private FirebaseAuth mAuth;
   public String[] specialty={
            "Select One",
            "Cardiology",
            "Radiology",
            "Neurology",
            "Ophthalmology",
            "Haematology",
            "Anesthesiology",
            "Psychiatry",
            "Oncology",
            "Dermatology",
            "Emergency Medicine",
            "Pathology",
            "Pharmacy",
            "Orthopedics",
            "Rheumatology",
            "Surgery",
            "Physiotherapy",
            "Microbiology"
    };

   public String[] myTime={
            "Select One",
            "8-10 AM",
            "9-11 AM",
            "10-12 AM",
            "2-4 PM",
            "3-5 PM",
            "4-6 PM",
            "5-7 PM"
    };

   public String firstSpinnerText="";
   public String secondSpinnerText="";
   public String ID,phoneNumber,employeeNumber,userName,email,password;



   public EditText myId,myPhoneNumber,myEmployeeNumber,myUserName,myEmail,myPassword;
   public Button btnRegisterDoc;
   DatabaseReference doctorDatabase;

   public Spinner spin1;
   public  Spinner spin2;
   public String doctorID,doctorPhoneNumber,doctorEmployeeNumber,doctorUserName,doctorEmail,doctorTime,doctorDepartment;
   public  String doctorKey="";

    DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

         usersRef = FirebaseDatabase.getInstance().getReference().child("users");


        progressBar = (ProgressBar)  this.findViewById(R.id.progressBarRegsterDoctor_NEW);

        doctorDatabase=FirebaseDatabase.getInstance().getReference("PendingDoctors");

        mAuth = FirebaseAuth.getInstance();

        spin1=(Spinner) findViewById(R.id.spinnerTime);
        spin2=(Spinner) findViewById(R.id.spinnerSpecialty);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myTime);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myText=spin1.getSelectedItem().toString().trim();
                if (myText=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                    Toast.makeText(doctorRegistration.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    firstSpinnerText=myText;
                    doctorTime=myText;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,specialty);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myText2=spin2.getSelectedItem().toString().trim();
                if (myText2=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                    Toast.makeText(doctorRegistration.this,"You have Selected "+myText2,Toast.LENGTH_SHORT).show();
                    secondSpinnerText=myText2;
                    doctorDepartment=myText2;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

            //Setting Variables
        /*
         public EditText myId,myPhoneNumber,myEmployeeNumber,myUserName,myEmail,myPassword;
          public Button register;
           public String ID,phoneNumber,employeeNumber,userName,email,password;
         */

        myId=(EditText) findViewById(R.id.editTextNumberID);
        myPhoneNumber=(EditText) findViewById(R.id.editTextPhoneNumber);
        myEmployeeNumber=(EditText)  findViewById(R.id.editTextTextEmployee);
        myUserName=(EditText) findViewById(R.id.editTextTextUserName);
        myEmail=(EditText) findViewById(R.id.editTextTextMyEmailAddress);
        myPassword=(EditText) findViewById(R.id.editTextTextMyPassword);
        btnRegisterDoc =(Button) findViewById(R.id.buttonRegister);

        myId.requestFocus();
        btnRegisterDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               registerUser();

            }
        });



    }

    private void registerUser()
    {
        // doctorID,doctorPhoneNumber,doctorEmployeeNumber,doctorUserName,doctorEmail,doctorTime,doctorDepartment;
        ID= myId.getText().toString().trim();
        doctorID=ID;
        phoneNumber=myPhoneNumber.getText().toString().trim();
        doctorPhoneNumber=phoneNumber;
        employeeNumber=myEmployeeNumber.getText().toString().trim();
        doctorEmployeeNumber=employeeNumber;
        userName=myUserName.getText().toString().trim();
        doctorUserName=userName;
        email=myEmail.getText().toString().trim();
        doctorEmail=email;
        password=myPassword.getText().toString().trim();

        int Counter=doctorEmail.length();
        for(int a=0; a<Counter; a++)
        {
            if(doctorEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                doctorKey=doctorKey+doctorEmail.charAt(a);
            }
        }
        doctorKey =doctorKey.trim();

        if (userName.isEmpty() )
        {
            myUserName.setError("Cannot be blank");
            myUserName.requestFocus();
        }
        else if(ID.isEmpty())
        {
            myId.setError("Cannot be blank");
            myId.requestFocus();
        }
        else if(phoneNumber.isEmpty())
        {
            myPhoneNumber.setError("Cannot be blank");
            myPhoneNumber.requestFocus();
        }
        else if(employeeNumber.isEmpty())
        {
            myEmployeeNumber.setError("Cannot be blank");
            myEmployeeNumber.requestFocus();
        }
        else if(email.isEmpty())
        {
            myEmail.setError("Cannot be blank");
            myEmail.requestFocus();
        }
        else if(password.isEmpty())
        {
            myPassword.setError("Cannot be blank");
            myPassword.requestFocus();
        }
        else if(firstSpinnerText.isEmpty())
        {

            Toast.makeText(doctorRegistration.this,"PLEASE INPUT TIME THAT YOU WILL BE AVAILABLE",Toast.LENGTH_SHORT).show();
            spin1.requestFocus();
        }
        else if(secondSpinnerText.isEmpty())
        {
            Toast.makeText(doctorRegistration.this,"PLEASE SELECT YOUR SPECIALTY ABOVE",Toast.LENGTH_SHORT).show();
            spin2.requestFocus();
        }
       else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            myEmail.setError("Please Provide a valid Email Address");
            myEmail.requestFocus();
        }
       else if(password.length()<8)
        {
            myPassword.setError("Minimum password characters should be 8");
            myPassword.requestFocus();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
          myRelativeLayout=(RelativeLayout)  findViewById(R.id.LayoutUserWaitingPage);

            verificationText =(TextView)  findViewById(R.id.textViewVerification);
            myImageView =(ImageView)  findViewById(R.id.imageViewokayOrError);
            doneButton=(Button)  findViewById(R.id.buttonDone);
          //  register.setVisibility(View.GONE);//register button visibility set to Gone
           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
               @SuppressLint("SetTextI18n")
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   Query query = usersRef.orderByChild("email").equalTo(email);
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if (dataSnapshot.exists()) {
                               //If email exists do this
                               btnRegisterDoc.setVisibility(View.GONE);
                               progressBar.setVisibility(View.GONE);
                               myImageView.setImageResource(R.drawable.computer);
                               myImageView.setVisibility(View.VISIBLE);
                               verificationText.setText("Error 105:User Email Found in Database!");

                               myRelativeLayout.setVisibility(View.VISIBLE);
                               doneButton.setText("RETRY");
                               doneButton.setVisibility(View.VISIBLE);
                               doneButton.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       myRelativeLayout.setVisibility(View.GONE);
                                       btnRegisterDoc.setVisibility(View.VISIBLE);
                                       myEmail.setText("");
                                       myEmail.requestFocus();

                                   }
                               });


                           } else {
                               // Email does not exist in database, proceed with registration

                               if (task.isSuccessful())
                               {
                                   // public String ID,phoneNumber,employeeNumber,userName,email,password;
                                   user myUser=new user(ID,phoneNumber,employeeNumber,userName,email,firstSpinnerText,secondSpinnerText);
                                   FirebaseDatabase.getInstance().getReference("users")
                                           .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                           .setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful())
                                                   {
                                                       //we set an image to our ImageView

                                                       PendingDoctors myDoctor=new PendingDoctors (ID,phoneNumber,employeeNumber,userName,email,firstSpinnerText,secondSpinnerText) ;
                                                       doctorDatabase.child(doctorKey).setValue(myDoctor);
                                                       progressBar.setVisibility(View.GONE);
                                                       myRelativeLayout.setVisibility(View.VISIBLE);
                                                       DatabaseReference doctorApprovalData;
                                                       doctorApprovalData= FirebaseDatabase.getInstance().getReference("DoctorApproval");
                                                       String Status="HELLO Dr."+userName+" KINDLY AWAIT ADMIN APPROVAL WITHIN 24 HOURS";
                                                       DoctorApproval myDoctorApproval=new DoctorApproval(Status);
                                                       doctorApprovalData.child(doctorKey).setValue(myDoctorApproval);
                                                       //Toast.makeText(doctorRegistration.this, "KINDLY AWAIT ADMIN APPROVAL WITHIN 24 HOURS", Toast.LENGTH_SHORT).show();
                                                       myImageView.setImageResource(R.drawable.check);
                                                       myImageView.setVisibility(View.VISIBLE);
                                                       btnRegisterDoc.setVisibility(View.GONE);
                                                       //verificationText.setText("Details captured and saved successfully, A CODE WILL BE SENT TO YOUR EMAIL WITHIN THE NEXT 24 HOURS");
                                                       //progressBar.setVisibility(View.GONE);

                                                       verificationText.setText("AWAIT ADMIN APPROVAL WITHIN 24 HOURS");
                                                       doneButton.setVisibility(View.VISIBLE);
                                                       doneButton.setText("PROCEED");
                                                       doneButton.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view) {
                                                               myRelativeLayout.setVisibility(View.GONE);
                                                               btnRegisterDoc.setVisibility(View.GONE);
                                                               Toast.makeText(doctorRegistration.this, "LOG IN WITH CREDENTIALS", Toast.LENGTH_SHORT).show();
                                                               Intent myIntent=new Intent(doctorRegistration.this,Launcher_Screen.class);
                                                               myIntent.putExtra("usersEmail",email);
                                                               startActivity(myIntent);
                                                               finish();
                                                           }
                                                       });



                                                       //  Toast.makeText(doctorRegistration.this,"Details captured and saved successfully please await Admin's Confirmation within 24 hrs",Toast.LENGTH_SHORT).show();
                                                   }
                                                   else
                                                   {
                                                       btnRegisterDoc.setVisibility(View.GONE);
                                                       progressBar.setVisibility(View.GONE);
                                                       myImageView.setImageResource(R.drawable.computer);
                                                       myImageView.setVisibility(View.VISIBLE);
                                                       verificationText.setText("Error 103:Failed to save user's data, Try Again!");

                                                       myRelativeLayout.setVisibility(View.VISIBLE);
                                                       doneButton.setText("RETRY");
                                                       doneButton.setVisibility(View.VISIBLE);
                                                       doneButton.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view) {
                                                               myRelativeLayout.setVisibility(View.GONE);
                                                               btnRegisterDoc.setVisibility(View.VISIBLE);

                                                           }
                                                       });


                                                       // Toast.makeText(doctorRegistration.this,"Error 103:Failed to save user's data, Try Again!",Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });
                               }
                               //else ?
                               else
                               {
                                   btnRegisterDoc.setVisibility(View.GONE);
                                   progressBar.setVisibility(View.GONE);
                                   myImageView.setImageResource(R.drawable.computer);
                                   myImageView.setVisibility(View.VISIBLE);
                                   verificationText.setText("Error 102:Failed to save user's data,CHECK YOUR INTERNET CONNECTION then  Try Again!");

                                   myRelativeLayout.setVisibility(View.VISIBLE);
                                   doneButton.setText("RETRY");
                                   doneButton.setVisibility(View.VISIBLE);
                                   doneButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           myRelativeLayout.setVisibility(View.GONE);
                                           btnRegisterDoc.setVisibility(View.VISIBLE);
                                       }
                                   });


                                   // Toast.makeText(doctorRegistration.this,"Error 102:Failed to register User,Try Again",Toast.LENGTH_SHORT).show();
                               }
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                           // Handle errors here
                       }
                   });


               }
           });
        }
    }

    @Override
    public void onClick(View view) {

    }
}