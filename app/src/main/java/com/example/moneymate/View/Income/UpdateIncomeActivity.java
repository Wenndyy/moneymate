package com.example.moneymate.View.Income;

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


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.IncomeController;
import com.example.moneymate.Interface.UpdateIncomeListener;
import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.R;
import com.example.moneymate.Model.Income;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class UpdateIncomeActivity extends AppCompatActivity implements UpdateIncomeListener {
    private LinearLayout btnCancel, submitButton;
    private EditText amountTextEdit;
    private EditText dateTextEdit;
    private TextView categoryTitle;

    private FirebaseAuth mAuth;
    private String incomeId,incomeCategoryId;
    private String incomeDateString;
    private ImageView img_category;
    private String selectedDate,userId;
    private LinearLayout layoutProgress;
    private CardView layoutUpdateIncome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_income);

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateIncomeActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        mAuth = FirebaseAuth.getInstance();


        amountTextEdit = findViewById(R.id.amountTextEdit);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        categoryTitle = findViewById(R.id.category_title);
        submitButton = findViewById(R.id.submitButton);
        layoutUpdateIncome = findViewById(R.id.layoutUpdateIncome);
        layoutProgress = findViewById(R.id.layoutProgress);
        Intent intent = getIntent();
        incomeId = intent.getStringExtra("income_id");
        incomeCategoryId = intent.getStringExtra("income_category_id");
        incomeDateString = intent.getStringExtra("income_date");

        img_category = findViewById(R.id.img_category);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        dateTextEdit.setOnClickListener(v -> showDatePickerDialog());
        IncomeController incomeController = new IncomeController("","","",0.0,new Date(),new Date(),new Date());

        incomeController.setUpdateIncomeListener(UpdateIncomeActivity.this);
        incomeController.loadIncomeByIdIncome(incomeId);
        incomeController.getCategoryDataByIdForUpdate(incomeCategoryId);



        userId = mAuth.getCurrentUser().getUid();
        submitButton.setOnClickListener(v -> {
            String amountString = amountTextEdit.getText().toString().trim();
            String dateString = dateTextEdit.getText().toString().trim();

            try {

                String cleanedAmountString = amountString.replaceAll("[^\\d.]", "").trim();

                double amount;
                try {
                    amount = Double.parseDouble(cleanedAmountString);
                } catch (NumberFormatException e) {
                    Log.e("UpdateIncome", "Amount Parsing Error", e);
                    showMotionToast("Update Income", "Invalid amount format", MotionToastStyle.ERROR);
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
                            Log.d("UpdateIncome", "Date parsed successfully with format: " + sdf.toPattern());
                            break;
                        }
                    } catch (ParseException e) {
                        Log.d("UpdateIncome", "Failed to parse with format: " + sdf.toPattern());
                    }
                }

                if (selectedDateObj == null) {
                    showMotionToast("Update Income", "Invalid date format", MotionToastStyle.ERROR);
                    return;
                }

                Date incomeDate = parseOriginalDate(incomeDateString);


                Income updatedIncome = new Income(
                        incomeId,
                        incomeCategoryId,
                        userId,
                        amount,
                        selectedDateObj,
                        incomeDate,
                        new Date()
                );

                incomeController.updateIncome(updatedIncome);

            } catch (Exception e) {
                Log.e("UpdateIncome", "Unexpected Error", e);
                showMotionToast("Update Income", "An unexpected error occurred", MotionToastStyle.ERROR);
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
    public void onGetIncomeSuccess(CategoryIncome category) {
        categoryTitle.setText(category.getIncomeCategoryName());
        img_category.setImageResource(getResources().getIdentifier(category.getCategoryIncomeImage(), "drawable", getPackageName()));
    }

    @Override
    public void onMessageSuccess(String message) {
        showMotionToast("Update Income",message, MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");
        dateTextEdit.setText("");
        Intent intent = new Intent(UpdateIncomeActivity.this, RecordIncomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Update Income", message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutUpdateIncome.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutUpdateIncome.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataIncomeSuccess(Income income) {
        double amount = income.getAmount();
        BigDecimal formattedAmount = new BigDecimal(amount).stripTrailingZeros();
        amountTextEdit.setText(formattedAmount.toPlainString());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateOfIncome = income.getDateOfIncome();
        String formattedDate = sdf.format(dateOfIncome);
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