package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Doctor_Appointments_Adapter extends RecyclerView.Adapter <Doctor_Appointments_Adapter.myDataViewHolder>{


     ArrayList<myPatient> patientList=new ArrayList<>();
    ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();

    public Doctor_Appointments_Adapter(ArrayList<myPatient> patientAppointmentsList) {
        this.patientList=patientAppointmentsList;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<myPatient> patientAppointmentsList) {
        this.patientList=patientAppointmentsList;
        notifyDataSetChanged();
    }

    protected static class myDataViewHolder extends RecyclerView.ViewHolder{
     TextView textViewPatientName,textViewPatientAge,textViewPatientDate,textViewPatientTime,textViewPatientDescription;
        public myDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPatientName =(TextView)  itemView.findViewById(R.id.textViewPatientName_SET);
            textViewPatientAge =(TextView)  itemView.findViewById(R.id.textViewPatientAge_SET);
            textViewPatientDate  =(TextView)  itemView.findViewById(R.id.textViewPatientDate_SET);
            textViewPatientTime  =(TextView)  itemView.findViewById(R.id.textViewPatientTime_SET);
            textViewPatientDescription  =(TextView)  itemView.findViewById(R.id.textViewPatientDescription_SET);
            ImageView  imageView = (ImageView)  itemView.findViewById(R.id.imageView12);

            Picasso.get()
                    .load(R.drawable.happydoctor)
                    .transform(new RoundedTransformation() )
                    .into(imageView);

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

        myPatient patients= patientList.get(position);

        holder.textViewPatientName.setText(patients.getName());
        holder.textViewPatientAge.setText(patients.getAge());
        holder.textViewPatientDate.setText(patients.getDate());
        holder.textViewPatientTime.setText(patients.getTime());
        holder.textViewPatientDescription.setText(patients.getDescription());

    }



    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
