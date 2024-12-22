package com.example.moneymate.View.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.BudgetController;
import com.example.moneymate.Controller.ExpenseController;
import com.example.moneymate.Interface.BudgetListener;
import com.example.moneymate.Model.Budget;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.R;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.example.moneymate.View.Expense.ExpenseActivity;
import com.example.moneymate.View.Expense.RecordExpenseActivity;
import com.example.moneymate.View.Goals.SetGoalsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class BudgetActivity extends AppCompatActivity implements BudgetListener {
    private String categoryId;
    private FirebaseAuth mAuth;
    private String userId;
    private Toolbar toolbar;

    private TextView categoryTitleText;
    private ImageView categoryIcon;

    private EditText amountTextEdit;
    private LinearLayout submitButton,cancelButton, layoutProgress;
    private CardView layoutBudget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        categoryId = getIntent().getStringExtra("budgetCategory");
        amountTextEdit = findViewById(R.id.amountTextEdit);
        submitButton = findViewById(R.id.submitButton);

        cancelButton = findViewById(R.id.cancelButton);
        layoutBudget = findViewById(R.id.layoutBudget);
        layoutProgress = findViewById(R.id.layoutProgress);



        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(BudgetActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        if (categoryId != null) {
            BudgetController budget = new BudgetController();
            budget.setBudgetListener(this);
            budget.getCategoryDataById(categoryId);
        }

        submitButton.setOnClickListener(view -> submitBudget());

    }


    private void submitBudget() {
        String amountStr = amountTextEdit.getText().toString();

        if (amountStr.isEmpty()) {
            showMotionToast("Budget", "Please fill in all fields", MotionToastStyle.WARNING);
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Date createdAt = new Date();
        Date updatedAt = new Date();




        BudgetController budgetController = new BudgetController();
        budgetController.setBudgetListener(this);
        Budget newBudget = new Budget(
                null,
                categoryId,
                userId,
                amount,
                createdAt,
                updatedAt,
                new ArrayList<>()
        );


        budgetController.saveBudget(newBudget);
    }
    @Override
    public void onGetExpenseSuccess(CategoryBudget category) {
        if (category != null) {
            categoryTitleText.setText(category.getExpenseCategoryName());
            String imageName = category.getCategoryExpenseImage();
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageResId != 0) {
                categoryIcon.setImageResource(imageResId);
            } else {
                categoryIcon.setImageResource(R.drawable.ic_default);
            }
        }
    }

    @Override
    public void onMessageSuccess(String message) {
        showMotionToast("Budget",message, MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");

        Intent intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Budget",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutBudget.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutBudget.setVisibility(View.VISIBLE);
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
}