package com.example.moneymate.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.view.account.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirstTime = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstTime", true);
                boolean isLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .getBoolean("isLoggedIn", false);
                if(isFirstTime){
                    startActivity(new Intent(SplashActivity.this, Splash2Activity.class));
                }else {
                    if (!isLoggedIn) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {

                       /* startActivity(new Intent(SplashActivity.this, MainActivity.class));*/
                    }
                }
                finish();
            }
        },500);
    }
}