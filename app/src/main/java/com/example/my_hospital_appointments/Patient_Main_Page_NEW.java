package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.RoundedCorner;
import android.widget.ImageView;
import com.squareup.picasso.Transformation;


import com.squareup.picasso.Picasso;

public class Patient_Main_Page_NEW extends AppCompatActivity {
   ImageView userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_page_new);

        userProfile =(ImageView)  findViewById(R.id.imageViewPatientProfileNew);



        Picasso.get()
                .load(R.drawable.happydoctor)
                .transform(new RoundedTransformation() )
                .into(userProfile);


    }
}