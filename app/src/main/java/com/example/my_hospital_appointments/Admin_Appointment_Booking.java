package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Admin_Appointment_Booking extends AppCompatActivity {
    DatabaseReference myPatientsData;
    DatabaseReference myDoctorsData;
    DatabaseReference myAssignedDoctorData;
    DatabaseReference myAssignedPatientData;
    DatabaseReference myDataStatus;
    private Spinner spinnerEmails,spinnerDoctorEmails1;
    public String  patientEmail,doctorEmail,myText;
    public String emailKey="";
    public String doctorKey="";
    private TextView textViewName,textViewEmail, textViewAge,textViewDate,textViewTime,textViewDescription;
    private Button viewAppointment,assignAppointment,viewDoctorDetails;
    private  ArrayList <String>myArrayList=new ArrayList<String>();
    private ArrayList <String> doctorArrayList =new ArrayList<String>();
    private TextView doctorTextViewName,doctorTextViewEmail,doctorTextViewPhone,doctorTextViewDepartment;


    public String[] myTime={
            "Select One",
            "8-10 AM",
            "9-11 AM",
            "10-12 AM",
            "2-4 PM",
            "3-5 PM",
            "4-6 PM",
            "5-7 PM"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_admin);

        myAssignedDoctorData=FirebaseDatabase.getInstance().getReference("AssignedDoctor");
        myAssignedPatientData=FirebaseDatabase.getInstance().getReference("AssignedPatient");
        myDataStatus=FirebaseDatabase.getInstance().getReference("AppointmentStatus");

        textViewName=(TextView)  findViewById(R.id.textViewPatientAdaminName);
        textViewEmail=(TextView)  findViewById(R.id.textViewPatientAdminEmail);
        textViewAge =(TextView)  findViewById(R.id.textViewPatientAdminAge);
        textViewDate=(TextView)  findViewById(R.id.textViewPatientAdminDate);
        textViewTime=(TextView)  findViewById(R.id.textViewPatientAdminTime);
        textViewDescription=(TextView)  findViewById(R.id.textViewPatientAdminDescription);

        doctorTextViewName=(TextView)  findViewById(R.id.textViewDoctorConsultName3);
        doctorTextViewEmail=(TextView)  findViewById(R.id.textViewDoctorConsultEmail3);
        doctorTextViewPhone=(TextView)  findViewById(R.id.textViewDoctorConsultPhone3);
        doctorTextViewDepartment=(TextView) findViewById(R.id.textViewDoctorConsultDepartment3);

        myArrayList.add("Select One");

        myPatientsData= FirebaseDatabase.getInstance().getReference().child("PatientAppointments");
        myPatientsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //here incase of error
                    myArrayList.add(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerEmails=(Spinner) findViewById(R.id.spinnerPatientEmails);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myArrayList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerEmails.setAdapter(adapter1);
        spinnerEmails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                  myText=spinnerEmails.getSelectedItem().toString().trim();
                if(myText=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                  //  Toast.makeText(Admin_Appointment_Booking.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    patientEmail=myText;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        myDoctorsData=FirebaseDatabase.getInstance().getReference().child("Doctors");
        doctorArrayList.add("Select One");
        myDoctorsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    doctorArrayList.add(snapshot.child("email").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewAppointment=(Button) findViewById(R.id.buttonViewPatientAppointment);
        viewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patientEmail!=null)
                {
                    patientData(patientEmail);
                }
                else {
                    Toast.makeText(Admin_Appointment_Booking.this, "Select a Patient First", Toast.LENGTH_SHORT).show();
                }

            }
        });

        spinnerDoctorEmails1=(Spinner) findViewById(R.id.spinnerDoctorEmails3);


        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,doctorArrayList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDoctorEmails1.setAdapter(adapter2);
        spinnerDoctorEmails1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String myText2=spinnerDoctorEmails1.getSelectedItem().toString().trim();
                if(myText2=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                  //  Toast.makeText(Admin_Appointment_Booking.this,"You have Selected "+myText2,Toast.LENGTH_SHORT).show();
                    doctorEmail=myText2;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        viewDoctorDetails=(Button) findViewById(R.id.buttonViewDoctorDetails3);
        viewDoctorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doctorEmail!=null)
                {
                    getDoctorData(doctorEmail);
                }
                else {
                    Toast.makeText(Admin_Appointment_Booking.this, "Select a Doctor First", Toast.LENGTH_SHORT).show();
                }

            }
        });

        assignAppointment=(Button) findViewById(R.id.buttonAssignDoctor);
        assignAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  assert patientEmail !=null;
                if(doctorEmail != null && patientEmail!=null)
                {
                    assignAppointment(patientEmail,doctorEmail);
                }
                else
                {
                    Toast.makeText(Admin_Appointment_Booking.this, "Select a Doctor and Patient", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void patientData(String myKey)
    {
        emailKey="";
        int Counter=myKey.length();
        for(int a=0; a<Counter; a++)
        {
            if(myKey.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+myKey.charAt(a);
            }
        }
        emailKey=emailKey.trim();


        if(emailKey.isEmpty())
        {
            Toast.makeText(Admin_Appointment_Booking.this,"KINDLY SELECT AN EMAIL TO VIEW THE PATIENTS DATA",Toast.LENGTH_SHORT ).show();

        }
        else
        {

        //    Toast.makeText(Admin_Appointment_Booking.this,"YOUR KEY = "+emailKey,Toast.LENGTH_SHORT ).show();

            myPatientsData.child(emailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot myDataSnapshot =task.getResult();
                            //String patientDescription=String.valueOf(myDataSnapshot.child("description").getValue());
                            String patientName=String.valueOf(myDataSnapshot.child("name").getValue());
                            String patientEmail=String.valueOf(myDataSnapshot.child("email").getValue());
                            String patientDescription=String.valueOf(myDataSnapshot.child("description").getValue());
                            String patientAge=String.valueOf(myDataSnapshot.child("age").getValue());
                            String patientDate=String.valueOf(myDataSnapshot.child("date").getValue());
                            String patientTime=String.valueOf(myDataSnapshot.child("time").getValue());


                            textViewName.setText(patientName);
                            textViewEmail.setText(patientEmail);
                            textViewAge.setText(patientAge+" Years");
                            textViewDate.setText(patientDate);
                            textViewTime.setText(patientTime);
                            textViewDescription.setText(patientDescription);

                           // myArrayList.remove(myKey);
                        }
                        else
                        {
                            Toast.makeText(Admin_Appointment_Booking.this,"ERROR!!! EMAIL DOESN'T EXIST",Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(Admin_Appointment_Booking.this,"ERROR WHILE READING THE SELECTED PATIENT'S EMAIL",Toast.LENGTH_SHORT ).show();

                    }
                }
            });
        }
    }

    private void getDoctorData(String doctorEmail)
    {
         doctorKey="";
        int Counter=doctorEmail.length();
        for(int a=0; a<Counter; a++)
        {
            if(doctorEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                doctorKey=doctorKey+doctorEmail.charAt(a);
            }
        }
        doctorKey=doctorKey.trim();

        if(doctorKey.isEmpty())
        {
            Toast.makeText(Admin_Appointment_Booking.this,"KINDLY SELECT AN EMAIL TO VIEW THE DOCTOR'S DATA",Toast.LENGTH_SHORT ).show();

        }
        else
        {
          //  Toast.makeText(Admin_Appointment_Booking.this,"YOUR KEY = "+doctorKey,Toast.LENGTH_SHORT ).show();

            myDoctorsData.child(doctorKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot myDataSnapshot =task.getResult();
                            String doctorName=String.valueOf(myDataSnapshot.child("userName").getValue());
                            String doctorEmail=String.valueOf(myDataSnapshot.child("email").getValue());
                            String doctorPhone=String.valueOf(myDataSnapshot.child("phoneNumber").getValue());
                            String doctorDepartment=String.valueOf(myDataSnapshot.child("department").getValue());

                            doctorTextViewName.setText(doctorName);
                            doctorTextViewEmail.setText(doctorEmail);
                            doctorTextViewPhone.setText(doctorPhone);
                            doctorTextViewDepartment.setText(doctorDepartment);


                            doctorArrayList.remove(doctorEmail);
                        }
                        else
                        {

                            Toast.makeText(Admin_Appointment_Booking.this,"ERROR!!! EMAIL DOESN'T EXIST",Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else
                    {

                        Toast.makeText(Admin_Appointment_Booking.this,"ERROR WHILE READING THE SELECTED DOCTOR'S EMAIL",Toast.LENGTH_SHORT ).show();

                    }
                }
            });
        }

    }

    private void assignAppointment(String myPatientParameterEmail,String myDoctorParameterEmail)
    {
        emailKey="";
        doctorKey="";

        int Counter=myPatientParameterEmail.length();
        for(int a=0; a<Counter; a++)
        {
            if(myPatientParameterEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailKey=emailKey+myPatientParameterEmail.charAt(a);
            }
        }
        emailKey=emailKey.trim();


        int myCounter=myDoctorParameterEmail.length();
        for(int a=0; a<myCounter; a++)
        {
            if(myDoctorParameterEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                doctorKey=doctorKey+myDoctorParameterEmail.charAt(a);
            }
        }
        doctorKey=doctorKey.trim();


        String myPatientName=textViewName.getText().toString().trim();
        String myPatientEmail=textViewEmail.getText().toString().trim();
        String myPatientDescription=textViewDescription.getText().toString().trim();
        String myPatientDate=textViewDate.getText().toString().trim();
        String myPatientTime=textViewTime.getText().toString().trim();
        String myPatientAge= textViewAge.getText().toString().trim();

        String myDoctorName=doctorTextViewName.getText().toString().trim();
        String myDoctorEmail=doctorTextViewEmail.getText().toString().trim();
        String myDoctorPhone=doctorTextViewPhone.getText().toString().trim();

        if(!myPatientAge.equals("") || !myDoctorName.equals("null")) {


            String Key = myAssignedPatientData.push().getKey();

            myPatient myAssignedPatient = new myPatient(myPatientName, myPatientEmail, myPatientDescription, myPatientDate, myPatientTime, myPatientAge);
            myAssignedPatientData.child(doctorKey).child(Key).setValue(myAssignedPatient).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        String myDoctorName = doctorTextViewName.getText().toString().trim();
                        String myDoctorEmail = doctorTextViewEmail.getText().toString().trim();
                        String myDoctorPhone = doctorTextViewPhone.getText().toString().trim();
                        myDoctor myAssignedDoctor = new myDoctor(myDoctorName, myDoctorEmail, myDoctorPhone);
                        myAssignedDoctorData.child(emailKey).setValue(myAssignedDoctor);

                        String status = "BOOKED";
                        AppointmentStatus myStatus = new AppointmentStatus(status);
                        myDataStatus.child(emailKey).setValue(myStatus);

                        myPatientsData.child(emailKey).removeValue();
                        myArrayList.remove(myText);
                        textViewName.setText("");
                        textViewEmail.setText("");
                        textViewDescription.setText("");
                        textViewDate.setText("");
                        textViewTime.setText("");
                        textViewAge.setText("");
                        Toast.makeText(Admin_Appointment_Booking.this, "APPOINTMENT BOOKED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(Admin_Appointment_Booking.this, "Check Patient & Doctor Details!", Toast.LENGTH_SHORT).show();
        }




    }
}

