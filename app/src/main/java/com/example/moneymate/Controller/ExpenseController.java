package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.ExpenseListener;
import com.example.moneymate.Model.Budget;
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

        // Simpan expense terlebih dahulu
        db.collection("Expense").add(expense)
                .addOnSuccessListener(documentReference -> {
                    String expenseId = documentReference.getId();
                    expense.setIdExpense(expenseId);

                    // Diperbarui di firestore
                    db.collection("Expense").document(expenseId).set(expense)
                            .addOnSuccessListener(aVoid -> {
                                expenseListener.onMessageSuccess("Created expense successfully!");

                                // Setelah expense berhasil disimpan, periksa apakah ada budget dengan kategori yang sama
                                updateBudgetWithExpense(expense);
                            })
                            .addOnFailureListener(e -> {
                                expenseListener.onMessageFailure("Failed to create expense");
                            });

                    expenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    expenseListener.onMessageFailure("Failed to create Expense");
                    expenseListener.onMessageLoading(false);
                });
    }


    private void updateBudgetWithExpense(Expense expense) {

        String categoryId = expense.getIdCategoryExpense();


        db.collection("Budget")
                .whereEqualTo("idCategory", categoryId)
                .whereEqualTo("idUser", expense.getIdUser())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {

                        for (DocumentSnapshot budgetDoc : querySnapshot.getDocuments()) {
                            Budget budget = budgetDoc.toObject(Budget.class);


                            if (budget != null && budget.getItemBudget() != null) {

                                if (!budget.getItemBudget().contains(expense.getIdExpense())) {
                                    budget.getItemBudget().add(expense.getIdExpense());
                                }


                                db.collection("Budget").document(budgetDoc.getId())
                                        .update("itemBudget", budget.getItemBudget())
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("ExpenseController", "ItemBudget updated for budget: " + budgetDoc.getId());
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ExpenseController", "Failed to update itemBudget", e);
                                        });
                            }
                        }
                    } else {
                        Log.d("ExpenseController", "No budget found for category: " + categoryId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ExpenseController", "Error getting budget", e);
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

        // Hapus expense dari koleksi Expense
        db.collection("Expense").document(expense.getIdExpense()).delete()
                .addOnSuccessListener(aVoid -> {
                    expenseListener.onDataExpenseSuccess(expense);

                    // Setelah expense dihapus, perbarui itemBudget pada budget terkait
                    removeExpenseFromBudget(expense);

                    expenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    expenseListener.onMessageFailure("Failed to delete expense!");
                    expenseListener.onMessageLoading(false);
                });
    }


    private void removeExpenseFromBudget(Expense expense) {

        String categoryId = expense.getIdCategoryExpense();


        db.collection("Budget")
                .whereEqualTo("idCategory", categoryId)
                .whereEqualTo("idUser", expense.getIdUser())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {

                        for (DocumentSnapshot budgetDoc : querySnapshot.getDocuments()) {
                            Budget budget = budgetDoc.toObject(Budget.class);


                            if (budget != null && budget.getItemBudget() != null) {

                                if (budget.getItemBudget().contains(expense.getIdExpense())) {
                                    budget.getItemBudget().remove(expense.getIdExpense());
                                }

                                db.collection("Budget").document(budgetDoc.getId())
                                        .update("itemBudget", budget.getItemBudget())
                                        .addOnSuccessListener(aVoid1 -> {
                                            Log.d("ExpenseController", "ItemBudget updated after deletion for budget: " + budgetDoc.getId());
                                        })
                                        .addOnFailureListener(e1 -> {
                                            Log.e("ExpenseController", "Failed to update itemBudget after deletion", e1);
                                        });
                            }
                        }
                    } else {
                        Log.d("ExpenseController", "No budget found for category: " + categoryId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ExpenseController", "Error getting budget", e);
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
