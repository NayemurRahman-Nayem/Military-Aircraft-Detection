package com.example.militaryaircraft;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile_Page extends AppCompatActivity {

    private TextView textViewWelcome , textViewFullName , textViewDob , textViewGender , textViewMobile , textViewEmail ;
    ProgressBar progressBar  ;
    String fullname , email , dob , gender , mobile ;
    ImageView imageView ;
    FirebaseAuth firebaseAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        getSupportActionBar().setTitle("Home") ;
        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDob = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);

        firebaseAuth = FirebaseAuth.getInstance() ;
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser() ;
        String userID = firebaseUser.getUid() ;
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users") ;
        referenceProfile.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        Toast.makeText(UserProfile_Page.this,"Successfully Read" , Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot  = task.getResult() ;
                        fullname = String.valueOf(dataSnapshot.child("fullName").getValue()) ;
                        email = String.valueOf(dataSnapshot.child("email").getValue()) ;
                        dob =  String.valueOf(dataSnapshot.child("doB").getValue()) ;
                        gender = String.valueOf(dataSnapshot.child("gender").getValue()) ;
                        mobile = String.valueOf(dataSnapshot.child("mobile").getValue()) ;
                        textViewDob.setText(dob);
                        textViewEmail.setText(email);
                        textViewGender.setText(gender);
                        textViewMobile.setText(mobile);
                        textViewWelcome.setText("Welcome " +fullname+" !");
                        textViewFullName.setText(fullname);
                    }
                }
                else {
                    Toast.makeText(UserProfile_Page.this,"Failed to Read !" , Toast.LENGTH_SHORT).show();
                }
            }
        }) ;

    }

}
