package com.example.moneymate.Controller;

import android.content.Context;

import com.example.moneymate.Interface.MessageListener;
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
    public UserController(String idUser, String email, String fullname, String noTelepon, String password, Date created_at, Date updated_at) {
        super(idUser, email, fullname, noTelepon, password, created_at, updated_at);
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
                        messageListener.onMessageFailure("Login Failed!");
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
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        messageListener.onMessageFailure("Email is already registered!");
                    } else {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(registrationTask -> {
                            if (registrationTask.isSuccessful()) {
                                FirebaseUser  user = mAuth.getCurrentUser ();
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


                                            });
                                }
                            } else {
                                messageListener.onMessageFailure("Registrasi Gagal!");
                            }
                        });
                    }
                    messageListener.onMessageLoading(false);
                });
    }

    @Override
    public void logout() {
        messageListener.onMessageLoading(true);
        try {
            mAuth.signOut();
            messageListener.onMessageSuccess("Logout Success");
        } catch (Exception e) {
            messageListener.onMessageFailure("Logout Failed: " + e.getMessage());
        } finally {
            messageListener.onMessageLoading(false);
        }
    }



    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
