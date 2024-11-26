package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryIncome;

import java.util.List;

public interface CategoryIncomeListener {

    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onCategoryIncomeSuccess(List<CategoryIncome> categoryList);
}
