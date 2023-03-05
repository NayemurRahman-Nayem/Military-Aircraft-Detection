package com.example.militaryaircraft;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;


public class Home_Page extends AppCompatActivity {

    Button button , profile , Logout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
         button = findViewById(R.id.gotoMLPage) ;
         profile = findViewById(R.id.gotoProfile) ;
         Logout = findViewById(R.id.logout) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, MLPage.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Page.this,UserProfile_Page.class));
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home_Page.this,Login_Page.class));
            }
        });
    }
}