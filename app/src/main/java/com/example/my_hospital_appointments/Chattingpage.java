package com.example.my_hospital_appointments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Chattingpage extends AppCompatActivity {
    public ArrayList<String> data=new ArrayList<>();
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattingpage);

        FirebaseAuth myAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=myAuth.getCurrentUser();
        String userId= currentUser.getUid();

        DatabaseReference myDoctors= FirebaseDatabase.getInstance().getReference(userId);


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
                String text=myEditText.getText().toString().trim();
                data.add(text);
                myAdapter.notifyDataSetChanged();

                myEditText.setText("");
            }
        });

    }
}