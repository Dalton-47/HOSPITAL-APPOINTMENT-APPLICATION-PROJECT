package com.example.my_hospital_appointments;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Doctor_Appointments_Adapter extends RecyclerView.Adapter <Doctor_Appointments_Adapter.myDataViewHolder>{



     String docID,titleOriginal,reportContentOriginal,patientEmail,emailID;
     String patientName,description,appointmentDate,patientAge;
    ArrayList<myPatient> patientList=new ArrayList<>();
    ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();
    RelativeLayout relativeLayout;
    Doctor_View_Appointments_NEW  docApp = new Doctor_View_Appointments_NEW();
    EditText editTextReportTitle,editTextReportContent;


    public Doctor_Appointments_Adapter(ArrayList<myPatient> patientAppointmentsList, RelativeLayout relativeLayout, String id, EditText editTextReportTitle, EditText editTextReportContent, String titleOriginal, String userID) {
   this.docID=userID;
      this.relativeLayout =relativeLayout;
        this.patientList=patientAppointmentsList;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<myPatient> patientAppointmentsList) {
        this.patientList=patientAppointmentsList;
        notifyDataSetChanged();
    }



    protected static class myDataViewHolder extends RecyclerView.ViewHolder{
     TextView textViewPatientName,textViewPatientAge,textViewPatientDate,textViewPatientTime,textViewPatientDescription;
       ImageView imageViewPatientProfile;
       Button btnAppointmentAttended, btnAppointmentCancel;
        public myDataViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAppointmentAttended = (Button)  itemView.findViewById(R.id.buttonMarkAttendanceAppointment);
            btnAppointmentCancel =(Button)  itemView.findViewById(R.id.buttonCancelAppointmentAttendance);
            imageViewPatientProfile = (ImageView)  itemView.findViewById(R.id.imageViewPatientAppointments_DOC);
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

    void setReportData(EditText editTextReportTitle,EditText editTextReportContent,String titleOriginal,String reportContentOriginal,String docName,String reportDate)
    {
        this.editTextReportTitle =editTextReportTitle;
        this.editTextReportContent=editTextReportContent;
        this.titleOriginal=titleOriginal;
        this.reportContentOriginal =reportContentOriginal;
        String title= editTextReportTitle.getText().toString().trim();
        String reportContent=editTextReportContent.getText().toString().trim();

        if(title.equals(titleOriginal))
        {
            editTextReportTitle.setError("Cannot be blank");
        }
        else if(reportContent.equals(reportContentOriginal))
        {
            editTextReportContent.setError("Cannot be blank");
        }
        else
        {

            DatabaseReference patientRef= FirebaseDatabase.getInstance().getReference("PatientReport").child(emailID);
            patientRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        PatientAppointmentClass patientAppointment=new PatientAppointmentClass(patientEmail,patientName,description,appointmentDate,patientAge, reportDate,docName,title,reportContent);

                        patientRef.setValue(patientAppointment);
                       // Toast.makeText(Doctor_View_Appointments_NEW.this, "REPORT SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        relativeLayout.setVisibility(View.GONE);

                    }
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(@NonNull myDataViewHolder holder, int position) {

        myPatient patients= patientList.get(position);

        holder.textViewPatientName.setText(patients.getName());
        holder.textViewPatientAge.setText(patients.getAge());
        holder.textViewPatientDate.setText(patients.getDate());
        holder.textViewPatientTime.setText(patients.getTime());
        holder.textViewPatientDescription.setText(patients.getDescription());

        patientEmail=patients.getEmail();
        patientName=patients.getName();
        description=patients.getDescription();
        appointmentDate=patients.getDate();
        patientAge=patients.getAge();


        Uri uriImage;

        String email = patients.getEmail();
        String Parts[]=email.split("@");
         emailID=Parts[0];

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
                                        .into(holder.imageViewPatientProfile);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Picasso.get()
                                        .load(R.drawable.iconpatient)
                                        .transform(new RoundedTransformation() )
                                        .into(holder.imageViewPatientProfile);
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
                            .into(holder.imageViewPatientProfile);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Handle any errors
            }
        }); //here

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
            }
        });
        holder.btnAppointmentAttended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  docApp.getDetails(email,patients.getName(),patients.getAge(),patients.getDate(),patients.getDescription());

                relativeLayout.bringToFront();
                relativeLayout.setVisibility(View.VISIBLE);
                 }
        });



    }



    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
