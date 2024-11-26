package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.IncomeListener;
import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.Model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class IncomeController  extends  Income{
    private IncomeListener incomeListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public IncomeController(String idIncome, String idCategoryIncome, String idUser, double amount, Date dateOfIncome, Date created_at, Date updated_at) {
        super(idIncome, idCategoryIncome, idUser, amount, dateOfIncome, created_at, updated_at);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void getCategoryDataById(String categoryId) {
        db.collection("CategoryIncome").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategoryIncome category = document.toObject(CategoryIncome.class);
                            if (category != null) {
                                incomeListener.onGetIncomeSuccess(category);
                            }
                        }
                    } else {
                        incomeListener.onMessageFailure("Error getting category");
                        Log.w("IncomeActivity", "Error getting category", task.getException());
                    }
                });
    }

    public void saveIncome(Income income, Date selectedDateObj) {
       incomeListener.onMessageLoading(true);
        db.collection("Income").add(income)
                .addOnSuccessListener(documentReference -> {
                    String incomeId = documentReference.getId();
                    income.setIdIncome(incomeId);
                    db.collection("Income").document(incomeId).set(income)
                            .addOnSuccessListener(aVoid -> {
                                incomeListener.onMessageSuccess("Created Income suceess!");

                            })
                            .addOnFailureListener(e -> {
                                incomeListener.onMessageFailure("Created Income failed!");
                            });
                    incomeListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    incomeListener.onMessageFailure("Created Income failed!");
                    incomeListener.onMessageLoading(false);
                });
    }

    public void loadIncomeDataByUser() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Income")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Income> incomeList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Income income = document.toObject(Income.class);
                            incomeList.add(income);
                        }


                        if (incomeList != null) {
                            incomeListener.onLoadDataIncomeSuccess(incomeList);
                        } else {

                            incomeListener.onMessageFailure("Empty Data");
                        }
                    } else {
                        incomeListener.onMessageFailure("Failed to get data");
                    }
                });
    }

    public void deleteIncome(Income income) {
        incomeListener.onMessageLoading(true);
        db.collection("Income").document(income.getIdIncome()).delete()
                .addOnSuccessListener(aVoid -> {
                    incomeListener.onDataIncomeSuccess(income);
                    incomeListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    incomeListener.onMessageFailure("Failed to get data!");
                    incomeListener.onMessageLoading(false);
                });
    }

    public void loadIncomeByIdIncome(String incomeId) {

        db.collection("Income").whereEqualTo("idIncome", incomeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Income income = document.toObject(Income.class);
                            incomeListener.onDataIncomeSuccess(income);
                        }
                        incomeListener.onMessageLoading(false);
                    } else {
                        Log.e("IncomeController", "Error getting income", task.getException());
                        incomeListener.onMessageFailure("Error getting income");
                        incomeListener.onMessageLoading(false);
                    }
                });
    }
    public void updateIncome(Income income) {
        incomeListener.onMessageLoading(true);
        db.collection("Income").document(income.getIdIncome())
                .set(income)
                .addOnSuccessListener(aVoid -> {
                    incomeListener.onMessageSuccess("Updated Success!");
                    incomeListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("IncomeController", "Error updating income", e);
                    incomeListener.onMessageFailure("Error getting income");
                    incomeListener.onMessageLoading(false);
                });
    }
    public void setIncomeListener(IncomeListener incomeListener) {
        this.incomeListener = incomeListener;
    }
}
