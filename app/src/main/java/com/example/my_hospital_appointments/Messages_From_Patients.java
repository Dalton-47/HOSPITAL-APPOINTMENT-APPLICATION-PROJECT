package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Messages_From_Patients extends AppCompatActivity {
    ArrayList<Patients> dataList=new ArrayList<>();
    DatabaseReference myDoctors= FirebaseDatabase.getInstance().getReference().child("Doctors");


    DatabaseReference myPatients= FirebaseDatabase.getInstance().getReference().child("Patients");

    String emailKey;
    String docName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_from_patients);

        Intent intent =getIntent();
         docName=intent.getExtras().getString("doctorName");
        Toast.makeText(Messages_From_Patients.this, "Doctor's Name = "+docName, Toast.LENGTH_SHORT).show();


        RecyclerView myRecyclerView=(RecyclerView)  findViewById(R.id.recyclerViewPatientsMessages);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PatientMessagesDatAdapter adapter=new PatientMessagesDatAdapter (dataList,docName) ;
        myRecyclerView.setAdapter(adapter);

        myPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  ArrayList<Doctors> dataList= new ArrayList<>();
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    //we then do deserialization which is the process of converting a JSON object into an object that
                    //can be loaded into the java memory
                    //so we deserialize the DataSnapshot object into an instance of the Doctors class
                    //when you retrieve an object from firebase database it is returned in form of a DataSnapshot object
                    Patients myPatients=childSnapshot.getValue(Patients.class);//.class is used to access the class object of a given type in java
                    dataList.add(myPatients);

                }
                adapter.setData(dataList);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void getDocName()
    {
        myDoctors.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    DataSnapshot mySnapshot=task.getResult();
                    docName=String.valueOf(mySnapshot.child("userName").getValue());
                        }
            }
        });
    }
}