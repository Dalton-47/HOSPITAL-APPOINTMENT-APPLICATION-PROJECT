package com.example.my_hospital_appointments;

public class patientMessages {
    String Message;
    String Email;

    public patientMessages() {
    }

    public patientMessages(String message, String email) {
        Message = message;
        Email = email;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
