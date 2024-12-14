package com.example.moneymate.View.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.SavingGoalsController;
import com.example.moneymate.Interface.GoalsListener;
import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.Model.SavingGoals;
import com.example.moneymate.R;

import java.util.ArrayList;
import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class UpdateGoalsActivity extends AppCompatActivity implements MessageListener {
    private LinearLayout submitButton,cancelButton, layoutProgress;
    private CardView layoutGoals;
    private ImageView categoryImageView;
    private TextView categoryNameView;
    private EditText amountView;


    private String goalId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_goals);


        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateGoalsActivity.this, SetGoalsActivity.class);
            startActivity(intent);
            finish();
        });


        Intent intent = getIntent();
        goalId = intent.getStringExtra("idGoals");
        String categoryName = intent.getStringExtra("categoryName");
        String categoryImage = intent.getStringExtra("categoryImage");
        double goalAmount = intent.getDoubleExtra("budgetAmount", 0);


        int amount = (int) goalAmount;
        categoryImageView = findViewById(R.id.img_category);
        amountView = findViewById(R.id.amountTextEdit);
        categoryNameView = findViewById(R.id.category_title);

        categoryNameView.setText(categoryName);
        amountView.setText(String.valueOf(amount));

        cancelButton = findViewById(R.id.cancelButton);
        layoutGoals= findViewById(R.id.layoutGoals);
        layoutProgress = findViewById(R.id.layoutProgress);

        if (categoryImage != null && !categoryImage.isEmpty()) {
            int iconResId = getResources().getIdentifier(categoryImage, "drawable", getPackageName());
            if (iconResId != 0) {
                categoryImageView.setImageResource(iconResId);
            } else {
                categoryImageView.setImageResource(R.drawable.ic_default);
            }
        } else {
            categoryImageView.setImageResource(R.drawable.ic_default);
        }


        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            updateGoals();
        });
    }

    public void updateGoals(){
        String amountStr = amountView.getText().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        SavingGoalsController savingGoalsController = new SavingGoalsController();
        savingGoalsController.setMessageListener(this);
        savingGoalsController.updateGoals(goalId,amount,new Date());
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
    public void onMessageSuccess(String message) {
        showMotionToast("Saving Goals" , message , MotionToastStyle.SUCCESS);
        Intent intent = new Intent(this, SetGoalsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Saving Goals" , message , MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutGoals.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutGoals.setVisibility(View.VISIBLE);
        }
    }
}