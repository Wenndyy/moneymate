package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.IncomeListener;
import com.example.moneymate.Interface.RecordIncomeListener;
import com.example.moneymate.Interface.UpdateIncomeListener;
import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.Model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;

public class IncomeController  extends  Income{
    private IncomeListener incomeListener;
    private UpdateIncomeListener updateIncomeListener;
    private RecordIncomeListener recordIncomeListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public IncomeController(String idIncome, String idCategoryIncome, String idUser, double amount, Date dateOfIncome, Date created_at, Date updated_at) {
        super(idIncome, idCategoryIncome, idUser, amount, dateOfIncome, created_at, updated_at);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void getCategoryDataById(String categoryId) {
        incomeListener.onMessageLoading(true);
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
                    incomeListener.onMessageLoading(false);
                }).addOnFailureListener(e -> {
                    incomeListener.onMessageLoading(false);
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
        recordIncomeListener.onMessageLoading(true);
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
                            recordIncomeListener.onLoadDataIncomeSuccess(incomeList);
                        } else {
                            recordIncomeListener.onMessageFailure("Empty Data");
                        }
                    } else {
                        recordIncomeListener.onMessageFailure("Failed to get data");
                    }
                    recordIncomeListener.onMessageLoading(false);
                }).addOnFailureListener(e -> {
                    recordIncomeListener.onMessageLoading(false);
                });
    }

    public void deleteIncome(Income income) {
        recordIncomeListener.onMessageLoading(true);
        db.collection("Income").document(income.getIdIncome()).delete()
                .addOnSuccessListener(aVoid -> {
                    recordIncomeListener.onDataIncomeSuccess(income);
                    recordIncomeListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    recordIncomeListener.onMessageFailure("Failed to get data!");
                    recordIncomeListener.onMessageLoading(false);
                });
    }

    public void loadIncomeByIdIncome(String incomeId) {
        updateIncomeListener.onMessageLoading(true);
        db.collection("Income").whereEqualTo("idIncome", incomeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Income income = document.toObject(Income.class);
                            updateIncomeListener.onDataIncomeSuccess(income);
                        }
                        updateIncomeListener.onMessageLoading(false);
                    } else {
                        Log.e("IncomeController", "Error getting income", task.getException());
                        updateIncomeListener.onMessageFailure("Error getting income");
                        updateIncomeListener.onMessageLoading(false);
                    }
                    updateIncomeListener.onMessageLoading(false);
                }).addOnFailureListener(e -> {
                    updateIncomeListener.onMessageLoading(false);
                });
    }
    public void updateIncome(Income income) {
        updateIncomeListener.onMessageLoading(true);
        db.collection("Income").document(income.getIdIncome())
                .set(income)
                .addOnSuccessListener(aVoid -> {
                    updateIncomeListener.onMessageSuccess("Updated Success!");
                    updateIncomeListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("IncomeController", "Error updating income", e);
                    updateIncomeListener.onMessageFailure("Error getting income");
                    updateIncomeListener.onMessageLoading(false);
                });
    }

    public void getCategoryDataByIdForUpdate(String categoryId) {
        updateIncomeListener.onMessageLoading(true);
        db.collection("CategoryIncome").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategoryIncome category = document.toObject(CategoryIncome.class);
                            if (category != null) {
                                updateIncomeListener.onGetIncomeSuccess(category);
                            }
                        }
                    } else {
                        updateIncomeListener.onMessageFailure("Error getting category");
                        Log.w("IncomeActivity", "Error getting category", task.getException());
                    }
                    updateIncomeListener.onMessageLoading(false);
                }).addOnFailureListener(e -> {
                    updateIncomeListener.onMessageLoading(false);
                });
    }
    public void setIncomeListener(IncomeListener incomeListener) {
        this.incomeListener = incomeListener;
    }

    public void setUpdateIncomeListener(UpdateIncomeListener updateIncomeListener) {
        this.updateIncomeListener = updateIncomeListener;
    }

    public void setRecordIncomeListener(RecordIncomeListener recordIncomeListener) {
        this.recordIncomeListener = recordIncomeListener;
    }
}
