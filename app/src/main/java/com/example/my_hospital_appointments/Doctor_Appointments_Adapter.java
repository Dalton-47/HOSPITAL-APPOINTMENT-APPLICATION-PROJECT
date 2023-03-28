package com.example.my_hospital_appointments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class Doctor_Appointments_Adapter extends RecyclerView.Adapter <Doctor_Appointments_Adapter.myDataViewHolder>{


     Context context;
    String docID,titleOriginal,reportContentOriginal,patientEmail,emailID;
     String patientName,description,appointmentDate,patientAge;
    ArrayList<myPatient> patientList=new ArrayList<>();
    ArrayList<PatientAppointmentData> appointmentsList=new ArrayList<>();
    RelativeLayout relativeLayout;
    Doctor_View_Appointments_NEW  docApp = new Doctor_View_Appointments_NEW();
    EditText editTextReportTitle,editTextReportContent;
     ProgressBar progressBar;
     TextView textViewPatientName;
    int positionChecker;


    public Doctor_Appointments_Adapter( Context context, TextView textViewPatientName, ArrayList<myPatient> patientAppointmentsList, RelativeLayout relativeLayout, String id, EditText editTextReportTitle, EditText editTextReportContent, String titleOriginal, String userID) {
   this.context=context;
        this.textViewPatientName=textViewPatientName;
        this.docID=id;
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

    private void showAlertDialogue(int position, String patientID) {
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Appointment");
        builder.setMessage("Are You Sure You want to cancel "+patientName+"'s Appointment?");

        // Open email Apps if User clicks/taps Continue button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                patientList.remove(position);
               // patientList.clear();
               // notifyDataSetChanged();
                notifyItemChanged(position);
                DatabaseReference assignedDocRef= FirebaseDatabase.getInstance().getReference().child("AssignedDoctor").child(patientID);

                DatabaseReference patientRef= FirebaseDatabase.getInstance().getReference("AssignedPatient").child(docID);

                Query query = patientRef.orderByChild("email").equalTo(patientID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            // delete the child node that contains the email address
                            childSnapshot.getRef().removeValue();
                            assignedDocRef.removeValue();

                            //we remove the cancelled appointment from patient's side
                            DatabaseReference patientAppRef= FirebaseDatabase.getInstance().getReference("Appointments").child(emailID);
                            patientAppRef.removeValue();

                            //class to hold all appointment data relevant to use in our database
                            Appointment_Data_Class appointmentDataObj=new Appointment_Data_Class(patientEmail,patientName,description,appointmentDate,patientAge, "N/A","N/A","N/A","N/A");

                            //we save the appointment to attended appointments
                            DatabaseReference attendedAppointmentsRef= FirebaseDatabase.getInstance().getReference("Appointments History").child("Cancelled");
                            String key= attendedAppointmentsRef.push().getKey();
                            assert key != null;
                            attendedAppointmentsRef.child(key).setValue(appointmentDataObj);

                            //we save the appointment to Doctor Appointments under the specific doctor
                            DatabaseReference myDocAttendedAppointments= FirebaseDatabase.getInstance().getReference("Doctor Appointments History").child("Cancelled").child(docID);
                            //we use the key from attendedAppointments
                            myDocAttendedAppointments.child(key).setValue(appointmentDataObj);

                            //we then save the appointment to Patient Appointments under the patients email
                            DatabaseReference myPatientAttendedAppointments= FirebaseDatabase.getInstance().getReference("Patient Appointments History").child("Cancelled").child(emailID);
                            //we use the key from attendedAppointments
                            myPatientAttendedAppointments.child(key).setValue(appointmentDataObj);

                            //

                            showUserAlertDialog(patientName);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // handle errors here
                    }
                });


            }//here
        });

        // Close the AlertDialog if User clicks/taps No button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }

    void setReportData( ProgressBar progressBar, EditText editTextReportTitle, EditText editTextReportContent, String titleOriginal, String reportContentOriginal, String docName, String reportDate)
    {

        this.progressBar =progressBar;
        this.editTextReportTitle =editTextReportTitle;
        this.editTextReportContent=editTextReportContent;
        this.titleOriginal=titleOriginal;
        this.reportContentOriginal =reportContentOriginal;
        String title= editTextReportTitle.getText().toString().trim();
        String reportContent=editTextReportContent.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);


        if(title.equals(titleOriginal))
        {
            progressBar.setVisibility(View.GONE);
            editTextReportTitle.setError("Cannot be blank");
        }
        else if(reportContent.equals(reportContentOriginal))
        {
            progressBar.setVisibility(View.GONE);
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
                        //class to hold all appointment data relevant to use in our database
                        Appointment_Data_Class appointmentDataObj=new Appointment_Data_Class(patientEmail,patientName,description,appointmentDate,patientAge, reportDate,docName,title,reportContent);


                        //we save the appointment to attended appointments
                        DatabaseReference attendedAppointmentsRef= FirebaseDatabase.getInstance().getReference("Appointments History").child("Attended");
                        String key= attendedAppointmentsRef.push().getKey();
                        assert key != null;
                        attendedAppointmentsRef.child(key).setValue(appointmentDataObj);

                        //we save the appointment to Doctor Appointments under the specific doctor
                        DatabaseReference myDocAttendedAppointments= FirebaseDatabase.getInstance().getReference("Doctor Appointments History").child("Attended").child(docID);
                       //we use the key from attendedAppointments
                        myDocAttendedAppointments.child(key).setValue(appointmentDataObj);

                        //we then save the appointment to Patient Appointments under the patients email
                        DatabaseReference myPatientAttendedAppointments= FirebaseDatabase.getInstance().getReference("Patient Appointments History").child("Attended").child(emailID);
                        //we use the key from attendedAppointments
                        myPatientAttendedAppointments.child(key).setValue(appointmentDataObj);

                        //we clear the attended appointment on the patient's side
                        DatabaseReference patientAppointmentRef= FirebaseDatabase.getInstance().getReference("Appointments").child(emailID);
                        patientAppointmentRef.removeValue();


                        //we send report to patient
                           progressBar.setVisibility(View.GONE);
                        patientRef.setValue(appointmentDataObj);
                       // Toast.makeText(Doctor_View_Appointments_NEW.this, "REPORT SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        relativeLayout.setVisibility(View.GONE);
                        editTextReportTitle.setText("");
                        editTextReportContent.setText("");


                        //we then remove this data from the doctors and patients appointment list because it has been attended
                       // patientList.remove(position);

                        DatabaseReference assignedDocRef= FirebaseDatabase.getInstance().getReference().child("AssignedDoctor").child(emailID);

                        DatabaseReference newPatientRef= FirebaseDatabase.getInstance().getReference("AssignedPatient").child(docID);

                        Query query = newPatientRef.orderByChild("email");

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    // delete the child node that contains the email address

                                    myPatient myAssignedPatient = childSnapshot.getValue(myPatient.class);
                                    assert myAssignedPatient != null;
                                     String patientEmail= myAssignedPatient.getEmail();
                                     String[] parts =patientEmail.split("@");
                                     String patientEmailID=parts[0];

                                    myDoctor myAssignedDoctor= childSnapshot.getValue(myDoctor.class);
                                    assert myAssignedDoctor != null;
                                    String docEmail=myAssignedDoctor.getEmail();
                                     String [] docParts=docEmail.split("@");
                                     String docEmailID=docParts[0];



                                    if(Objects.equals(patientEmailID, emailID))
                                    {
                                        childSnapshot.getRef().removeValue();
                                        assignedDocRef.removeValue();//remove appointment from Patient's list
                                    }


                                    if(Objects.equals(docEmailID, docID))
                                    {
                                        newPatientRef.removeValue();//remove appointment from Doctor's List
                                        patientList.remove(positionChecker); //refresh the recyclerView
                                        showReportSentAlert(patientName);
                                        notifyDataSetChanged();

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // handle errors here
                            }
                        });


                    }
                    else
                    {
                        editTextReportTitle.setError("Not Saved, Check Network!");


                        editTextReportContent.setError("Not Saved, Check Network");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

    }
    //
    private void showUserAlertDialog(String userName) {
// Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Appointment Cancelled");
        builder.setMessage("You have cancelled appointment with "+userName);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }
    //
    private void showReportSentAlert(String userName) {
// Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report Sent Successfully");
        builder.setMessage("You have sent a report to "+userName);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }


    //

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
                patientName=patients.getName();

                String email = patients.getEmail();
                String Parts[]=email.split("@");
                emailID=Parts[0];
                textViewPatientName.setText(patientName + "'s" + " Report");
                relativeLayout.bringToFront();
                relativeLayout.setVisibility(View.VISIBLE);
                 positionChecker=holder.getAdapterPosition();
                 }
        });

        holder.btnAppointmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = patients.getEmail();
                String Parts[]=email.split("@");
                emailID=Parts[0];

                showAlertDialogue(holder.getAdapterPosition(), emailID);
            }
        });



    }



    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
