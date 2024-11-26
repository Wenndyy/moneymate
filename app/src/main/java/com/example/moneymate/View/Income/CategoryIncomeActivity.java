package com.example.moneymate.View.Income;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.moneymate.Controller.CategoryIncomeController;
import com.example.moneymate.Interface.CategoryIncomeListener;
import com.example.moneymate.R;
import com.example.moneymate.Model.CategoryIncome;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class CategoryIncomeActivity extends AppCompatActivity implements CategoryIncomeListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private String incomeCategory = "";

    private static final String TAG = "CategoryIncomeActivity";
    private FirebaseFirestore db;
    private List<CategoryIncome> categoryIncomeList;
    private GridLayout categoryGrid;
    private CategoryIncomeController categoryIncomeController;
    private CardView layoutCategory;
    private LinearLayout  layoutProgress;
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
        layoutCategory = findViewById(R.id.layoutCategory);
        layoutProgress = findViewById(R.id.layoutProgress);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!incomeCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryIncomeActivity.this, IncomeActivity.class);
                    intent.putExtra("incomeCategory", incomeCategory);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    resetCategories();
                    startActivity(intent);
                }
            }
        });


        db = FirebaseFirestore.getInstance();
        categoryIncomeList = new ArrayList<>();
        categoryGrid = findViewById(R.id.categoryGrid);

        categoryIncomeController = new CategoryIncomeController("", "", "", new Date(), new Date());
        categoryIncomeController.setCategoryIncomeListener(CategoryIncomeActivity.this);


        categoryIncomeController.getCategoryIncome();
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
            params.setMargins(16, 16, 16, 16);
            categoryView.setLayoutParams(params);
            categoryView.setOnClickListener(v -> {
                incomeCategory = category.getIdCategoryIncome();
                selectCategory((LinearLayout) categoryView, category.getIdCategoryIncome());
                nextButton.setEnabled(true);
            });

            categoryGrid.addView(categoryView);
        }
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
    public void onCategoryIncomeSuccess(List<CategoryIncome> categoryIncomeList) {
        this.categoryIncomeList = categoryIncomeList;
        displayCategories();
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
}