package com.example.my_hospital_appointments;

public class user {
    public String userID,userPhoneNumber,userEmployeeNumber,userName,email,firstUserSpinnerText,secondUserSpinnerText;

    public user()
    {

    }

    public user(String myUserID,String myUserPhoneNumber,String myUserEmployeeNumber,String myUserName,String myEmail,String myFirstUserSpinnerText,String mySecondUserSpinnerText)
    {
        this.userID=myUserID;
        this.userPhoneNumber=myUserPhoneNumber;
        this.userEmployeeNumber=myUserEmployeeNumber;
        this.userName=myUserName;
        this.email=myEmail;
        this.firstUserSpinnerText=myFirstUserSpinnerText;
        this.secondUserSpinnerText=mySecondUserSpinnerText;
    }

}
