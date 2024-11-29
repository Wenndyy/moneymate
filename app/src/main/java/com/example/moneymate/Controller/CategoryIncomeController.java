package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.CategoryIncomeListener;
import com.example.moneymate.Model.CategoryIncome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryIncomeController extends CategoryIncome {

    private FirebaseFirestore db;
    private CategoryIncomeListener categoryIncomeListener;
    private List<CategoryIncome> categoryIncomeList = new ArrayList<>();


    public CategoryIncomeController(String idCategoryIncome, String incomeCategoryName, String categoryIncomeImage, Date created_at, Date updated_at) {
        super(idCategoryIncome, incomeCategoryName, categoryIncomeImage, created_at, updated_at);
        this.db = FirebaseFirestore.getInstance();
    }

    public void setCategoryIncomeListener(CategoryIncomeListener categoryIncomeListener) {
        this.categoryIncomeListener = categoryIncomeListener;
    }

    public void getCategoryIncome() {
        categoryIncomeListener.onMessageLoading(true);
        db.collection("CategoryIncome")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            categoryIncomeList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                CategoryIncome categoryIncome = document.toObject(CategoryIncome.class);
                                if (categoryIncome != null) {
                                    categoryIncome.setIdCategoryIncome(document.getId());
                                    categoryIncomeList.add(categoryIncome);
                                }
                            }
                            if (categoryIncomeListener != null) {
                                categoryIncomeListener.onCategoryIncomeSuccess(categoryIncomeList);
                            }
                        } else {
                            if (categoryIncomeListener != null) {
                                categoryIncomeListener.onMessageFailure("No categories found.");
                            }
                        }
                    } else {
                        if (categoryIncomeListener != null) {
                            categoryIncomeListener.onMessageFailure("Error fetching categories: " + task.getException().getMessage());
                        }
                    }
                    categoryIncomeListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {

                    if (categoryIncomeListener != null) {
                        categoryIncomeListener.onMessageFailure("Error getting categories");
                    }
                    categoryIncomeListener.onMessageLoading(false);
                });
    }

}
