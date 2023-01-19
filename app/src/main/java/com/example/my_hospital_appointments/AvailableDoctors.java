package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableDoctors extends AppCompatActivity {

    ArrayList<Doctors> dataList=new ArrayList<>();
    DatabaseReference myDoctors= FirebaseDatabase.getInstance().getReference().child("Doctors");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_doctors);

        RecyclerView myRecyclerView=(RecyclerView)  findViewById(R.id.recyclerViewDoctors);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DataAdapter adapter=new DataAdapter(dataList);
        myRecyclerView.setAdapter(adapter);

        //we now listen to changes to the data in the Firebase Realtime DataBase

        myDoctors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Doctors> dataList= new ArrayList<>();
                for(DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    //we then do deserialization which is the process of converting a JSON object into an object that
                    //can be loaded into the java memory
                    //so we deserialize the DataSnapshot object into an instance of the Doctors class
                    //when you retrieve an object from firebase database it is returned in form of a DataSnapshot object
                    Doctors doctors=childSnapshot.getValue(Doctors.class);//.class is used to access the class object of a given type in java
                    dataList.add(doctors);
                }
                adapter.setData(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AvailableDoctors.this,"There was an error in loading Database " ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop()
    {
        super.onStop();
       // myDoctors.removeEventListener();
    }
}