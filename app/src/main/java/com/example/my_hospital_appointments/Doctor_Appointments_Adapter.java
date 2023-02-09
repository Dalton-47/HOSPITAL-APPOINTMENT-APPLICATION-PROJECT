package com.example.my_hospital_appointments;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Doctor_Appointments_Adapter extends RecyclerView.Adapter <DoctorDataAdapter.DataViewHolder>{


    ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();
    @NonNull
    @Override
    public DoctorDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorDataAdapter.DataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
