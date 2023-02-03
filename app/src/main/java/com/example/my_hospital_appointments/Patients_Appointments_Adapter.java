package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Patients_Appointments_Adapter extends RecyclerView.Adapter<Patients_Appointments_Adapter.DataViewHolder>{

    ArrayList<appointmentsNew> appointmentsList=new ArrayList<>();

   public Patients_Appointments_Adapter (ArrayList<appointmentsNew> appointmentsList)
    {
        this.appointmentsList=appointmentsList;
    }


    public void setData(ArrayList<appointmentsNew> appointmentsList) {
       this.appointmentsList=appointmentsList;
       notifyDataSetChanged();
    }


    public static class DataViewHolder extends RecyclerView.ViewHolder {
         TextView appointmentDescription,appointmentDepartment,appointmentTime,appointmentDate;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentDescription = (TextView)  itemView.findViewById(R.id.textViewAppointmentDescriptionNew);
            appointmentDepartment = (TextView)  itemView.findViewById(R.id.textViewAppointmentDepartmentNew);
            appointmentDate = (TextView)  itemView.findViewById(R.id.textViewAppointmentDateNew);
            appointmentTime = (TextView)  itemView.findViewById(R.id.textViewAppointmentTimeNew);

        }
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appointments_recyclerview_layout,parent,false ) ;

        return new Patients_Appointments_Adapter.DataViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        appointmentsNew  appoints=appointmentsList.get(position);
        holder.appointmentDescription.setText(appoints.getDescription());
        holder.appointmentDepartment.setText(appoints.getDepartment());
        holder.appointmentDate.setText(appoints.getDate());
        holder.appointmentTime.setText(appoints.getTime());

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


}
