package com.example.my_hospital_appointments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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
        ImageView imageViewPatientMessages;



        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewPatientMessages = (ImageView)  itemView.findViewById(R.id.imageViewPatientMessagesIcon);
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

                   // Intent myIntent=new Intent(v.getContext(),Patient_Chatting_Activity.class);

                    Intent myIntent=new Intent(v.getContext(),Doctor_Chatting_Activity.class);
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



            Uri uriImage;

            String email = patients.getEmailAddress();
            String Parts[]=email.split("@");
           String emailID=Parts[0];

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("PatientPictures");
        String imagePathPrefix = emailID + ".";
            // StorageReference imageRef = storageReference.child("");
            storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    int checker=0;
                    for (StorageReference item : listResult.getItems()) {
                        String itemName = item.getName();
                        if (itemName.startsWith(imagePathPrefix)) {
                            checker=1;
                            // Found a file with the correct prefix, load the image using Picasso
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Picasso.get()
                                            .load(uri)
                                            .transform(new RoundedTransformation() )
                                            .into(holder.imageViewPatientMessages);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors
                                    Picasso.get()
                                            .load(R.drawable.iconpatient)
                                            .transform(new RoundedTransformation() )
                                            .into(holder.imageViewPatientMessages);
                                }
                            });
                            break; // Break out of the loop since we've found the file we're looking for
                        }
                    }
                    if(checker==0)
                    {

                        //if the doctor hasn't set a profile picture
                        Picasso.get()
                                .load(R.drawable.iconpatient)
                                .transform(new RoundedTransformation() )
                                .into(holder.imageViewPatientMessages);
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
