package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.Model.Income;

public interface UpdateIncomeListener {
    void onGetIncomeSuccess(CategoryIncome category);
    void onMessageSuccess(String message);
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onDataIncomeSuccess(Income income);
}
