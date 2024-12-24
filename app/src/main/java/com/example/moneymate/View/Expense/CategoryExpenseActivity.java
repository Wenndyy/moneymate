package com.example.moneymate.View.Expense;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.CategoryExpenseController;
import com.example.moneymate.Interface.CategoryExpenseListener;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class CategoryExpenseActivity extends AppCompatActivity implements CategoryExpenseListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private String expenseCategory = "";

    private List<CategoryExpense> categoryExpenseList;
    private GridLayout categoryGrid;
    private CategoryExpenseController categoryExpenseController;
    private CardView layoutCategory;
    private LinearLayout  layoutProgress, layoutCard;
    private double screenDiagonalInches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_expense);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseCategory = "";
                resetCategories();
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        layoutCategory = findViewById(R.id.layoutCategory);
        layoutProgress = findViewById(R.id.layoutProgress);
        layoutCard = findViewById(R.id.layoutCard);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expenseCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryExpenseActivity.this, ExpenseActivity.class);
                    intent.putExtra("expenseCategory", expenseCategory);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    resetCategories();
                    startActivity(intent);
                }
            }
        });



        categoryExpenseList = new ArrayList<>();
        categoryGrid = findViewById(R.id.categoryGrid);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidthInches = displayMetrics.widthPixels / displayMetrics.xdpi;
        float screenHeightInches = displayMetrics.heightPixels / displayMetrics.ydpi;
        screenDiagonalInches = Math.sqrt(
                Math.pow(screenWidthInches, 2) + Math.pow(screenHeightInches, 2)
        );

        if (screenDiagonalInches <= 6.4){
            layoutCard.setPadding(20,20,20,20);
            categoryGrid.setPadding(0,0,0,0);
        }

        categoryExpenseController = new CategoryExpenseController("", "", "", new Date(), new Date());
        categoryExpenseController.setCategoryExpenseListener(CategoryExpenseActivity.this);


        categoryExpenseController.getCategoryExpense();

    }

    private void displayCategories() {
        categoryGrid.removeAllViews();
        for (CategoryExpense category : categoryExpenseList) {
            View categoryView = LayoutInflater.from(this).inflate(R.layout.item_category, null);
            ImageView categoryIcon = categoryView.findViewById(R.id.categoryIcon);
            TextView categoryName = categoryView.findViewById(R.id.categoryName);

            categoryName.setText(category.getExpenseCategoryName());
            String imageName = category.getCategoryExpenseImage();
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageResId != 0) {
                categoryIcon.setImageResource(imageResId);
            } else {

                categoryIcon.setImageResource(R.drawable.ic_default);
            }
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(16, 16, 16, 16);

            params.setGravity(Gravity.CENTER);
            categoryView.setLayoutParams(params);

            categoryView.setOnClickListener(v -> {
                expenseCategory = category.getIdCategoryExpense();
                selectCategory((LinearLayout) categoryView, category.getIdCategoryExpense());
                nextButton.setEnabled(true);
            });

            categoryGrid.addView(categoryView);
        }
    }
    private void selectCategory(LinearLayout selectedCategoryLayout, String category) {
        resetCategories();
        expenseCategory = category;
        selectedCategoryLayout.setBackgroundResource(R.drawable.bg_selected_category_icon);
        nextButton.setEnabled(true);
    }

    private void resetCategories() {
        for (int i = 0; i < categoryGrid.getChildCount(); i++) {
            View child = categoryGrid.getChildAt(i);
            if (child instanceof LinearLayout) {
                child.setBackgroundResource(R.drawable.bg_category_icon);
            }
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

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Category Income", message, MotionToastStyle.WARNING);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutCategory.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutCategory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCategoryExpenseSuccess(List<CategoryExpense> categoryList) {
        this.categoryExpenseList = categoryList;
        displayCategories();
    }
}