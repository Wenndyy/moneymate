package com.example.moneymate.View.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;
import com.example.moneymate.View.Goals.SetGoalsActivity;

public class BudgetActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView categoryIcon;

    private TextView categoryTitleText;
    private LinearLayout submitButton,btnCancel;
    private  String budgetCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(BudgetActivity.this, SetGoalsActivity.class);
            startActivity(intent);
            finish();
        });
        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        budgetCategory = getIntent().getStringExtra("budgetCategory");
        updateCategoryUI(budgetCategory);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            Intent intent = getIntent();
            switch (budgetCategory) {
                case "Food":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusFoodCategory", true);
                    startActivity(intent);
                    break;
                case "Groceries":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusGroceriesCategory", true);
                    startActivity(intent);
                    break;

                case "Lifestyle":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusLifestyleCategory", true);
                    startActivity(intent);
                    break;
                case "Beauty":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusBeautyCategory", true);
                    startActivity(intent);
                    break;
                case "Tax":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusTaxCategory", true);
                    startActivity(intent);
                    break;
                case "Education":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusEducationCategory", true);
                    startActivity(intent);
                    break;
                case "Residential":
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusResidentialCategory", true);
                    startActivity(intent);
                    break;

                default:
                    intent = new Intent(BudgetActivity.this, SetBudgetActivity.class);
                    intent.putExtra("statusFoodCategory", true);
                    startActivity(intent);
                    break;
            }
        });
    }

    private void updateCategoryUI(String category) {
        switch (category) {
            case "Food":
                categoryTitleText.setText("Food");
                categoryIcon.setImageResource(R.drawable.ic_food);
                break;
            case "Groceries":
                categoryTitleText.setText("Groceries");
                categoryIcon.setImageResource(R.drawable.ic_groceries);
                break;

            case "Lifestyle":
                categoryTitleText.setText("Lifestyle");
                categoryIcon.setImageResource(R.drawable.ic_lifestyle);
                break;
            case "Beauty":
                categoryTitleText.setText("Beauty");
                categoryIcon.setImageResource(R.drawable.ic_beauty);
                break;
            case "Tax":
                categoryTitleText.setText("Tax");
                categoryIcon.setImageResource(R.drawable.ic_tax);
                break;
            case "Education":
                categoryTitleText.setText("Education");
                categoryIcon.setImageResource(R.drawable.ic_education);
                break;
            case "Residential":
                categoryTitleText.setText("Residential");
                categoryIcon.setImageResource(R.drawable.ic_residential);
                break;

            default:
                categoryTitleText.setText("Food");
                categoryIcon.setImageResource(R.drawable.ic_food);
                break;
        }
    }
}