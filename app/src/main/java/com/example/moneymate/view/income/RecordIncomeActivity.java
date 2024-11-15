package com.example.moneymate.view.income;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.moneymate.R;
import com.example.moneymate.model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordIncomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private LinearLayout recordLayout;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_income);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();  // Get current user's ID

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> onBackPressed());

        recordLayout = findViewById(R.id.recordLayout);  // Initialize layout for the records

        loadIncomeData();  // Load data from Firestore
    }

    private void loadIncomeData() {
        db.collection("Income")
                .whereEqualTo("idUser", userId)  // Fetch data for current user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Income> incomeList = new ArrayList<>();

                        // Process the retrieved income data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Income income = document.toObject(Income.class);
                            incomeList.add(income);
                        }

                        // Sort income data manually by created_at field
                        Collections.sort(incomeList, (income1, income2) -> income2.getCreated_at().compareTo(income1.getCreated_at()));

                        // Group the sorted data by date
                        Map<String, ArrayList<Income>> groupedData = new HashMap<>();
                        SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd");

                        for (Income income : incomeList) {
                            Date createdAt = income.getCreated_at();
                            String formattedDate = sdf.format(createdAt);

                            // Group by date
                            if (!groupedData.containsKey(formattedDate)) {
                                groupedData.put(formattedDate, new ArrayList<>());
                            }
                            groupedData.get(formattedDate).add(income);
                        }

                        // Display grouped income data
                        displayGroupedIncome(groupedData);
                    } else {
                        Log.w("RecordIncomeActivity", "Error getting documents.", task.getException());
                        Toast.makeText(RecordIncomeActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayGroupedIncome(Map<String, ArrayList<Income>> groupedData) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Map.Entry<String, ArrayList<Income>> entry : groupedData.entrySet()) {
            String date = entry.getKey();
            ArrayList<Income> incomeList = entry.getValue();


            View dateView = inflater.inflate(R.layout.record_date_layout, null);
            TextView dateText = dateView.findViewById(R.id.dateText);
            dateText.setText(date);


            LinearLayout dateLayout = new LinearLayout(this);
            dateLayout.setOrientation(LinearLayout.VERTICAL);
            dateLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_item_record));

            dateLayout.addView(dateView);


            for (int i = 0; i < incomeList.size(); i++) {
                Income income = incomeList.get(i);

                View incomeView = inflater.inflate(R.layout.record_income_item, null);
                TextView incomeType = incomeView.findViewById(R.id.incomeType);
                TextView incomeAmount = incomeView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = incomeView.findViewById(R.id.categoryIcon);

                getIncomeCategory(income.getIdCategoryIncome(), incomeType, incomeAmount, categoryIcon, income);


                dateLayout.addView(incomeView);


                if (i < incomeList.size() - 1) {
                    View divider = new View(this);
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    divider.setLayoutParams(dividerParams);
                    divider.setBackgroundColor(ContextCompat.getColor(this, R.color.grey)); // or any color you prefer
                    dateLayout.addView(divider);
                }
            }


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 16);

            dateLayout.setLayoutParams(layoutParams);
            recordLayout.addView(dateLayout);
        }
    }



    private void getIncomeCategory(String categoryId, TextView incomeType, TextView incomeAmount, ImageView categoryIcon, Income income) {
        db.collection("CategoryIncome")
                .document(categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String categoryName = document.getString("IncomeCategoryName");
                            String iconName = document.getString("categoryIncomeImage");


                            incomeType.setText(categoryName);


                            if (iconName != null && !iconName.isEmpty()) {

                                int iconResId = getResources().getIdentifier(iconName, "drawable", getPackageName());
                                if (iconResId != 0) {
                                    categoryIcon.setImageResource(iconResId);  // Set the icon
                                } else {
                                    Log.w("getIncomeCategory", "Icon not found for " + iconName);
                                    categoryIcon.setImageResource(R.drawable.ic_default);  // Set a default icon
                                }
                            } else {

                                categoryIcon.setImageResource(R.drawable.ic_default);
                            }


                            incomeAmount.setText("+Rp" + String.format("%,.2f", income.getAmount()));
                        } else {
                            Log.d("getIncomeCategory", "No such category found!");
                            incomeType.setText("Unknown Category");
                            categoryIcon.setImageResource(R.drawable.ic_default);  // Use default icon
                        }
                    } else {
                        Log.d("getIncomeCategory", "Error getting category", task.getException());
                        incomeType.setText("Error");
                        categoryIcon.setImageResource(R.drawable.ic_default);  // Use default icon
                    }
                });
    }

}
