package com.example.my_hospital_appointments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PatientMessagesDatAdapter extends RecyclerView.Adapter<PatientMessagesDatAdapter.DataViewHolder> {

    private static ArrayList<Patients> dataList=new ArrayList<>() ;
    private static String doctorName;

    public void setData(ArrayList<Patients> dataList) {
        PatientMessagesDatAdapter.dataList =dataList;
        notifyDataSetChanged();
    }

    public PatientMessagesDatAdapter(ArrayList<Patients> dataList)
    {
        PatientMessagesDatAdapter.dataList =dataList;
    }
    public PatientMessagesDatAdapter(ArrayList<Patients> dataList, String docName) { //constructor to pass doctorsName
        //A static variable is accessed directly using the class name without instantiating the class
        //in order to access the static ArrayList
        PatientMessagesDatAdapter.dataList = dataList;
        doctorName = docName;
    }


    public static class DataViewHolder extends RecyclerView.ViewHolder {
        DatabaseReference patientsData;
        TextView patientName,patientAge,patientCounty;



        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName=(TextView) itemView.findViewById(R.id.textViewPatientMessagesName);
            patientAge =(TextView) itemView.findViewById(R.id.textViewPatientMessagesAge);
            patientCounty=(TextView) itemView.findViewById(R.id.textViewPatientMessagesResidence);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Patients myPatients= dataList.get(position);

                   // String myPatientName= myPatients.getFirstName() ;
                    String myPatientsEmail=myPatients.getEmailAddress();

                    String emailKey="";
                    int Counter=myPatientsEmail.length();
                    for(int a=0; a<Counter; a++)
                    {
                        if(myPatientsEmail.charAt(a)=='@')
                        {
                            break;
                        }
                        else
                        {
                            emailKey=emailKey+myPatientsEmail.charAt(a);
                        }
                    }

                    Intent myIntent=new Intent(v.getContext(),Chattingpage.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("emailKey",emailKey); //emailKey belongs to the patient
                    bundle.putString("doctorsName",doctorName );
                    myIntent.putExtras(bundle);

                         v.getContext().startActivity(myIntent);
                }
            });
        }
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_new_messages_layout_view,parent, false) ;
        return new PatientMessagesDatAdapter.DataViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        Patients  patients=dataList.get(position);
        holder.patientName.setText("Name : "+patients.getFirstName());
        holder.patientAge.setText("Age : "+patients.getAge());
        holder.patientCounty.setText("Residence : "+patients.getCounty());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
