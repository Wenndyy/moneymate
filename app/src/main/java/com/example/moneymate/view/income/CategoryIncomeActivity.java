package com.example.moneymate.view.income;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
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
import com.example.moneymate.model.CategoryIncome;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryIncomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private LinearLayout wageCategory, bonusCategory, investmentCategory, parttimeCategory;
    private String incomeCategory = "";

    private static final String TAG = "CategoryIncomeActivity";
    private FirebaseFirestore db;
    private List<CategoryIncome> categoryIncomeList;
    private GridLayout categoryGrid;
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





        MaterialButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CategoryIncomeActivity.this, IncomeActivity.class);
                intent.putExtra("incomeCategory", incomeCategory);
                startActivity(intent);
            }
        });


        db = FirebaseFirestore.getInstance();
        categoryIncomeList = new ArrayList<>();
        categoryGrid = findViewById(R.id.categoryGrid);

        getCategoryIncome();

    }

    private void selectCategory(LinearLayout selectedCategoryLayout, String category) {
        resetCategories();
        incomeCategory = category;
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


    private void getCategoryIncome() {
        CollectionReference categoryIncomeRef = db.collection("CategoryIncome");

        categoryIncomeRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CategoryIncome categoryIncome = document.toObject(CategoryIncome.class);
                            categoryIncome.setIdCategoryIncome(document.getId());
                            categoryIncomeList.add(categoryIncome);
                        }
                        displayCategories();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void displayCategories() {
        categoryGrid.removeAllViews();


        for (CategoryIncome category : categoryIncomeList) {

            View categoryView = LayoutInflater.from(this).inflate(R.layout.item_category, null);
            ImageView categoryIcon = categoryView.findViewById(R.id.categoryIcon);
            TextView categoryName = categoryView.findViewById(R.id.categoryName);

            categoryName.setText(category.getIncomeCategoryName());
            String imageName = category.getCategoryIncomeImage();
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageResId != 0) {
                categoryIcon.setImageResource(imageResId);
            } else {

                categoryIcon.setImageResource(R.drawable.ic_default);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 16, 16, 16);  // Set margin
            categoryView.setLayoutParams(params);
            categoryView.setOnClickListener(v -> {
                incomeCategory = category.getIdCategoryIncome();
                selectCategory((LinearLayout) categoryView, category.getIdCategoryIncome());
                nextButton.setEnabled(true);
            });

            categoryGrid.addView(categoryView);
        }
    }

}