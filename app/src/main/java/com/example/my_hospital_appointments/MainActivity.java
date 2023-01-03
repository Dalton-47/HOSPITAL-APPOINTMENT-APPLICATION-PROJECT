package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class MainActivity extends AppCompatActivity  {
   public TextView forgot_password;
   public TextView register;

   public EditText emailText,passwordText;
   public Button myLogin;
   private FirebaseAuth mAuth;

   public static String usersEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forgot_password=(android.widget.TextView) findViewById(R.id.textViewpassword);
        register=(TextView) findViewById(R.id.textViewRegister);
        //getActionBar().hide();
        String myText="forgot password?";
        String myText2="Register With Us?";

        SpannableString myString = new SpannableString(myText);
        SpannableString myString2=new SpannableString(myText2);

        ClickableSpan myClick1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(MainActivity.this,"We will send you an email shortly",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setUnderlineText(true);
            }
        };

        ClickableSpan myClick2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent myIntent=new Intent(MainActivity.this, askuseroccupation.class);
                startActivity(myIntent);
            }
        };

        myString.setSpan(myClick1,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgot_password.setText(myString);
        forgot_password.setMovementMethod(LinkMovementMethod.getInstance());

        myString2.setSpan(myClick2,0,17,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(myString2);
        register.setMovementMethod(LinkMovementMethod.getInstance());

       /*
            public EditText emailText,passwordText;
            public Button myLogin;
        */

         mAuth= FirebaseAuth.getInstance();

         myLogin=(Button) findViewById(R.id.buttonLogin);
         myLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 userLogin();

             }
         });



    }

    private void userLogin()
    {
        emailText=(EditText) findViewById(R.id.editTextTextEmailAddress);
        passwordText=(EditText) findViewById(R.id.editTextTextPassword);
       String email=emailText.getText().toString().trim();
       String password=passwordText.getText().toString().trim();

       if(email.isEmpty())
       {
           emailText.setError("Email is Required!");
           emailText.requestFocus();
       }
       else if(password.isEmpty())
       {
           passwordText.setError("Password is Required!");
           passwordText.requestFocus();
       }
       else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
       {
           emailText.setError("PLease use a valid email address!");
           emailText.requestFocus();
       }
       else
       {
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                   if(task.isSuccessful())
                   {
                       usersEmail =email;
                       Toast.makeText(MainActivity.this,"LOGIN WAS SUCCESSFUL",Toast.LENGTH_SHORT).show();
                       Intent myEmailIntent=new Intent(MainActivity.this,secondLoginPage.class);
                       myEmailIntent.putExtra("usersEmail",usersEmail);

                       //redirect to the next activity
                     //  Intent myIntent=new Intent(MainActivity.this,secondLoginPage.class);
                       startActivity(myEmailIntent);
                   }
                   else
                   {
                       Toast.makeText(MainActivity.this,"ERROR 101:FAILED TO LOGIN, PLEASE CHECK YOUR CREDENTIALS",Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }

    }
}