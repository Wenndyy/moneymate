package com.example.moneymate.Interface;

public interface MessageListener {
     void onMessageSuccess(String message);
     void onMessageFailure(String message);
     void onMessageLoading(boolean isLoading);
}
