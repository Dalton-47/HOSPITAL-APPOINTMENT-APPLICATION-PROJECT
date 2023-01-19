package com.example.my_hospital_appointments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//declare a class called  recyclerViewer class that extends the RecyclerView.Adapter
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private ArrayList<Doctors> dataList=new ArrayList<>() ;

    public void setData(ArrayList<Doctors> dataList) {
        this.dataList=dataList;
        notifyDataSetChanged();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        DatabaseReference doctorsData;
        ImageView doctorsProfile;
        TextView doctorsName;
        TextView doctorsEmail;
        TextView doctorsPhone;
        TextView doctorsProfession;

        //a constructor
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorsProfile =(ImageView)  itemView.findViewById(R.id.imageViewDoctorRecyclerView);
            doctorsName =(TextView)   itemView.findViewById(R.id.textViewRecyclerDoctorName);
            doctorsEmail =(TextView)  itemView.findViewById(R.id.textViewRecyclerDoctorEmail);
            doctorsPhone =(TextView)  itemView.findViewById(R.id.textViewRecyclerDoctorPhone) ;
            doctorsProfession =(TextView)  itemView.findViewById(R.id.textViewRecyclerDoctorProfession);

        }
    }

    public DataAdapter(ArrayList<Doctors> dataList)
    {
         this.dataList=dataList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_data_layout_view,parent, false) ;

       // doctorsData = FirebaseDatabase.getInstance().getReference().child("Doctors");

               return new DataViewHolder (view);

    }


    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        Doctors doctors=dataList.get(position);
        holder.doctorsName.setText(doctors.getUserName());
        holder.doctorsEmail.setText(doctors.getEmail());
        holder.doctorsPhone.setText(doctors.getPhoneNumber());
        holder.doctorsProfession.setText(doctors.getDepartment());


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
