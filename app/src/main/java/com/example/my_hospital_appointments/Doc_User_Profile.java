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

public class Doc_User_Profile extends AppCompatActivity {
    FirebaseAuth authProfile;
    ProgressBar progressBar;
    DatabaseReference userRef;
    TextView txtDocName, txtDocEmail, txtDocOccupation, txtDocBadgeNo, txtDocPhoneNumber;
    ImageView imageViewDOC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_user_profile);

        //get database reference to read users details
        userRef= FirebaseDatabase.getInstance().getReference("Doctors");

        txtDocName = (TextView)  this.findViewById(R.id.textViewDocName_PROF);
        txtDocEmail = (TextView)  this.findViewById(R.id.textViewDocEmail_PROF );
        txtDocOccupation = (TextView)  this.findViewById(R.id.textViewDocOccupation_PROF);
        txtDocBadgeNo = (TextView)  this.findViewById(R.id.textViewDocBadgeNumber_PROF);
        txtDocPhoneNumber = (TextView)  this.findViewById(R.id.textViewDocPhoneNumber_PROF);

        imageViewDOC = (ImageView)  this.findViewById(R.id.imageViewDocImage_PROF);

        progressBar = (ProgressBar)  this.findViewById(R.id.progressBarDoc_PROF);


        //get Current User
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(Doc_User_Profile.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
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
                            txtDocEmail.setText(String.valueOf(thisDataSnapshot.child("email").getValue()));
                            String docName=String.valueOf(thisDataSnapshot.child("userName").getValue());
                            txtDocOccupation.setText(String.valueOf(thisDataSnapshot.child("department").getValue()));
                            txtDocBadgeNo.setText(String.valueOf(thisDataSnapshot.child("employeeNumber").getValue()));
                            txtDocPhoneNumber.setText(String.valueOf(thisDataSnapshot.child("phoneNumber").getValue()));
                            String firstName=String.valueOf(thisDataSnapshot.child("firstName").getValue());
                            String secondName=String.valueOf(thisDataSnapshot.child("secondName").getValue());

                            txtDocName.setText("Dr. "+docName);

                            // Set User DP (After user has uploaded)
                            Uri uri = firebaseUser.getPhotoUrl();

                            if(uri==null)
                            {

                            }
                            else
                            {
                                // ImageViewer setImageURI() should not be used with regular URIs. So we are using Picasso
                                Picasso.get().load(uri)
                                        .transform(new SquareTransformation())
                                        .into(imageViewDOC);
                            }


                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Doc_User_Profile.this, "Error 202: User Not Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Doc_User_Profile.this, "Error 404: Check Network Connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            progressBar.setVisibility(View.GONE);
            // User is not signed in, you should redirect them to sign in page
            // Or you can perform some other action here
        }
    }

    @Override
    protected void onResume() {
        //get Current User
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        super.onResume();
        if (firebaseUser == null){
            Toast.makeText(Doc_User_Profile.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Doc_User_Profile.this);
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