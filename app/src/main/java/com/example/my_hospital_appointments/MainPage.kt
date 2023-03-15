package com.example.my_hospital_appointments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainPage : AppCompatActivity() {
    var userID = ""
    var patientDetails: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        val intent = intent
        val myUsersEmail = intent.extras!!.getString("usersEmail")
        // Toast.makeText(this,"Your Email is "+myUsersEmail ,Toast.LENGTH_SHORT).show();
        patientDetails = FirebaseDatabase.getInstance().getReference("Patients")
        val Counter = myUsersEmail!!.length
        for (a in 0 until Counter) {
            userID = if (myUsersEmail[a] == '@') {
                break
            } else {
                // emailFirstAppt=emailFirstAppt+myUsersEmail.charAt(a);
                userID + myUsersEmail[a]
            }
        }
        val myHello = findViewById<View>(R.id.textViewMainPageHello) as TextView
        patientDetails!!.child(userID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val thisDataSnapshot = task.result
                val Name = thisDataSnapshot.child("secondName").value.toString()
                myHello.text = "HELLO $Name,"
            }
        }
        val myContacts = findViewById<View>(R.id.imageButtonContacts) as ImageButton
        myContacts.setOnClickListener {
            val myIntent = Intent(this@MainPage, Contacts::class.java)
            myIntent.putExtra("usersEmail", myUsersEmail)
            startActivity(myIntent)
        }
        val bookAppointment = findViewById<View>(R.id.imageButtonBookAppointment) as ImageButton
        bookAppointment.setOnClickListener { /*
                Intent myIntent=new Intent(MainPage.this,AppointmentBooking.class);
                myIntent.putExtra("usersEmail",myUsersEmail);
                startActivity(myIntent);
                
                 */
        }
        val consultDoctor = findViewById<View>(R.id.imageButtonPatientConsultDoctor) as ImageButton
        consultDoctor.setOnClickListener { // Intent myIntent=new Intent(MainPage.this,PatientViewDoctorConsult.class);
            // myIntent.putExtra("usersEmail",myUsersEmail);
            val myIntent = Intent(this@MainPage, Patient_Check_Doctors_to_Consult_New::class.java)
            myIntent.putExtra("userEmailID", userID)
            startActivity(myIntent)
        }
        val medicalReports =
            findViewById<View>(R.id.imageButtonPatientMedicalReports) as ImageButton
        medicalReports.setOnClickListener { // Intent myIntent=new Intent(MainPage.this,PatientMedicalReport.class);
            val myIntent = Intent(this@MainPage, Patient_Main_Page_Activity::class.java)
            myIntent.putExtra("usersEmail", myUsersEmail)
            startActivity(myIntent)
        }
    }
}