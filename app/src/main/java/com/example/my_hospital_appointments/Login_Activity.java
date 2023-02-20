package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
  TextView txtViewForgotPassword;
  Button btnLogin;
  EditText editTextEmail, editTextPassword;
  FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar =(ProgressBar)  this.findViewById(R.id.progressBarLogin_MAIN);
        editTextEmail = (EditText)  this.findViewById(R.id.editTextTextLoginEmail);
        editTextPassword =(EditText)  this.findViewById(R.id.editTextTextLoginPassword);

        authProfile = FirebaseAuth.getInstance();
        txtViewForgotPassword = (TextView)  findViewById(R.id.textViewForgotPassword);
        String txtForgotPassword=txtViewForgotPassword.getText().toString().trim();
        SpannableString myString = new SpannableString(txtForgotPassword);

        ClickableSpan myClick1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent myIntent=new Intent(Login_Activity.this, Forgot_Password_Activity.class);
                startActivity(myIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setUnderlineText(true);
            }
        };

        myString.setSpan(myClick1,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtViewForgotPassword.setText(myString);
        txtViewForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());


        btnLogin =(Button)  this.findViewById(R.id.buttonLoginMain);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
               userLogin();

            }
        });


    }

    private void userLogin()
    {

        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is Required!");
            editTextEmail.requestFocus();
        }
        else if(password.isEmpty())
        {
            editTextPassword.setError("Password is Required!");
            editTextPassword.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("PLease use a valid email address!");
            editTextEmail.requestFocus();
        }
        else
        {
            authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login_Activity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Login_Activity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                        // Get instance of the current user
                        FirebaseUser firebaseUser = authProfile.getCurrentUser();

                        // Check if the email is verified before user can access their profile
                        if (firebaseUser.isEmailVerified()){
                          //  Toast.makeText(Login_Activity.this,"You are logged in now", Toast.LENGTH_SHORT).show();

                            // Open user profile
                            // Start the main homepage.

                            String usersEmail = editTextEmail.getText().toString().trim();

                           Toast.makeText(Login_Activity.this,"EMAIL : "+usersEmail,Toast.LENGTH_SHORT).show();
                            Intent myIntent=new Intent(Login_Activity.this,secondLoginPage.class);
                            myIntent.putExtra("usersEmail",usersEmail);
                            startActivity(myIntent); //start next Activity
                            finish(); // Close LoginActivity

                        }else {
                            firebaseUser.sendEmailVerification();
                            authProfile.signOut(); // Sign out user.
                            showAlertDialogue();
                        }

                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e){
                            editTextEmail.setError("User does not exist or is no longer valid. Please register again.");
                            editTextEmail.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            editTextEmail.setError("Invalid credentials. Kindly check and re-enter.");
                            editTextEmail.requestFocus();
                        } catch (Exception e) {
                            Log.e(TAG,e.getMessage());
                            Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(Login_Activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    private void showAlertDialogue() {
        // Set up the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification.");

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