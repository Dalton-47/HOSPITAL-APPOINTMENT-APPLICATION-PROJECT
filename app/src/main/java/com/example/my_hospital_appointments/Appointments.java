package com.example.my_hospital_appointments;

public class Appointments {
    String Description;
    String Department;
    String Date;
    String Time;
    String Status;



    public Appointments()
    {

    }

    public Appointments(String description, String department, String date, String time) {
        Description = description;
        Department = department;
        Date = date;
        Time = time;

    }

   public Appointments(String status) {
        this.Status = status;
    }



    public String getDescription() {
        return Description;
    }

    public String getDepartment() {
        return Department;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
