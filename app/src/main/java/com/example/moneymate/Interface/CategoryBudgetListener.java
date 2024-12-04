package com.example.moneymate.Interface;

import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.CategoryExpense;

import java.util.List;

public interface CategoryBudgetListener {
    void onMessageFailure(String message);
    void onMessageLoading(boolean isLoading);
    void onCategoryExpenseSuccess(List<CategoryBudget> categoryList);
}
