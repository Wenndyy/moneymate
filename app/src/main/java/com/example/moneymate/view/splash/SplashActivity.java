package com.example.moneymate.view.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;
import com.example.moneymate.view.account.login.LoginActivity;
import com.example.moneymate.view.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
                getSharedPreferences("MyPrefs",MODE_PRIVATE).edit().putBoolean("isLoggedIn",false).apply();
                if(isFirstTime){
                    startActivity(new Intent(SplashActivity.this, Splash2Activity.class));
                }else {
                    checkUserAuthentication();
                }
                finish();
            }
        },500);
    }
    private void checkUserAuthentication() {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            String savedUid = sharedPreferences.getString("uid", "");
            if (!savedUid.isEmpty() && savedUid.equals(user.getUid())) {
                String savedRole = sharedPreferences.getString("idUser", "");
                if (!savedRole.isEmpty()) {
                    Intent intent;
                    intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    String idUser = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("idUser", null);
                    if (idUser != null) {
                        intent.putExtra("idUser", idUser);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    checkUser(user.getUid());
                }
            } else {
                checkUser(user.getUid());
            }
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void checkUser(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String email = documentSnapshot.getString("email");
                    String fullname = documentSnapshot.getString("fullname");
                    String noTelepon  = documentSnapshot.getString("noTelepon");
                    if (email != null && fullname != null && noTelepon != null) {
                        saveUserInfo(uid, email, fullname, noTelepon);
                        Intent intent;
                        intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        String outletId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("idUser", null);
                        if (outletId != null) {
                            intent.putExtra("idUser", outletId);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        logoutAndRedirect("User information not found. Please log in again.");
                    }

                } else {
                    logoutAndRedirect("User information not found. Please log in again.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                logoutAndRedirect("Error: " + e.getMessage() + ". Please try again.");
            }
        });
    }

    private void saveUserInfo(String uid, String email, String fullName, String noTelepon) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);
        editor.putString("email", email);
        editor.putString("fullname", fullName);
        editor.putString("noTelepon", noTelepon);
        editor.apply();

    }



    private void logoutAndRedirect(String message) {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}