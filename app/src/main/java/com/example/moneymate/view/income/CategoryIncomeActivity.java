package com.example.moneymate.view.income;

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
import com.google.android.material.button.MaterialButton;

public class CategoryIncomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private LinearLayout wageCategory, bonusCategory, investmentCategory, parttimeCategory;
    private String incomeCategory = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_income);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeCategory = "";
                resetCategories();
                onBackPressed();
            }
        });


        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!incomeCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryIncomeActivity.this, IncomeActivity.class);
                    intent.putExtra("incomeCategory", incomeCategory);
                    startActivity(intent);
                }
            }
        });


        wageCategory = findViewById(R.id.wage_category);
        bonusCategory = findViewById(R.id.bonus_category);
        investmentCategory = findViewById(R.id.investment_category);
        parttimeCategory = findViewById(R.id.parttime_category);


        wageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(wageCategory, "Wage");
            }
        });

        bonusCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(bonusCategory, "Bonus");
            }
        });

        investmentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(investmentCategory, "Investment");
            }
        });

        parttimeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(parttimeCategory, "Parttime");
            }
        });


        MaterialButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CategoryIncomeActivity.this, IncomeActivity.class);
                intent.putExtra("incomeCategory", incomeCategory);
                startActivity(intent);
            }
        });

    }

    private void selectCategory(LinearLayout selectedCategoryLayout, String category) {
        resetCategories();
        incomeCategory = category;
        selectedCategoryLayout.setBackgroundResource(R.drawable.bg_selected_category_icon);
        nextButton.setEnabled(true);
    }

    private void resetCategories() {
        wageCategory.setBackgroundResource(R.drawable.bg_category_icon);
        bonusCategory.setBackgroundResource(R.drawable.bg_category_icon);
        investmentCategory.setBackgroundResource(R.drawable.bg_category_icon);
        parttimeCategory.setBackgroundResource(R.drawable.bg_category_icon);
    }
}