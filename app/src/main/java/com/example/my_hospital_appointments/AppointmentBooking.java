package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentBooking extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(AppointmentBooking.this,"ON RESUME METHOD CALLED",Toast.LENGTH_SHORT).show();

        int Counter=myUsersEmail.length();
        for(int a=0; a<Counter; a++)
        {
            if(myUsersEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailFirstAppt=emailFirstAppt+myUsersEmail.charAt(a);
            }
        }
       checkAppointments(emailFirstAppt);
    }

    DatabaseReference myDatabaseAppointments;
    DatabaseReference myDoctorDetails;
    DatabaseReference myDataStatus;

        public String myUsersEmail;
        public  String  textDescription1;
        public  String  textDescription2;
        public  String  textDescription3;

    public TextView  myAppointmentDescription1;
    public TextView  myAppointmentDescription2;
    public TextView  myAppointmentDescription3;

    public TextView myAppointmentDepartment1;
    public TextView myAppointmentDepartment2;
    public TextView myAppointmentDepartment3;

    public TextView myAppointmentDate1;
    public TextView myAppointmentDate2;
    public TextView myAppointmentDate3;

    public TextView myAppointmentTime1;
    public TextView myAppointmentTime2;
    public TextView myAppointmentTime3;

    public String  textDepartment1;
    public String  textDepartment2;
    public String  textDepartment3;
    public String  textDate1;
    public String  textDate2;
    public String  textDate3;
    public String  textTime1;
    public String  textTime2;
    public String  textTime3;
    public String emailFirstAppt="";
    public String emailSecondAppt="secondappointment";
    public String emailThirdAppt="thirdappointment";

    private RelativeLayout cancelAppointmentsLayout;
    private FloatingActionButton myFirstFloatButton;
    private Button noCancelAppointments;

    private Button bookingStatus1;
    private Button bookingStatus2;
    private Button bookingStatus3;

    private RelativeLayout myFirstApptLayout,mySecondApptLayout,myThirdApptLayout;
    private Button refreshPage;
    private TextView doctorName,doctorEmail,doctorPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        myFirstApptLayout =(RelativeLayout)  findViewById(R.id.relativeLayoutfirstAppt);
        mySecondApptLayout=(RelativeLayout)  findViewById(R.id.relativeLayoutSecondAppt);
        myThirdApptLayout =(RelativeLayout)  findViewById(R.id.relativeLayoutThirdAppt);

        doctorName=(TextView)  findViewById(R.id.textViewDoctorName1);
        doctorEmail=(TextView)  findViewById(R.id.textViewDoctorEmail1);
        doctorPhone=(TextView)  findViewById(R.id.textViewDoctorPhoneNumber1);


        myDatabaseAppointments = FirebaseDatabase.getInstance().getReference("Appointments");
        myDataStatus=FirebaseDatabase.getInstance().getReference("AppointmentStatus");
        myDoctorDetails=FirebaseDatabase.getInstance().getReference("AssignedDoctor");

        Intent intent =getIntent();
        myUsersEmail=intent.getExtras().getString("usersEmail");

        int Counter=myUsersEmail.length();

        for(int a=0; a<Counter; a++)
        {
            if(myUsersEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailFirstAppt=emailFirstAppt+myUsersEmail.charAt(a);
            }
        }

        for(int a=0; a<Counter; a++)
        {
            if(myUsersEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailSecondAppt =emailSecondAppt+myUsersEmail.charAt(a);
            }
        }

        for(int a=0; a<Counter; a++)
        {
            if(myUsersEmail.charAt(a)=='@')
            {
                break;
            }
            else
            {
                emailThirdAppt=emailThirdAppt+myUsersEmail.charAt(a);
            }
        }

        myAppointmentDescription1 = (TextView)   findViewById(R.id.textViewAppointmentDescription1);
        textDescription1=myAppointmentDescription1.getText().toString().trim();

        myAppointmentDescription2 =(TextView)  findViewById(R.id.textViewAppointmentDescription2);
        textDescription2=myAppointmentDescription2.getText().toString().trim();

        myAppointmentDescription3 = (TextView)  findViewById(R.id.textViewAppointmentDescription3);
        textDescription3=myAppointmentDescription3.getText().toString().trim();


        myAppointmentDepartment1=(TextView) findViewById(R.id.textViewAppointmentDepartment1);
        textDepartment1=myAppointmentDepartment1.getText().toString().trim();

        myAppointmentDepartment2=(TextView) findViewById(R.id.textViewAppointmentDepartment2);
        textDepartment2=myAppointmentDepartment2.getText().toString().trim();

        myAppointmentDepartment3=(TextView) findViewById(R.id.textViewAppointmentDepartment3);
        textDepartment3=myAppointmentDepartment3.getText().toString().trim();

        myAppointmentDate1=(TextView) findViewById(R.id.textViewAppointmentDate1);
        textDate1=myAppointmentDate1.getText().toString().trim();

        myAppointmentDate2=(TextView) findViewById(R.id.textViewAppointmentDate2);
        textDate2=myAppointmentDate2.getText().toString().trim();

        myAppointmentDate3=(TextView) findViewById(R.id.textViewAppointmentDate3);
        textDate3=myAppointmentDate3.getText().toString().trim();

        myAppointmentTime1=(TextView) findViewById(R.id.textViewAppointmentTime1);
        textTime1=myAppointmentTime1.getText().toString().trim();

        myAppointmentTime2=(TextView) findViewById(R.id.textViewAppointmentTime2);
        textTime2=myAppointmentTime2.getText().toString().trim();

        myAppointmentTime3=(TextView) findViewById(R.id.textViewAppointmentTime3);
        textTime3=myAppointmentTime3.getText().toString().trim();



        Button bookAppointment = (Button) findViewById(R.id.buttonBookAppointment);
        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textDescription1=myAppointmentDescription1.getText().toString().trim();
                textDescription2=myAppointmentDescription2.getText().toString().trim();
                textDescription3=myAppointmentDescription3.getText().toString().trim();

                Intent myIntent=new Intent(AppointmentBooking.this, GetAppointmentDetails.class);
                Bundle extras = new Bundle();
                extras.putString("usersEmail",myUsersEmail);
                extras.putString("textDescription1",textDescription1);
                extras.putString("textDescription2",textDescription2);
                extras.putString("textDescription3",textDescription3);
                myIntent.putExtras(extras);
                startActivity(myIntent);




            }
        });



        checkAppointments(emailFirstAppt);

        noCancelAppointments =(Button) findViewById(R.id.buttonNoCancelAppointments);
        cancelAppointmentsLayout =(RelativeLayout) findViewById(R.id.relativeLayoutCancelAppointments);
        myFirstFloatButton=(FloatingActionButton) findViewById(R.id.floatingActionButtonFirst);
        myFirstFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAppointmentsLayout.setVisibility(View.VISIBLE);
                noCancelAppointments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelAppointmentsLayout.setVisibility(View.GONE);
                    }
                });
            }
        });


             bookingStatus1=(Button) findViewById(R.id.buttonAppointmentBooked1);
             bookingStatus2=(Button)  findViewById(R.id.buttonAppointmentBooked2);
             bookingStatus3=(Button)  findViewById(R.id.buttonAppointmentBooked3);


             refreshPage =(Button) findViewById(R.id.btnRefreshPage);
             refreshPage.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     checkAppointments(emailFirstAppt);
                     Toast.makeText(AppointmentBooking.this,"Page Refreshed",Toast.LENGTH_SHORT).show();
                 }
             });



    }


    private void checkAppointments(String userEmailKey)
    {

            myDoctorDetails.child(userEmailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot thisDataSnapshot=task.getResult();
                            String myDoctorName=String.valueOf(thisDataSnapshot.child("name").getValue());
                            String myDoctorEmail=String.valueOf(thisDataSnapshot.child("email").getValue());
                            String myDoctorPhone=String.valueOf(thisDataSnapshot.child("phoneNumber").getValue());

                            if(myDoctorName.isEmpty())
                            {
                                doctorName.setText("NOT YET ASSIGNED");
                            }
                            else if(myDoctorEmail.isEmpty())
                            {
                                doctorEmail.setText("NOT YET ASSIGNED");
                            }
                            else if(myDoctorPhone.isEmpty())
                            {
                                doctorPhone.setText("NOT YET ASSIGNED");
                            }
                            else
                                {
                                doctorName.setText(myDoctorName);
                                doctorEmail.setText(myDoctorEmail);
                                doctorPhone.setText(myDoctorPhone);
                            }
                        }
                        else
                        {
                           // Toast.makeText(AppointmentBooking.this,"Error while reading Status Database...",Toast.LENGTH_SHORT).show();
                        }

                        }

                }
            });
            //String firstAppointment="FirstAppointment_"+userEmail;
            //Toast.makeText(AppointmentBooking.this,emailFirstAppt,Toast.LENGTH_SHORT).show();
            myDataStatus.child(userEmailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot thisDataSnapshot=task.getResult();
                            String myStatus=String.valueOf(thisDataSnapshot.child("status").getValue());
                            bookingStatus1.setText(myStatus);
                        }
                        else
                        {
                         //   Toast.makeText(AppointmentBooking.this,"Checking appointment Status FAILED...",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                      //  Toast.makeText(AppointmentBooking.this,"Error while reading Status Database...",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //now we read the appointment details
            myDatabaseAppointments.child(userEmailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                          if(task.isSuccessful())
                          {
                              if(task.getResult().exists())
                              {

                                  myFirstApptLayout.setVisibility(View.VISIBLE);
                                  DataSnapshot myDataSnapshot =task.getResult();
                                  String patientDescription=String.valueOf(myDataSnapshot.child("description").getValue());
                                  String patientDepartment=String.valueOf(myDataSnapshot.child("department").getValue());
                                  String patientDate=String.valueOf(myDataSnapshot.child("date").getValue());
                                  String patientTime=String.valueOf(myDataSnapshot.child("time").getValue());


                                  myAppointmentDescription1.setText(patientDescription);
                                  myAppointmentDepartment1.setText(patientDepartment);
                                  myAppointmentDate1.setText(patientDate);
                                  myAppointmentTime1.setText(patientTime);

                              }
                              else
                              {
                            //      Toast.makeText(AppointmentBooking.this,"User DOESN'T EXIST",Toast.LENGTH_SHORT).show();
                              }
                          }
                          else
                          {
                              Toast.makeText(AppointmentBooking.this,"USER HASN't BOOKED ANY APPOINTMENT YET",Toast.LENGTH_SHORT).show();
                          }
                }
            });




/*
        if(textDescription2.isEmpty())
        {


            //String firstAppointment="FirstAppointment_"+userEmail;
            //Toast.makeText(AppointmentBooking.this,emailFirstAppt,Toast.LENGTH_SHORT).show();
            myDataStatus.child(emailSecondAppt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot thisDataSnapshot=task.getResult();
                            String myStatus=String.valueOf(thisDataSnapshot.child("status").getValue());
                            bookingStatus2.setText(myStatus);
                        }
                        else
                        {
                            //Toast.makeText(AppointmentBooking.this,"NO SECOND APPOINTMENT",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(AppointmentBooking.this,"Error while reading Status Database...",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //now we read the appointment details
            mydatabaseAppointements.child(emailSecondAppt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {

                            mySecondApptLayout.setVisibility(View.VISIBLE);
                            DataSnapshot myDataSnapshot =task.getResult();
                            String patientDescription=String.valueOf(myDataSnapshot.child("description").getValue());
                            String patientDepartment=String.valueOf(myDataSnapshot.child("department").getValue());
                            String patientDate=String.valueOf(myDataSnapshot.child("date").getValue());
                            String patientTime=String.valueOf(myDataSnapshot.child("time").getValue());

                            myAppointmentDescription2.setText(patientDescription);
                            myAppointmentDepartment2.setText(patientDepartment);
                            myAppointmentDate2.setText(patientDate);
                            myAppointmentTime2.setText(patientTime);


                        }
                        else
                        {
                            Toast.makeText(AppointmentBooking.this,"NO SECOND APPOINTMENT",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(AppointmentBooking.this,"SECOND APPOINTMENT IS EMPTY",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        if(textDescription3.isEmpty())
        {
            //String firstAppointment="FirstAppointment_"+userEmail;
            //Toast.makeText(AppointmentBooking.this,emailFirstAppt,Toast.LENGTH_SHORT).show();
            myDataStatus.child(emailThirdAppt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            DataSnapshot thisDataSnapshot=task.getResult();
                            String myStatus=String.valueOf(thisDataSnapshot.child("status").getValue());
                            bookingStatus3.setText(myStatus);
                        }
                        else
                        {
                            //Toast.makeText(AppointmentBooking.this,"NO SECOND APPOINTMENT",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(AppointmentBooking.this,"Error while reading Status Database...",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //now we read the appointment details
            mydatabaseAppointements.child(emailThirdAppt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {

                            myThirdApptLayout.setVisibility(View.VISIBLE);
                            DataSnapshot myDataSnapshot =task.getResult();
                            String patientDescription=String.valueOf(myDataSnapshot.child("description").getValue());
                            String patientDepartment=String.valueOf(myDataSnapshot.child("department").getValue());
                            String patientDate=String.valueOf(myDataSnapshot.child("date").getValue());
                            String patientTime=String.valueOf(myDataSnapshot.child("time").getValue());

                            myAppointmentDescription3.setText(patientDescription);
                            myAppointmentDepartment3.setText(patientDepartment);
                            myAppointmentDate3.setText(patientDate);
                            myAppointmentTime3.setText(patientTime);


                        }
                        else
                        {
                            Toast.makeText(AppointmentBooking.this,"NO SECOND APPOINTMENT",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(AppointmentBooking.this,"FIRST APPOINTMENT IS EMPTY",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

 */





    }


}