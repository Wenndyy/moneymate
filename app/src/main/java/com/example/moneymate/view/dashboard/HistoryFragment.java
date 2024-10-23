package com.example.moneymate.view.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.moneymate.R;
import com.example.moneymate.view.budget.SetBudgetActivity;
import com.example.moneymate.view.expense.CategoryExpenseActivity;
import com.example.moneymate.view.expense.RecordExpenseActivity;
import com.example.moneymate.view.goals.SetGoalsActivity;
import com.example.moneymate.view.income.CategoryIncomeActivity;
import com.example.moneymate.view.income.RecordIncomeActivity;

public class HistoryFragment extends Fragment {


    private LinearLayout showMoreIncome,showMoreExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showMoreIncome = view.findViewById(R.id.show_more_income);
        showMoreExpense= view.findViewById(R.id.show_more_expense);


        showMoreIncome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordIncomeActivity.class);
            startActivity(intent);
        });
        showMoreExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordExpenseActivity.class);
            startActivity(intent);
        });
    }
}