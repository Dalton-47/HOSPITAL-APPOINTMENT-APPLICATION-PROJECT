package com.example.my_hospital_appointments;

public class PendingDoctors {
    String ID,PhoneNumber,EmployeeNumber,UserName,Email,Time,Department;

    public PendingDoctors() {
    }

    public PendingDoctors(String ID, String phoneNumber, String employeeNumber, String userName, String email, String time, String department) {
        this.ID = ID;
        PhoneNumber = phoneNumber;
        EmployeeNumber = employeeNumber;
        UserName = userName;
        Email = email;
        Time = time;
        Department = department;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmployeeNumber() {
        return EmployeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        EmployeeNumber = employeeNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
