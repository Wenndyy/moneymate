package com.example.moneymate.view.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;
import com.google.android.material.button.MaterialButton;

public class SetBudgetActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton setBudgetButton;
    private boolean statusFoodCategory = false;
    private boolean statusGroceriesCategory = false;
    private boolean statusLifestyleCategory = false;
    private boolean statusBeautyCategory = false;
    private boolean statusTaxCategory = false;
    private boolean statusEducationCategory = false;
    private boolean statusResidentialCategory = false;
    private String detailBudgetCategory = "";
    private ImageView detailFood, detailGroceries,detailLifestyle, detailBeauty, detailTax, detailEducation, detailResidential;
    private ProgressBar progressFood,progressGroceries, progressLifestyle, progressBeauty, progressTax, progressEducation, progressResidential;
    private TextView tvFoodPercentage,tvGroceriesPercentage,tvLifestylePercentage,tvBeautyPercentage,tvTaxPercentage,tvEducationPercentage,tvResidentialPercentage;

    private LinearLayout layoutBudgetFood, layoutBudgetGroceries,layoutBudgetLifestyle, layoutBudgetBeauty, layoutBudgetTax,layoutBudgetEducation, layoutBudgetResidential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        layoutBudgetFood = findViewById(R.id.layoutBudgetFood);
        layoutBudgetGroceries = findViewById(R.id.layoutBudgetGroceries);
        layoutBudgetLifestyle = findViewById(R.id.layoutBudgetLifestyle);
        layoutBudgetBeauty = findViewById(R.id.layoutBudgetBeauty);
        layoutBudgetTax = findViewById(R.id.layoutBudgetTax);
        layoutBudgetEducation = findViewById(R.id.layoutBudgetEducation);
        layoutBudgetResidential = findViewById(R.id.layoutBudgetResidential);

        progressFood = findViewById(R.id.progressFood);
        progressGroceries = findViewById(R.id.progressGroceries);
        progressLifestyle = findViewById(R.id.progressLifestyle);
        progressBeauty = findViewById(R.id.progressBeauty);
        progressTax = findViewById(R.id.progressTax);
        progressEducation = findViewById(R.id.progressEducation);
        progressResidential = findViewById(R.id.progressResidential);

        tvFoodPercentage = findViewById(R.id.tvFoodPercentage);
        tvGroceriesPercentage = findViewById(R.id.tvGroceriesPercentage);
        tvLifestylePercentage = findViewById(R.id.tvLifestylePercentage);
        tvBeautyPercentage = findViewById(R.id.tvBeautyPercentage);
        tvTaxPercentage = findViewById(R.id.tvTaxPercentage);
        tvEducationPercentage = findViewById(R.id.tvEducationPercentage);
        tvResidentialPercentage = findViewById(R.id.tvResidentialPercentage);

        detailFood = findViewById(R.id.detailFood);
        detailGroceries= findViewById(R.id.detailGroceries);
        detailLifestyle = findViewById(R.id.detailLifestyle);
        detailBeauty = findViewById(R.id.detailBeauty);
        detailTax= findViewById(R.id.detailTax);
        detailEducation = findViewById(R.id.detailEducation);
        detailResidential = findViewById(R.id.detailResidential);

        statusFoodCategory = getIntent().getBooleanExtra("statusFoodCategory", false);
        statusGroceriesCategory= getIntent().getBooleanExtra("statusGroceriesCategory", false);
        statusLifestyleCategory = getIntent().getBooleanExtra("statusLifestyleCategory", false);
        statusBeautyCategory = getIntent().getBooleanExtra("statusBeautyCategory", false);
        statusTaxCategory = getIntent().getBooleanExtra("statusTaxCategory", false);
        statusEducationCategory = getIntent().getBooleanExtra("statusEducationCategory", false);
        statusResidentialCategory = getIntent().getBooleanExtra("statusResidentialCategory", false);

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setBudgetButton = findViewById(R.id.setBudget);
        setBudgetButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, CategoryBudgetActivity.class);
            intent.putExtra("statusFoodCategory", statusFoodCategory);
            intent.putExtra("statusGroceriesCategory", statusGroceriesCategory);
            intent.putExtra("statusLifestyleCategory", statusLifestyleCategory);
            intent.putExtra("statusBeautyCategory", statusBeautyCategory);
            intent.putExtra("statusTaxCategory", statusTaxCategory);
            intent.putExtra("statusEducationCategory", statusEducationCategory);
            intent.putExtra("statusResidentialCategory", statusResidentialCategory);
            startActivity(intent);
        });



        if (statusFoodCategory){
            layoutBudgetFood.setVisibility(View.VISIBLE);
            progressFood.setProgress(75);
            tvFoodPercentage.setText("75%");
        }

        if (statusGroceriesCategory){
            layoutBudgetGroceries.setVisibility(View.VISIBLE);
            progressGroceries.setProgress(75);
            tvGroceriesPercentage.setText("75%");
        }
        if (statusLifestyleCategory){
            layoutBudgetLifestyle.setVisibility(View.VISIBLE);
            progressLifestyle.setProgress(75);
            tvLifestylePercentage.setText("75%");
        }
        if (statusBeautyCategory){
            layoutBudgetBeauty.setVisibility(View.VISIBLE);
            progressBeauty.setProgress(75);
            tvBeautyPercentage.setText("75%");
        }
        if (statusTaxCategory){
            layoutBudgetTax.setVisibility(View.VISIBLE);
            progressTax.setProgress(75);
            tvTaxPercentage.setText("75%");
        }
        if (statusEducationCategory){
            layoutBudgetEducation.setVisibility(View.VISIBLE);
            progressEducation.setProgress(75);
            tvEducationPercentage.setText("75%");
        }
        if (statusResidentialCategory){
            layoutBudgetResidential.setVisibility(View.VISIBLE);
            progressResidential.setProgress(75);
            tvResidentialPercentage.setText("75%");
        }
        detailFood.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Food");
            startActivity(intent);
        });
        detailGroceries.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Groceries");
            startActivity(intent);
        });
        detailLifestyle.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Lifestyle");
            startActivity(intent);
        });
        detailBeauty.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Beauty");
            startActivity(intent);
        });
        detailTax.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Tax");
            startActivity(intent);
        });
        detailEducation.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Education");
            startActivity(intent);
        });
        detailResidential.setOnClickListener(v -> {
            Intent intent = new Intent(SetBudgetActivity.this, BudgetDetailActivity.class);
            intent.putExtra("detailBudgetCategory", "Residential");
            startActivity(intent);
        });


    }
}