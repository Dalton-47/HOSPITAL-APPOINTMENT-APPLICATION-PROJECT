package com.example.my_hospital_appointments;

public class Appointments {
    String Description;
    String age;
    String Date;
    String Time;
    String Status;

    public Appointments(String description, String age, String date, String time) {
        Description = description;
        this.age = age;
        Date = date;
        Time = time;
    }

    public Appointments(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
