package com.example.moneymate.Controller;

import com.example.moneymate.Interface.DashboardListener;
import com.example.moneymate.Interface.ProfileListener;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.Model.Income;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DashboardController  {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DashboardListener dashboardListener;

    private ProfileListener profileListener;

    public DashboardController() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void setdashboardListener(DashboardListener dashboardListener) {
        this.dashboardListener = dashboardListener;
    }

    public void setProfileListener(ProfileListener profileListener) {
        this.profileListener = profileListener;
    }

    public void getDataUser() {
        dashboardListener.onMessageLoading(true);

        String idUser = mAuth.getCurrentUser().getUid();

        db.collection("User")
                .document(idUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Map<String, Object> userData = task.getResult().getData();
                            if (userData != null) {
                                dashboardListener.onLoadDataSuccess(userData);
                            }
                        }
                    }
                    dashboardListener.onMessageLoading(false);
                });
    }

    public void getDataUserProfile() {
        profileListener.onMessageLoading(true);

        String idUser = mAuth.getCurrentUser().getUid();

        db.collection("User")
                .document(idUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Map<String, Object> userData = task.getResult().getData();
                            if (userData != null) {
                                profileListener.onLoadDataSuccess(userData);
                            }
                        }
                    }
                    profileListener.onMessageLoading(false);
                });
    }

    public void getTotalIncome() {
        dashboardListener.onMessageLoading(true);

        String idUser = mAuth.getCurrentUser().getUid();

        db.collection("Income")
                .whereEqualTo("idUser", idUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double totalIncome = 0.0;
                        for (DocumentSnapshot document : task.getResult()) {
                            Object amountObj = document.get("amount");

                            if (amountObj instanceof Number) {
                                totalIncome += ((Number) amountObj).doubleValue();
                            }
                        }
                        dashboardListener.onLoadDataSuccess(Map.of("totalIncome", totalIncome));
                    }
                    dashboardListener.onMessageLoading(false);
                });
    }

    public void getTotalExpense() {
        dashboardListener.onMessageLoading(true);
        String idUser = mAuth.getCurrentUser().getUid();
        db.collection("Expense")
                .whereEqualTo("idUser", idUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        double totalExpense = 0.0;
                        for (DocumentSnapshot document : task.getResult()) {
                            Object amountObj = document.get("amount");

                            if (amountObj instanceof Number) {
                                totalExpense += ((Number) amountObj).doubleValue();
                            }
                        }
                        dashboardListener.onLoadDataSuccess(Map.of("totalExpense", totalExpense));

                    }
                    dashboardListener.onMessageLoading(false);
                });
    }

    public void loadLastIncomeData() {
        dashboardListener.onMessageLoading(true);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Income")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Income> incomeList = new ArrayList<>();
                        Income latestIncome = null;
                        Date latestDate = null;

                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Income income = document.toObject(Income.class);
                                incomeList.add(income);
                                Date createdAt = income.getDateOfIncome();

                                if (latestDate == null || (createdAt != null && createdAt.after(latestDate))) {
                                    latestDate = createdAt;
                                    latestIncome = income;
                                }
                            }

                            if (latestIncome != null) {
                                dashboardListener.onLoadLastIncomeSuccess(latestIncome, incomeList);
                            }
                        }
                    }
                    dashboardListener.onMessageLoading(false);
                });
    }

    public void loadLastExpenseData() {
        dashboardListener.onMessageLoading(true);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Expense")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Expense> expenseList = new ArrayList<>();
                        Expense latestExpense = null;
                        Date latestDate = null;

                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Expense expense = document.toObject(Expense.class);
                                expenseList.add(expense);
                                Date createdAt = expense.getDateOfExpense();

                                if (latestDate == null || (createdAt != null && createdAt.after(latestDate))) {
                                    latestDate = createdAt;
                                    latestExpense = expense;
                                }
                            }

                            if (latestExpense!= null) {
                                dashboardListener.onLoadLastExpenseSuccess(latestExpense, expenseList);
                            }
                        }
                    }
                    dashboardListener.onMessageLoading(false);
                });
    }


}
