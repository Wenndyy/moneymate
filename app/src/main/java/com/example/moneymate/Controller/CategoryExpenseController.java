package com.example.moneymate.Controller;

import com.example.moneymate.Interface.CategoryExpenseListener;
import com.example.moneymate.Model.CategoryExpense;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryExpenseController extends CategoryExpense{

    private FirebaseFirestore db;
    private CategoryExpenseListener categoryExpenseListener;
    private List<CategoryExpense> categoryExpenseList = new ArrayList<>();

    public CategoryExpenseController(String idCategoryExpense, String expenseCategoryName, String categoryExpenseImage, Date created_at, Date updated_at) {
        super(idCategoryExpense, expenseCategoryName, categoryExpenseImage, created_at, updated_at);
        this.db = FirebaseFirestore.getInstance();
    }

    public void setCategoryExpenseListener(CategoryExpenseListener categoryExpenseListener) {
        this.categoryExpenseListener = categoryExpenseListener;
    }

    public void getCategoryIncome() {
        categoryExpenseListener.onMessageLoading(true);
        db.collection("CategoryExpense")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            categoryExpenseList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                CategoryExpense categoryExpense = document.toObject(CategoryExpense.class);
                                if (categoryExpense != null) {
                                    categoryExpense.setIdCategoryExpense(document.getId());
                                    categoryExpenseList.add(categoryExpense);
                                }
                            }
                            if (categoryExpenseListener != null) {
                                categoryExpenseListener.onCategoryExpenseSuccess(categoryExpenseList);
                            }
                        } else {
                            if (categoryExpenseListener != null) {
                                categoryExpenseListener.onMessageFailure("No categories found.");
                            }
                        }
                    } else {
                        if (categoryExpenseListener != null) {
                            categoryExpenseListener.onMessageFailure("Error fetching categories: " + task.getException().getMessage());
                        }
                    }
                    categoryExpenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {

                    if (categoryExpenseListener != null) {
                        categoryExpenseListener.onMessageFailure("Error getting categories");
                    }
                    categoryExpenseListener.onMessageLoading(false);
                });
    }
}
