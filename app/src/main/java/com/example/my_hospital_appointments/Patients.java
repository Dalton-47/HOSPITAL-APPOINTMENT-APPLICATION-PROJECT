package com.example.my_hospital_appointments;

public class Patients {
    //  public String firstName,secondName,phoneNumber,emailAddress,password;
    String FirstName;
    String SecondName;
    String PhoneNumber;
    String EmailAddress;

    public Patients() {
    }

    public Patients(String firstName, String secondName, String phoneNumber, String emailAddress) {
        FirstName = firstName;
        SecondName = secondName;
        PhoneNumber = phoneNumber;
        EmailAddress = emailAddress;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String secondName) {
        SecondName = secondName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }
}
