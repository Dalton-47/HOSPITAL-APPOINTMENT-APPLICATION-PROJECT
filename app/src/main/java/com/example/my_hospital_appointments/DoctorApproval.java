package com.example.my_hospital_appointments;

public class DoctorApproval {
    String Status;

    public DoctorApproval() {
    }

    public DoctorApproval(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
