package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.Model.Income;

import java.util.ArrayList;

public interface RecordIncomeListener {
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onLoadDataIncomeSuccess(ArrayList<Income> incomeList);
    void onDataIncomeSuccess(Income income);
}
