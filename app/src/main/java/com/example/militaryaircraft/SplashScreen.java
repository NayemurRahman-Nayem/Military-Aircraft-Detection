package com.example.militaryaircraft;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviderGetKt;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;


public class SplashScreen extends AppCompatActivity {


    ProgressBar progressBar ;
    int progress = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressbar)  ;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork() ;
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
            }
        });
        thread.start();
    }


    public void  doWork() {
        for(progress=30;progress<=100;progress+=30) {
            try {
                Thread.sleep(1000) ;
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}