package com.example.my_hospital_appointments;

public class myDoctor {
    String Name;
    String Email;
    String PhoneNumber;

    public myDoctor() {
    }

    public myDoctor(String name, String email, String phoneNumber) {
        Name = name;
        Email = email;
        PhoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
