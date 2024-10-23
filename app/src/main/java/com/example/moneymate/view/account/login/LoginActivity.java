package com.example.moneymate.view.account.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.view.account.register.RegisterActivity;
import com.example.moneymate.view.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {
    private TextView signuplink ;
    private Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signuplink = findViewById(R.id.signuplink);
        loginbtn = findViewById(R.id.loginButton);
        signuplink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginbtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}