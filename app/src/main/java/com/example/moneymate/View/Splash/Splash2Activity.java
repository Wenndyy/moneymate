package com.example.moneymate.View.Splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.View.Account.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;

public class Splash2Activity extends AppCompatActivity {
    private MaterialButton materialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash2);

        getSharedPreferences("FirstInstalled", MODE_PRIVATE)
                .edit()
                .putBoolean("firstTime", true)
                .apply();
        materialButton = findViewById(R.id.btnStarted);
        materialButton.setOnClickListener(v -> {
            Intent intent = new Intent(Splash2Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}