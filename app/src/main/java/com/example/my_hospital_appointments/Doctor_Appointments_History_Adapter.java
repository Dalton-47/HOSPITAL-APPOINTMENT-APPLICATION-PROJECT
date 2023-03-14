package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Doctor_Appointments_History_Adapter extends RecyclerView.Adapter <Doctor_Appointments_History_Adapter.Doctor_History_DataViewHolder>{

 ArrayList <Appointment_Data_Class> appointmentsList= new ArrayList<>();

    public Doctor_Appointments_History_Adapter(ArrayList<Appointment_Data_Class> appointmentList) {
        this.appointmentsList=appointmentList;
    }

    public void setData(ArrayList<Appointment_Data_Class> appointmentList) {
        this.appointmentsList=appointmentList;
        notifyDataSetChanged();
    }


    protected static class Doctor_History_DataViewHolder extends RecyclerView.ViewHolder{
        TextView txtViewAppointmentDate, txtViewAppointmentDescription,txtViewPatientName,txtViewPatientAge,txtViewReportContent;

        public Doctor_History_DataViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewAppointmentDate = (TextView)  itemView.findViewById(R.id.textViewAppointmentDate_REC);
            txtViewAppointmentDescription =(TextView)  itemView.findViewById(R.id.textViewAppointmentDescription_REC);
            txtViewPatientName = (TextView)  itemView.findViewById(R.id.textViewPatientName_REC);
            txtViewPatientAge = (TextView)  itemView.findViewById(R.id.textViewPatientAge_REC);
            txtViewReportContent = (TextView)  itemView.findViewById(R.id.textViewReportContent_REC);



        }
    }

    @NonNull
    @Override
    public Doctor_Appointments_History_Adapter.Doctor_History_DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_report_history_recyclerview, parent,false);
        return new Doctor_History_DataViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull Doctor_Appointments_History_Adapter.Doctor_History_DataViewHolder holder, int position) {


        Appointment_Data_Class  appointments = appointmentsList.get(position);

        holder.txtViewPatientName.setText(appointments.getPatientName());
        holder.txtViewPatientAge.setText(appointments.getPatientAge());
        holder.txtViewAppointmentDescription.setText(appointments.getAppointmentDescription());
        holder.txtViewReportContent.setText(appointments.getReportContent());

        holder.txtViewAppointmentDate.setText(appointments.getAppointmentDate());

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


}
