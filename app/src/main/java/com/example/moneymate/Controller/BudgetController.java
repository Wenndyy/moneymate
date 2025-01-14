package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.BudgetDetailListener;
import com.example.moneymate.Interface.BudgetListener;
import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.Interface.SetBudgetListener;
import com.example.moneymate.Model.Budget;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetController extends Budget{
    private FirebaseFirestore db;
    private BudgetListener budgetListener;
    private BudgetDetailListener budgetDetailListener;
    private MessageListener messageListener;
    private SetBudgetListener setBudgetListener;




    public BudgetController() {
        super();
        db = FirebaseFirestore.getInstance();
    }

    public void setBudgetListener(BudgetListener budgetListener) {
        this.budgetListener = budgetListener;
    }


    public void getCategoryDataById(String categoryId) {
        db.collection("CategoryExpense").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategoryBudget category = document.toObject(CategoryBudget.class);
                            if (category != null) {
                                budgetListener.onGetExpenseSuccess(category);
                            }
                        }
                    } else {
                        budgetListener.onMessageFailure("Error getting category");
                        Log.w("ExpenseActivity", "Error getting category", task.getException());
                    }
                });
    }
    public void saveBudget(Budget budget) {
        budgetListener.onMessageLoading(true);

        db.collection("Budget")
                .whereEqualTo("idCategory", budget.getIdCategory())
                .whereEqualTo("idUser", budget.getIdUser())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        budgetListener.onMessageFailure("Budget with this category already exists.");
                        budgetListener.onMessageLoading(false);
                    } else {
                        budget.setCreated_at(new Date());
                        budget.setUpdated_at(new Date());

                        db.collection("Budget")
                                .add(budget)
                                .addOnSuccessListener(documentReference -> {
                                    budget.setIdBudget(documentReference.getId());
                                    documentReference.update("idBudget", budget.getIdBudget())
                                            .addOnSuccessListener(aVoid -> {
                                                budgetListener.onMessageSuccess("Budget added successfully.");
                                                budgetListener.onMessageLoading(false);
                                            })
                                            .addOnFailureListener(e -> {
                                                budgetListener.onMessageFailure("Failed to update budget ID: " + e.getMessage());
                                                budgetListener.onMessageLoading(false);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    budgetListener.onMessageFailure("Failed to add budget: " + e.getMessage());
                                    budgetListener.onMessageLoading(false);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    budgetListener.onMessageFailure("Error validating budget: " + e.getMessage());
                    budgetListener.onMessageLoading(false);
                });
    }

    public void getBudgetDetails(String userId) {
        setBudgetListener.onMessageLoading(true);


        db.collection("Budget")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnSuccessListener(budgetQuery -> {
                    if (budgetQuery.isEmpty()) {
                        setBudgetListener.onMessageLoading(false);
                        return;
                    }

                    List<Map<String, Object>> budgetDetailList = new ArrayList<>();
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                    for (DocumentSnapshot budgetDoc : budgetQuery.getDocuments()) {
                        Budget budget = budgetDoc.toObject(Budget.class);

                        Task<DocumentSnapshot> categoryTask = db.collection("CategoryExpense")
                                .document(budget.getIdCategory())
                                .get()
                                .addOnSuccessListener(categoryDoc -> {
                                    if (categoryDoc.exists()) {
                                        String categoryName = categoryDoc.getString("expenseCategoryName");
                                        String categoryImage = categoryDoc.getString("categoryExpenseImage");

                                        calculateItemBudgetTotal(budget, categoryName, categoryImage, budgetDetailList);
                                    }
                                });

                        tasks.add(categoryTask);
                    }

                    Tasks.whenAll(tasks)
                            .addOnSuccessListener(aVoid -> {
                                setBudgetListener.onMessageLoading(false);
                                setBudgetListener.onBudgetDetailsLoaded(budgetDetailList);
                            })
                            .addOnFailureListener(e -> {
                                setBudgetListener.onMessageLoading(false);
                            });
                })
                .addOnFailureListener(e -> {

                    setBudgetListener.onMessageLoading(false);
                });
    }
    private void calculateItemBudgetTotal(Budget budget, String categoryName, String categoryImage, List<Map<String, Object>> budgetDetailList) {
        if (budget.getItemBudget() == null || budget.getItemBudget().isEmpty()) {
            Map<String, Object> budgetDetail = new HashMap<>();
            budgetDetail.put("idBudget", budget.getIdBudget());
            budgetDetail.put("categoryId", budget.getIdCategory());
            budgetDetail.put("categoryName", categoryName);
            budgetDetail.put("categoryImage", categoryImage);
            budgetDetail.put("budgetAmount", budget.getAmount());
            budgetDetail.put("totalSpent", 0.0);
            budgetDetail.put("percentage", 0.0);

            budgetDetailList.add(budgetDetail);
            return;
        }


        db.collection("Expense")
                .whereIn("idExpense", budget.getItemBudget())
                .whereEqualTo("idCategoryExpense", budget.getIdCategory())
                .get()
                .addOnSuccessListener(expenseQuery -> {
                    Log.d("BudgetController", "Expense query size: " + expenseQuery.size());
                    double totalAmount = 0;

                    for (DocumentSnapshot expenseDoc : expenseQuery.getDocuments()) {
                        Log.d("BudgetController", "Expense ID: " + expenseDoc.getId());
                        Double amount = expenseDoc.getDouble("amount");
                        if (amount != null) {
                            totalAmount += amount;
                        }
                    }
                    double percentage = (budget.getAmount() > 0) ? (totalAmount / budget.getAmount()) * 100 : 0;

                    Map<String, Object> budgetDetail = new HashMap<>();
                    budgetDetail.put("idBudget", budget.getIdBudget());
                    budgetDetail.put("categoryId", budget.getIdCategory());
                    budgetDetail.put("categoryName", categoryName);
                    budgetDetail.put("categoryImage", categoryImage);
                    budgetDetail.put("budgetAmount", budget.getAmount());
                    budgetDetail.put("totalSpent", totalAmount);
                    budgetDetail.put("percentage", percentage);


                    budgetDetailList.add(budgetDetail);
                    setBudgetListener.onBudgetDetailsLoaded(budgetDetailList);

                });
    }

    private void calculateItemBudgetTotalbyId(Budget budget, String categoryName, String categoryImage, List<Map<String, Object>> budgetDetailList) {
        if (budget.getItemBudget() == null || budget.getItemBudget().isEmpty()) {
            Log.w("BudgetController", "No items in budget for ID: " + budget.getIdBudget());
            Map<String, Object> budgetDetail = new HashMap<>();
            budgetDetail.put("idBudget", budget.getIdBudget());
            budgetDetail.put("categoryId", budget.getIdCategory());
            budgetDetail.put("categoryName", categoryName);
            budgetDetail.put("categoryImage", categoryImage);
            budgetDetail.put("budgetAmount", budget.getAmount());
            budgetDetail.put("totalSpent", 0.0);
            budgetDetail.put("percentage", 0.0);

            budgetDetailList.add(budgetDetail);
            return;
        }


        db.collection("Expense")
                .whereIn("idExpense", budget.getItemBudget())
                .whereEqualTo("idCategoryExpense", budget.getIdCategory())
                .get()
                .addOnSuccessListener(expenseQuery -> {
                    Log.d("BudgetController", "Expense query size: " + expenseQuery.size());
                    double totalAmount = 0;

                    for (DocumentSnapshot expenseDoc : expenseQuery.getDocuments()) {
                        Log.d("BudgetController", "Expense ID: " + expenseDoc.getId());
                        Double amount = expenseDoc.getDouble("amount");
                        if (amount != null) {
                            totalAmount += amount;
                        } else {
                            Log.w("BudgetController", "Expense with null amount: " + expenseDoc.getId());
                        }
                    }

                    // Perhitungan persentase
                    double percentage = (budget.getAmount() > 0) ? (totalAmount / budget.getAmount()) * 100 : 0;

                    Map<String, Object> budgetDetail = new HashMap<>();
                    budgetDetail.put("idBudget", budget.getIdBudget());
                    budgetDetail.put("categoryId", budget.getIdCategory());
                    budgetDetail.put("categoryName", categoryName);
                    budgetDetail.put("categoryImage", categoryImage);
                    budgetDetail.put("budgetAmount", budget.getAmount());
                    budgetDetail.put("totalSpent", totalAmount);
                    budgetDetail.put("percentage", percentage);


                    budgetDetailList.add(budgetDetail);

                    budgetDetailListener.onBudgetDetailsLoaded(budgetDetailList);

                    Log.d("BudgetController", "Budget detail added: " + budgetDetail);
                })
                .addOnFailureListener(e -> {
                    Log.e("BudgetController", "Error calculating total amount", e);

                    budgetDetailListener.onMessageFailure("Error calculating budget totals: " + e.getMessage());
                });
    }

    public void getBudgetDetailsByCategory(String userId, String categoryId) {
        if (budgetDetailListener != null) {
            budgetDetailListener.onMessageLoading(true);
        } else {
            Log.e("BudgetController", "BudgetDetailListener is not set");
            return;
        }

        Log.d("GoalsController", "Fetching Goals for User ID: " + userId + " and Category ID: " + categoryId);

        db.collection("Budget")
                .whereEqualTo("idUser", userId)
                .whereEqualTo("idCategory", categoryId)
                .get()
                .addOnSuccessListener(budgetQuery -> {

                    Log.d("BudgetController", "Found " + budgetQuery.size() + " budgets for user: " + userId);

                    if (budgetQuery.isEmpty()) {
                        budgetDetailListener.onMessageFailure("No budgets found for this user");
                        budgetDetailListener.onMessageLoading(false);
                        return;
                    }

                    List<Map<String, Object>> budgetDetailList = new ArrayList<>();
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                    for (DocumentSnapshot budgetDoc : budgetQuery.getDocuments()) {
                        Budget budget = budgetDoc.toObject(Budget.class);


                        Log.d("BudgetController", "Budget ID: " + budget.getIdBudget());
                        Log.d("BudgetController", "Category ID: " + budget.getIdCategory());
                        Log.d("BudgetController", "Item Budget: " + budget.getItemBudget());

                        Task<DocumentSnapshot> categoryTask = db.collection("CategoryExpense")
                                .document(budget.getIdCategory())
                                .get()
                                .addOnSuccessListener(categoryDoc -> {
                                    if (categoryDoc.exists()) {
                                        String categoryName = categoryDoc.getString("expenseCategoryName");
                                        String categoryImage = categoryDoc.getString("categoryExpenseImage");

                                        Log.d("BudgetController", "Category Name: " + categoryName);

                                        calculateItemBudgetTotalbyId(budget, categoryName, categoryImage, budgetDetailList);
                                    } else {
                                        Log.w("BudgetController", "Category not found for ID: " + budget.getIdCategory());
                                    }
                                });

                        tasks.add(categoryTask);
                    }

                    Tasks.whenAll(tasks)
                            .addOnSuccessListener(aVoid -> {
                                budgetDetailListener.onMessageLoading(false);
                                budgetDetailListener.onBudgetDetailsLoaded(budgetDetailList);
                            })
                            .addOnFailureListener(e -> {
                                budgetDetailListener.onMessageFailure("Error processing budgets: " + e.getMessage());
                                budgetDetailListener.onMessageLoading(false);
                            });
                })
                .addOnFailureListener(e -> {
                    budgetDetailListener.onMessageFailure("Error fetching budgets: " + e.getMessage());
                    budgetDetailListener.onMessageLoading(false);
                });
    }

    public void loadExpenseDataByBudget(String idBudget) {
        if (idBudget == null || idBudget.isEmpty()) {
            budgetDetailListener.onMessageFailure("Budget ID cannot be null or empty.");
            return;
        }

        db.collection("Budget")
                .document(idBudget)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            ArrayList<String> itemBudget = (ArrayList<String>) document.get("itemBudget");
                            if (itemBudget != null && !itemBudget.isEmpty()) {
                                loadExpensesFromIds(itemBudget);
                            }
                        } else {
                            budgetDetailListener.onMessageFailure("Budget document does not exist.");
                        }
                    } else {
                        budgetDetailListener.onMessageFailure("Failed to fetch Budget data.");
                    }
                });
    }

    private void loadExpensesFromIds(List<String> expenseIds) {
        db.collection("Expense")
                .whereIn("idExpense", expenseIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Expense> expenseList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Expense expense = document.toObject(Expense.class);
                            expenseList.add(expense);
                        }

                        if (!expenseList.isEmpty()) {
                            budgetDetailListener.onLoadDataExpenseSuccess(expenseList);
                        } else {
                            budgetDetailListener.onMessageFailure("No expenses found for the provided IDs.");
                        }
                    } else {
                        budgetDetailListener.onMessageFailure("Failed to fetch Expense data.");
                    }
                });
    }

    public void updateGoals(String idBudget,  double updatedAmount, Date updateDate){

        messageListener.onMessageLoading(true);

        try {

            Map<String, Object> updatedGoal = new HashMap<>();
            updatedGoal.put("amount", updatedAmount);
            updatedGoal.put("updated_at", updateDate);

            db.collection("Budget")
                    .document(idBudget)
                    .update(updatedGoal)
                    .addOnSuccessListener(aVoid -> {
                        messageListener.onMessageSuccess("Budget Successfully edited");
                        messageListener.onMessageLoading(false);
                    })
                    .addOnFailureListener(e -> {
                        messageListener.onMessageSuccess("Budget failed to edit");
                        messageListener.onMessageLoading(false);
                    });

        }catch (Exception e){

            Log.e("BudgetController", "Error calculating total amount", e);
            messageListener.onMessageFailure("Error calculating Budget totals: " + e.getMessage());

        }
    }

    public void deleteBudget(String idBudget) {
        budgetDetailListener.onMessageLoading(true);

        db.collection("Budget")
                .document(idBudget)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    budgetDetailListener.onMessageSuccess("Budget deleted successfully.");
                    budgetDetailListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    budgetDetailListener.onMessageFailure("Failed to delete budget: " + e.getMessage());
                    budgetDetailListener.onMessageLoading(false);
                });
    }


    public void setBudgetDetailListener(BudgetDetailListener budgetDetailListener) {
        this.budgetDetailListener = budgetDetailListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setSetBudgetListener(SetBudgetListener setBudgetListener) {
        this.setBudgetListener = setBudgetListener;
    }
}
