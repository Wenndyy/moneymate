package com.example.moneymate.Interface;

import java.util.List;
import java.util.Map;

public interface SetBudgetListener {
    void onMessageLoading(boolean isLoading);
    void onBudgetDetailsLoaded(List<Map<String, Object>> budgetDetails);
}
