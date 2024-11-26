package com.example.moneymate.View.Account.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.R;

import com.example.moneymate.Controller.UserController;
import com.example.moneymate.View.Account.Register.RegisterActivity;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class LoginActivity extends AppCompatActivity implements MessageListener {
    private TextView signuplink ;
    private Button loginbtn;

    private EditText etEmail, etPassword;
    private LinearLayout layoutLogin, layoutProgress;
    private TextView forgotPasswordTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signuplink = findViewById(R.id.signuplink);
        loginbtn = findViewById(R.id.loginButton);
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutProgress = findViewById(R.id.layoutProgress);
        mAuth = FirebaseAuth.getInstance();
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
                if (!isValidEmail(email)) {
                    showMotionToast("Login Account", "Invalid Email Formatting!", MotionToastStyle.WARNING);
                    return;
                }
                if (!isValidPassword(password)) {
                    showMotionToast("Login Account", "Passwords must contain letters, numbers and symbols!", MotionToastStyle.WARNING);
                    return;
                }
                if (!isValidLengthPass(password)) {
                    showMotionToast("Login Account", "Password must contain at least 8 characters!", MotionToastStyle.WARNING);
                    return;
                }
                if (!(email.isEmpty() && password.isEmpty())){
                    UserController account = new UserController("", email, "", "", password, new Date(), new Date());
                    account.setMessageListener(LoginActivity.this);
                    account.login(email, password);

                }else{
                    showMotionToast("Login Account", "Data should not be empty!", MotionToastStyle.WARNING);
                }

            }
        });

        forgotPasswordTextView = findViewById(R.id.forgotPassword);

        forgotPasswordTextView.setOnClickListener(v -> {
            showForgotPasswordDialog();
        });

    }


    @Override
    public void onMessageSuccess(String message) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        showMotionToast("Login Account", message, MotionToastStyle.SUCCESS);
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Login Account", message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
            if (isLoading){
                layoutProgress.setVisibility(View.VISIBLE);
                layoutLogin.setVisibility(View.GONE);
            }else{
                layoutProgress.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);
            }
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


    private void showForgotPasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        EditText emailInput = dialogView.findViewById(R.id.emailInput);
        Button resetPasswordButton = dialogView.findViewById(R.id.btnReset);
        Button cancelButton = dialogView.findViewById(R.id.btnCancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Reset Password")
                .setMessage("Please enter your email address to reset your password.")
                .setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (isValidEmail(email)) {
                resetPassword(email);

            } else {
                showMotionToast("Reset Password", "Enter a valid email!", MotionToastStyle.ERROR);
            }
        });

        cancelButton.setOnClickListener(v -> {

            dialog.dismiss();
        });
    }

    public void resetPassword(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showMotionToast("Reset Account", "Please check your email!", MotionToastStyle.SUCCESS);

                    } else {
                        showMotionToast("Reset Account", "Reset Password Failed!", MotionToastStyle.ERROR);
                    }

                });
    }
}