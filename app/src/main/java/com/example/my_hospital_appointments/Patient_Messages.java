package com.example.my_hospital_appointments;

public class Patient_Messages {
    String senderID;
    String timestamp;
    String Message;

    public Patient_Messages() {
    }

    public Patient_Messages(String senderID, String timestamp, String message) {
        this.senderID = senderID;
        this.timestamp = timestamp;
        Message = message;
    }

    public String getSender_id() {
        return senderID;
    }

    public void setSender_id(String sender_id) {
        this.senderID = sender_id;
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
