package com.example.moneymate.Interface;

import java.util.Map;

public interface ProfileListener {
    void onMessageLogoutSuccess(String message);
    void onMessageLogoutFailure(String message);

    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onLoadDataSuccess(Map<String, Object> data);
}
