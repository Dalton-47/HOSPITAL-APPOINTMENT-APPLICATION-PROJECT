package com.example.my_hospital_appointments;

public class AppointmentsAttended {
    String patientEmail;
    String doctorEmail;
    String description;
    String time;
    String date;

    public AppointmentsAttended() {
    }

    public AppointmentsAttended(String patientEmail, String doctorEmail, String description, String time, String date) {
        this.patientEmail = patientEmail;
        this.doctorEmail = doctorEmail;
        this.description = description;
        this.time = time;
        this.date = date;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
