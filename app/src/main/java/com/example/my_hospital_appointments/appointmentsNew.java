package com.example.my_hospital_appointments;

public class appointmentsNew {
    String Description;
    String Department;
    String Date;
    String Time;

    public appointmentsNew() {

    }


    public appointmentsNew(String description, String department, String date, String time) {
        Description = description;
        Department = department;
        Date = date;
        Time = time;
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
