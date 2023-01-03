package com.example.my_hospital_appointments;

public class PatientAppointmentData {
    String Name;
    String Email;
    String Description;
    String Department;
    String Date;
    String Time;

    public PatientAppointmentData() {
    }

    public PatientAppointmentData(String name, String email, String description, String department, String date, String time) {
        Name = name;
        Email = email;
        Description = description;
        Department = department;
        Date = date;
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
