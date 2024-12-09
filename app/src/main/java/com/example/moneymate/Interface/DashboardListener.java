package com.example.moneymate.Interface;

import com.example.moneymate.Model.Expense;
import com.example.moneymate.Model.Income;
import com.example.moneymate.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DashboardListener {


    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onLoadDataSuccess(Map<String, Object> data);

    void onLoadLastIncomeSuccess(Income lastIncome, List<Income> incomeList);
    void onLoadLastExpenseSuccess(Expense lastExpense, List<Expense> expenseList);


}
