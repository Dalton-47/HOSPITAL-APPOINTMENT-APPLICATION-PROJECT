package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Doctor_Appointments_Adapter extends RecyclerView.Adapter <Doctor_Appointments_Adapter.myDataViewHolder>{


    ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();

    protected static class myDataViewHolder extends RecyclerView.ViewHolder{
     TextView textViewPatientName,textViewPatientAge,textViewPatientDate,textViewPatientTime,textViewPatientDescription;
        public myDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPatientName =(TextView)  itemView.findViewById(R.id.textViewPatientName_SET);
            textViewPatientAge =(TextView)  itemView.findViewById(R.id.textViewPatientAge_SET);
            textViewPatientDate  =(TextView)  itemView.findViewById(R.id.textViewPatientDate_SET);
            textViewPatientTime  =(TextView)  itemView.findViewById(R.id.textViewPatientTime_SET);
            textViewPatientDescription  =(TextView)  itemView.findViewById(R.id.textViewPatientDescription_SET);

        }
    }


    @NonNull
    @Override
    public myDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointments_reyclerview_layout_new, parent,false);
        return new myDataViewHolder(myView);

    }

    @Override
    public void onBindViewHolder(@NonNull myDataViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 0;
    }
}
