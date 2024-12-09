package com.example.moneymate.Interface;

import com.example.moneymate.Model.ItemGoals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GoalsDetailListener {
    void onMessageLoading(boolean isLoading);
    void onGoalsDetailsLoaded(List<Map<String, Object>> goalsDetails);
    void onMessageFailure(String message);
    void onLoadDataItemSuccess(ArrayList<ItemGoals> itemGoalsArrayList);
    void onMessageSuccess(String message);
}
