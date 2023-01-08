package com.appdeb.myinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ParseUser currentUser = ParseUser.getCurrentUser();
                try{
                    if (currentUser.getBoolean("emailVerified")) {
                        Intent intent = new Intent(SplashActivity.this, SocialMediaActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}