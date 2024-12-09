package com.example.moneymate.View.Goals;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.SavingGoalsController;
import com.example.moneymate.Interface.GoalsDetailListener;
import com.example.moneymate.Model.ItemGoals;
import com.example.moneymate.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class GoalsDetailActivity extends AppCompatActivity implements GoalsDetailListener {
    private ImageView editButton,deleteButton,categoryImg;
    private TextView categoryNameView, amount, percentage;
    private ProgressBar progress;
    private ImageView backArrow;

    private LinearLayout layoutProgress;
    private RelativeLayout layoutGoals;
    private LinearLayout recordLayout;
    private FirebaseFirestore db;
    private MaterialButton buttonDeposit;
    private SavingGoalsController savingGoalsController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_detail);

        categoryNameView = findViewById(R.id.toolbarTitle);
        progress = findViewById(R.id.progressGoals);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        backArrow = findViewById(R.id.backArrow);
        layoutGoals = findViewById(R.id.layoutGoals);
        layoutProgress = findViewById(R.id.layoutProgress);
        recordLayout = findViewById(R.id.recordLayout);
        amount = findViewById(R.id.amount);
        percentage = findViewById(R.id.percentage);
        categoryImg = findViewById(R.id.img_category);
        buttonDeposit = findViewById(R.id.buttonDeposit);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        String goalsId =  intent.getStringExtra("idGoals");
        String goalCategory = intent.getStringExtra("categoryName");
        String goalCategoryImage = intent.getStringExtra("categoryImage");
        double goalBudget = intent.getDoubleExtra("budgetAmount", 0);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalsDetailActivity.this, UpdateGoalsActivity.class);
                intent.putExtra("idGoals",goalsId);
                intent.putExtra("categoryImage",goalCategoryImage);
                intent.putExtra("categoryName", goalCategory);
                intent.putExtra("budgetAmount", goalBudget);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(goalsId);
            }
        });


        buttonDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalsDetailActivity.this, DepositActivity.class);
                intent.putExtra("idGoals",goalsId);
                intent.putExtra("idCategory",categoryId);
                intent.putExtra("categoryImage",goalCategoryImage);
                intent.putExtra("categoryName", goalCategory);
                intent.putExtra("budgetAmount", goalBudget);
                startActivity(intent);
            }
        });


        savingGoalsController = new SavingGoalsController();
        savingGoalsController.setGoalsDetailListener(this);
        savingGoalsController.getGoalsDetailsbyId(userId, categoryId);


        savingGoalsController.loadItemDataByGoals(goalsId);

    }

    private String formatCurrency(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }



    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutGoals.setVisibility(View.GONE);
            recordLayout.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutGoals.setVisibility(View.VISIBLE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGoalsDetailsLoaded(List<Map<String, Object>> goalsDetails) {
        if (!goalsDetails.isEmpty()) {
            Map<String, Object> goalsDetail = goalsDetails.get(0);

            String categoryName = (String) goalsDetail.get("categoryName");
            String categoryImage = (String) goalsDetail.get("categoryImage");
            double budgetAmount = (double) goalsDetail.get("budgetAmount");
            double totalSpent = (double) goalsDetail.get("totalSpent");
            double percentageBudget = (double) goalsDetail.get("percentage");

            if (categoryImage != null && !categoryImage.isEmpty()) {
                int iconResId = getResources().getIdentifier(categoryImage, "drawable",getPackageName());
                if (iconResId != 0) {
                    categoryImg.setImageResource(iconResId);
                } else {
                    categoryImg.setImageResource(R.drawable.ic_default);
                }
            } else {
                categoryImg.setImageResource(R.drawable.ic_default);
            }

            categoryNameView.setText(categoryName);
            amount.setText(formatCurrency(budgetAmount));
            progress.setMax((int) budgetAmount);
            progress.setProgress((int) totalSpent);
            percentage.setText(String.format("%.1f%%", percentageBudget));

        }
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Detail Budget", message,MotionToastStyle.ERROR);
    }

    @Override
    public void onLoadDataItemSuccess(ArrayList<ItemGoals> itemGoalsArrayList) {
        if (itemGoalsArrayList.isEmpty()) {
            layoutProgress.setVisibility(View.GONE);
            recordLayout.setVisibility(View.GONE);
        } else {
            // Mengelompokkan data berdasarkan tanggal
            Map<String, ArrayList<ItemGoals>> groupedData = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd", Locale.getDefault());

            for (ItemGoals itemGoals : itemGoalsArrayList) {
                Date dateOfExpense = itemGoals.getDateOfGoals();
                String formattedDate = sdf.format(dateOfExpense);

                if (!groupedData.containsKey(formattedDate)) {
                    groupedData.put(formattedDate, new ArrayList<>());
                }
                groupedData.get(formattedDate).add(itemGoals);
            }

            // Tampilkan data yang sudah dikelompokkan
            displayGroupedItemGoals(groupedData);

            layoutProgress.setVisibility(View.GONE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMessageSuccess(String message) {
        showMotionToast("Detail Saving Goals", message,MotionToastStyle.SUCCESS);
        Intent intent = new Intent(GoalsDetailActivity.this, SetGoalsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showMotionToast(String title, String message, MotionToastStyle style) {
        MotionToast.Companion.createColorToast(
                this,
                title,
                message,
                style,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_regular)
        );
    }

    private void displayGroupedItemGoals(Map<String, ArrayList<ItemGoals>> groupedData) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Map.Entry<String, ArrayList<ItemGoals>> entry : groupedData.entrySet()) {
            String date = entry.getKey();
            ArrayList<ItemGoals> itemGoals = entry.getValue();


            View dateView = inflater.inflate(R.layout.record_date_layout, null);
            TextView dateText = dateView.findViewById(R.id.dateText);
            dateText.setText(date);

            LinearLayout dateLayout = new LinearLayout(this);
            dateLayout.setOrientation(LinearLayout.VERTICAL);
            dateLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_item_record));
            dateLayout.addView(dateView);
            for (ItemGoals goals : itemGoals) {
                View expenseView = inflater.inflate(R.layout.record_expense_item, null);
                TextView expenseType = expenseView.findViewById(R.id.incomeType);
                TextView expenseAmount = expenseView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = expenseView.findViewById(R.id.categoryIcon);

                // Get category and update the expense view
                getExpenseCategory(goals.getIdCategoryGoals(), expenseType, expenseAmount, categoryIcon, goals);

                dateLayout.addView(expenseView);

                if (itemGoals.indexOf(goals) < itemGoals.size() - 1) {
                    View divider = new View(this);
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    divider.setLayoutParams(dividerParams);
                    divider.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
                    dateLayout.addView(divider);
                }
            }

            // Add the dateLayout to the recordLayout
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 0, 0);
            dateLayout.setLayoutParams(layoutParams);
            recordLayout.addView(dateLayout);
        }
    }


    private void getExpenseCategory(String categoryId, TextView expenseType, TextView expenseAmount, ImageView categoryIcon, ItemGoals itemGoals) {
        db.collection("CategorySavingGoals")
                .document(categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String categoryName = document.getString("goalsCategoryName");
                            String iconName = document.getString("categoryGoalsImage");

                            expenseType.setText(categoryName != null ? categoryName : "Unknown Category");

                            if (iconName != null && !iconName.isEmpty()) {
                                int iconResId = getResources().getIdentifier(iconName, "drawable", getPackageName());
                                if (iconResId != 0) {
                                    categoryIcon.setImageResource(iconResId);
                                } else {
                                    categoryIcon.setImageResource(R.drawable.ic_default); // Default icon
                                }
                            } else {
                                categoryIcon.setImageResource(R.drawable.ic_default);
                            }

                            expenseAmount.setText(formatRupiah(itemGoals.getAmount()));
                        } else {
                            expenseType.setText("Unknown Category");
                            categoryIcon.setImageResource(R.drawable.ic_default);
                        }
                    } else {
                        Log.e("ExpenseCategory", "Error fetching category", task.getException());
                        expenseType.setText("Error");
                        categoryIcon.setImageResource(R.drawable.ic_default);
                    }
                });
    }

    public String formatRupiah(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }

    private void showDeleteConfirmationDialog(String idGoals) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoalsDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_budget, null);
        builder.setView(dialogView);



        TextView cancelButton = dialogView.findViewById(R.id.btn_cancel);
        TextView okButton = dialogView.findViewById(R.id.btn_ok);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingGoalsController.deleteGoals(idGoals);
                dialog.dismiss();
            }
        });

        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 265, getResources().getDisplayMetrics());
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 109, getResources().getDisplayMetrics());
        dialog.getWindow().setAttributes(params);
    }

}