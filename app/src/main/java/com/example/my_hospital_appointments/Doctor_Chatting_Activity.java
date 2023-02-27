package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Doctor_Chatting_Activity extends AppCompatActivity {

    private ArrayList<String> messages_from_user=new ArrayList<>();
    ArrayList <Patient_Messages> myList=new ArrayList();
    ArrayList<String> data=new ArrayList<>();
    Button myButton;
    DatabaseReference doctorMessagesRef;
    String messageKey;
    String userMessages;
    long timestamp;
    String senderID;
    String doctorName;
    Doc_Chatting_Adapter docAdapter;
    int counter=0;
    String userEmailKey;
    LinearLayoutManager layoutManager;
    RecyclerView myRecycleViewer;
    ImageView imageView;
    TextView txtViewPatientName;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chatting);

       storageReference = FirebaseStorage.getInstance().getReference("PatientPictures");

        //getting the data passed from previous activity
        userEmailKey=getIntent().getStringExtra("emailKey");
        doctorName=getIntent().getStringExtra("doctorsName");


        txtViewPatientName = (TextView)  this.findViewById(R.id.textViewDocChatting_SET);
        //get Patient's name to be displayed
        getUserName();

        //we get the patient's profile picture
        imageView =(ImageView)  this.findViewById(R.id.imageViewDocChattingPage_SET);
        getProfilePicture();


        Toast.makeText(Doctor_Chatting_Activity.this,"user's key = "+userEmailKey+" Doc name = "+doctorName,Toast.LENGTH_SHORT).show();
        FirebaseAuth myAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=myAuth.getCurrentUser();
        String userId= currentUser.getUid();


        //DatabaseReference myDoctors= FirebaseDatabase.getInstance().getReference(userId);
        doctorMessagesRef =FirebaseDatabase.getInstance().getReference("PATIENTMESSAGES").child(userEmailKey).child(doctorName);
        //now we generate unique ID's to store messages from the sender


        myRecycleViewer = (RecyclerView)  this.findViewById(R.id.recyclerViewDoctorPage);

        readDoctorMessages();
        docAdapter =new Doc_Chatting_Adapter(messages_from_user) ;
        myRecycleViewer.setAdapter(docAdapter);
        //  myRecycleViewer.smoothScrollToPosition(myAdapter.getItemCount()-1); //to set the recyclerView items to the last position


        layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);//to set the recyclerView items to the last position
        myRecycleViewer.setLayoutManager(layoutManager);


        EditText myEditText= (EditText) findViewById(R.id.editTextTextDoctorChattingPage);
        String nullText=myEditText.getText().toString().trim(); //get text that is available before user clicks anything



        myButton=(Button) findViewById(R.id.buttonDoctorChattingPage);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMessages =myEditText.getText().toString().trim();//get users message only after they press send
                messages_from_user.clear();

                //messages_from_user.add(usersMessage);
                // data.add(usersMessage);
                //myAdapter.setData(data);

                timestamp=System.currentTimeMillis();//get the timestamp for when the user was sending the message
                senderID ="doctor";

                myEditText.setText("");
                if(!Objects.equals(userMessages, nullText))
                {
                    addMessage();
                }
                else
                {
                    myEditText.setError("MESSAGE CANNOT BE BLANK");
                }

                // readPatientMessages();
            }
        });





    }

    void getProfilePicture()
    {

          Uri uriImage;

        String emailID = userEmailKey;

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
                                        .into(imageView);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Picasso.get()
                                        .load(R.drawable.personwhite)
                                        .transform(new RoundedTransformation() )
                                        .into(imageView);
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
                            .into(imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Handle any errors
            }
        }); //here

    }

    private void addMessage()
    {
        messageKey=FirebaseDatabase.getInstance().getReference("PATIENTMESSAGES").child(userEmailKey).child(doctorName).push().getKey();


        String myTime=Long.toString(timestamp);
        Patient_Messages userMessagesData= new Patient_Messages(senderID,myTime, userMessages);
        doctorMessagesRef.child(messageKey).setValue(userMessagesData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Doctor_Chatting_Activity.this,"MESSAGE SAVED",Toast.LENGTH_SHORT).show();
                    // Patient_Chatting_Activity.this.notify();
                    readDoctorMessages();
                    layoutManager.setStackFromEnd(true);
                    myRecycleViewer.scrollToPosition(docAdapter.getItemCount()-1);
                }
            }
        });
    }

    private void readDoctorMessages()
    {
        //create a query to get the messages from database
        messages_from_user.clear();
       doctorMessagesRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot childSnapshot:snapshot.getChildren())
                {
                    //check here no data is being read from db seems it is reading twice
                    Patient_Messages messages= childSnapshot.getValue(Patient_Messages.class);
                    assert messages != null;
                    if(Objects.equals(messages.getSender_id(), "doctor"))
                    {

                        messages_from_user.add(messages.getMessage());

                    }
                    else
                    {

                        messages_from_user.add("Patient : "+messages.getMessage());

                    }


                }

                docAdapter.setData(messages_from_user);
                layoutManager.setStackFromEnd(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Doctor_Chatting_Activity.this,"ERROR RETRIEVING MESSAGES, CHECK NETWORK!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    void getUserName()
    {


        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("Patients").child(userEmailKey);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    DataSnapshot thisDataSnapshot=task.getResult();
                    txtViewPatientName.setText(String.valueOf(thisDataSnapshot.child("firstName").getValue())+" "+String.valueOf(thisDataSnapshot.child("secondName").getValue()));
                }
            }
        });
    }


}