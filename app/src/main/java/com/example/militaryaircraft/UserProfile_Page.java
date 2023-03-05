package com.example.militaryaircraft ;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull ;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission ;
import androidx.appcompat.app.AppCompatActivity ;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle ;
import android.view.View ;
import android.widget.ImageView ;
import android.widget.ProgressBar ;
import android.widget.TextView ;
import android.widget.Toast ;

import com.bumptech.glide.Glide;
import com.example.militaryaircraft.databinding.ActivityUserProfilePageBinding;
import com.google.android.gms.tasks.OnCompleteListener ;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task ;
import com.google.apphosting.datastore.testing.DatastoreTestTrace;
import com.google.firebase.auth.FirebaseAuth ;
import com.google.firebase.auth.FirebaseUser ;
import com.google.firebase.database.DataSnapshot ;
import com.google.firebase.database.DatabaseError ;
import com.google.firebase.database.DatabaseReference ;
import com.google.firebase.database.FirebaseDatabase ;
import com.google.firebase.database.ValueEventListener ;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfile_Page extends AppCompatActivity {

    private TextView uploadPicture , textViewFullName , textViewDob , textViewGender , textViewMobile , textViewEmail ;
    ProgressBar progressBar  ;

    ActivityUserProfilePageBinding binding ;
    String fullname , email , dob , gender , mobile ;
    ActivityResultLauncher<String> launcher;
    ImageView imageView ;
    FirebaseAuth firebaseAuth ;
    FirebaseDatabase firebaseDatabase ;
    FirebaseStorage firebaseStorage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        getSupportActionBar().setTitle("Home") ;
        uploadPicture = findViewById(R.id.uploadImage);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDob = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        //imageView = findViewById(R.id.image) ;
        firebaseAuth = FirebaseAuth.getInstance() ;
        String userID = firebaseAuth.getUid() ;

        firebaseDatabase = FirebaseDatabase.getInstance() ;
        firebaseStorage = FirebaseStorage.getInstance() ;


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userID) ;
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            String name , email , dateofbirth , phone , gender ;
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("fname") ;
                email = value.getString("email") ;
                dateofbirth = value.getString("dateOfBirth") ;
                phone = value.getString("phone") ;
                gender = value.getString("gender") ;

                textViewDob.setText(dateofbirth);
                textViewEmail.setText(email);
                textViewGender.setText(gender);
                textViewMobile.setText(phone);
                textViewFullName.setText(name);

                Toast.makeText(UserProfile_Page.this,dateofbirth,Toast.LENGTH_LONG).show();
            }
        }) ;





        // To Upload picture from user gallery

        binding = ActivityUserProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {

                    @Override
                    public void onActivityResult(Uri result) {
                        binding.image.setImageURI(result);

                        StorageReference reference = firebaseStorage.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //  Toast.makeText(ProfileActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri result) {
                                                        firebaseDatabase.getReference().child("users").child("Image").child(firebaseAuth.getCurrentUser().getUid())
                                                                .setValue(result.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(UserProfile_Page.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(UserProfile_Page.this, "not uploaded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UserProfile_Page.this, "failed uploaded", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserProfile_Page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });



        binding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });

        //Loading Image
        StorageReference dc = firebaseStorage.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
        dc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Glide
                        .with(UserProfile_Page.this)
                        .load(uri) // the uri you got from Firebase
                        .into(binding.image); //Your imageView variable
                //   Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






    }

}
