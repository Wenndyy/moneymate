package com.example.moneymate.view.account.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.service.Account;
import com.example.moneymate.view.account.login.LoginActivity;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private TextView signinlink ;
    private Button registerButton;
    private EditText etEmail,etPassword, etFullname,etNoTelepon, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signinlink = findViewById(R.id.signinlink);
        signinlink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton = findViewById(R.id.registerButton);
        etEmail = findViewById(R.id.emailTextEdit);
        etPassword= findViewById(R.id.passwordTextEdit);
        etFullname = findViewById(R.id.fullnameTextEdit);
        etNoTelepon = findViewById(R.id.noPhoneTextEdit);
        etConfirmPassword = findViewById(R.id.confirmPasswordTextEdit);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String fullname = etFullname.getText().toString().trim();
                String noTelepon = etNoTelepon.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (!(email.isEmpty() || password.isEmpty() || fullname.isEmpty() ||  noTelepon.isEmpty() || confirmPassword.isEmpty())){
                    Account account = new Account(0, email, fullname, noTelepon, password, new Date(), new Date(),RegisterActivity.this);
                    account.register(email, password);

                }else{
                    Toast.makeText(RegisterActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}