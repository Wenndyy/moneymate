package com.example.moneymate.View.Income;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.IncomeController;
import com.example.moneymate.Interface.IncomeListener;
import com.example.moneymate.R;
import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.Model.Income;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class IncomeActivity extends AppCompatActivity implements IncomeListener {

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
    private LinearLayout submitButton,cancelButton, layoutProgress;
    private CardView layoutIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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
        cancelButton = findViewById(R.id.cancelButton);
        layoutIncome = findViewById(R.id.layoutIncome);
        layoutProgress = findViewById(R.id.layoutProgress);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(IncomeActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        if (categoryId != null) {
            IncomeController income = new IncomeController("","","",0,new Date(),new Date(),new Date());
            income.setIncomeListener(IncomeActivity.this);
            income.getCategoryDataById(categoryId);
        }

        submitButton.setOnClickListener(view -> submitIncome());
        dateTextEdit.setOnClickListener(v -> showDatePickerDialog());
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
                        selectedDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
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


        String[] dateParts = selectedDate.split("/");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1;
        int day = Integer.parseInt(dateParts[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date selectedDateObj = calendar.getTime();


        IncomeController save = new IncomeController("",categoryId,userId,amount,selectedDateObj,createdAt,updatedAt);
        save.setIncomeListener(IncomeActivity.this);
        save.getCategoryDataById(categoryId);

        save.saveIncome(save,selectedDateObj);
    }


    @Override
    public void onGetIncomeSuccess(CategoryIncome category) {
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

    @Override
    public void onMessageSuccess(String message) {
        showMotionToast("Income",message, MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");
        dateTextEdit.setText("");
        Intent intent = new Intent(IncomeActivity.this, RecordIncomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Income",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutIncome.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutIncome.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataIncomeSuccess(ArrayList<Income> incomeList) {

    }

    @Override
    public void onDataIncomeSuccess(Income income) {

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