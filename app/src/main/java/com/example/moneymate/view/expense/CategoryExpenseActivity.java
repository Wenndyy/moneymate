package com.example.moneymate.view.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.R;
import com.example.moneymate.view.income.CategoryIncomeActivity;
import com.example.moneymate.view.income.IncomeActivity;
import com.google.android.material.button.MaterialButton;

public class CategoryExpenseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private String expenseCategory = "";
    private LinearLayout foodCategory, groceriesCategory, lifestyleCategory, beautyCategory,taxCategory, educationCategory, residentialCategory;
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
                onBackPressed();
            }
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expenseCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryExpenseActivity.this, ExpenseActivity.class);
                    intent.putExtra("expenseCategory", expenseCategory);
                    startActivity(intent);
                }
            }
        });
        foodCategory = findViewById(R.id.food_category);
        groceriesCategory = findViewById(R.id.groceries_category);
        lifestyleCategory = findViewById(R.id.lifestyle_category);
        beautyCategory = findViewById(R.id.beauty_category);
        taxCategory = findViewById(R.id.tax_category);
        educationCategory = findViewById(R.id.education_category);
        residentialCategory = findViewById(R.id.residential_category);


        foodCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(foodCategory, "Food");
            }
        });

        groceriesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(groceriesCategory, "Groceries");
            }
        });

        lifestyleCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(lifestyleCategory,"Lifestyle");
            }
        });

        beautyCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(beautyCategory,"Beauty");
            }
        });
        taxCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(taxCategory,"Tax");
            }
        });
        educationCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(educationCategory, "Education");
            }
        });
        residentialCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(residentialCategory,"Residential");
            }
        });
    }

    private void selectCategory(LinearLayout selectedCategoryLayout, String category) {
        resetCategories();
        expenseCategory = category;
        selectedCategoryLayout.setBackgroundResource(R.drawable.bg_selected_category_icon);
        nextButton.setEnabled(true);
    }

    private void resetCategories() {
        foodCategory.setBackgroundResource(R.drawable.bg_category_icon);
        groceriesCategory.setBackgroundResource(R.drawable.bg_category_icon);
        lifestyleCategory.setBackgroundResource(R.drawable.bg_category_icon);
        beautyCategory.setBackgroundResource(R.drawable.bg_category_icon);
        taxCategory.setBackgroundResource(R.drawable.bg_category_icon);
        educationCategory.setBackgroundResource(R.drawable.bg_category_icon);
        residentialCategory.setBackgroundResource(R.drawable.bg_category_icon);
    }
}