package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.ExpenseListener;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ExpenseController extends Expense {
    private ExpenseListener expenseListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public ExpenseController(String idExpense, String idCategoryExpense, String idUser, double amount, Date dateOfExpense, Date created_at, Date updated_at) {
        super(idExpense, idCategoryExpense, idUser, amount, dateOfExpense, created_at, updated_at);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void getCategoryDataById(String categoryId) {
        db.collection("CategoryExpense").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategoryExpense category = document.toObject(CategoryExpense.class);
                            if (category != null) {
                                expenseListener.onGetExpenseSuccess(category);
                            }
                        }
                    } else {
                        expenseListener.onMessageFailure("Error getting category");
                        Log.w("ExpenseActivity", "Error getting category", task.getException());
                    }
                });
    }

    public void saveExpense(Expense expense, Date selectedDateObj) {
        expenseListener.onMessageLoading(true);
        db.collection("Expense").add(expense)
                .addOnSuccessListener(documentReference -> {
                    String expenseId = documentReference.getId();
                    expense.setIdExpense(expenseId);
                    db.collection("Expense").document(expenseId).set(expense)
                            .addOnSuccessListener(aVoid -> {
                                expenseListener.onMessageSuccess("Created expense suceess!");

                            })
                            .addOnFailureListener(e -> {
                                expenseListener.onMessageFailure("Created expense failed!");
                            });
                    expenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    expenseListener.onMessageFailure("Created Expense failed!");
                    expenseListener.onMessageLoading(false);
                });
    }

    public void loadExpenseDataByUser() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Expense")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Expense> expenseList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Expense expense = document.toObject(Expense.class);
                            expenseList.add(expense);
                        }


                        if (expenseList != null) {
                            expenseListener.onLoadDataExpenseSuccess(expenseList);
                        } else {

                            expenseListener.onMessageFailure("Empty Data");
                        }
                    } else {
                        expenseListener.onMessageFailure("Failed to get data");
                    }
                });
    }

    public void deleteExpense(Expense expense) {
        expenseListener.onMessageLoading(true);
        db.collection("Expense").document(expense.getIdExpense()).delete()
                .addOnSuccessListener(aVoid -> {
                    expenseListener.onDataExpenseSuccess(expense);
                    expenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    expenseListener.onMessageFailure("Failed to get data!");
                    expenseListener.onMessageLoading(false);
                });
    }

    public void loadExpenseByIdExpense(String expenseId) {

        db.collection("Expense").whereEqualTo("idExpense", expenseId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Expense expense = document.toObject(Expense.class);
                            expenseListener.onDataExpenseSuccess(expense);
                        }
                        expenseListener.onMessageLoading(false);
                    } else {
                        Log.e("ExpenseController", "Error getting expense", task.getException());
                        expenseListener.onMessageFailure("Error getting expense");
                        expenseListener.onMessageLoading(false);
                    }
                });
    }
    public void updateExpense(Expense expense) {
        expenseListener.onMessageLoading(true);
        db.collection("Expense").document(expense.getIdExpense())
                .set(expense)
                .addOnSuccessListener(aVoid -> {
                    expenseListener.onMessageSuccess("Updated Success!");
                    expenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("ExpenseController", "Error updating expense", e);
                    expenseListener.onMessageFailure("Error getting expense");
                    expenseListener.onMessageLoading(false);
                });
    }
    public void setExpenseListener(ExpenseListener expenseListener) {
        this.expenseListener = expenseListener;
    }
}
