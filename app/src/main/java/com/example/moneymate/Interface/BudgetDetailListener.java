package com.example.moneymate.Interface;

import com.example.moneymate.Model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BudgetDetailListener {
    void onMessageLoading(boolean isLoading);
    void onBudgetDetailsLoaded(List<Map<String, Object>> budgetDetails);
    void onMessageFailure(String message);
    void onLoadDataExpenseSuccess(ArrayList<Expense> expenseList);
    void onMessageSuccess(String message);

}
