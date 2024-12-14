package com.example.moneymate.Interface;

import com.example.moneymate.Model.Budget;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BudgetListener {
    void onGetExpenseSuccess(CategoryBudget category);
    void onMessageSuccess(String message);
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);

}
