package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Patient_User_Profile_Activity extends AppCompatActivity {
    FirebaseAuth authProfile;
    ProgressBar progressBar;
    DatabaseReference userRef;
    TextView txtViewEmail,txtViewDateOfBirth,txtViewCounty,txtViewPhone,txtViewPatientName;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_user_profile);

        //get database reference to read users details
        userRef= FirebaseDatabase.getInstance().getReference("Patients");

        txtViewEmail = (TextView)  this.findViewById(R.id.textViewPatientEmail_PROFILE);
        txtViewDateOfBirth =(TextView)  this.findViewById(R.id.textViewPatientDOB_PROFILE);
        txtViewCounty = (TextView)  this.findViewById(R.id.textViewPatientCounty_PROFILE);
        txtViewPhone = (TextView)  this.findViewById(R.id.textViewPatientPhoneNo_PROFILE);
        txtViewPatientName  =(TextView)  this.findViewById(R.id.textViewPatientName_PROFILE);
        progressBar = (ProgressBar)  this.findViewById(R.id.progressBarPatientProfile);
        imageView = (ImageView)  this.findViewById(R.id.imageViewPatientViewProfile);


        //send user to upload profile
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_User_Profile_Activity.this,PatientUploadPicActivity.class));
            }
        });


        //get Current User
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(Patient_User_Profile_Activity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }


    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, you can get their details here
            String userId = currentUser.getUid();
            String email = currentUser.getEmail();

            String parts[]=email.split("@");
            String emailID = parts[0];

            userRef.child(emailID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            progressBar.setVisibility(View.GONE);
                            DataSnapshot thisDataSnapshot=task.getResult();
                            txtViewEmail.setText(String.valueOf(thisDataSnapshot.child("emailAddress").getValue()));
                            txtViewDateOfBirth.setText(String.valueOf(thisDataSnapshot.child("age").getValue()));
                            txtViewCounty.setText(String.valueOf(thisDataSnapshot.child("county").getValue()));
                            txtViewPhone.setText(String.valueOf(thisDataSnapshot.child("phoneNumber").getValue()));
                              String firstName=String.valueOf(thisDataSnapshot.child("firstName").getValue());
                              String secondName=String.valueOf(thisDataSnapshot.child("secondName").getValue());
                              txtViewPatientName.setText(firstName+" "+secondName);

                            // Set User DP (After user has uploaded)
                            Uri uri = firebaseUser.getPhotoUrl();

                            // ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
                            Picasso.get().load(uri)
                                            .transform(new SquareTransformation())
                                    .into(imageView);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Patient_User_Profile_Activity.this, "Error 202: User Not Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Patient_User_Profile_Activity.this, "Error 404: Check Network Connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            progressBar.setVisibility(View.GONE);
            // User is not signed in, you should redirect them to sign in page
            // Or you can perform some other action here
        }
    }


    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            firebaseUser.sendEmailVerification();
            authProfile.signOut(); // Sign out user.
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Patient_User_Profile_Activity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time.");

        // Open email Apps if User clicks/taps Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // To email app in new window and not within our app
                startActivity(intent);
            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }
}