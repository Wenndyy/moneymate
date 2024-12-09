package com.example.moneymate.Controller;

import com.example.moneymate.Interface.RecordListener;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.Model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryController {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecordListener recordListener;
    public HistoryController() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    public void loadLastIncomeData() {
        recordListener.onMessageLoading(true);
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
                                recordListener.onLoadLastIncomeSuccess(latestIncome, incomeList);
                            } else {
                                // Tidak ada data income yang ditemukan
                                recordListener.onMessageFailure("No income data found");
                            }
                        } else {
                            // Jika hasil query kosong
                            recordListener.onMessageFailure("No income data found");
                        }
                    } else {
                        // Jika terjadi kesalahan saat pengambilan data
                        recordListener.onMessageFailure("Failed to get data: " + task.getException().getMessage());
                    }

                    // Menyembunyikan loading indicator
                    recordListener.onMessageLoading(false);
                });
    }

    public void loadLastExpenseData() {
        recordListener.onMessageLoading(true);
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
                                recordListener.onLoadLastExpenseSuccess(latestExpense, expenseList);
                            } else {
                                // Tidak ada data income yang ditemukan
                                recordListener.onMessageFailure("No income data found");
                            }
                        } else {
                            // Jika hasil query kosong
                            recordListener.onMessageFailure("No income data found");
                        }
                    } else {
                        // Jika terjadi kesalahan saat pengambilan data
                        recordListener.onMessageFailure("Failed to get data: " + task.getException().getMessage());
                    }

                    // Menyembunyikan loading indicator
                    recordListener.onMessageLoading(false);
                });
    }
}
