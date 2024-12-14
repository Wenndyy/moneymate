package com.example.moneymate.View.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.Controller.BudgetController;
import com.example.moneymate.Interface.BudgetListener;
import com.example.moneymate.Interface.SetBudgetListener;
import com.example.moneymate.Model.Budget;
import com.example.moneymate.Model.CategoryBudget;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SetBudgetActivity extends AppCompatActivity  implements SetBudgetListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton setBudgetButton;
    private LinearLayout  layoutProgress;
    private CardView layoutSetBudget;
    private RecyclerView recyclerViewBudget;
    private BudgetAdapter budgetAdapter;
    private BudgetController budgetController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        layoutSetBudget = findViewById(R.id.layoutSetBudget);
        layoutProgress = findViewById(R.id.layoutProgress);

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
            startActivity(intent);
        });
        recyclerViewBudget = findViewById(R.id.recyclerViewBudget);
        recyclerViewBudget.setLayoutManager(new LinearLayoutManager(this));


        budgetAdapter = new BudgetAdapter(this);
        recyclerViewBudget.setAdapter(budgetAdapter);

        budgetController = new BudgetController();
        budgetController.setSetBudgetListener(this);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Log.d("SetBudgetActivity", "Current User ID: " + currentUserId);
            budgetController.getBudgetDetails(currentUserId);
        } else {
            Log.e("SetBudgetActivity", "No user is currently logged in");
        }

    }


    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading) {
            layoutProgress.setVisibility(View.VISIBLE);
            recyclerViewBudget.setVisibility(View.GONE);
        } else {
            layoutProgress.setVisibility(View.GONE);
            recyclerViewBudget.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBudgetDetailsLoaded(List<Map<String, Object>> budgetDetails) {
        if (budgetDetails != null && !budgetDetails.isEmpty()) {
            budgetAdapter.setBudgetList(budgetDetails);
            recyclerViewBudget.setVisibility(View.VISIBLE);
        } else {
            recyclerViewBudget.setVisibility(View.GONE);

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
}