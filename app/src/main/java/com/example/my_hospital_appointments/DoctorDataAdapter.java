package com.example.my_hospital_appointments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//declare a class called  recyclerViewer class that extends the RecyclerView.Adapter
public class DoctorDataAdapter extends RecyclerView.Adapter<DoctorDataAdapter.DataViewHolder> {

    private  ArrayList<Doctors> dataList=new ArrayList<>() ;
    private String emailKeyID;
    FirebaseAuth authProfile = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = authProfile.getCurrentUser();



    public void setData(ArrayList<Doctors> dataList) {
        this.dataList=dataList;
        notifyDataSetChanged();
    }

    protected class DataViewHolder extends RecyclerView.ViewHolder {
        DatabaseReference doctorsData;
        ImageView imageViewDocProfile;
        TextView doctorsName;
        TextView doctorsEmail;
        TextView doctorsPhone;
        TextView doctorsProfession;


        //a constructor
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);


            imageViewDocProfile =(ImageView)  itemView.findViewById(R.id.imageViewDoctorRecyclerView);
            doctorsName =(TextView)   itemView.findViewById(R.id.textViewRecyclerDoctorName);
            doctorsEmail =(TextView)  itemView.findViewById(R.id.textViewRecyclerDoctorEmail);
            doctorsPhone =(TextView)  itemView.findViewById(R.id.textViewRecyclerDoctorPhone) ;
            doctorsProfession =(TextView)  itemView.findViewById(R.id.textViewRecyclerDoctorProfession);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Doctors doctors= dataList.get(position);

                    String doctorName= doctors.getUserName();
                    String docEmail=doctors.getEmail();
                    Intent myIntent=new Intent(v.getContext(), Patient_Chatting_Activity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("emailKey",emailKeyID);
                    bundle.putString("doctorsName",doctorName);
                    bundle.putString("docEmail",docEmail);
                    myIntent.putExtras(bundle);

                    v.getContext().startActivity(myIntent);

                }
            });
        }
    }

    public DoctorDataAdapter(ArrayList<Doctors> dataList)
    {
         this.dataList=dataList;
    }

    public DoctorDataAdapter(ArrayList<Doctors> dataList, String emailKeyID) { //constructor to pass emailkey
        this.dataList = dataList;
        this.emailKeyID = emailKeyID;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_data_layout_view,parent, false) ;
       return new DataViewHolder (view);

    }


    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        Doctors doctors=dataList.get(position);
        holder.doctorsName.setText(doctors.getUserName());
        holder.doctorsEmail.setText(doctors.getEmail());
        holder.doctorsPhone.setText(doctors.getPhoneNumber());
        holder.doctorsProfession.setText(doctors.getDepartment());
//textViewAppointmentDescriptionNew
        String email= doctors.getEmail();
        Uri uriImage;
        String[] parts = email.split("@");
        String emailID = parts[0];

        String imagePathPrefix = emailID + ".";
       // StorageReference imageRef = storageReference.child("");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("DoctorsPictures");
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {
                    String itemName = item.getName();
                    if (itemName.startsWith(imagePathPrefix)) {
                        // Found a file with the correct prefix, load the image using Picasso
                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(holder.imageViewDocProfile);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                            }
                        });
                        break; // Break out of the loop since we've found the file we're looking for
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        }); //here



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }



}
