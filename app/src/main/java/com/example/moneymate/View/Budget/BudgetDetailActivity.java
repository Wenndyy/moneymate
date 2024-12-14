package com.example.moneymate.View.Budget;

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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.BudgetController;
import com.example.moneymate.Controller.ExpenseController;
import com.example.moneymate.Interface.BudgetDetailListener;
import com.example.moneymate.Interface.BudgetListener;
import com.example.moneymate.Interface.SetGoalsListener;
import com.example.moneymate.Model.Budget;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.R;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.example.moneymate.View.Expense.RecordExpenseActivity;
import com.example.moneymate.View.Expense.UpdateExpenseActivity;
import com.example.moneymate.View.Goals.GoalsDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class BudgetDetailActivity extends AppCompatActivity implements BudgetDetailListener {
    private ImageView editButton,deleteButton,categoryImg;
    private TextView categoryNameView, amount, percentage;
    private ProgressBar progress;
    private ImageView backArrow;
    private BudgetController budgetController;
    private LinearLayout  layoutProgress;
    private RelativeLayout layoutBudget;
    private LinearLayout recordLayout;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_detail);


        categoryNameView = findViewById(R.id.toolbarTitle);
        progress = findViewById(R.id.progressBudget);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        backArrow = findViewById(R.id.backArrow);
        layoutBudget = findViewById(R.id.layoutBudget);
        layoutProgress = findViewById(R.id.layoutProgress);
        recordLayout = findViewById(R.id.recordLayout);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        String budgetId =  intent.getStringExtra("idBudget");
        String budgetCategory = intent.getStringExtra("categoryName");
        String budgetCategoryImage = intent.getStringExtra("categoryImage");
        double budget_amount = intent.getDoubleExtra("budgetAmount", 0);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetDetailActivity.this, UpdateBudgetActivity.class);
                intent.putExtra("idBudget",budgetId);
                intent.putExtra("categoryImage",budgetCategoryImage);
                intent.putExtra("categoryName", budgetCategory);
                intent.putExtra("budgetAmount", budget_amount);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(budgetId);
            }
        });

        budgetController = new BudgetController();
        budgetController.setBudgetDetailListener(this);
        budgetController.getBudgetDetailsByCategory(userId, categoryId);


        amount = findViewById(R.id.amount);
        percentage = findViewById(R.id.percentage);
        categoryImg = findViewById(R.id.img_category);


        budgetController.loadExpenseDataByBudget(budgetId);

    }



    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Detail Budget", message,MotionToastStyle.ERROR);
    }

    @Override
    public void onLoadDataExpenseSuccess(ArrayList<Expense> expenseList) {
        if (expenseList.isEmpty()) {
            layoutProgress.setVisibility(View.GONE);
            recordLayout.setVisibility(View.GONE);
        } else {
            Map<String, ArrayList<Expense>> groupedData = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd", Locale.getDefault());

            for (Expense expense : expenseList) {
                Date dateOfExpense = expense.getDateOfExpense();
                String formattedDate = sdf.format(dateOfExpense);

                if (!groupedData.containsKey(formattedDate)) {
                    groupedData.put(formattedDate, new ArrayList<>());
                }
                groupedData.get(formattedDate).add(expense);
            }

            displayGroupedExpense(groupedData);

            layoutProgress.setVisibility(View.GONE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMessageSuccess(String message) {
        showMotionToast("Detail Budget", message,MotionToastStyle.SUCCESS);
        recordLayout.removeAllViews();
        Intent intent = new Intent(BudgetDetailActivity.this, SetBudgetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
        finish();
    }


    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutBudget.setVisibility(View.GONE);
            recordLayout.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutBudget.setVisibility(View.VISIBLE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBudgetDetailsLoaded(List<Map<String, Object>> budgetDetails) {
        if (!budgetDetails.isEmpty()) {
            Map<String, Object> goalsDetail = budgetDetails.get(0);

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
            amount.setText(formatRupiah(budgetAmount));
            progress.setMax((int) budgetAmount);
            progress.setProgress((int) totalSpent);
            percentage.setText(String.format("%.1f%%", percentageBudget));

        }
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


    private void displayGroupedExpense(Map<String, ArrayList<Expense>> groupedData) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Map.Entry<String, ArrayList<Expense>> entry : groupedData.entrySet()) {
            String date = entry.getKey();
            ArrayList<Expense> expenseList = entry.getValue();


            View dateView = inflater.inflate(R.layout.record_date_layout, null);
            TextView dateText = dateView.findViewById(R.id.dateText);
            dateText.setText(date);

            LinearLayout dateLayout = new LinearLayout(this);
            dateLayout.setOrientation(LinearLayout.VERTICAL);
            dateLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_item_record));
            dateLayout.addView(dateView);
            for (Expense expense : expenseList) {
                View expenseView = inflater.inflate(R.layout.record_expense_item, null);
                TextView expenseType = expenseView.findViewById(R.id.incomeType);
                TextView expenseAmount = expenseView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = expenseView.findViewById(R.id.categoryIcon);

                getExpenseCategory(expense.getIdCategoryExpense(), expenseType, expenseAmount, categoryIcon, expense);

                dateLayout.addView(expenseView);

                if (expenseList.indexOf(expense) < expenseList.size() - 1) {
                    View divider = new View(this);
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    divider.setLayoutParams(dividerParams);
                    divider.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
                    dateLayout.addView(divider);
                }
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 0, 20);
            dateLayout.setLayoutParams(layoutParams);
            recordLayout.addView(dateLayout);
        }
    }


    private void getExpenseCategory(String categoryId, TextView expenseType, TextView expenseAmount, ImageView categoryIcon, Expense expense) {
        db.collection("CategoryExpense")
                .document(categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String categoryName = document.getString("expenseCategoryName");
                            String iconName = document.getString("categoryExpenseImage");

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

                            expenseAmount.setText(formatRupiah(expense.getAmount()));
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
        formatRupiah.setMaximumFractionDigits(0);
        formatRupiah.setMinimumFractionDigits(0);
        return formatRupiah.format(amount);
    }

    private void showDeleteConfirmationDialog(String idBudget) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BudgetDetailActivity.this);
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
                budgetController.deleteBudget(idBudget);
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