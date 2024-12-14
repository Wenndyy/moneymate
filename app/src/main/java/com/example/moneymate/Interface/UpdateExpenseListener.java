package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;

public interface UpdateExpenseListener {
    void onGetExpenseSuccess(CategoryExpense category);
    void onMessageSuccess(String message);
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onDataExpenseSuccess(Expense expense);
}
