package com.example.my_hospital_appointments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

class DoctorAppointments() : AppCompatActivity() {
    private val myArrayList = ArrayList<String?>()
    var myDoctorsData: DatabaseReference? = null
    var attendedAppointmentsData: DatabaseReference? = null
    var spinnerEmails: Spinner? = null
    var patientEmail = ""
    var textViewPatientName: TextView? = null
    var textViewPatientDescription: TextView? = null
    var textViewPatientDate: TextView? = null
    var textViewPatientTime: TextView? = null
    var btnViewAppointments: Button? = null
    var btnCancelAppointments: Button? = null
    var btnAttendAppointments: Button? = null
    var backHomeAppointments: Button? = null
    private var patientName: String? = null
    private var patientDescription: String? = null
    private var patientDate: String? = null
    private var patientTime: String? = null
    var myLayoutAttend: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointments)
        myLayoutAttend = findViewById<View>(R.id.relativeLayoutViewAttend) as RelativeLayout
        attendedAppointmentsData =
            FirebaseDatabase.getInstance().getReference("AttendedAppointments")
        textViewPatientName =
            findViewById<View>(R.id.textViewPatientNameDoctorAppointments2) as TextView
        textViewPatientDescription =
            findViewById<View>(R.id.textViewPatientDescriptionDoctorAppointments2) as TextView
        textViewPatientDate =
            findViewById<View>(R.id.textViewPatientDateDoctorAppointments2) as TextView
        textViewPatientTime =
            findViewById<View>(R.id.textViewPatientTimeDoctorAppointments2) as TextView
        val intent = intent
        val myUsersEmail = intent.extras!!.getString("usersEmail")
        spinnerEmails = findViewById<View>(R.id.spinnerDoctorAppointments) as Spinner
        myArrayList.add("Select One")
        myDoctorsData = FirebaseDatabase.getInstance().reference.child("AssignedPatient")
        myDoctorsData!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    myArrayList.add(snapshot.child("email").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,
                myArrayList as List<Any?>
            )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmails!!.adapter = adapter1
        spinnerEmails!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val myText: String = spinnerEmails!!.selectedItem.toString().trim { it <= ' ' }
                if (myText === "Select One".trim { it <= ' ' }) {
                    //Nothing
                } else {
                    Toast.makeText(
                        this@DoctorAppointments,
                        "You have Selected $myText",
                        Toast.LENGTH_SHORT
                    ).show()
                    patientEmail = myText
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        btnViewAppointments = findViewById<View>(R.id.buttonDoctorViewAppointments) as Button
        btnViewAppointments!!.setOnClickListener(View.OnClickListener { ViewAppointment(myUsersEmail) })
        btnAttendAppointments = findViewById<View>(R.id.buttonApointmentAttendedDoctor) as Button
        btnAttendAppointments!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                AttendedAppointments(myUsersEmail)
            }
        })
        btnCancelAppointments = findViewById<View>(R.id.buttonCancelAppointmentDoctor) as Button
        btnCancelAppointments!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                myDoctorsData!!.child((myUsersEmail)!!).removeValue()
                Toast.makeText(
                    this@DoctorAppointments,
                    "APPOINTMENT CANCELLED SUCCESSSFULLY",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun ViewAppointment(doctorEmail: String?) {
        var emailKey = ""
        val Counter = doctorEmail!!.length
        for (a in 0 until Counter) {
            if (doctorEmail[a] == '@') {
                break
            } else {
                emailKey = emailKey + doctorEmail[a]
            }
        }
        emailKey = emailKey.trim { it <= ' ' }
        myDoctorsData!!.child(emailKey).get()
            .addOnCompleteListener(object : OnCompleteListener<DataSnapshot> {
                override fun onComplete(task: Task<DataSnapshot>) {
                    if (task.isSuccessful) {
                        if (task.result.exists()) {
                            // public TextView patientName,patientDescription,patientDate,patientTime;
                            val thisDataSnapshot = task.result
                            patientName = thisDataSnapshot.child("name").value.toString()
                            patientDescription =
                                thisDataSnapshot.child("description").value.toString()
                            patientDate = thisDataSnapshot.child("date").value.toString()
                            patientTime = thisDataSnapshot.child("time").value.toString()
                            textViewPatientName!!.text = patientName
                            textViewPatientDate!!.text = patientDate
                            textViewPatientDescription!!.text = patientDescription
                            textViewPatientTime!!.text = patientTime
                        }
                    }
                }
            })
    }

    fun AttendedAppointments(doctorEmail: String?) {
        var emailKey = ""
        val Counter = doctorEmail!!.length
        for (a in 0 until Counter) {
            if (doctorEmail[a] == '@') {
                break
            } else {
                emailKey = emailKey + doctorEmail[a]
            }
        }
        emailKey = emailKey.trim { it <= ' ' }

        //String id=databaseAppointments.push().getKey(); //unique path that identifies the location of the data
        val id = attendedAppointmentsData!!.push().key
        //patientName,patientDescription,patientDate,patientTime
        //String patientEmail, String doctorEmail, String description, String time, String date
        val myAppointments = AppointmentsAttended(
            patientEmail,
            doctorEmail,
            patientDescription,
            patientTime,
            patientDate
        )
        val finalEmailKey = emailKey
        attendedAppointmentsData!!.child((id)!!).setValue(myAppointments)
            .addOnCompleteListener(object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    if (task.isSuccessful) {
                        myDoctorsData!!.child(finalEmailKey).removeValue()
                        btnViewAppointments!!.visibility = View.GONE
                        btnAttendAppointments!!.visibility = View.GONE
                        btnCancelAppointments!!.visibility = View.GONE
                        myLayoutAttend!!.visibility = View.VISIBLE
                        backHomeAppointments =
                            findViewById<View>(R.id.buttonBackAfterAppointments) as Button
                        backHomeAppointments!!.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(view: View) {
                                val myIntent =
                                    Intent(this@DoctorAppointments, MainPageDoctor::class.java)
                                startActivity(myIntent)
                            }
                        })
                    }
                }
            })
    }
}