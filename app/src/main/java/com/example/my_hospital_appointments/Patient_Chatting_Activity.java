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

public class Patient_Chatting_Activity extends AppCompatActivity {

    private ArrayList<String> messages_from_user=new ArrayList<>();
    ArrayList <Patient_Messages> myList=new ArrayList();
    ArrayList<String> data=new ArrayList<>();
    Button myButton;
    DatabaseReference userMessages;
    String messageKey;
    String usersMessage;
    long timestamp;
    String senderID;
    String doctorName;
    patient_messages_Chatting_Adapter myAdapter;
    int counter=0;
    String userEmailKey,docEmail;
    LinearLayoutManager layoutManager;
    RecyclerView myRecycleViewer;
    TextView doctorNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_chatting_activity);


        StorageReference storageReference = FirebaseStorage.getInstance().getReference("DoctorsPictures");

        ImageView myUserProfile=(ImageView)  this.findViewById(R.id.imageViewPatientChattingPage_icon);

        doctorNameTextView =(TextView)  this.findViewById(R.id.textViewDoctorNamePatientChatting_new);

        //getting the data passed from previous activity
         userEmailKey=getIntent().getStringExtra("emailKey");
         doctorName=getIntent().getStringExtra("doctorsName");
         docEmail = getIntent().getStringExtra("docEmail");


        Toast.makeText(Patient_Chatting_Activity.this,"user's key = "+userEmailKey+" Doc Email = "+docEmail,Toast.LENGTH_SHORT).show();
        FirebaseAuth myAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=myAuth.getCurrentUser();
        String userId= currentUser.getUid();

        doctorNameTextView.setText("Dr."+doctorName);

        DatabaseReference myDoctors= FirebaseDatabase.getInstance().getReference(userId);
         userMessages=FirebaseDatabase.getInstance().getReference("PATIENTMESSAGES").child(userEmailKey).child(doctorName);
        //now we generate unique ID's to store messages from the sender

         myRecycleViewer = findViewById(R.id.recyclerViewChat);

        readPatientMessages();
        myAdapter=new patient_messages_Chatting_Adapter(messages_from_user) ;
        myRecycleViewer.setAdapter(myAdapter);
      //  myRecycleViewer.smoothScrollToPosition(myAdapter.getItemCount()-1); //to set the recyclerView items to the last position


         layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);//to set the recyclerView items to the last position
        myRecycleViewer.setLayoutManager(layoutManager);


        EditText myEditText= (EditText) findViewById(R.id.editTextTypeMessageChat);
        String nullText=myEditText.getText().toString().trim(); //get text that is available before user clicks anything



        myButton=(Button) findViewById(R.id.buttonSendMessageChat);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     usersMessage=myEditText.getText().toString().trim();//get users message only after they press send
               // data.add(usersMessage);

                //messages_from_user.add(usersMessage);
               // data.add(usersMessage);
                //myAdapter.setData(data);

               timestamp=System.currentTimeMillis();//get the timestamp for when the user was sending the message
               senderID ="patient";

                myEditText.setText("");
                if(!Objects.equals(usersMessage, nullText))
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

        Uri uriImage;

        String parts[]=docEmail.split("@");
        String emailID=parts[0];


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
                                        .into(myUserProfile);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Picasso.get()
                                        .load(R.drawable.doctor)
                                        .transform(new RoundedTransformation() )
                                        .into(myUserProfile);
                            }
                        });
                        break; // Break out of the loop since we've found the file we're looking for
                    }
                }
                if(checker==0)
                {
                    //if the doctor hasn't set a profile picture
                    Picasso.get()
                            .load(R.drawable.doctor)
                            .transform(new RoundedTransformation() )
                            .into(myUserProfile);
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
        Patient_Messages userMessagesData= new Patient_Messages(senderID,myTime,usersMessage);
        userMessages.child(messageKey).setValue(userMessagesData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Patient_Chatting_Activity.this,"MESSAGE SAVED",Toast.LENGTH_SHORT).show();
                   // Patient_Chatting_Activity.this.notify();
                  readPatientMessages();
                    layoutManager.setStackFromEnd(true);
                    myRecycleViewer.scrollToPosition(myAdapter.getItemCount()-1);
                }
            }
        });
    }

    private void readPatientMessages()
    {
        //create a query to get the messages from database
 messages_from_user.clear();
        Query query= userMessages.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot childSnapshot:snapshot.getChildren())
                {
                    //check here no data is being read from db seems it is reading twice
                    Patient_Messages messages= childSnapshot.getValue(Patient_Messages.class);
                    assert messages != null;
                    if(Objects.equals(messages.getSender_id(), "patient"))
                    {

                        messages_from_user.add(messages.getMessage());

                    }
                    else
                    {

                        messages_from_user.add("Doc : "+messages.getMessage());

                    }


                }

                 myAdapter.setData(messages_from_user);
                layoutManager.setStackFromEnd(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Patient_Chatting_Activity.this,"ERROR RETRIEVING MESSAGES, CHECK NETWORK!",Toast.LENGTH_SHORT).show();

            }
        });
    }
}