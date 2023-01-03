package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainPage extends AppCompatActivity {
    Button myAppointments,manageDoctors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        myAppointments=(Button) findViewById(R.id.buttonManageAppointmentsAdmin);
        myAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(AdminMainPage.this,PatientAppointmentAdmin.class);
                startActivity(myIntent);
            }
        });

        manageDoctors=(Button) findViewById(R.id.buttonManageDoctors);
        manageDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(AdminMainPage.this,AdminDoctorApproval.class);
                startActivity(myIntent);
            }
        });

       Button reportButton=(Button) findViewById(R.id.buttonAdminReports);
       reportButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent myIntent=new Intent(AdminMainPage.this,ReportGeneration.class);
               startActivity(myIntent);
           }
       });

    }


}