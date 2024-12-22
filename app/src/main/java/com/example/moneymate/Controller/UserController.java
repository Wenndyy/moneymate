package com.example.moneymate.Controller;

import android.content.Context;
import android.util.Log;

import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.Interface.ProfileListener;
import com.example.moneymate.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserController extends User {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MessageListener messageListener;
    private ProfileListener profileListener;
    public UserController(String idUser, String email, String fullname, String noTelepon, Date created_at, Date updated_at) {
        super(idUser, email, fullname, noTelepon, created_at, updated_at);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void login(String email, String password) {
        messageListener.onMessageLoading(true);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null){
                            messageListener.onMessageSuccess("Login Success!");
                        }
                    }else {
                        messageListener.onMessageFailure("Login Failed! Wrong email or password!");
                    }
                    messageListener.onMessageLoading(false);
                });
    }

    @Override
    public void register(String email, String password) {
        messageListener.onMessageLoading(true);
        db.collection("User")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Email sudah terdaftar
                            messageListener.onMessageFailure("Email is already registered!");
                            messageListener.onMessageLoading(false);
                        } else {
                            // Proses registrasi
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(registrationTask -> {
                                        if (registrationTask.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                String userId = mAuth.getUid();
                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("idUser", userId);
                                                userData.put("email", email);
                                                userData.put("fullname", getFullname());
                                                userData.put("noTelepon", getNoTelepon());
                                                userData.put("created_at", new Date());
                                                userData.put("updated_at", new Date());

                                                db.collection("User").document(userId)
                                                        .set(userData)
                                                        .addOnCompleteListener(firestoreTask -> {
                                                            if (firestoreTask.isSuccessful()) {
                                                                messageListener.onMessageSuccess("Registration Successful!");
                                                            } else {
                                                                messageListener.onMessageFailure("Failed to save user data");
                                                            }

                                                            messageListener.onMessageLoading(false);
                                                        });
                                            }
                                        } else {
                                            messageListener.onMessageFailure("Registrasi Failed!");
                                            messageListener.onMessageLoading(false);
                                        }
                                    });
                        }
                    } else {
                        Log.e("RegisterActivity", "Error checking email existence", task.getException());
                        messageListener.onMessageFailure("Failed to check email existence!");
                        messageListener.onMessageLoading(false);
                    }
                });
    }


    @Override
    public void logout() {
        profileListener.onMessageLoading(true);
        try {
            mAuth.signOut();
            profileListener.onMessageLogoutSuccess("Logout Success");
        } catch (Exception e) {
            profileListener.onMessageLogoutFailure("Logout Failed: " + e.getMessage());
        } finally {
            profileListener.onMessageLoading(false);
        }
    }



    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setProfileListener(ProfileListener profileListener) {
        this.profileListener = profileListener;
    }
}
