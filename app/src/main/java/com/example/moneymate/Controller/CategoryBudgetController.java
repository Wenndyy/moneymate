package com.example.moneymate.Controller;

import com.example.moneymate.Interface.CategoryBudgetListener;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.CategoryExpense;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryBudgetController {
    private FirebaseFirestore db;
    private CategoryBudgetListener categoryBudgetListener;
    private List<CategoryBudget> categoryBudgetList = new ArrayList<>();
    public CategoryBudgetController() {
        this.db = FirebaseFirestore.getInstance();
    }
    public void setCategoryBudgetListener(CategoryBudgetListener categoryBudgetListener) {
        this.categoryBudgetListener = categoryBudgetListener;
    }

    public void getCategoryExpense() {
        categoryBudgetListener.onMessageLoading(true);
        db.collection("CategoryExpense")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            categoryBudgetList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                CategoryBudget categoryBudget = document.toObject(CategoryBudget.class);
                                if (categoryBudget != null) {
                                    categoryBudget.setIdCategoryExpense(document.getId());
                                    categoryBudgetList.add(categoryBudget);
                                }
                            }
                            if (categoryBudgetListener != null) {
                                categoryBudgetListener.onCategoryExpenseSuccess(categoryBudgetList);
                            }
                        } else {
                            if (categoryBudgetListener != null) {
                                categoryBudgetListener.onMessageFailure("No categories found.");
                            }
                        }
                    } else {
                        if (categoryBudgetListener != null) {
                            categoryBudgetListener.onMessageFailure("Error fetching categories: " + task.getException().getMessage());
                        }
                    }
                    categoryBudgetListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {

                    if (categoryBudgetListener != null) {
                        categoryBudgetListener.onMessageFailure("Error getting categories");
                    }
                    categoryBudgetListener.onMessageLoading(false);
                });
    }
}
