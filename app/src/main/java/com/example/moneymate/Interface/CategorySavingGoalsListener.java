package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategorySavingGoals;

import java.util.List;

public interface CategorySavingGoalsListener {
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onCategorySavingGoalsSuccess(List<CategorySavingGoals> categoryList);
}
