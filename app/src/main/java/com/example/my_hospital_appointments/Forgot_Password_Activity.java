package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Forgot_Password_Activity extends AppCompatActivity {
  EditText editTextPassword;
  Button btnSendEmail;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private final static String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressBar =(ProgressBar)  this.findViewById(R.id.progressBarForgetPassword);
        editTextPassword = (EditText) this.findViewById(R.id.editTextTextForgotPasswordEmail);

        btnSendEmail =(Button)  this.findViewById(R.id.buttonForgotPasswordSend);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Forgot_Password_Activity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Email is required");
                    editTextPassword.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Forgot_Password_Activity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Valid email is required");
                    editTextPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });


    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Forgot_Password_Activity.this,"Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Forgot_Password_Activity.this, Login_Activity.class);

                    // Clear stack to prevent the user from coming back to ForgotPasswordActivity on pressing back button after Logging out
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intent);
                    finish(); // Close UserProfile Activity
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextPassword.setError("User does not exist or is no longer valid. Please register again.");
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Forgot_Password_Activity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}