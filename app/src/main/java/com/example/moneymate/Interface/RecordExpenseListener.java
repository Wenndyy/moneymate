package com.example.moneymate.Interface;

import com.example.moneymate.Model.Expense;
import java.util.ArrayList;
public interface RecordExpenseListener {
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onLoadDataExpenseSuccess(ArrayList<Expense> expenseList);
    void onDataExpenseSuccess(Expense expense);
}
