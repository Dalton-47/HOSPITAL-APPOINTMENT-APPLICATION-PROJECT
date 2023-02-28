package com.example.my_hospital_appointments;

public class PatientAppointmentClass {

     String patientEmail;
    String patientName;
   String patientAge;
     String appointmentDate;

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    String appointmentDescription;
     String docName;
     String reportDate;
    String reportTitle;
    String reportContent;

    public PatientAppointmentClass(String patientEmail, String patientName, String appointmentDescription, String appointmentDate, String patientAge, String reportDate, String docName, String reportTitle, String reportContent) {
        this.patientEmail=patientEmail;
        this.patientName=patientName;
        this.patientAge =patientAge;
        this.appointmentDate =appointmentDate;
        this.appointmentDescription =appointmentDescription;
        this.docName=docName;
        this.reportDate =reportDate;
        this.reportTitle=reportTitle;
        this.reportContent=reportContent;
    }
}
