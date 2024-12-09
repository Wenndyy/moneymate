package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.CategorySavingGoals;

public interface GoalsListener {
    void onGetGoalsSuccess(CategorySavingGoals category);
    void onMessageSuccess(String message);
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
}
