package com.example.moneymate.view.goals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.R;
import com.example.moneymate.view.budget.BudgetDetailActivity;
import com.example.moneymate.view.budget.CategoryBudgetActivity;
import com.example.moneymate.view.budget.SetBudgetActivity;
import com.google.android.material.button.MaterialButton;

public class SetGoalsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private MaterialButton setGoalsButton;
    private boolean statusNewVehicleCategory = false;
    private boolean statusCharityCategory = false;
    private boolean statusNewHomeCategory = false;
    private boolean statusEmergencyFundCategory = false;
    private boolean statusInvestmentCategory = false;
    private boolean statusEducationCategory = false;
    private boolean statusHolidayTripCategory = false;
    private boolean statusHealthCareCategory = false;
    private boolean statusSpecialPurchaseCategory = false;
    private String detailGoalsCategory = "";
    private ImageView detailNewVehicle, detailCharity,detailNewHome, detailEmergencyFund, detailInvestment, detailEducation, detailHolidayTrip, detailHealthCare, detailSpecialPurchase;
    private ProgressBar progressNewVehicle,progressCharity, progressNewHome, progressEmergencyFund, progressInvestment, progressEducation, progressHolidayTrip, progressHealthCare, progressSpecialPurchase;
    private TextView tvNewVehiclePercentage,tvCharityPercentage,tvNewHomePercentage,tvEmergencyFundPercentage,tvInvestmentPercentage,tvEducationPercentage,tvHolidayTripPercentage,tvHealthCarePercentage,tvSpecialPurchasePercentage;
    private LinearLayout layoutGoalsNewVehicle, layoutGoalsCharity,layoutGoalsNewHome, layoutGoalsEmergencyFund, layoutGoalsInvestment,layoutGoalsEducation, layoutGoalsHolidayTrip,layoutGoalsHealthCare,layoutGoalsSpecialPurchase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        layoutGoalsNewVehicle = findViewById(R.id.layoutGoalsNewVehicle);
        layoutGoalsCharity = findViewById(R.id.layoutGoalsCharity);
        layoutGoalsNewHome = findViewById(R.id.layoutGoalsNewHome);
        layoutGoalsEmergencyFund = findViewById(R.id.layoutGoalsEmergencyFund);
        layoutGoalsInvestment = findViewById(R.id.layoutGoalsInvestment);
        layoutGoalsEducation = findViewById(R.id.layoutGoalsEducation);
        layoutGoalsHolidayTrip = findViewById(R.id.layoutGoalsHolidayTrip);
        layoutGoalsHealthCare = findViewById(R.id.layoutGoalsHealthCare);
        layoutGoalsSpecialPurchase = findViewById(R.id.layoutGoalsSpecialPurchase);

        progressNewVehicle = findViewById(R.id.progressNewVehicle);
        progressCharity = findViewById(R.id.progressCharity);
        progressNewHome = findViewById(R.id.progressNewHome);
        progressEmergencyFund = findViewById(R.id.progressEmergencyFund);
        progressInvestment = findViewById(R.id.progressInvestment);
        progressEducation = findViewById(R.id.progressEducation);
        progressHolidayTrip = findViewById(R.id.progressHolidayTrip);
        progressHealthCare = findViewById(R.id.progressHealthCare);
        progressSpecialPurchase = findViewById(R.id.progressSpecialPurchase);

        tvNewVehiclePercentage = findViewById(R.id.tvNewVehiclePercentage);
        tvCharityPercentage = findViewById(R.id.tvCharityPercentage);
        tvNewHomePercentage = findViewById(R.id.tvNewHomePercentage);
        tvEmergencyFundPercentage = findViewById(R.id.tvEmergencyFundPercentage);
        tvInvestmentPercentage = findViewById(R.id.tvInvestmentPercentage);
        tvEducationPercentage = findViewById(R.id.tvEducationPercentage);
        tvHolidayTripPercentage = findViewById(R.id.tvHolidayTripPercentage);
        tvHealthCarePercentage = findViewById(R.id.tvHealthCarePercentage);
        tvSpecialPurchasePercentage = findViewById(R.id.tvSpecialPurchasePercentage);

        detailNewVehicle = findViewById(R.id.detailNewVehicle);
        detailCharity= findViewById(R.id.detailCharity);
        detailNewHome = findViewById(R.id.detailNewHome);
        detailEmergencyFund = findViewById(R.id.detailEmergencyFund);
        detailInvestment= findViewById(R.id.detailInvestment);
        detailEducation = findViewById(R.id.detailEducation);
        detailHolidayTrip = findViewById(R.id.detailHolidayTrip);
        detailHealthCare = findViewById(R.id.detailHealthCare);
        detailSpecialPurchase = findViewById(R.id.detailSpecialPurchase);

        statusNewVehicleCategory = getIntent().getBooleanExtra("statusNewVehicleCategory", false);
        statusCharityCategory= getIntent().getBooleanExtra("statusCharityCategory", false);
        statusNewHomeCategory = getIntent().getBooleanExtra("statusNewHomeCategory", false);
        statusEmergencyFundCategory = getIntent().getBooleanExtra("statusEmergencyFundCategory", false);
        statusInvestmentCategory = getIntent().getBooleanExtra("statusInvestmentCategory", false);
        statusEducationCategory = getIntent().getBooleanExtra("statusEducationCategory", false);
        statusHolidayTripCategory = getIntent().getBooleanExtra("statusHolidayTripCategory", false);
        statusHealthCareCategory = getIntent().getBooleanExtra("statusHealthCareCategory", false);
        statusSpecialPurchaseCategory = getIntent().getBooleanExtra("statusSpecialPurchaseCategory", false);

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
            intent.putExtra("statusNewVehicleCategory", statusNewVehicleCategory);
            intent.putExtra("statusCharityCategory", statusCharityCategory);
            intent.putExtra("statusNewHomeCategory", statusNewHomeCategory);
            intent.putExtra("statusEmergencyFundCategory", statusEmergencyFundCategory);
            intent.putExtra("statusInvestmentCategory", statusInvestmentCategory);
            intent.putExtra("statusEducationCategory", statusEducationCategory);
            intent.putExtra("statusHealthCareCategory", statusHealthCareCategory);
            intent.putExtra("statusSpecialPurchaseCategory", statusSpecialPurchaseCategory);
            startActivity(intent);
        });



        if (statusNewVehicleCategory){
            layoutGoalsNewVehicle.setVisibility(View.VISIBLE);
            progressNewVehicle.setProgress(75);
            tvNewVehiclePercentage.setText("75%");
        }

        if (statusCharityCategory){
            layoutGoalsCharity.setVisibility(View.VISIBLE);
            progressCharity.setProgress(75);
            tvCharityPercentage.setText("75%");
        }
        if (statusNewHomeCategory){
            layoutGoalsNewHome.setVisibility(View.VISIBLE);
            progressNewHome.setProgress(75);
            tvNewHomePercentage.setText("75%");
        }
        if (statusEmergencyFundCategory){
            layoutGoalsEmergencyFund.setVisibility(View.VISIBLE);
            progressEmergencyFund.setProgress(75);
            tvEmergencyFundPercentage.setText("75%");
        }
        if (statusInvestmentCategory){
            layoutGoalsInvestment.setVisibility(View.VISIBLE);
            progressInvestment.setProgress(75);
            tvInvestmentPercentage.setText("75%");
        }
        if (statusEducationCategory){
            layoutGoalsEducation.setVisibility(View.VISIBLE);
            progressEducation.setProgress(75);
            tvEducationPercentage.setText("75%");
        }
        if (statusHolidayTripCategory){
            layoutGoalsHolidayTrip.setVisibility(View.VISIBLE);
            progressHolidayTrip.setProgress(75);
            tvHolidayTripPercentage.setText("75%");
        }
        if (statusHealthCareCategory){
            layoutGoalsHealthCare.setVisibility(View.VISIBLE);
            progressHealthCare.setProgress(75);
            tvHealthCarePercentage.setText("75%");
        }
        if (statusSpecialPurchaseCategory){
            layoutGoalsSpecialPurchase.setVisibility(View.VISIBLE);
            progressSpecialPurchase.setProgress(75);
            tvSpecialPurchasePercentage.setText("75%");
        }
        detailNewVehicle.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "New Vehicle");
            startActivity(intent);
        });
        detailCharity.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Charity");
            startActivity(intent);
        });
        detailNewHome.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "New Home");
            startActivity(intent);
        });
        detailEmergencyFund.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Emergency Fund");
            startActivity(intent);
        });
        detailInvestment.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Investment");
            startActivity(intent);
        });
        detailEducation.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Education");
            startActivity(intent);
        });
        detailHolidayTrip.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Holiday Trip");
            startActivity(intent);
        });
       detailHealthCare.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Health Care");
            startActivity(intent);
        });
        detailSpecialPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(SetGoalsActivity.this, GoalsDetailActivity.class);
            intent.putExtra("detailGoalsCategory", "Special Purchase");
            startActivity(intent);
        });

    }
}