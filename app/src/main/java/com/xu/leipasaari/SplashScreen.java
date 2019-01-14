package com.xu.leipasaari;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Splash screen displaying logo of leipasaari ,leaves after 4 seconds
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();



        Thread timerThread = new Thread(){
            //Launch our MainActivity after 4s

            public void run(){
                try{

                    sleep(4000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent  = new Intent (SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }


}
