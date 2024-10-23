package com.example.moneymate.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.view.account.login.LoginActivity;

public class Splash2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putBoolean("isFirstTime", false)
                .apply();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash2Activity.this, LoginActivity.class));
                finish();
            }
        }, 5000);
    }
}