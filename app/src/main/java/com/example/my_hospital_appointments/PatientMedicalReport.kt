package com.example.my_hospital_appointments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PatientMedicalReport : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        getReport(myUsersEmail)
    }

    var textViewTitle: TextView? = null
    var textViewDoctorName: TextView? = null
    var textViewResult: TextView? = null
    var textViewConclusion: TextView? = null
    var myUsersEmail: String? = null
    var myDatabaseReports: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_medical_report)
        myDatabaseReports = FirebaseDatabase.getInstance().getReference("MedicalReport")
        val intent = intent
        myUsersEmail = intent.extras!!.getString("usersEmail")
        textViewTitle = findViewById<View>(R.id.textViewTitleMedicalPATIENT) as TextView
        textViewDoctorName = findViewById<View>(R.id.textViewDoctorNameMedicalPATIENT) as TextView
        textViewResult = findViewById<View>(R.id.textViewtestResultMedicalPATIENT) as TextView
        textViewConclusion = findViewById<View>(R.id.textViewConclusionMedicalPATIENT) as TextView
    }

    private fun getReport(myKey: String?) {
        var emailKey = ""
        val Counter = myKey!!.length
        for (a in 0 until Counter) {
            emailKey = if (myKey[a] == '@') {
                break
            } else {
                emailKey + myKey[a]
            }
        }
        emailKey = emailKey.trim { it <= ' ' }
        myDatabaseReports!!.child(emailKey).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.exists()) {
                    val thisDataSnapshot = task.result
                    val title = thisDataSnapshot.child("title").value.toString()
                    val doctorName = thisDataSnapshot.child("doctorName").value.toString()
                    val testResult = thisDataSnapshot.child("testResult").value.toString()
                    val conclusion = thisDataSnapshot.child("conclusion").value.toString()
                    textViewDoctorName!!.text = doctorName
                    textViewTitle!!.text = title
                    textViewResult!!.text = testResult
                    textViewConclusion!!.text = conclusion
                } else {
                    val notYet = "NOT AVAILABLE"
                    textViewDoctorName!!.text = notYet
                    textViewTitle!!.text = notYet
                    textViewResult!!.text = notYet
                    textViewConclusion!!.text = notYet
                }
            }
        }
    }
}