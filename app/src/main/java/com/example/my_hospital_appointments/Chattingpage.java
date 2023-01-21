package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Chattingpage extends AppCompatActivity {
    public ArrayList<String> data=new ArrayList<>();
    Button myButton;
    DatabaseReference userMessages;
    String messageKey;
    String usersMessage;
    long timestamp;
    String senderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattingpage);

        //getting the data passed from previous activity
        String userEmailKey=getIntent().getStringExtra("emailKey");
        String doctorName=getIntent().getStringExtra("doctorsName");

        Toast.makeText(Chattingpage.this,"user's key = "+userEmailKey+" Doc name = "+doctorName,Toast.LENGTH_SHORT).show();
        FirebaseAuth myAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=myAuth.getCurrentUser();
        String userId= currentUser.getUid();

        DatabaseReference myDoctors= FirebaseDatabase.getInstance().getReference(userId);
         userMessages=FirebaseDatabase.getInstance().getReference("PATIENTMESSAGES").child(userEmailKey).child(doctorName);

         messageKey=FirebaseDatabase.getInstance().getReference("PATIENTMESSAGES").child(userEmailKey).child(doctorName).push().getKey();

        RecyclerView myRecycleViewer = findViewById(R.id.recyclerViewChat);
        chattingAdapter  myAdapter=new chattingAdapter(data);
        myRecycleViewer.setAdapter(myAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        myRecycleViewer.setLayoutManager(layoutManager);

        myButton=(Button) findViewById(R.id.buttonSendMessageChat);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText myEditText= (EditText) findViewById(R.id.editTextTypeMessageChat);
                 usersMessage=myEditText.getText().toString().trim();//get users message only after they press send
                data.add(usersMessage);
                myAdapter.notifyDataSetChanged();

               timestamp=System.currentTimeMillis();//get the timestamp for when the user was sending the message
               senderID ="patient";

                myEditText.setText("");
                addMessage();
            }
        });

    }

     private void addMessage()
    {
        String myTime=Long.toString(timestamp);
        Patient_Messages userMessagesData= new Patient_Messages(senderID,myTime,usersMessage);
        userMessages.child(messageKey).setValue(userMessagesData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Chattingpage.this,"MESSAGE SAVED",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}