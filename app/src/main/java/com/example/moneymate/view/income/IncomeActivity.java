package com.example.moneymate.view.income;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.R;

public class IncomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;

    private TextView categoryTitleText;
    private ImageView categoryIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
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
        String incomeCategory = getIntent().getStringExtra("incomeCategory");
        updateCategoryUI(incomeCategory);
    }

    private void updateCategoryUI(String category) {
        switch (category) {
            case "Wage":
                categoryTitleText.setText("Wage");
                categoryIcon.setImageResource(R.drawable.ic_wage);
                break;
            case "Bonus":
                categoryTitleText.setText("Bonus");
                categoryIcon.setImageResource(R.drawable.ic_bonus);
                break;
            case "Investment":
                categoryTitleText.setText("Investment");
                categoryIcon.setImageResource(R.drawable.ic_investment);
                break;
            case "Parttime":
                categoryTitleText.setText("Parttime");
                categoryIcon.setImageResource(R.drawable.ic_parttime);
                break;
            default:
                categoryTitleText.setText("Wage");
                categoryIcon.setImageResource(R.drawable.ic_wage);
                break;
        }
    }
}