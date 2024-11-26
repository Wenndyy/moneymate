package com.example.moneymate.View.Expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;

public class ExpenseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private TextView categoryTitleText;
    private ImageView categoryIcon;
    private LinearLayout btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        String expenseCategory = getIntent().getStringExtra("expenseCategory");
        updateCategoryUI(expenseCategory);

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, CategoryExpenseActivity.class);
            startActivity(intent);
            finish();
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