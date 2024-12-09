package com.example.moneymate.Controller;

import com.example.moneymate.Interface.CategoryIncomeListener;
import com.example.moneymate.Interface.CategorySavingGoalsListener;
import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.Model.CategorySavingGoals;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategorySavingGoalsController extends CategorySavingGoals {
    private FirebaseFirestore db;
    private CategorySavingGoalsListener categorySavingGoalsListener;
    private List<CategorySavingGoals> categorySavingGoalsList = new ArrayList<>();

    public CategorySavingGoalsController(String idGoalsCategory, String goalsCategoryName, String categoryGoalsImage, Date created_at, Date updated_at) {
        super(idGoalsCategory, goalsCategoryName, categoryGoalsImage, created_at, updated_at);
        this.db = FirebaseFirestore.getInstance();
    }

    public void setcategorySavingGoalsListener(CategorySavingGoalsListener categorySavingGoalsListener) {
        this.categorySavingGoalsListener = categorySavingGoalsListener;
    }

    public void getCategorySavingGoals() {
        categorySavingGoalsListener.onMessageLoading(true);
        db.collection("CategorySavingGoals")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            categorySavingGoalsList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                CategorySavingGoals categorySavingGoals = document.toObject(CategorySavingGoals.class);
                                if (categorySavingGoals != null) {
                                    categorySavingGoals.setIdGoalsCategory(document.getId());
                                    categorySavingGoalsList.add(categorySavingGoals);
                                }
                            }
                            if (categorySavingGoalsListener != null) {
                                categorySavingGoalsListener.onCategorySavingGoalsSuccess(categorySavingGoalsList);
                            }
                        } else {
                            if (categorySavingGoalsListener != null) {
                                categorySavingGoalsListener.onMessageFailure("No categories found.");
                            }
                        }
                    } else {
                        if (categorySavingGoalsListener != null) {
                            categorySavingGoalsListener.onMessageFailure("Error fetching categories: " + task.getException().getMessage());
                        }
                    }
                    categorySavingGoalsListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {

                    if (categorySavingGoalsListener != null) {
                        categorySavingGoalsListener.onMessageFailure("Error getting categories");
                    }
                    categorySavingGoalsListener.onMessageLoading(false);
                });
    }
}
