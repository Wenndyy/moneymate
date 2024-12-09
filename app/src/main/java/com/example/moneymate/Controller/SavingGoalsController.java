package com.example.moneymate.Controller;

import android.util.Log;

import com.example.moneymate.Interface.GoalsDetailListener;
import com.example.moneymate.Interface.GoalsListener;
import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.Interface.SetGoalsListener;
import com.example.moneymate.Model.CategorySavingGoals;
import com.example.moneymate.Model.ItemGoals;
import com.example.moneymate.Model.SavingGoals;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavingGoalsController extends SavingGoals {
    private FirebaseFirestore db;
    private GoalsListener goalsListener;
    private GoalsDetailListener goalsDetailListener;
    private SetGoalsListener setGoalsListener;
    private MessageListener messageListener;

    public SavingGoalsController() {
        super();
        db = FirebaseFirestore.getInstance();
    }


    public void setGoalsListener(GoalsListener goalsListener) {
        this.goalsListener = goalsListener;
    }

    public void setSetGoalsListener(SetGoalsListener setGoalsListener) {
        this.setGoalsListener = setGoalsListener;
    }
    public void setGoalsDetailListener(GoalsDetailListener goalsDetailListener) {
        this.goalsDetailListener = goalsDetailListener;
    }
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void getCategoryDataById(String categoryId) {
        db.collection("CategorySavingGoals").document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategorySavingGoals category = document.toObject(CategorySavingGoals.class);
                            if (category != null) {
                                goalsListener.onGetGoalsSuccess(category);
                            }
                        }
                    } else {
                        goalsListener.onMessageFailure("Error getting category");
                        Log.w("GoalsActivity", "Error getting category", task.getException());
                    }
                });
    }

    public void saveGoals(SavingGoals savingGoals) {
        goalsListener.onMessageLoading(true);

        // Validasi jika Goals untuk kategori sudah ada
        db.collection("SavingGoals")
                .whereEqualTo("idCategory", savingGoals.getIdCategory())
                .whereEqualTo("idUser", savingGoals.getIdUser())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        goalsListener.onMessageFailure("Saving goals with this category already exists.");
                        goalsListener.onMessageLoading(false);
                    } else {
                        savingGoals.setCreated_at(new Date());
                        savingGoals.setUpdated_at(new Date());

                        db.collection("SavingGoals")
                                .add(savingGoals)
                                .addOnSuccessListener(documentReference -> {
                                    savingGoals.setIdSavingGoals(documentReference.getId());
                                    documentReference.update("idSavingGoals", savingGoals.getIdSavingGoals())
                                            .addOnSuccessListener(aVoid -> {
                                                goalsListener.onMessageSuccess("Goals added successfully.");
                                                goalsListener.onMessageLoading(false);
                                            })
                                            .addOnFailureListener(e -> {
                                                goalsListener.onMessageFailure("Failed to update Goals ID: " + e.getMessage());
                                                goalsListener.onMessageLoading(false);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    goalsListener.onMessageFailure("Failed to add Goals: " + e.getMessage());
                                    goalsListener.onMessageLoading(false);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    goalsListener.onMessageFailure("Error validating Goals: " + e.getMessage());
                    goalsListener.onMessageLoading(false);
                });
    }
    public void updateGoals(String idGoals,  double updatedGoalAmount, Date updateDate){

        messageListener.onMessageLoading(true);

        try {

            Map<String, Object> updatedGoal = new HashMap<>();
            updatedGoal.put("amount", updatedGoalAmount);
            updatedGoal.put("updated_at", updateDate);

            db.collection("SavingGoals")
                    .document(idGoals)
                    .update(updatedGoal)
                    .addOnSuccessListener(aVoid -> {
                        messageListener.onMessageSuccess("Saving Goals Successfully edited");
                        messageListener.onMessageLoading(false);
                    })
                    .addOnFailureListener(e -> {
                        messageListener.onMessageSuccess("Saving Goals failed to edit");
                        messageListener.onMessageLoading(false);
                    });

        }catch (Exception e){

            Log.e("GoalsController", "Error calculating total amount", e);
            messageListener.onMessageFailure("Error calculating Goals totals: " + e.getMessage());

        }
    }



    // set goal
    public void getGoalsDetails(String userId) {
        setGoalsListener.onMessageLoading(true);

        db.collection("SavingGoals")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnSuccessListener(goalsQuery -> {
                    if (goalsQuery.isEmpty()) {
                        setGoalsListener.onMessageLoading(false);
                        return;
                    }

                    List<Map<String, Object>> goalsDetailList = new ArrayList<>();
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                    for (DocumentSnapshot goalsDoc : goalsQuery.getDocuments()) {
                        SavingGoals savingGoals = goalsDoc.toObject(SavingGoals.class);

                        // Log detail setiap Goals
                        Log.d("GoalsController", "Goals ID: " + savingGoals.getIdSavingGoals());
                        Log.d("GoalsController", "Category ID: " + savingGoals.getIdCategory());
                        Log.d("GoalsController", "Item Goals: " + savingGoals.getItemSavingGoals());

                        Task<DocumentSnapshot> categoryTask = db.collection("CategorySavingGoals")
                                .document(savingGoals.getIdCategory())
                                .get()
                                .addOnSuccessListener(categoryDoc -> {
                                    if (categoryDoc.exists()) {
                                        String categoryName = categoryDoc.getString("goalsCategoryName");
                                        String categoryImage = categoryDoc.getString("categoryGoalsImage");

                                        Log.d("GoalsController", "Category Name: " + categoryName);

                                        calculateItemGoalsTotal(savingGoals, categoryName, categoryImage, goalsDetailList);
                                    } else {
                                        Log.w("GoalsController", "Category not found for ID: " + savingGoals.getIdCategory());
                                    }
                                });

                        tasks.add(categoryTask);
                    }

                    Tasks.whenAll(tasks)
                            .addOnSuccessListener(aVoid -> {
                                setGoalsListener.onMessageLoading(false);
                                setGoalsListener.onGoalsDetailsLoaded(goalsDetailList);
                            })
                            .addOnFailureListener(e -> {
                                setGoalsListener.onMessageFailure("Error processing Goalss: " + e.getMessage());
                                setGoalsListener.onMessageLoading(false);
                            });
                })
                .addOnFailureListener(e -> {
                    setGoalsListener.onMessageFailure("Error fetching Goalss: " + e.getMessage());
                    setGoalsListener.onMessageLoading(false);
                });
    }
    private void calculateItemGoalsTotal(SavingGoals savingGoals , String categoryName, String categoryImage, List<Map<String, Object>> goalsDetailList) {
        if (savingGoals.getItemSavingGoals() == null || savingGoals.getItemSavingGoals().isEmpty()) {
            Log.w("GoalsController", "No items in Goals for ID: " + savingGoals.getIdSavingGoals());
            Map<String, Object> goalsDetail = new HashMap<>();
            goalsDetail.put("idGoals", savingGoals.getIdSavingGoals());
            goalsDetail.put("categoryId", savingGoals.getIdCategory());
            goalsDetail.put("categoryName", categoryName);
            goalsDetail.put("categoryImage", categoryImage);
            goalsDetail.put("budgetAmount", savingGoals.getAmount());
            goalsDetail.put("totalSpent", 0.0);
            goalsDetail.put("percentage", 0.0);

            goalsDetailList.add(goalsDetail);
            return;
        }

        // Menggunakan whereIn untuk mendapatkan semua dokumen dengan idExpense yang ada di itemGoals
        db.collection("ItemGoals")
                .whereIn("idItemGoals", savingGoals.getItemSavingGoals())
                .whereEqualTo("idCategoryGoals", savingGoals.getIdCategory())
                .get()
                .addOnSuccessListener(expenseQuery -> {
                    Log.d("GoalsController", "Expense query size: " + expenseQuery.size());
                    double totalAmount = 0;

                    for (DocumentSnapshot incomeDoc : expenseQuery.getDocuments()) {
                        Log.d("GoalsController", "Expense ID: " + incomeDoc.getId());
                        Double amount = incomeDoc.getDouble("amount");
                        if (amount != null) {
                            totalAmount += amount;
                        } else {
                            Log.w("GoalsController", "Expense with null amount: " + incomeDoc.getId());
                        }
                    }

                    // Perhitungan persentase
                    double percentage = (savingGoals.getAmount() > 0) ? (totalAmount / savingGoals.getAmount()) * 100 : 0;

                    Map<String, Object> goalsDetail = new HashMap<>();
                    goalsDetail.put("idGoals", savingGoals.getIdSavingGoals());
                    goalsDetail.put("categoryId", savingGoals.getIdCategory());
                    goalsDetail.put("categoryName", categoryName);
                    goalsDetail.put("categoryImage", categoryImage);
                    goalsDetail.put("budgetAmount", savingGoals.getAmount());
                    goalsDetail.put("totalSpent", totalAmount);
                    goalsDetail.put("percentage", percentage);

                    // Menambahkan detail ke daftar Goals
                    goalsDetailList.add(goalsDetail);
                    setGoalsListener.onGoalsDetailsLoaded(goalsDetailList);

                    Log.d("GoalsController", "Goals detail added: " + goalsDetail);
                })
                .addOnFailureListener(e -> {
                    Log.e("GoalsController", "Error calculating total amount", e);
                    setGoalsListener.onMessageFailure("Error calculating Goals totals: " + e.getMessage());
                });
    }


    //detail goal

    public void getGoalsDetailsbyId(String userId, String idCategory) {
        goalsDetailListener.onMessageLoading(true);

        db.collection("SavingGoals")
                .whereEqualTo("idUser", userId)
                .whereEqualTo("idCategory", idCategory)
                .get()
                .addOnSuccessListener(goalsQuery -> {
                    // Log jumlah Goals yang ditemukan
                    Log.d("GoalsController", "Found " + goalsQuery.size() + " Goalss for user: " + userId);

                    if (goalsQuery.isEmpty()) {
                        goalsDetailListener.onMessageFailure("No Goalss found for this user");
                        goalsDetailListener.onMessageLoading(false);
                        return;
                    }

                    List<Map<String, Object>> goalsDetailList = new ArrayList<>();
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                    for (DocumentSnapshot goalsDoc : goalsQuery.getDocuments()) {
                        SavingGoals savingGoals = goalsDoc.toObject(SavingGoals.class);

                        Task<DocumentSnapshot> categoryTask = db.collection("CategorySavingGoals")
                                .document(savingGoals.getIdCategory())
                                .get()
                                .addOnSuccessListener(categoryDoc -> {
                                    if (categoryDoc.exists()) {
                                        String categoryName = categoryDoc.getString("goalsCategoryName");
                                        String categoryImage = categoryDoc.getString("categoryGoalsImage");

                                        Log.d("GoalsController", "Category Name: " + categoryName);

                                        calculateItemGoalsTotalbyId(savingGoals, categoryName, categoryImage, goalsDetailList);
                                    } else {
                                        Log.w("GoalsController", "Category not found for ID: " + savingGoals.getIdCategory());
                                    }
                                });

                        tasks.add(categoryTask);
                    }

                    Tasks.whenAll(tasks)
                            .addOnSuccessListener(aVoid -> {
                                goalsDetailListener.onMessageLoading(false);
                                goalsDetailListener.onGoalsDetailsLoaded(goalsDetailList);
                            })
                            .addOnFailureListener(e -> {
                                goalsDetailListener.onMessageFailure("Error processing Goalss: " + e.getMessage());
                                goalsDetailListener.onMessageLoading(false);
                            });
                })
                .addOnFailureListener(e -> {
                    goalsDetailListener.onMessageFailure("Error fetching Goalss: " + e.getMessage());
                    goalsDetailListener.onMessageLoading(false);
                });
    }


    private void calculateItemGoalsTotalbyId(SavingGoals savingGoals , String categoryName, String categoryImage, List<Map<String, Object>> goalsDetailList) {
        if (savingGoals.getItemSavingGoals() == null || savingGoals.getItemSavingGoals().isEmpty()) {
            Log.w("GoalsController", "No items in Goals for ID: " + savingGoals.getIdSavingGoals());
            Map<String, Object> goalsDetail = new HashMap<>();
            goalsDetail.put("idGoals", savingGoals.getIdSavingGoals());
            goalsDetail.put("categoryId", savingGoals.getIdCategory());
            goalsDetail.put("categoryName", categoryName);
            goalsDetail.put("categoryImage", categoryImage);
            goalsDetail.put("budgetAmount", savingGoals.getAmount());
            goalsDetail.put("totalSpent", 0.0);
            goalsDetail.put("percentage", 0.0);

            goalsDetailList.add(goalsDetail);
            return;
        }

        // Menggunakan whereIn untuk mendapatkan semua dokumen dengan idExpense yang ada di itemGoals
        db.collection("ItemGoals")
                .whereIn("idItemGoals", savingGoals.getItemSavingGoals())
                .whereEqualTo("idCategoryGoals", savingGoals.getIdCategory())
                .get()
                .addOnSuccessListener(expenseQuery -> {
                    Log.d("GoalsController", "Expense query size: " + expenseQuery.size());
                    double totalAmount = 0;

                    for (DocumentSnapshot incomeDoc : expenseQuery.getDocuments()) {
                        Log.d("GoalsController", "Expense ID: " + incomeDoc.getId());
                        Double amount = incomeDoc.getDouble("amount");
                        if (amount != null) {
                            totalAmount += amount;
                        } else {
                            Log.w("GoalsController", "Expense with null amount: " + incomeDoc.getId());
                        }
                    }

                    // Perhitungan persentase
                    double percentage = (savingGoals.getAmount() > 0) ? (totalAmount / savingGoals.getAmount()) * 100 : 0;

                    Map<String, Object> goalsDetail = new HashMap<>();
                    goalsDetail.put("idGoals", savingGoals.getIdSavingGoals());
                    goalsDetail.put("categoryId", savingGoals.getIdCategory());
                    goalsDetail.put("categoryName", categoryName);
                    goalsDetail.put("categoryImage", categoryImage);
                    goalsDetail.put("budgetAmount", savingGoals.getAmount());
                    goalsDetail.put("totalSpent", totalAmount);
                    goalsDetail.put("percentage", percentage);

                    // Menambahkan detail ke daftar Goals
                    goalsDetailList.add(goalsDetail);
                    goalsDetailListener.onGoalsDetailsLoaded(goalsDetailList);

                    Log.d("GoalsController", "Goals detail added: " + goalsDetail);
                })
                .addOnFailureListener(e -> {
                    Log.e("GoalsController", "Error calculating total amount", e);
                    setGoalsListener.onMessageFailure("Error calculating Goals totals: " + e.getMessage());
                });
    }


    public void loadItemDataByGoals(String idGoals) {
        if (idGoals == null || idGoals.isEmpty()) {
            goalsDetailListener.onMessageFailure("Budget ID cannot be null or empty.");
            return;
        }

        db.collection("SavingGoals")
                .document(idGoals)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> itemGoals = (List<String>) document.get("itemSavingGoals");
                            if (itemGoals != null && !itemGoals.isEmpty()) {
                                loadExpensesFromIds(itemGoals);
                            }
                        } else {
                            goalsDetailListener.onMessageFailure("Budget document does not exist.");
                        }
                    } else {
                        goalsDetailListener.onMessageFailure("Failed to fetch Budget data.");
                    }
                });
    }

    private void loadExpensesFromIds(List<String> expenseIds) {
        db.collection("ItemGoals")
                .whereIn("idItemGoals", expenseIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<ItemGoals> itemGoalsArrayList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ItemGoals itemGoals = document.toObject(ItemGoals.class);
                            itemGoalsArrayList.add(itemGoals);
                        }

                        if (!itemGoalsArrayList.isEmpty()) {
                            goalsDetailListener.onLoadDataItemSuccess(itemGoalsArrayList);
                        } else {
                            goalsDetailListener.onMessageFailure("No expenses found for the provided IDs.");
                        }
                    } else {
                        goalsDetailListener.onMessageFailure("Failed to fetch Expense data.");
                    }
                });
    }


    public void deleteGoals(String idGoals) {
        if (idGoals == null || idGoals.isEmpty()) {
            goalsDetailListener.onMessageFailure("Goals ID cannot be null or empty.");
            return;
        }

        goalsDetailListener.onMessageLoading(true);

        // Langkah 1: Ambil data itemSavingGoals dari SavingGoals
        db.collection("SavingGoals")
                .document(idGoals)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> itemSavingGoals = (List<String>) documentSnapshot.get("itemSavingGoals");

                        if (itemSavingGoals != null && !itemSavingGoals.isEmpty()) {
                            // Langkah 2: Hapus dokumen terkait dari ItemGoals
                            deleteItems(itemSavingGoals, idGoals);
                        } else {
                            // Jika tidak ada item terkait, hapus langsung SavingGoals
                            deleteSavingGoals(idGoals);
                        }
                    } else {
                        goalsDetailListener.onMessageFailure("Saving Goals document does not exist.");
                        goalsDetailListener.onMessageLoading(false);
                    }
                })
                .addOnFailureListener(e -> {
                    goalsDetailListener.onMessageFailure("Failed to fetch Saving Goals: " + e.getMessage());
                    goalsDetailListener.onMessageLoading(false);
                });
    }

    private void deleteItems(List<String> itemSavingGoals, String idGoals) {
        // Menghapus semua dokumen di ItemGoals berdasarkan ID
        db.collection("ItemGoals")
                .whereIn("idItemGoals", itemSavingGoals)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Task<Void>> deleteTasks = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        deleteTasks.add(document.getReference().delete());
                    }


                    Tasks.whenAll(deleteTasks)
                            .addOnSuccessListener(aVoid -> {
                                deleteSavingGoals(idGoals);
                            })
                            .addOnFailureListener(e -> {
                                goalsDetailListener.onMessageFailure("Failed to delete related items: " + e.getMessage());
                                goalsDetailListener.onMessageLoading(false);
                            });
                })
                .addOnFailureListener(e -> {
                    goalsDetailListener.onMessageFailure("Failed to fetch related items: " + e.getMessage());
                    goalsDetailListener.onMessageLoading(false);
                });
    }

    private void deleteSavingGoals(String idGoals) {
        db.collection("SavingGoals")
                .document(idGoals)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    goalsDetailListener.onMessageSuccess("Budget and related items deleted successfully.");
                    goalsDetailListener.onMessageLoading(false);
                })
                .addOnFailureListener(e -> {
                    goalsDetailListener.onMessageFailure("Failed to delete budget: " + e.getMessage());
                    goalsDetailListener.onMessageLoading(false);
                });
    }


}
