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
                            // Mendapatkan data sebagai Map
                            Map<String, Object> userData = task.getResult().getData();

                            if (userData != null) {

                                dashboardListener.onLoadDataSuccess(userData);

                            } else {
                                dashboardListener.onMessageFailure("User data is null.");
                            }
                        } else {
                            dashboardListener.onMessageFailure("User document not found.");
                        }
                    } else {
                        dashboardListener.onMessageFailure("Error retrieving user data: " + task.getException().getMessage());
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
                            // Mendapatkan data sebagai Map
                            Map<String, Object> userData = task.getResult().getData();

                            if (userData != null) {

                                profileListener.onLoadDataSuccess(userData);

                            } else {
                                profileListener.onMessageFailure("User data is null.");
                            }
                        } else {
                            profileListener.onMessageFailure("User document not found.");
                        }
                    } else {
                        profileListener.onMessageFailure("Error retrieving user data: " + task.getException().getMessage());
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

                        // Iterasi melalui dokumen
                        for (DocumentSnapshot document : task.getResult()) {
                            Object amountObj = document.get("amount");

                            if (amountObj instanceof Number) {
                                totalIncome += ((Number) amountObj).doubleValue();
                            } else {
                                dashboardListener.onMessageFailure("Invalid amount format in document: " + document.getId());
                            }
                        }

                        // Panggil listener untuk mengirimkan total income
                        dashboardListener.onLoadDataSuccess(Map.of("totalIncome", totalIncome));

                    } else {
                        dashboardListener.onMessageFailure("Failed to fetch income data: " + task.getException().getMessage());
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

                        // Iterasi melalui dokumen
                        for (DocumentSnapshot document : task.getResult()) {
                            Object amountObj = document.get("amount");

                            if (amountObj instanceof Number) {
                                totalExpense += ((Number) amountObj).doubleValue();
                            } else {
                                dashboardListener.onMessageFailure("Invalid amount format in document: " + document.getId());
                            }
                        }

                        // Panggil listener untuk mengirimkan total income
                        dashboardListener.onLoadDataSuccess(Map.of("totalExpense", totalExpense));

                    } else {
                        dashboardListener.onMessageFailure("Failed to fetch income data: " + task.getException().getMessage());
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

                        // Periksa apakah data ditemukan
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Income income = document.toObject(Income.class);
                                incomeList.add(income);
                                Date createdAt = income.getDateOfIncome();

                                // Mencari income dengan created_at terbaru
                                if (latestDate == null || (createdAt != null && createdAt.after(latestDate))) {
                                    latestDate = createdAt;
                                    latestIncome = income;
                                }
                            }

                            if (latestIncome != null) {
                                // Mengirim income terakhir dan semua income yang ditemukan ke listener
                                dashboardListener.onLoadLastIncomeSuccess(latestIncome, incomeList);
                            } else {
                                // Tidak ada data income yang ditemukan
                                dashboardListener.onMessageFailure("No income data found");
                            }
                        } else {
                            // Jika hasil query kosong
                            dashboardListener.onMessageFailure("No income data found");
                        }
                    } else {
                        // Jika terjadi kesalahan saat pengambilan data
                        dashboardListener.onMessageFailure("Failed to get data: " + task.getException().getMessage());
                    }

                    // Menyembunyikan loading indicator
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

                        // Periksa apakah data ditemukan
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Expense expense = document.toObject(Expense.class);
                                expenseList.add(expense);
                                Date createdAt = expense.getDateOfExpense();

                                // Mencari income dengan created_at terbaru
                                if (latestDate == null || (createdAt != null && createdAt.after(latestDate))) {
                                    latestDate = createdAt;
                                    latestExpense = expense;
                                }
                            }

                            if (latestExpense!= null) {
                                // Mengirim income terakhir dan semua income yang ditemukan ke listener
                                dashboardListener.onLoadLastExpenseSuccess(latestExpense, expenseList);
                            } else {
                                // Tidak ada data income yang ditemukan
                                dashboardListener.onMessageFailure("No income data found");
                            }
                        } else {
                            // Jika hasil query kosong
                            dashboardListener.onMessageFailure("No income data found");
                        }
                    } else {
                        // Jika terjadi kesalahan saat pengambilan data
                        dashboardListener.onMessageFailure("Failed to get data: " + task.getException().getMessage());
                    }

                    // Menyembunyikan loading indicator
                    dashboardListener.onMessageLoading(false);
                });
    }


}
