package com.example.my_hospital_appointments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainPageDoctor : AppCompatActivity() {
    var emailKey = ""
    var myDoctorData: DatabaseReference? = null
    var helloUser: TextView? = null
    var greetingUser: TextView? = null
    var doctorName: String? = null
    var doctorAppointments: Button? = null
    var btnDoctorConsult: Button? = null
    var btnMedicalReports: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page_doctor)
        helloUser = findViewById<View>(R.id.textViewHelloDr) as TextView
        greetingUser = findViewById<View>(R.id.textViewHowArYouDr) as TextView
        val intent = intent
        val myUsersEmail = intent.extras!!.getString("usersEmail")
        Toast.makeText(this, "DOC ID : $myUsersEmail", Toast.LENGTH_SHORT).show()
        val parts =
            myUsersEmail!!.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val emailID = parts[0]
        myDoctorData = FirebaseDatabase.getInstance().getReference("Doctors")
        getDoctorName(myUsersEmail)
        doctorAppointments = findViewById<View>(R.id.buttonDoctorAppointments1) as Button
        doctorAppointments!!.setOnClickListener { // Intent myIntent=new Intent(MainPageDoctor.this,DoctorAppointments.class);
            val myIntent = Intent(this@MainPageDoctor, Doctor_View_Appointments_NEW::class.java)
            myIntent.putExtra("docEmailId", emailID)
            startActivity(myIntent)
        }
        btnDoctorConsult = findViewById<View>(R.id.buttonDoctorConsultancy) as Button
        btnDoctorConsult!!.setOnClickListener { //  Intent myIntent=new Intent(MainPageDoctor.this,DoctorConsultation.class);
            val myIntent = Intent(this@MainPageDoctor, Messages_From_Patients::class.java)
            myIntent.putExtra("doctorName", doctorName)
            startActivity(myIntent)
        }
        btnMedicalReports = findViewById<View>(R.id.buttonLabReportsDoctor) as Button
        btnMedicalReports!!.setOnClickListener {
            val myIntent = Intent(this@MainPageDoctor, MedicalReport::class.java)
            myIntent.putExtra("usersEmail", myUsersEmail)
            startActivity(myIntent)
        }
    }

    private fun getDoctorName(doctorEmail: String?) {
        val Counter = doctorEmail!!.length
        for (a in 0 until Counter) {
            emailKey = if (doctorEmail[a] == '@') {
                break
            } else {
                emailKey + doctorEmail[a]
            }
        }

        // emailKey=emailKey.trim();
        myDoctorData!!.child(emailKey).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.exists()) {
                    val thisDataSnapshot = task.result
                    doctorName = thisDataSnapshot.child("userName").value.toString()
                    if (!doctorName!!.isEmpty()) {
                        val Hello = "Hello Dr. $doctorName"
                        val day = "How Are You Doing Today?"
                        helloUser!!.text = Hello
                        greetingUser!!.text = day
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        val myUsersEmail = intent.extras!!.getString("usersEmail")
        getDoctorName(myUsersEmail)
    }
}