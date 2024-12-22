package com.example.moneymate.View.Goals;

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
import com.example.moneymate.Controller.SavingGoalsController;
import com.example.moneymate.Interface.GoalsListener;
import com.example.moneymate.Model.Budget;
import com.example.moneymate.Model.CategorySavingGoals;
import com.example.moneymate.Model.SavingGoals;
import com.example.moneymate.R;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class GoalsActivity extends AppCompatActivity implements GoalsListener {
    private String categoryId;
    private FirebaseAuth mAuth;
    private String userId;
    private Toolbar toolbar;

    private TextView categoryTitleText;
    private ImageView categoryIcon;

    private EditText amountTextEdit;
    private LinearLayout submitButton,cancelButton, layoutProgress;
    private CardView layoutGoals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        categoryId = getIntent().getStringExtra("goalsCategory");
        amountTextEdit = findViewById(R.id.amountTextEdit);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);
        layoutGoals= findViewById(R.id.layoutGoals);
        layoutProgress = findViewById(R.id.layoutProgress);


        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(GoalsActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        if (categoryId != null) {
            SavingGoalsController savingGoalsController = new SavingGoalsController();
            savingGoalsController.setGoalsListener(this);
            savingGoalsController.getCategoryDataById(categoryId);
        }

        submitButton.setOnClickListener(view -> submitGoals());



    }

    private void submitGoals() {
        String amountStr = amountTextEdit.getText().toString();
        if (amountStr.isEmpty()) {
            showMotionToast("Saving Goals", "Please fill in all fields", MotionToastStyle.WARNING);
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Date createdAt = new Date();
        Date updatedAt = new Date();




        SavingGoalsController savingGoalsController = new SavingGoalsController();
        savingGoalsController.setGoalsListener(this);
        SavingGoals newGoals = new SavingGoals(
                null,
                categoryId,
                userId,
                amount,
                createdAt,
                updatedAt,
                new ArrayList<>()
        );


        savingGoalsController.saveGoals(newGoals);
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

    @Override
    public void onGetGoalsSuccess(CategorySavingGoals category) {

        if (category != null) {
            categoryTitleText.setText(category.getGoalsCategoryName());
            String imageName = category.getCategoryGoalsImage();
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

        showMotionToast("Saving Goals" , message , MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");

        Intent intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Saving Goals" , message , MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutGoals.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutGoals.setVisibility(View.VISIBLE);
        }
    }
}