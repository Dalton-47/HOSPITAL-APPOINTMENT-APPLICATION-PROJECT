package com.example.my_hospital_appointments;

public class Patient_Messages {
    String sender_id;
    String timestamp;
    String Message;

    public Patient_Messages() {
    }

    public Patient_Messages(String sender_id, String timestamp, String message) {
        this.sender_id = sender_id;
        this.timestamp = timestamp;
        Message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
