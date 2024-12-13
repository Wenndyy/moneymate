package com.example.moneymate.View.Account.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.R;
import com.example.moneymate.Controller.UserController;
import com.example.moneymate.View.Account.Login.LoginActivity;
import com.example.moneymate.View.Dashboard.DashboardActivity;

import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class RegisterActivity extends AppCompatActivity implements MessageListener {
    private TextView signinlink ;
    private Button registerButton;
    private LinearLayout layoutRegister, layoutProgress;
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
        layoutRegister = findViewById(R.id.layoutRegister);
        layoutProgress = findViewById(R.id.layoutProgress);
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
                if (!(email.isEmpty() && password.isEmpty() && fullname.isEmpty() &&  noTelepon.isEmpty() && confirmPassword.isEmpty())){
                    if (!isValidEmail(email)) {
                        showMotionToast("Register Account", "Invalid Email Formatting!", MotionToastStyle.WARNING);
                        return;
                    }
                    if (!isValidPassword(password)) {
                        showMotionToast("Register Account", "Passwords must contain letters, numbers and symbols!", MotionToastStyle.WARNING);
                        return;
                    }
                    if (!isValidLengthPass(password)) {
                        showMotionToast("Register Account", "Password must contain at least 8 characters!", MotionToastStyle.WARNING);
                        return;
                    }

                    if (!isMatchingPassword(password, confirmPassword)) {
                        showMotionToast("Register Account", "Password doesn't match!", MotionToastStyle.WARNING);
                        return;
                    }
                    UserController account = new UserController("", email, fullname, noTelepon,  new Date(), new Date());
                    account.setMessageListener(RegisterActivity.this);
                    account.register(email, password);


                }else{
                    Toast.makeText(RegisterActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showMotionToast(String title, String message, MotionToastStyle style) {
        MotionToast.Companion.createColorToast(
                this,
                title,
                message,
                style,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_regular)
        );
    }

    private  boolean isMatchingPassword(String password, String confirm){
        if (password.equals(confirm)){
            return  true;
        }
        return  false;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidLengthPass(String password){
        if (password.length() < 8) {
            return false;
        }
        return true;
    }
    private boolean isValidPassword(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }


        return hasLetter && hasDigit && hasSpecialChar;
    }

    @Override
    public void onMessageSuccess(String message) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        showMotionToast("Register Account", message, MotionToastStyle.SUCCESS);
        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Register Account", message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutRegister.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutRegister.setVisibility(View.VISIBLE);
        }
    }
}