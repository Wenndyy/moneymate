package com.example.moneymate.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moneymate.model.User;
import com.example.moneymate.view.account.login.LoginActivity;
import com.example.moneymate.view.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Account extends User {
    private FirebaseAuth mAuth;
    private Context context;
    private FirebaseFirestore db;
    public Account(int idUser, String email, String fullname, String noTelepon, String password, Date created_at, Date updated_at, Context context) {
        super(idUser, email, fullname, noTelepon, password, created_at, updated_at);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context,  new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                   if (user != null){
                       SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putBoolean("isLoggedIn", true);
                       editor.apply();

                       Intent intent = new Intent(context, DashboardActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);
                       ((Activity) context).finish();

                       Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show();
                   }
                } else {
                    Toast.makeText(context, "Login gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context,  new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();


                                Map<String, Object> userData = new HashMap<>();
                                userData.put("idUser", userId);
                                userData.put("email", getEmail());
                                userData.put("fullname", getFullname());
                                userData.put("noTelepon", getNoTelepon());
                                userData.put("created_at", new Date());
                                userData.put("updated_at", new Date());


                                db.collection("User").document(userId)
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            Intent intent = new Intent(context, DashboardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {

                                            Toast.makeText(context, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {

                            Toast.makeText(context, "Registrasi gagal!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void logout() {
        mAuth.signOut();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Toast.makeText(context, "Logout berhasil!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getUserData() {

    }


}
