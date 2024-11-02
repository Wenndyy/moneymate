package com.example.moneymate.view.account.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;

import com.example.moneymate.service.Account;
import com.example.moneymate.view.account.register.RegisterActivity;
import com.example.moneymate.view.dashboard.DashboardActivity;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private TextView signuplink ;
    private Button loginbtn;

    private EditText etEmail, etPassword;


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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        etEmail = findViewById(R.id.emailTextEdit);
        etPassword = findViewById(R.id.passwordTextEdit);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (!(email.isEmpty() || password.isEmpty())){
                    Account account = new Account(0, email, "", "", password, new Date(), new Date(), LoginActivity.this);
                    account.login(email, password);
                }else{
                    Toast.makeText(LoginActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}