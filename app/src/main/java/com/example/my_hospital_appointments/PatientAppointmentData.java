package com.example.my_hospital_appointments;

public class PatientAppointmentData {
    String Name;
    String Email;
    String Description;
    String age;
    String Date;
    String Time;

    public PatientAppointmentData() {
    }

    public PatientAppointmentData(String name, String email, String description, String age, String date, String time) {
        Name = name;
        Email = email;
        Description = description;
        this.age = age;
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
}
