package com.example.moneymate.Interface;

import com.example.moneymate.Model.Expense;
import com.example.moneymate.Model.Income;

import java.util.List;


public interface RecordListener {
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);

    void onLoadLastIncomeSuccess(Income lastIncome, List<Income> incomeList);
    void onLoadLastExpenseSuccess(Expense lastExpense, List<Expense> expenseList);
}
