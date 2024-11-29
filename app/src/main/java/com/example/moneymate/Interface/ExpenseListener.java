package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;


import java.util.ArrayList;

public interface ExpenseListener {
    void onGetExpenseSuccess(CategoryExpense category);
    void onMessageSuccess(String message);
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onLoadDataExpenseSuccess(ArrayList<Expense> expenseList);
    void onDataExpenseSuccess(Expense expense);
}
