package com.example.moneymate.view.income;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.R;
import com.example.moneymate.model.CategoryIncome;
import com.example.moneymate.model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class IncomeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String categoryId;
    private FirebaseAuth mAuth;
    private String userId;
    private Toolbar toolbar;
    private ImageView backArrow;

    private TextView categoryTitleText;
    private ImageView categoryIcon;

    private String selectedDate;
    private EditText amountTextEdit;
    private EditText dateTextEdit;
    private LinearLayout submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        categoryId = getIntent().getStringExtra("incomeCategory");
        amountTextEdit = findViewById(R.id.amountTextEdit);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        submitButton = findViewById(R.id.submitButton);
        backArrow = findViewById(R.id.backArrow);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (categoryId != null) {
            getCategoryDataById(categoryId);
        }

        submitButton.setOnClickListener(view -> submitIncome());
        dateTextEdit.setOnClickListener(v -> showDatePickerDialog());
    }

    private void getCategoryDataById(String categoryId) {
        CollectionReference categoryIncomeRef = db.collection("CategoryIncome");
        categoryIncomeRef.document(categoryId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            CategoryIncome category = document.toObject(CategoryIncome.class);
                            if (category != null) {
                                categoryTitleText.setText(category.getIncomeCategoryName());
                                String imageName = category.getCategoryIncomeImage();
                                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                                if (imageResId != 0) {
                                    categoryIcon.setImageResource(imageResId);
                                } else {
                                    categoryIcon.setImageResource(R.drawable.ic_default);
                                }
                            }
                        }
                    } else {
                        Log.w("IncomeActivity", "Error getting category", task.getException());
                    }
                });
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        dateTextEdit.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }
    private void submitIncome() {

        String amountStr = amountTextEdit.getText().toString();
        String dateStr = dateTextEdit.getText().toString();


        if (amountStr.isEmpty() || dateStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Date createdAt = new Date();
        Date updatedAt = new Date();


        String[] dateParts = selectedDate.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1;
        int day = Integer.parseInt(dateParts[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date selectedDateObj = calendar.getTime();


        Income income = new Income();
        income.setIdCategoryIncome(categoryId);
        income.setIdUser(userId);
        income.setAmount(amount);
        income.setCreated_at(createdAt);
        income.setUpdated_at(updatedAt);


        saveIncome(income,selectedDateObj);
    }

    private void saveIncome(Income income, Date selectedDateObj) {
        CollectionReference incomeRef = db.collection("Income");
        incomeRef.add(income)
                .addOnSuccessListener(documentReference -> {
                    String incomeId = documentReference.getId();
                    income.setIdIncome(incomeId);

                    incomeRef.document(incomeId).set(income)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("IncomeActivity", "Income successfully saved!");
                                Toast.makeText(IncomeActivity.this, "Income saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(IncomeActivity.this, RecordIncomeActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.w("IncomeActivity", "Error saving income", e);
                                Toast.makeText(IncomeActivity.this, "Failed to save income", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w("IncomeActivity", "Error saving income", e);
                    Toast.makeText(IncomeActivity.this, "Failed to save income", Toast.LENGTH_SHORT).show();
                });
    }
}