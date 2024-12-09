package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.CategorySavingGoals;

public interface ItemDepositListener {
    void onGetExpenseSuccess(CategorySavingGoals category);
    void onMessageSuccess(String message);
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
}
