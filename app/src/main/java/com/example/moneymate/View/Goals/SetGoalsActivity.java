package com.example.moneymate.View.Goals;

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
import com.example.moneymate.Controller.SavingGoalsController;
import com.example.moneymate.Interface.SetGoalsListener;
import com.example.moneymate.R;
import com.example.moneymate.View.Budget.BudgetAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SetGoalsActivity extends AppCompatActivity implements SetGoalsListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton setGoalsButton;
    private LinearLayout  layoutProgress;
    private CardView layoutSetGoals;
    private RecyclerView recyclerViewBudget;
    private GoalsAdapter goalsAdapter;
    private SavingGoalsController savingGoalsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        layoutSetGoals = findViewById(R.id.layoutSetGoals);
        layoutProgress = findViewById(R.id.layoutProgress);

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setGoalsButton = findViewById(R.id.setGoals);
        setGoalsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, CategoryGoalsActivity.class);
            startActivity(intent);
        });

        recyclerViewBudget = findViewById(R.id.recyclerViewBudget);
        recyclerViewBudget.setLayoutManager(new LinearLayoutManager(this));


        goalsAdapter = new GoalsAdapter(this);
        recyclerViewBudget.setAdapter(goalsAdapter);

        savingGoalsController = new SavingGoalsController();
        savingGoalsController.setSetGoalsListener(this);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Log.d("SetBudgetActivity", "Current User ID: " + currentUserId);
            savingGoalsController.getGoalsDetails(currentUserId);
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
    public void onGoalsDetailsLoaded(List<Map<String, Object>> goalsDetails) {

        Log.d("SetBudgetActivity", "Budget Details Received: " + (goalsDetails != null ? goalsDetails.size() : "null"));

        if (goalsDetails != null && !goalsDetails.isEmpty()) {
            goalsAdapter.setGoalsList(goalsDetails);
            recyclerViewBudget.setVisibility(View.VISIBLE);
        } else {
            recyclerViewBudget.setVisibility(View.GONE);

        }
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Set Goals", message, MotionToastStyle.ERROR);
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