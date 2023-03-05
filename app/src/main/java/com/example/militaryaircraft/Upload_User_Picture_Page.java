package com.example.militaryaircraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Upload_User_Picture_Page extends AppCompatActivity {


    private ProgressBar progressBar ;
    private ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_user_picture_page);

        getSupportActionBar().setTitle("Upload Profile Picture");
    }


}