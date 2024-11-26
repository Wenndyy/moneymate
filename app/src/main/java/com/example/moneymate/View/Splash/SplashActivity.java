package com.example.moneymate.View.Splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.View.Account.Login.LoginActivity;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fAuth = FirebaseAuth.getInstance();
        boolean isFirst = getSharedPreferences("FirstInstalled", Context.MODE_PRIVATE).getBoolean("firstTime", false);

        if (isFirst){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean isLoggedIn = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getBoolean("isLoggedIn", false);
                    if (isLoggedIn && fAuth.getCurrentUser() != null) {
                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000);
        }else{
            Intent intent = new Intent(SplashActivity.this, Splash2Activity.class);
            startActivity(intent);
            finish();
        }
    }


}
