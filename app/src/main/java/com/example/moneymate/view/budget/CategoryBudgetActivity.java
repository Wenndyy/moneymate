package com.example.moneymate.view.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;
import com.google.android.material.button.MaterialButton;

public class CategoryBudgetActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private String budgetCategory = "";
    private boolean statusFoodCategory = false;
    private boolean statusGroceriesCategory = false;
    private boolean statusLifestyleCategory = false;
    private boolean statusBeautyCategory = false;
    private boolean statusTaxCategory = false;
    private boolean statusEducationCategory = false;
    private boolean statusResidentialCategory = false;
    private LinearLayout foodCategory, groceriesCategory, lifestyleCategory, beautyCategory,taxCategory, educationCategory, residentialCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_budget);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetCategory = "";
                resetCategories();
                onBackPressed();
            }
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!budgetCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryBudgetActivity.this, BudgetActivity.class);
                    intent.putExtra("budgetCategory", budgetCategory);
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
        statusFoodCategory = getIntent().getBooleanExtra("statusFoodCategory", false);
        statusGroceriesCategory= getIntent().getBooleanExtra("statusGroceriesCategory", false);
        statusLifestyleCategory = getIntent().getBooleanExtra("statusLifestyleCategory", false);
        statusBeautyCategory = getIntent().getBooleanExtra("statusBeautyCategory", false);
        statusTaxCategory = getIntent().getBooleanExtra("statusTaxCategory", false);
        statusEducationCategory = getIntent().getBooleanExtra("statusEducationCategory", false);
        statusResidentialCategory = getIntent().getBooleanExtra("statusResidentialCategory", false);


        setupCategoryClickListener(foodCategory, "Food", statusFoodCategory);
        setupCategoryClickListener(groceriesCategory, "Groceries", statusGroceriesCategory);
        setupCategoryClickListener(lifestyleCategory, "Lifestyle", statusLifestyleCategory);
        setupCategoryClickListener(beautyCategory, "Beauty", statusBeautyCategory);
        setupCategoryClickListener(taxCategory, "Tax", statusTaxCategory);
        setupCategoryClickListener(educationCategory, "Education", statusEducationCategory);
        setupCategoryClickListener(residentialCategory, "Residential", statusResidentialCategory);


        updateCategoryAppearance();

    }

    private void setupCategoryClickListener(LinearLayout categoryLayout, String category, boolean status) {
        categoryLayout.setOnClickListener(v -> {
            if (!status) {
                selectCategory(categoryLayout, category);
            }
        });
    }

    private void selectCategory(LinearLayout selectedCategoryLayout, String category) {
        resetCategories();
        budgetCategory = category;
        selectedCategoryLayout.setBackgroundResource(R.drawable.bg_selected_category_icon);
        nextButton.setEnabled(true);
    }

    private void resetCategories() {
        resetCategoryBackground(foodCategory, statusFoodCategory);
        resetCategoryBackground(groceriesCategory, statusGroceriesCategory);
        resetCategoryBackground(lifestyleCategory, statusLifestyleCategory);
        resetCategoryBackground(beautyCategory, statusBeautyCategory);
        resetCategoryBackground(taxCategory, statusTaxCategory);
        resetCategoryBackground(educationCategory, statusEducationCategory);
        resetCategoryBackground(residentialCategory, statusResidentialCategory);
    }

    private void resetCategoryBackground(LinearLayout category, boolean status) {
        if (status) {
            category.setBackgroundResource(R.drawable.bg_disabled_category_icon);
            category.setAlpha(0.5f);
        } else {
            category.setBackgroundResource(R.drawable.bg_category_icon);
            category.setAlpha(1.0f);
        }
    }

    private void updateCategoryAppearance() {
        resetCategoryBackground(foodCategory, statusFoodCategory);
        resetCategoryBackground(groceriesCategory, statusGroceriesCategory);
        resetCategoryBackground(lifestyleCategory, statusLifestyleCategory);
        resetCategoryBackground(beautyCategory, statusBeautyCategory);
        resetCategoryBackground(taxCategory, statusTaxCategory);
        resetCategoryBackground(educationCategory, statusEducationCategory);
        resetCategoryBackground(residentialCategory, statusResidentialCategory);
    }
}