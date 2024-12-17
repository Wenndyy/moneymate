package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.ItemDepositListener;
import com.example.moneymate.Model.CategorySavingGoals;
import com.example.moneymate.Model.ItemGoals;
import com.example.moneymate.Model.SavingGoals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemSavingGoalsController extends ItemGoals {
    private ItemDepositListener itemDepositListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public ItemSavingGoalsController(String idItemGoals, String idCategoryGoals, String idUser, double amount, Date dateOfGoals, Date created_at, Date updated_at) {
        super(idItemGoals, idCategoryGoals, idUser, amount, dateOfGoals, created_at, updated_at);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    public void getCategoryDataById(String categoryId) {
        db.collection("CategorySavingGoals").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategorySavingGoals category = document.toObject(CategorySavingGoals.class);
                            if (category != null) {
                                itemDepositListener.onGetExpenseSuccess(category);
                            }
                        }
                    } else {
                        itemDepositListener.onMessageFailure("Error getting category");
                        Log.w("ExpenseActivity", "Error getting category", task.getException());
                    }
                });
    }
    public void saveDeposit(ItemGoals itemGoals, Date selectedDateObj, String idGoals) {
        itemDepositListener.onMessageLoading(true);

        // Simpan data ke ItemGoals terlebih dahulu
        db.collection("ItemGoals").add(itemGoals)
                .addOnSuccessListener(documentReference -> {
                    String itemId = documentReference.getId();
                    itemGoals.setIdItemGoals(itemId);

                    // Perbarui ItemGoals dengan ID yang baru dibuat
                    db.collection("ItemGoals").document(itemId).set(itemGoals)
                            .addOnSuccessListener(aVoid -> {
                                // Setelah berhasil menyimpan ItemGoals, update SavingGoals
                                db.collection("SavingGoals").document(idGoals).get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                SavingGoals savingGoals = documentSnapshot.toObject(SavingGoals.class);

                                                if (savingGoals != null) {
                                                    List<String> itemSavingGoals = savingGoals.getItemSavingGoals();
                                                    if (itemSavingGoals == null) {
                                                        itemSavingGoals = new ArrayList<>();
                                                    }
                                                    itemSavingGoals.add(itemId);

                                                    // Perbarui field itemSavingGoals di SavingGoals
                                                    db.collection("SavingGoals").document(idGoals)
                                                            .update("itemSavingGoals", itemSavingGoals)
                                                            .addOnSuccessListener(updateVoid -> {
                                                                itemDepositListener.onMessageSuccess("Deposit saved and SavingGoals updated successfully!");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                itemDepositListener.onMessageFailure("Failed to update SavingGoals");
                                                            });
                                                }
                                            } else {
                                                itemDepositListener.onMessageFailure("SavingGoals not found");
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            itemDepositListener.onMessageFailure("Failed to fetch SavingGoals");
                                        });
                            })
                            .addOnFailureListener(e -> {
                                itemDepositListener.onMessageFailure("Failed to save ItemGoals");
                            });

                    itemDepositListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    itemDepositListener.onMessageFailure("Failed to create ItemGoals");
                    itemDepositListener.onMessageLoading(false);
                });
    }

    public void setItemDepositListener(ItemDepositListener itemDepositListener) {
        this.itemDepositListener = itemDepositListener;
    }
}
