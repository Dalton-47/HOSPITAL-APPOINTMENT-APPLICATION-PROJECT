package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Patients_Appointments_Adapter extends RecyclerView.Adapter<Patients_Appointments_Adapter.DataViewHolder>{

    ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();
    private ArrayList<myDoctor> assignedDoctorList=new ArrayList<>();
    String name,email,phoneNumber;

   public Patients_Appointments_Adapter (ArrayList<PatientAppointmentData> appointmentsList)
    {
        this.appointmentsList=appointmentsList;
    }


    public void setData(ArrayList<PatientAppointmentData> appointmentsList) {
       this.appointmentsList=appointmentsList;
       notifyDataSetChanged();
    }

    public void setDoc(ArrayList <myDoctor> addDoc) {
       this.assignedDoctorList =addDoc;
       notifyDataSetChanged();
    }


    public static class DataViewHolder extends RecyclerView.ViewHolder {
         TextView appointmentDescription,appointmentDepartment,appointmentTime,appointmentDate,docName,docPhone,docEmail;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentDescription = (TextView)  itemView.findViewById(R.id.textViewAppointmentDescriptionNew);
            appointmentDepartment = (TextView)  itemView.findViewById(R.id.textViewAppointmentDepartmentNew);
            appointmentDate = (TextView)  itemView.findViewById(R.id.textViewAppointmentDateNew);
            appointmentTime = (TextView)  itemView.findViewById(R.id.textViewAppointmentTimeNew);

            docName  = (TextView)  itemView.findViewById(R.id.textViewDocName_APP);
            docPhone = (TextView)  itemView.findViewById(R.id.textViewDocPhone_APP);
            docEmail = (TextView)  itemView.findViewById(R.id.textViewDocEmail_APP);

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

        PatientAppointmentData  appoints=appointmentsList.get(position);

        holder.appointmentDescription.setText(appoints.getDescription());
        holder.appointmentDepartment.setText(appoints.getAge()+" Years"); //where age should be
        holder.appointmentDate.setText(appoints.getDate());
        holder.appointmentTime.setText(appoints.getTime());

     myDoctor newDoctor= assignedDoctorList.get(position);
        holder.docName.setText(newDoctor.getName());
        holder.docPhone.setText(newDoctor.PhoneNumber);
        holder.docEmail.setText(newDoctor.getEmail());

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


}
