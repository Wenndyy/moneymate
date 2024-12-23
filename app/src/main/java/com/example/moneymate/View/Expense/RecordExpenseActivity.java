package com.example.moneymate.View.Expense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.ExpenseController;
import com.example.moneymate.Interface.ExpenseListener;
import com.example.moneymate.Interface.RecordExpenseListener;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.R;
import com.example.moneymate.View.Income.UpdateIncomeActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class RecordExpenseActivity extends AppCompatActivity implements RecordExpenseListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private LinearLayout recordLayout;

    private FirebaseFirestore db;


    private ProgressBar progressBar;
    private TextView noValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_expense);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        db = FirebaseFirestore.getInstance();


        progressBar = findViewById(R.id.progressBar);

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recordLayout = findViewById(R.id.recordLayout);
        noValue = findViewById(R.id.noValue);

        progressBar.setVisibility(View.VISIBLE);
        recordLayout.setVisibility(View.GONE);
        noValue.setVisibility(View.GONE);

        ExpenseController expenseController = new ExpenseController("","","",0.0,new Date(),new Date(),new Date());
        expenseController.setRecordExpenseListener(RecordExpenseActivity.this);
        expenseController.loadExpenseDataByUser();
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


            for (int i = 0; i < expenseList.size(); i++) {
                Expense expense = expenseList.get(i);

                View expenseView = inflater.inflate(R.layout.record_expense_item, null);
                TextView expenseType = expenseView.findViewById(R.id.incomeType);
                TextView expenseAmount = expenseView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = expenseView.findViewById(R.id.categoryIcon);
                View divider = expenseView.findViewById(R.id.garis);

                getExpenseCategory(expense.getIdCategoryExpense(), expenseType, expenseAmount, categoryIcon, expense);
                expenseView.setOnClickListener(v -> showExpenseDialog(expense));

                dateLayout.addView(expenseView);


                if (i < expenseList.size() - 1) {
                    divider.setVisibility(View.VISIBLE);
                }else{
                    divider.setVisibility(View.GONE);
                    expenseView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_item_record_last));
                    expenseView.setPadding(
                            expenseView.getPaddingLeft(),
                            expenseView.getPaddingTop(),
                            expenseView.getPaddingRight(),
                            expenseView.getPaddingBottom()
                    );
                }
            }


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 16);

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
                            expenseType.setText(categoryName);
                            if (iconName != null && !iconName.isEmpty()) {
                                int iconResId = getResources().getIdentifier(iconName, "drawable", getPackageName());
                                if (iconResId != 0) {
                                    categoryIcon.setImageResource(iconResId);
                                } else {
                                    Log.w("getExpenseCategory", "Icon not found for " + iconName);
                                    categoryIcon.setImageResource(R.drawable.ic_default);
                                }
                            } else {

                                categoryIcon.setImageResource(R.drawable.ic_default);
                            }


                            expenseAmount.setText("- "+formatRupiah(expense.getAmount()));
                        } else {
                            Log.d("getIncomeCategory", "No such category found!");
                            expenseType.setText("Unknown Category");
                            categoryIcon.setImageResource(R.drawable.ic_default);
                        }
                    } else {
                        Log.d("getIncomeCategory", "Error getting category", task.getException());
                        expenseType.setText("Error");
                        categoryIcon.setImageResource(R.drawable.ic_default);
                    }
                });
    }
    private void showExpenseDialog(Expense expense) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_expense_detail, null);


        ImageView closeButton = dialogView.findViewById(R.id.close_button);
        ImageView deleteButton = dialogView.findViewById(R.id.delete_button);
        ImageView editButton = dialogView.findViewById(R.id.edit_button);
        ImageView imgCategory = dialogView.findViewById(R.id.img_category);
        TextView categoryTitle = dialogView.findViewById(R.id.category_title);
        TextView amountText = dialogView.findViewById(R.id.amount_text);
        TextView dateText = dialogView.findViewById(R.id.date_text);


        getExpenseCategory(expense.getIdCategoryExpense(), categoryTitle, amountText, imgCategory, expense);
        amountText.setText(formatRupiah(expense.getAmount()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateText.setText(sdf.format(expense.getDateOfExpense()));

        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        deleteButton.setOnClickListener(v -> {

            LayoutInflater inflaterDelete= LayoutInflater.from(this);
            View dialogViewDelete = inflaterDelete.inflate(R.layout.dialog_delete_expense, null);


            TextView btnCancel = dialogViewDelete.findViewById(R.id.btn_cancel);
            TextView btnOk = dialogViewDelete.findViewById(R.id.btn_ok);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogViewDelete);

            AlertDialog confirmationDialog = builder.create();
            confirmationDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
            btnCancel.setOnClickListener(view -> confirmationDialog.dismiss());


            btnOk.setOnClickListener(view -> {
                ExpenseController  expenseController = new ExpenseController(expense.getIdExpense(),expense.getIdCategoryExpense(),expense.getIdUser(),expense.getAmount(),expense.getDateOfExpense(),expense.getCreated_at(),expense.getUpdated_at());
                expenseController.setRecordExpenseListener(RecordExpenseActivity.this);
                expenseController.deleteExpense(expense);
                confirmationDialog.dismiss();
                dialog.dismiss();
            });


            confirmationDialog.show();
        });



        editButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, UpdateExpenseActivity.class);
            intent.putExtra("expense_id", expense.getIdExpense());
            intent.putExtra("expense_amount", expense.getAmount());
            intent.putExtra("expense_date", expense.getDateOfExpense().toString());
            intent.putExtra("expense_category_id", expense.getIdCategoryExpense());

            startActivity(intent);
        });

        dialogBuilder.setView(dialogView);

        dialog.show();
    }
    public String formatRupiah(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.setMaximumFractionDigits(0);
        formatRupiah.setMinimumFractionDigits(0);
        return formatRupiah.format(amount);
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Record Expense",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            recordLayout.setVisibility(View.GONE);

        } else {
            progressBar.setVisibility(View.GONE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataExpenseSuccess(ArrayList<Expense> expenseList) {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("E, dd/MM", Locale.getDefault());


        if (expenseList.isEmpty()) {
            noValue.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            recordLayout.setVisibility(View.GONE);
        } else {

            Collections.sort(expenseList, (expense1, expense2) ->
                    expense2.getDateOfExpense().compareTo(expense1.getDateOfExpense())
            );
            Map<String, ArrayList<Expense>> groupedData = new LinkedHashMap<>();
            for (Expense expense : expenseList) {
                String formattedDate = displayDateFormat.format(expense.getDateOfExpense());
                if (!groupedData.containsKey(formattedDate)) {
                    groupedData.put(formattedDate, new ArrayList<>());
                }
                groupedData.get(formattedDate).add(expense);
            }

            displayGroupedExpense(groupedData);
            progressBar.setVisibility(View.GONE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataExpenseSuccess(Expense expense) {
        showMotionToast("Record Expense","Expense successfully deleted!", MotionToastStyle.SUCCESS);
        recordLayout.removeAllViews();
        ExpenseController expenseController = new ExpenseController(expense.getIdExpense(),expense.getIdCategoryExpense(),expense.getIdUser(),expense.getAmount(),expense.getDateOfExpense(),expense.getCreated_at(),expense.getUpdated_at());
        expenseController.setRecordExpenseListener(RecordExpenseActivity.this);
        expenseController.loadExpenseDataByUser();
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
}