package com.example.moneymate.View.Expense;

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

import com.example.moneymate.Controller.ExpenseController;
import com.example.moneymate.Controller.IncomeController;
import com.example.moneymate.Interface.ExpenseListener;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.R;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.example.moneymate.View.Income.IncomeActivity;
import com.example.moneymate.View.Income.RecordIncomeActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ExpenseActivity extends AppCompatActivity implements ExpenseListener {
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
    private CardView layoutExpense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);
        categoryId = getIntent().getStringExtra("expenseCategory");
        amountTextEdit = findViewById(R.id.amountTextEdit);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        submitButton = findViewById(R.id.submitButton);
        backArrow = findViewById(R.id.backArrow);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        cancelButton = findViewById(R.id.cancelButton);
        layoutExpense = findViewById(R.id.layoutExpense);
        layoutProgress = findViewById(R.id.layoutProgress);

        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        if (categoryId != null) {
            ExpenseController expense = new ExpenseController("","","",0,new Date(),new Date(),new Date());
            expense.setExpenseListener(ExpenseActivity.this);
            expense.getCategoryDataById(categoryId);
        }

        submitButton.setOnClickListener(view -> submitExpense());
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
    private void submitExpense() {

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


        ExpenseController expense = new ExpenseController("",categoryId,userId,amount,selectedDateObj,createdAt,updatedAt);
        expense.setExpenseListener(ExpenseActivity.this);
        expense.getCategoryDataById(categoryId);

        expense.saveExpense(expense,selectedDateObj);
    }
    @Override
    public void onGetExpenseSuccess(CategoryExpense category) {
        if (category != null) {
            categoryTitleText.setText(category.getExpenseCategoryName());
            String imageName = category.getCategoryExpenseImage();
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
        showMotionToast("Expense",message, MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");
        dateTextEdit.setText("");
        Intent intent = new Intent(ExpenseActivity.this, RecordExpenseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Expense",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutExpense.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutExpense.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataExpenseSuccess(ArrayList<Expense> expenseList) {

    }

    @Override
    public void onDataExpenseSuccess(Expense expense) {

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