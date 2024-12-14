package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.ExpenseListener;
import com.example.moneymate.Interface.RecordExpenseListener;
import com.example.moneymate.Interface.UpdateExpenseListener;
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
    private RecordExpenseListener recordExpenseListener;
    private UpdateExpenseListener updateExpenseListener;
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
        recordExpenseListener.onMessageLoading(true);
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
                            recordExpenseListener.onLoadDataExpenseSuccess(expenseList);
                        } else {

                            recordExpenseListener.onMessageFailure("Empty Data");
                        }
                    } else {
                        recordExpenseListener.onMessageFailure("Failed to get data");
                    }
                    recordExpenseListener.onMessageLoading(false);
                }).addOnFailureListener(e -> {
                    recordExpenseListener.onMessageLoading(false);
                });
    }

    public void deleteExpense(Expense expense) {
        recordExpenseListener.onMessageLoading(true);
        db.collection("Expense").document(expense.getIdExpense()).delete()
                .addOnSuccessListener(aVoid -> {
                    recordExpenseListener.onDataExpenseSuccess(expense);

                    removeExpenseFromBudget(expense);

                    recordExpenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    recordExpenseListener.onMessageFailure("Failed to delete expense!");
                    recordExpenseListener.onMessageLoading(false);
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
        updateExpenseListener.onMessageLoading(true);
        db.collection("Expense").whereEqualTo("idExpense", expenseId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Expense expense = document.toObject(Expense.class);
                            updateExpenseListener.onDataExpenseSuccess(expense);
                        }
                        updateExpenseListener.onMessageLoading(false);
                    } else {
                        Log.e("ExpenseController", "Error getting expense", task.getException());
                        updateExpenseListener.onMessageFailure("Error getting expense");
                        updateExpenseListener.onMessageLoading(false);
                    }
                });
    }
    public void updateExpense(Expense expense) {
        updateExpenseListener.onMessageLoading(true);
        db.collection("Expense").document(expense.getIdExpense())
                .set(expense)
                .addOnSuccessListener(aVoid -> {
                    updateExpenseListener.onMessageSuccess("Updated Success!");
                    updateExpenseListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("ExpenseController", "Error updating expense", e);
                    updateExpenseListener.onMessageFailure("Error getting expense");
                    updateExpenseListener.onMessageLoading(false);
                });
    }

    public void getCategoryDataByIdForUpdate(String categoryId) {
        updateExpenseListener.onMessageLoading(true);
        db.collection("CategoryExpense").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategoryExpense category = document.toObject(CategoryExpense.class);
                            if (category != null) {
                                updateExpenseListener.onGetExpenseSuccess(category);
                            }
                        }
                    } else {
                        updateExpenseListener.onMessageFailure("Error getting category");
                        Log.w("ExpenseActivity", "Error getting category", task.getException());
                    }
                    updateExpenseListener.onMessageLoading(false);
                }).addOnFailureListener(e -> {
                    updateExpenseListener.onMessageLoading(false);
                });
    }
    public void setExpenseListener(ExpenseListener expenseListener) {
        this.expenseListener = expenseListener;
    }

    public void setRecordExpenseListener(RecordExpenseListener recordExpenseListener) {
        this.recordExpenseListener = recordExpenseListener;
    }

    public void setUpdateExpenseListener(UpdateExpenseListener updateExpenseListener) {
        this.updateExpenseListener = updateExpenseListener;
    }
}
