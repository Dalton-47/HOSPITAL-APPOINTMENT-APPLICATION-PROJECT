package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class GetAppointmentDetails extends AppCompatActivity {


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

    //public String firstSpinnerText="";
    public String secondSpinnerText="";

   // public Spinner spinDepartment;
    public  Spinner spinTime;

    private Button doneAppointments;

    private String descriptionText;

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
    TextView textViewCalendar_new;

    private DatePickerDialog picker;
    EditText editTextPatientAge;


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
        Toast.makeText(this, "USER ID :"+userID, Toast.LENGTH_SHORT).show();

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


        spinTime=(Spinner) findViewById(R.id.spinnerTimeBooking);


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



        editTextPatientAge=(EditText) this.findViewById(R.id.editTextPatientAge_new);


       textViewCalendar_new=(TextView)  this.findViewById(R.id.textViewAppointmentDate_New);
        textViewCalendar_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                picker = new DatePickerDialog(GetAppointmentDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        textViewCalendar_new.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },year, month, day);
                picker.show();
            }
        });

        doneAppointments =(Button)  this.findViewById(R.id.buttonDoneSaveAppointment2);
        doneAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppointment();
                // doneAppointments.setVisibility(View.INVISIBLE);


            }
        });

    }

    private void addAppointment()
    {

        String newDate= textViewCalendar_new.getText().toString().trim();
        String patientAge=editTextPatientAge.getText().toString().trim();
        descriptionText=myDescription.getText().toString().trim();
        if(descriptionText.isEmpty())
        {
           myDescription.setError("Cannot Be Blank");
           myDescription.requestFocus();
        }
        else if(descriptionText.length() > 69)
        {
            myDescription.setError("Description is too long");
            myDescription.requestFocus();
        }
        else if(patientAge.isEmpty())
        {
            Toast.makeText(GetAppointmentDetails.this,"PLEASE ENTER PATIENT'S AGE",Toast.LENGTH_SHORT).show();
             editTextPatientAge.requestFocus();
        }

        else if(secondSpinnerText.isEmpty())
        {
            Toast.makeText(GetAppointmentDetails .this,"PLEASE INPUT TIME THAT YOU WILL BE AVAILABLE",Toast.LENGTH_SHORT).show();
            spinTime.requestFocus();
        }
        else if(newDate.isEmpty())
        {
            textViewCalendar_new.setError("Cannot be blank");
            Toast.makeText(GetAppointmentDetails .this,"PLEASE SELECT A DATE",Toast.LENGTH_LONG).show();

        }
        else
        {


               //String myTime="DATE : "+myDayOfMonth+"/"+myMonth+"/"+myYear;
               //String id=databaseAppointments.push().getKey(); //unique path that identifies the location of the data
               //String description, String department, String date, String time

               patientDetails.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DataSnapshot> task) {
                       if(task.isSuccessful())
                       {
                           //change firstSpinnerText to age

                               DataSnapshot thisDataSnapshot=task.getResult();
                               String Name=String.valueOf(thisDataSnapshot.child("secondName").getValue());

                               PatientAppointmentData myPatientsData=new PatientAppointmentData(Name,myUsersEmail,descriptionText,patientAge,newDate,secondSpinnerText);
                               patientAppointments.child(userID).setValue(myPatientsData);

                               String data="PENDING...";
                               Appointments myAppointments=new Appointments(descriptionText,patientAge,newDate,secondSpinnerText);

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