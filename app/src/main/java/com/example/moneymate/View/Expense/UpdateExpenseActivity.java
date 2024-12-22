package com.example.moneymate.View.Expense;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.ExpenseController;
import com.example.moneymate.Interface.ExpenseListener;
import com.example.moneymate.Interface.UpdateExpenseListener;
import com.example.moneymate.Model.CategoryExpense;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.Model.Income;
import com.example.moneymate.R;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.example.moneymate.View.Income.UpdateIncomeActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class UpdateExpenseActivity extends AppCompatActivity implements UpdateExpenseListener {
    private LinearLayout btnCancel, submitButton;
    private EditText amountTextEdit;
    private EditText dateTextEdit;
    private TextView categoryTitle;

    private FirebaseAuth mAuth;
    private String expenseId,expenseCategoryId;
    private String expenseDateString;
    private ImageView img_category;
    private String selectedDate,userId;
    private LinearLayout layoutProgress;
    private CardView layoutUpdateExpense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_expense);

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateExpenseActivity.this, CategoryExpenseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        mAuth = FirebaseAuth.getInstance();


        amountTextEdit = findViewById(R.id.amountTextEdit);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        categoryTitle = findViewById(R.id.category_title);
        submitButton = findViewById(R.id.submitButton);
        layoutUpdateExpense = findViewById(R.id.layoutUpdateExpense);
        layoutProgress = findViewById(R.id.layoutProgress);
        Intent intent = getIntent();
        expenseId = intent.getStringExtra("expense_id");
        expenseCategoryId = intent.getStringExtra("expense_category_id");
        expenseDateString = intent.getStringExtra("expense_date");

        img_category = findViewById(R.id.img_category);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        dateTextEdit.setOnClickListener(v -> showDatePickerDialog());
        ExpenseController expenseController = new ExpenseController("","","",0.0,new Date(),new Date(),new Date());

        expenseController.setUpdateExpenseListener(UpdateExpenseActivity.this);
        expenseController.loadExpenseByIdExpense(expenseId);
        expenseController.getCategoryDataByIdForUpdate(expenseCategoryId);



        userId = mAuth.getCurrentUser().getUid();
        submitButton.setOnClickListener(v -> {
            String amountString = amountTextEdit.getText().toString().trim();
            String dateString = dateTextEdit.getText().toString().trim();

            try {
                Log.d("UpdateExpense", "Original Amount String: " + amountString);
                Log.d("UpdateExpense", "Original Date String: " + dateString);

                String cleanedAmountString = amountString.replaceAll("[^\\d.]", "").trim();
                Log.d("UpdateExpense", "Cleaned Amount String: " + cleanedAmountString);
                double amount;
                try {
                    amount = Double.parseDouble(cleanedAmountString);
                } catch (NumberFormatException e) {
                    Log.e("UpdateExpense", "Amount Parsing Error", e);
                    showMotionToast("Update Expense", "Invalid amount format", MotionToastStyle.ERROR);
                    return;
                }
                SimpleDateFormat[] possibleDateFormats = {
                        new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()),
                        new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
                        new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                };

                Date selectedDateObj = null;
                for (SimpleDateFormat sdf : possibleDateFormats) {
                    try {
                        selectedDateObj = sdf.parse(dateString);
                        if (selectedDateObj != null) {
                            Log.d("UpdateExpense", "Date parsed successfully with format: " + sdf.toPattern());
                            break;
                        }
                    } catch (ParseException e) {
                        Log.d("UpdateExpense", "Failed to parse with format: " + sdf.toPattern());
                    }
                }

                if (selectedDateObj == null) {
                    showMotionToast("Update Expense", "Invalid date format", MotionToastStyle.ERROR);
                    return;
                }

                Date expenseDate = parseOriginalDate(expenseDateString);
                Expense expense = new Expense(expenseId,expenseCategoryId,userId, amount, selectedDateObj, expenseDate, new Date());

                expenseController.updateExpense(expense);
            } catch (Exception e) {
                Log.e("UpdateExpense", "Unexpected Error", e);
                showMotionToast("Update Expense", "An unexpected error occurred", MotionToastStyle.ERROR);
            }

        });

    }

    private Date parseOriginalDate(String incomeDateString) {

        SimpleDateFormat[] dateFormats = {
                new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
                new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        };

        for (SimpleDateFormat sdf : dateFormats) {
            try {

                Log.d("UpdateIncome", "Attempting to parse with format: " + sdf.toPattern());

                Date parsedDate = sdf.parse(incomeDateString);

                if (parsedDate != null) {
                    Log.d("UpdateIncome", "Date parsed successfully: " + parsedDate);
                    return parsedDate;
                }
            } catch (ParseException e) {

                Log.d("UpdateIncome", "Failed to parse with format: " + sdf.toPattern());
            }
        }


        Log.e("UpdateIncome", "Unable to parse date string: " + incomeDateString);

        return new Date();
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
    @Override
    public void onGetExpenseSuccess(CategoryExpense category) {
        categoryTitle.setText(category.getExpenseCategoryName());
        img_category.setImageResource(getResources().getIdentifier(category.getCategoryExpenseImage(), "drawable", getPackageName()));
    }

    @Override
    public void onMessageSuccess(String message) {
        showMotionToast("Update Expense",message, MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");
        dateTextEdit.setText("");
        Intent intent = new Intent(UpdateExpenseActivity.this, RecordExpenseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Update Expense", message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutUpdateExpense.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutUpdateExpense.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onDataExpenseSuccess(Expense expense) {
        double amount = expense.getAmount();
        BigDecimal formattedAmount = new BigDecimal(amount).stripTrailingZeros();
        amountTextEdit.setText(formattedAmount.toPlainString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateOfExpense = expense.getDateOfExpense();
        String formattedDate = sdf.format(dateOfExpense);
        dateTextEdit.setText(formattedDate);
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