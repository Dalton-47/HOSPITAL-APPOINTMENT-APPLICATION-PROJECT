package com.example.my_hospital_appointments;

public class ReportClass {
    String Title;
    String DoctorName;
    String TestResult;
    String  Conclusion;

    public ReportClass() {
    }

    public ReportClass(String title, String doctorName, String testResult, String conclusion) {
        Title = title;
        DoctorName = doctorName;
        TestResult = testResult;
        Conclusion = conclusion;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getTestResult() {
        return TestResult;
    }

    public void setTestResult(String testResult) {
        TestResult = testResult;
    }

    public String getConclusion() {
        return Conclusion;
    }

    public void setConclusion(String conclusion) {
        Conclusion = conclusion;
    }
}
