package com.example.my_hospital_appointments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

class MedicalReport() : AppCompatActivity() {
    var editTextTitle: EditText? = null
    var editTextDoctorName: EditText? = null
    var editTextResult: EditText? = null
    var editTextConclusion: EditText? = null
    private val myArrayList = ArrayList<String?>()
    var myReport: DatabaseReference? = null
    private var patientEmail: String? = null
    var Reports: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_report)
        editTextTitle = findViewById<View>(R.id.editTextTextTitleMedical) as EditText
        editTextDoctorName = findViewById<View>(R.id.editTextTextDoctorNameMedical) as EditText
        editTextResult = findViewById<View>(R.id.editTextTextTestResultMedical) as EditText
        editTextConclusion = findViewById<View>(R.id.editTextTextConclusionMedical) as EditText
        Reports = FirebaseDatabase.getInstance().getReference("MedicalReport")
        myArrayList.add("Select One")
        myReport = FirebaseDatabase.getInstance().reference.child("Patients")
        myReport!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot: DataSnapshot in dataSnapshot.children) {
                    myArrayList.add(snapshot.child("emailAddress").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        val adapter1: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,
                myArrayList as List<Any?>
            )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerEmails = findViewById<View>(R.id.spinnerPatientMedical) as Spinner
        spinnerEmails.adapter = adapter1
        spinnerEmails.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val myText: String = spinnerEmails.selectedItem.toString().trim { it <= ' ' }
                if (myText === "Select One".trim { it <= ' ' }) {
                    //Nothing
                } else {
                    Toast.makeText(
                        this@MedicalReport,
                        "You have Selected $myText",
                        Toast.LENGTH_SHORT
                    ).show()
                    patientEmail = myText
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        val myReport = findViewById<View>(R.id.buttonSendReportMedical) as Button
        myReport.setOnClickListener(View.OnClickListener {
            if (patientEmail!!.isEmpty()) {
                Toast.makeText(
                    this@MedicalReport,
                    "FIRST SELECT A PATIENT FROM ABOVE",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setReport(patientEmail)
            }
        })
    }

    private fun setReport(myKey: String?) {
        var emailKey = ""
        val Counter = myKey!!.length
        for (a in 0 until Counter) {
            if (myKey[a] == '@') {
                break
            } else {
                emailKey = emailKey + myKey[a]
            }
        }
        emailKey = emailKey.trim { it <= ' ' }
        val title = editTextTitle!!.text.toString().trim { it <= ' ' }
        val doctorName = editTextDoctorName!!.text.toString().trim { it <= ' ' }
        val testResult = editTextResult!!.text.toString().trim { it <= ' ' }
        val conclusion = editTextConclusion!!.text.toString().trim { it <= ' ' }
        if (title.isEmpty()) {
            editTextTitle!!.error = "REPORT TITLE REQUIRED"
            editTextTitle!!.requestFocus()
        } else if (doctorName.isEmpty()) {
            editTextDoctorName!!.error = "DOCTOR NAME REQUIRED"
            editTextDoctorName!!.requestFocus()
        } else if (testResult.isEmpty()) {
            editTextResult!!.error = "TEST RESULT CANNOT BE BLANK"
            editTextResult!!.requestFocus()
        } else if (conclusion.isEmpty()) {
            editTextConclusion!!.error = "CONCLUSION IS REQUIRED"
            editTextConclusion!!.requestFocus()
        } else {
            val finalEmailKey = emailKey
            Reports!!.child(emailKey).get()
                .addOnCompleteListener(object : OnCompleteListener<DataSnapshot?> {
                    override fun onComplete(task: Task<DataSnapshot?>) {
                        if (task.isSuccessful) {
                            //String title, String doctorName, String testResult, String conclusion
                            val myReportsClass =
                                ReportClass(title, doctorName, testResult, conclusion)
                            Reports!!.child(finalEmailKey).setValue(myReportsClass)
                            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(
                                this@MedicalReport,
                                "REPORT SAVED SUCCESSFULLY",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }
}