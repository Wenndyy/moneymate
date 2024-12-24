package com.example.moneymate.View.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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

import com.example.moneymate.Controller.CategoryBudgetController;
import com.example.moneymate.Controller.CategorySavingGoalsController;
import com.example.moneymate.Interface.CategorySavingGoalsListener;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.CategorySavingGoals;
import com.example.moneymate.R;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class CategoryGoalsActivity extends AppCompatActivity implements CategorySavingGoalsListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton nextButton;
    private String goalsCategory = "";
    private List<CategorySavingGoals> categorySavingGoalsList;
    private GridLayout categoryGrid;
    private CategorySavingGoalsController categorySavingGoalsController;
    private CardView layoutCategory;
    private LinearLayout  layoutProgress, layoutCard;
    private double screenDiagonalInches;

    private List<String> usedCategoryIds = new ArrayList<>();
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
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        layoutCategory = findViewById(R.id.layoutCategory);
        layoutProgress = findViewById(R.id.layoutProgress);
        layoutCard = findViewById(R.id.layoutCard);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!goalsCategory.isEmpty()) {
                    Intent intent = new Intent(CategoryGoalsActivity.this, GoalsActivity.class);
                    intent.putExtra("goalsCategory", goalsCategory);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    resetCategories();
                    startActivity(intent);
                }
            }
        });

        categorySavingGoalsList = new ArrayList<>();
        categoryGrid = findViewById(R.id.categoryGrid);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidthInches = displayMetrics.widthPixels / displayMetrics.xdpi;
        float screenHeightInches = displayMetrics.heightPixels / displayMetrics.ydpi;
        screenDiagonalInches = Math.sqrt(
                Math.pow(screenWidthInches, 2) + Math.pow(screenHeightInches, 2)
        );

        if (screenDiagonalInches <= 6.4){
            layoutCard.setPadding(20,20,20,20);
            categoryGrid.setPadding(0,0,0,0);
        }

        categorySavingGoalsController = new CategorySavingGoalsController("","","",new Date(),new Date());
        categorySavingGoalsController.setcategorySavingGoalsListener(this);


        categorySavingGoalsController.getCategorySavingGoals();
    }


    private void displayCategories(List<CategorySavingGoals> categoryList) {
        categoryGrid.removeAllViews();

        for (CategorySavingGoals category : categoryList) {
            View categoryView = LayoutInflater.from(this).inflate(R.layout.item_category, null);
            ImageView categoryIcon = categoryView.findViewById(R.id.categoryIcon);
            TextView categoryName = categoryView.findViewById(R.id.categoryName);

            categoryName.setText(category.getGoalsCategoryName());
            String imageName = category.getCategoryGoalsImage();
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageResId != 0) {
                categoryIcon.setImageResource(imageResId);
            } else {
                categoryIcon.setImageResource(R.drawable.ic_default);
            }


            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(16, 16, 16, 16);

            params.setGravity(Gravity.CENTER);
            categoryView.setLayoutParams(params);


            if (usedCategoryIds.contains(category.getIdGoalsCategory())) {
                categoryView.setAlpha(0.5f);
                categoryView.setEnabled(false);
            } else {
                categoryView.setBackgroundResource(R.drawable.bg_category_icon);
                categoryView.setAlpha(1.0f);
                categoryView.setEnabled(true);


                categoryView.setOnClickListener(v -> {
                    goalsCategory = category.getIdGoalsCategory();
                    selectCategory((LinearLayout) categoryView, category.getIdGoalsCategory());
                    nextButton.setEnabled(true);
                });
            }

            categoryGrid.addView(categoryView);
        }
    }


    private void selectCategory(LinearLayout selectedCategoryLayout, String category) {
        resetCategories();
        goalsCategory = category;
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


    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Category Saving Goal", message, MotionToastStyle.WARNING);
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
    public void onCategorySavingGoalsSuccess(List<CategorySavingGoals> categoryList) {
        this.categorySavingGoalsList = categoryList;
        fetchUsedCategoryIds();

    }

    private void fetchUsedCategoryIds() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("SavingGoals")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        usedCategoryIds.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            String categoryId = document.getString("idCategory");
                            usedCategoryIds.add(categoryId);
                        }

                        displayCategories(categorySavingGoalsList);
                    } else {
                        Log.d("CategorySavingGoalsActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}