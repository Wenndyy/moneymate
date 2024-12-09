package com.example.moneymate.Interface;

import java.util.List;
import java.util.Map;

public interface SetGoalsListener {
    void onMessageLoading(boolean isLoading);
    void onGoalsDetailsLoaded(List<Map<String, Object>> budgetDetails);
    void onMessageFailure(String message);

}
