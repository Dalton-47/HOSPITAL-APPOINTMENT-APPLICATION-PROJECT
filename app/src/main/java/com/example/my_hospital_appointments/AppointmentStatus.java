package com.example.my_hospital_appointments;

public class AppointmentStatus {
    String Status;

    public AppointmentStatus() {
    }

    public AppointmentStatus(String status) {
        this.Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
