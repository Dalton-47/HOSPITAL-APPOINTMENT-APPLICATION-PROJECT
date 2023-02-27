package com.example.my_hospital_appointments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Doc_Update_ProfilePicture_Activity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imageViewDocUploadPic;
    Button btnChoosePic,btnUploadPic;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    FirebaseAuth authProfile;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_update_profile_picture);

        storageReference = FirebaseStorage.getInstance().getReference("DoctorsPictures");

        imageViewDocUploadPic = (ImageView) this.findViewById(R.id.imageViewDoc_UPDATE);
        btnChoosePic = (Button) this.findViewById(R.id.buttonDocChoose_UPDATE);
        btnUploadPic = (Button) this.findViewById(R.id.buttonDocUpload_UPDATE);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBarDoc_UPDATE);


        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        Uri uri = firebaseUser.getPhotoUrl();

        // Set User's current DP in ImageView (if uploaded already). We will Picasso since imageViewer setImage
        // Regular URIs.
        Picasso.get()
                .load(uri)
                .transform(new RoundedSquareTransformation())
                .into(imageViewDocUploadPic);

        // Choosing image to upload
        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        // Upload Image
        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

        private void UploadPic() {

            if (uriImage != null){
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userId = currentUser.getUid();
                String email = currentUser.getEmail();
                String[] parts =email.split("@");
                String emailID = parts[0];


                // Save the image with uid of the currently logged user
                StorageReference fileReference = storageReference.child(emailID + '.' + getFileExtension(uriImage));

                // Upload image to storage
                fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;
                                firebaseUser = authProfile.getCurrentUser();

                                // Finally set the display image of the user after upload.
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(downloadUri).build();
                                firebaseUser.updateProfile(profileUpdates);
                            }
                        });
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Doc_Update_ProfilePicture_Activity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Doc_Update_ProfilePicture_Activity.this, Patient_User_Profile_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Doc_Update_ProfilePicture_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Doc_Update_ProfilePicture_Activity.this, "No file selected!", Toast.LENGTH_SHORT).show();
            }

        }

        // Obtain file extension of the Image.
        private String getFileExtension(Uri uri){
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            imageViewDocUploadPic.setImageURI(uriImage);
        }
    }

}