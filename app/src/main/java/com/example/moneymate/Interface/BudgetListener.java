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
    void onLoadDataExpenseSuccess(ArrayList<Budget> budgetList);
    void onDataExpenseSuccess(Budget budget);
    void onBudgetDataReady(List<Expense> expenses, double totalBudgetAmount, double totalItemAmount);
    void onBudgetDetailsLoaded(List<Map<String, Object>> budgetDetails);
}
