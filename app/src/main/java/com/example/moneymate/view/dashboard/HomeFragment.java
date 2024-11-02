package com.example.moneymate.view.dashboard;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moneymate.R;
import com.example.moneymate.model.User;
import com.example.moneymate.service.Account;
import com.example.moneymate.view.budget.SetBudgetActivity;
import com.example.moneymate.view.expense.CategoryExpenseActivity;
import com.example.moneymate.view.expense.RecordExpenseActivity;
import com.example.moneymate.view.goals.GoalsActivity;
import com.example.moneymate.view.goals.SetGoalsActivity;
import com.example.moneymate.view.income.CategoryIncomeActivity;
import com.example.moneymate.view.income.RecordIncomeActivity;


public class HomeFragment extends Fragment {

    private LinearLayout income, expense, budget, goals, showMoreIncome,showMoreExpense;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private TextView tvUsername;
    private String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        budget = view.findViewById(R.id.budget);
        goals = view.findViewById(R.id.goals);
        showMoreIncome = view.findViewById(R.id.show_more_income);
        showMoreExpense= view.findViewById(R.id.show_more_expense);

        tvUsername = view.findViewById(R.id.usernameLabelhome);
        progressBar = view.findViewById(R.id.loading);
        frameLayout = view.findViewById(R.id.layoutHome);




        income.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CategoryIncomeActivity.class);
            startActivity(intent);

        });
        expense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CategoryExpenseActivity.class);
            startActivity(intent);

        });
        budget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetBudgetActivity.class);
            startActivity(intent);

        });
        goals.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetGoalsActivity.class);
            startActivity(intent);

        });
        showMoreIncome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordIncomeActivity.class);
            startActivity(intent);
        });
        showMoreExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordExpenseActivity.class);
            startActivity(intent);
        });







    }


    public void displayUserData() {


        if (username != null){
            tvUsername.setText(username);
            progressBar.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }

    }




}