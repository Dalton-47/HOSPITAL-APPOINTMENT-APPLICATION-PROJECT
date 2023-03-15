package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorWaitingPage extends AppCompatActivity {

    DatabaseReference doctorApprovalData;
    public String emailKey="";
    TextView myTextView;
    Button btnProceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_waiting_page);
        myTextView=(TextView)  findViewById(R.id.textViewWaitingPage);


        Intent intent =getIntent();
        String myUsersEmail=intent.getExtras().getString("usersEmail");

        doctorApprovalData= FirebaseDatabase.getInstance().getReference("DoctorApproval");

        btnProceed=(Button) findViewById(R.id.buttonProceedWaitingPage);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        myApproval(myUsersEmail);
    }

    private void myApproval(String myUsersEmail)
    {
        int Counter=myUsersEmail.length();

        for(int a=0; a<Counter; a++)
        {
            if(myUsersEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+myUsersEmail.charAt(a);

            }
        }

        doctorApprovalData.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        DataSnapshot thisDataSnapshot=task.getResult();
                        String doctorStatus=String.valueOf(thisDataSnapshot.child("status").getValue());
                        if(doctorStatus=="")
                        {
                            myTextView.setText("WAITING ADMIN APPROVAL");
                        }
                        else
                        {
                            myTextView.setText(doctorStatus);
                        }
                    }
                }


            }
        });

    }
}