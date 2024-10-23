package com.example.moneymate.view.account.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.view.account.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    private TextView signinlink ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signinlink = findViewById(R.id.signinlink);
        signinlink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}