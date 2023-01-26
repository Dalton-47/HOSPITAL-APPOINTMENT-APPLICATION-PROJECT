package com.example.my_hospital_appointments;

public class Patients {
    //  public String firstName,secondName,phoneNumber,emailAddress,password;
    String FirstName;
    String SecondName;
    String PhoneNumber;
    String EmailAddress;
    String Age;
    String County;

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public Patients() {
    }

    public Patients(String firstName, String secondName, String phoneNumber, String emailAddress, String age, String county) {
        FirstName = firstName;
        SecondName = secondName;
        PhoneNumber = phoneNumber;
        EmailAddress = emailAddress;
        Age = age;
        County = county;
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
