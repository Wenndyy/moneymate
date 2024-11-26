package com.example.moneymate.View.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;

public class GoalsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView categoryIcon;

    private TextView categoryTitleText;
    private LinearLayout submitButton,btnCancel;
    private  String goalsCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
            startActivity(intent);
            finish();
        });

        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        goalsCategory = getIntent().getStringExtra("goalsCategory");
        updateCategoryUI(goalsCategory);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            Intent intent = getIntent();
            switch (goalsCategory) {
                case "New Vehicle":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusNewVehicleCategory", true);
                    startActivity(intent);
                    break;
                case "Charity":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusCharityCategory", true);
                    startActivity(intent);
                    break;

                case "New Home":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusNewHomeCategory", true);
                    startActivity(intent);
                    break;
                case "EmergencyFund":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusEmergencyFundCategory", true);
                    startActivity(intent);
                    break;
                case "Investment":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusInvestmentCategory", true);
                    startActivity(intent);
                    break;
                case "Education":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusEducationCategory", true);
                    startActivity(intent);
                    break;
                case "Holiday Trip":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusHolidayTripCategory", true);
                    startActivity(intent);
                    break;
                case "Health Care":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusHealthCareCategory", true);
                    startActivity(intent);
                    break;
                case "Special Purchase":
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusSpecialPurchaseCategory", true);
                    startActivity(intent);
                    break;
                default:
                    intent = new Intent(GoalsActivity.this, SetGoalsActivity.class);
                    intent.putExtra("statusNewVehicleCategory", true);
                    startActivity(intent);
                    break;
            }
        });
    }

    private void updateCategoryUI(String category) {
        switch (category) {
            case "New Vehicle":
                categoryTitleText.setText("New Vehicle");
                categoryIcon.setImageResource(R.drawable.ic_new_vehivcle);
                break;
            case "Charity":
                categoryTitleText.setText("Charity");
                categoryIcon.setImageResource(R.drawable.ic_charity);
                break;

            case "New Home":
                categoryTitleText.setText("New Home");
                categoryIcon.setImageResource(R.drawable.ic_new_home);
                break;
            case "Emergency Fund":
                categoryTitleText.setText("Emergency Fund");
                categoryIcon.setImageResource(R.drawable.ic_emergency_fund);
                break;
            case "Investment":
                categoryTitleText.setText("Investment");
                categoryIcon.setImageResource(R.drawable.ic_investment);
                break;
            case "Education":
                categoryTitleText.setText("Education");
                categoryIcon.setImageResource(R.drawable.ic_education);
                break;
            case "Holiday Trip":
                categoryTitleText.setText("Holiday Trip");
                categoryIcon.setImageResource(R.drawable.ic_holiday_trip);
                break;
            case "Health Care":
                categoryTitleText.setText("Health Care");
                categoryIcon.setImageResource(R.drawable.ic_health_care);
                break;
            case "Special Purchase":
                categoryTitleText.setText("Special Purchase");
                categoryIcon.setImageResource(R.drawable.ic_special_purchase);
                break;
            default:
                categoryTitleText.setText("New Vehicle");
                categoryIcon.setImageResource(R.drawable.ic_new_vehivcle);
                break;
        }
    }
}