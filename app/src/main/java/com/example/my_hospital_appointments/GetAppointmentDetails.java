package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetAppointmentDetails extends AppCompatActivity {

    public String[] specialty={
            "Select One",
            "Cardiology",
            "Radiology",
            "Neurology",
            "Ophthalmology",
            "Haematology",
            "Anesthesiology",
            "Psychiatry",
            "Oncology",
            "Dermatology",
            "Emergency Medicine",
            "Pathology",
            "Pharmacy",
            "Orthopedics",
            "Rheumatology",
            "Surgery",
            "Physiotherapy",
            "Microbiology"
    };

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

    public String firstSpinnerText="";
    public String secondSpinnerText="";

    public Spinner spinDepartment;
    public  Spinner spinTime;

    private Button setDate,doneAppointments,saveCalendar;


    private CalendarView myCal;
    private String myYear,myMonth,myDayOfMonth;
    private String descriptionText;

    private RelativeLayout myCalendarLayout;

    private EditText myDescription;

    DatabaseReference databaseAppointments;
    DatabaseReference myDataStatus;
    DatabaseReference patientAppointments;
    DatabaseReference patientDetails;

    public String  textDescription1;
    public String  textDescription2;
    public String  textDescription3;
    public String myUsersEmail;
    public String emailFirstAppt="";
    public String emailSecondAppt="secondappointment";
    public String emailThirdAppt="thirdappointment";
    public String userID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_appointment_details);

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        myUsersEmail=extras.getString("userID");

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
                userID=userID+myUsersEmail.charAt(a);
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

        //Toast.makeText(this,"Your CODE IS "+emailFirstAppt,Toast.LENGTH_SHORT).show();

        textDescription1=extras.getString("textDescription1");
        textDescription2=extras.getString("textDescription2");
        textDescription3=extras.getString("textDescription3");
      //  Toast.makeText(this,"Your Description is "+textDescription1,Toast.LENGTH_SHORT).show();


        databaseAppointments=FirebaseDatabase.getInstance().getReference("Appointments");
        myDataStatus =FirebaseDatabase.getInstance().getReference("AppointmentStatus");
        patientAppointments =FirebaseDatabase.getInstance().getReference("PatientAppointments");
        patientDetails=FirebaseDatabase.getInstance().getReference("Patients");

        myDescription=(EditText)  findViewById(R.id.editTextTextMyDescription);
        //myDescription.requestFocus();

        spinDepartment=(Spinner) findViewById(R.id.spinnerDepartmentBooking);
        spinTime=(Spinner) findViewById(R.id.spinnerTimeBooking);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,specialty);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinDepartment.setAdapter(adapter1);
        spinDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String myText=spinDepartment.getSelectedItem().toString().trim();
                if (myText=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                    Toast.makeText(GetAppointmentDetails.this,"You have Selected "+myText,Toast.LENGTH_SHORT).show();
                    firstSpinnerText=myText;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,myTime);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinTime.setAdapter(adapter2);
        spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String myText2=spinTime.getSelectedItem().toString().trim();
                if (myText2=="Select One".trim())
                {
                    //Nothing
                }
                else
                {
                    Toast.makeText(GetAppointmentDetails.this,"You have Selected "+myText2,Toast.LENGTH_SHORT).show();
                    secondSpinnerText=myText2;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

         setDate=(Button) findViewById(R.id.buttonSetDate);
         doneAppointments=(Button) findViewById(R.id.buttonDoneSaveAppointment2);
         myCal=(CalendarView)  findViewById(R.id.calendarViewappointments);
         myCalendarLayout=(RelativeLayout)  findViewById(R.id.relativeLayoutCalendarAppointments);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate.setVisibility(View.GONE);
                doneAppointments.setVisibility(View.GONE);
                myCalendarLayout.setVisibility(View.VISIBLE);

            }
        });

        myCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                myYear=String.valueOf(year);
                myMonth=String.valueOf(month+1);
                myDayOfMonth=String.valueOf(dayOfMonth);
            }
        });


        saveCalendar=(Button) findViewById(R.id.buttonDoneCalendar);
        saveCalendar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                myCalendarLayout.setVisibility(View.GONE);
                setDate.setText("DATE : "+myDayOfMonth+"/"+myMonth+"/"+myYear);
                setDate.setVisibility(View.VISIBLE );
                doneAppointments.setVisibility(View.VISIBLE );

            }
        });



       doneAppointments.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addAppointment();
              // doneAppointments.setVisibility(View.INVISIBLE);


           }
       });

       Button backAppointments=(Button) findViewById(R.id.btnBackAppointmentsSet);
       backAppointments.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent myIntent = new Intent(GetAppointmentDetails.this,AppointmentBooking.class);
               startActivity(myIntent);
           }
       });

    }

    private void addAppointment()
    {
        descriptionText=myDescription.getText().toString().trim();
        if(descriptionText.isEmpty())
        {
           myDescription.setError("Cannot Be Blank");
           myDescription.requestFocus();
        }
        else if(firstSpinnerText.isEmpty())
        {
            Toast.makeText(GetAppointmentDetails.this,"PLEASE SELECT YOUR SPECIALTY ABOVE",Toast.LENGTH_SHORT).show();
             spinDepartment.requestFocus();
        }
        else if(secondSpinnerText.isEmpty())
        {
            Toast.makeText(GetAppointmentDetails .this,"PLEASE INPUT TIME THAT YOU WILL BE AVAILABLE",Toast.LENGTH_SHORT).show();
            spinTime.requestFocus();
        }
        else if(myYear.isEmpty())
        {
            Toast.makeText(GetAppointmentDetails .this,"PLEASE SELECT A DATE",Toast.LENGTH_LONG).show();

        }
        else
        {
           if(textDescription1.isEmpty())
           {

               String myTime="DATE : "+myDayOfMonth+"/"+myMonth+"/"+myYear;
               //String id=databaseAppointments.push().getKey(); //unique path that identifies the location of the data
               //String description, String department, String date, String time

               patientDetails.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DataSnapshot> task) {
                       if(task.isSuccessful())
                       {

                               DataSnapshot thisDataSnapshot=task.getResult();
                               String Name=String.valueOf(thisDataSnapshot.child("secondName").getValue());

                               PatientAppointmentData myPatientsData=new PatientAppointmentData(Name,myUsersEmail,descriptionText,firstSpinnerText,myTime,secondSpinnerText);
                               patientAppointments.child(userID).setValue(myPatientsData);

                               String data="PENDING...";
                               Appointments myAppointments=new Appointments(descriptionText,firstSpinnerText,myTime,secondSpinnerText);

                               emailFirstAppt =emailFirstAppt.trim();
                               databaseAppointments.child(emailFirstAppt).setValue(myAppointments);

                               AppointmentStatus myAppointmentStatus=new AppointmentStatus(data);
                               myDataStatus.child(emailFirstAppt).setValue(myAppointmentStatus);

                               Toast.makeText(GetAppointmentDetails .this,"YOUR APPOINTMENT HAS BEEN SAVED SUCCESSFULLY",Toast.LENGTH_LONG).show();



                       }
                       else
                       {
                           Toast.makeText(GetAppointmentDetails .this,"AN ERROR HAS OCCURRED CHECK INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                       }
                   }
               });



           }
           /*
           else if(textDescription2.isEmpty())
           {

               String myTime="DATE : "+myDayOfMonth+"/"+myMonth+"/"+myYear;
               // String id=databaseAppointments.push().getKey(); //unique path that identifies the location of the data
               //String description, String department, String date, String time

               String data="Pending...";
               Appointments myAppointments=new Appointments(descriptionText,firstSpinnerText,myTime,secondSpinnerText);

               databaseAppointments.child(emailSecondAppt).setValue(myAppointments);// changed
               Toast.makeText(GetAppointmentDetails .this,"SECOND APPOINTMENT BOOKED SUCCESSFULLY",Toast.LENGTH_LONG).show();

               AppointmentStatus myAppointmentStatus=new AppointmentStatus(data);
               myDataStatus.child(emailSecondAppt).setValue(myAppointmentStatus);

           }
           else if(textDescription3.isEmpty())
           {
               String myTime="DATE : "+myDayOfMonth+"/"+myMonth+"/"+myYear;
               // String id=databaseAppointments.push().getKey(); //unique path that identifies the location of the data
               //String description, String department, String date, String time

               String data="Pending...";
               Appointments myAppointments=new Appointments(descriptionText,firstSpinnerText,myTime,secondSpinnerText);

               databaseAppointments.child(emailThirdAppt).setValue(myAppointments);// changed

               AppointmentStatus myAppointmentStatus=new AppointmentStatus(data);
               myDataStatus.child(emailThirdAppt).setValue(myAppointmentStatus);


               Toast.makeText(GetAppointmentDetails .this,"THIRD BOOKED SUCCESSFULLY",Toast.LENGTH_LONG).show();

           }
           else
           {
               Toast.makeText(GetAppointmentDetails .this,"AN ERROR HAS OCCURRED WHILE BOOKING",Toast.LENGTH_LONG).show();
           }

            */


        }
    }
}