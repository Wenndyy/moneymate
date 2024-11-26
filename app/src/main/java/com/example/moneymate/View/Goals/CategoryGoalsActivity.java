package com.example.moneymate.View.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;

import com.google.android.material.button.MaterialButton;

public class CategoryGoalsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private String goalsCategory = "";
    private boolean statusNewVehicleCategory = false;
    private boolean statusCharityCategory = false;
    private boolean statusNewHomeCategory = false;
    private boolean statusEmergencyFundCategory = false;
    private boolean statusInvestmentCategory = false;
    private boolean statusEducationCategory = false;
    private boolean statusHolidayTripCategory = false;
    private boolean statusHealthCareCategory = false;
    private boolean statusSpecialPurchaseCategory = false;
    private LinearLayout newVehicleCategory, charityCategory, newHomeCategory, emergencyFundCategory,investmentCategory, educationCategory, holidayTripCategory, healthCareCategory, specialPurchaseCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_goals);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsCategory = "";
                resetCategories();
                onBackPressed();
            }
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!goalsCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryGoalsActivity.this, GoalsActivity.class);
                    intent.putExtra("goalsCategory", goalsCategory);
                    startActivity(intent);
                }
            }
        });
        newVehicleCategory = findViewById(R.id.new_vehicle_category);
        charityCategory = findViewById(R.id.charity_category);
        newHomeCategory = findViewById(R.id.new_home_category);
        emergencyFundCategory = findViewById(R.id.emergency_fund_category);
        investmentCategory = findViewById(R.id.investment_category);
        educationCategory = findViewById(R.id.education_category);
        holidayTripCategory = findViewById(R.id.holiday_trip_category);
        healthCareCategory = findViewById(R.id.health_care_category);
        specialPurchaseCategory = findViewById(R.id.special_purchase_category);


        newVehicleCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(newVehicleCategory, "New Vehicle");
            }
        });

        charityCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(charityCategory, "Charity");
            }
        });

        newHomeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(newHomeCategory,"New Home");
            }
        });

        emergencyFundCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(emergencyFundCategory,"Emergency Fund");
            }
        });
        investmentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(investmentCategory,"Investment");
            }
        });
        educationCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(educationCategory, "Education");
            }
        });
        holidayTripCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(holidayTripCategory,"Holiday Trip");
            }
        });
        healthCareCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(healthCareCategory,"Health Care");
            }
        });
        specialPurchaseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(specialPurchaseCategory,"Special Purchase");
            }
        });
        statusNewVehicleCategory = getIntent().getBooleanExtra("statusNewVehicleCategory", false);
        statusCharityCategory= getIntent().getBooleanExtra("statusCharityCategory", false);
        statusNewHomeCategory = getIntent().getBooleanExtra("statusNewHomeCategory", false);
        statusEmergencyFundCategory = getIntent().getBooleanExtra("statusEmergencyFundCategory", false);
        statusInvestmentCategory = getIntent().getBooleanExtra("statusInvestmentCategory", false);
        statusEducationCategory = getIntent().getBooleanExtra("statusEducationCategory", false);
        statusHolidayTripCategory = getIntent().getBooleanExtra("statusHolidayTripCategory", false);
        statusHealthCareCategory = getIntent().getBooleanExtra("statusHealthCareCategory", false);
        statusSpecialPurchaseCategory = getIntent().getBooleanExtra("statusSpecialPurchaseCategory", false);


        setupCategoryClickListener(newVehicleCategory, "New Vehicle", statusNewVehicleCategory);
        setupCategoryClickListener(charityCategory, "Charity", statusCharityCategory);
        setupCategoryClickListener(newHomeCategory, "New Home", statusNewHomeCategory);
        setupCategoryClickListener(emergencyFundCategory, "Emergency Fund", statusEmergencyFundCategory);
        setupCategoryClickListener(investmentCategory, "Investment", statusInvestmentCategory);
        setupCategoryClickListener(educationCategory, "Education", statusEducationCategory);
        setupCategoryClickListener(holidayTripCategory, "Holiday Trip", statusHolidayTripCategory);
        setupCategoryClickListener(healthCareCategory, "Health Care", statusHealthCareCategory);
        setupCategoryClickListener(specialPurchaseCategory, "Special Purchase", statusSpecialPurchaseCategory);


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
        goalsCategory = category;
        selectedCategoryLayout.setBackgroundResource(R.drawable.bg_selected_category_icon);
        nextButton.setEnabled(true);
    }

    private void resetCategories() {
        resetCategoryBackground(newVehicleCategory, statusNewVehicleCategory);
        resetCategoryBackground(charityCategory, statusCharityCategory);
        resetCategoryBackground(newHomeCategory, statusNewHomeCategory);
        resetCategoryBackground(emergencyFundCategory, statusEmergencyFundCategory);
        resetCategoryBackground(investmentCategory, statusInvestmentCategory);
        resetCategoryBackground(educationCategory, statusEducationCategory);
        resetCategoryBackground(holidayTripCategory, statusHolidayTripCategory);
        resetCategoryBackground(healthCareCategory, statusHealthCareCategory);
        resetCategoryBackground(specialPurchaseCategory, statusSpecialPurchaseCategory);
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
        resetCategoryBackground(newVehicleCategory, statusNewVehicleCategory);
        resetCategoryBackground(charityCategory, statusCharityCategory);
        resetCategoryBackground(newHomeCategory, statusNewHomeCategory);
        resetCategoryBackground(emergencyFundCategory, statusEmergencyFundCategory);
        resetCategoryBackground(investmentCategory, statusInvestmentCategory);
        resetCategoryBackground(educationCategory, statusEducationCategory);
        resetCategoryBackground(holidayTripCategory, statusHolidayTripCategory);
        resetCategoryBackground(healthCareCategory, statusHealthCareCategory);
        resetCategoryBackground(specialPurchaseCategory, statusSpecialPurchaseCategory);
    }
}