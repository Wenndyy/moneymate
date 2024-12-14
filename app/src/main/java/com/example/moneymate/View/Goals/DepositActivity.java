package com.example.moneymate.View.Goals;

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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.Controller.ExpenseController;
import com.example.moneymate.Controller.ItemBudgetController;
import com.example.moneymate.Interface.ItemDepositListener;
import com.example.moneymate.Model.CategorySavingGoals;
import com.example.moneymate.R;
import com.example.moneymate.View.Dashboard.DashboardActivity;
import com.example.moneymate.View.Expense.ExpenseActivity;
import com.example.moneymate.View.Expense.RecordExpenseActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class DepositActivity extends AppCompatActivity implements ItemDepositListener {

    private Toolbar toolbar;
    private TextView categoryTitleText;
    private ImageView categoryIcon;

    private String selectedDate;
    private EditText amountTextEdit;
    private EditText dateTextEdit;
    private LinearLayout submitButton,cancelButton, layoutProgress;
    private CardView layoutDeposit;
    private String idGoals,idCategory, idUser,categoryId,goalsId,goalCategory,goalCategoryImage;
    private  double goalBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        toolbar = findViewById(R.id.toolbar);
        categoryTitleText = findViewById(R.id.category_title);
        categoryIcon = findViewById(R.id.img_category);

        amountTextEdit = findViewById(R.id.amountTextEdit);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        submitButton = findViewById(R.id.submitButton);
        dateTextEdit = findViewById(R.id.DateTextEdit);
        cancelButton = findViewById(R.id.cancelButton);
        layoutDeposit = findViewById(R.id.layoutDeposit);
        layoutProgress = findViewById(R.id.layoutProgress);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }



        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(DepositActivity.this, GoalsDetailActivity.class);
            startActivity(intent);
            finish();
        });
        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idGoals = getIntent().getStringExtra("idGoals");
        idCategory = getIntent().getStringExtra("idCategory");

        if (idCategory != null) {
            ItemBudgetController itemBudgetController = new ItemBudgetController("","","",0,new Date(),new Date(),new Date());
            itemBudgetController.setItemDepositListener(this);
            itemBudgetController.getCategoryDataById(idCategory);
        }

        submitButton.setOnClickListener(view -> submitDeposit());
        dateTextEdit.setOnClickListener(v -> showDatePickerDialog());

        Intent intent = getIntent();
         categoryId = intent.getStringExtra("categoryId");
         goalsId =  intent.getStringExtra("idGoals");
         goalCategory = intent.getStringExtra("categoryName");
         goalCategoryImage = intent.getStringExtra("categoryImage");
         goalBudget = intent.getDoubleExtra("budgetAmount", 0);

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
    private void submitDeposit() {

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


        ItemBudgetController itemBudgetController = new ItemBudgetController("",idCategory,idUser,amount,selectedDateObj,createdAt,updatedAt);
        itemBudgetController.setItemDepositListener(this);
        itemBudgetController.getCategoryDataById(idCategory);

        itemBudgetController.saveDeposit(itemBudgetController,selectedDateObj,idGoals);
    }

    @Override
    public void onGetExpenseSuccess(CategorySavingGoals category) {
        if (category != null) {
            categoryTitleText.setText(category.getGoalsCategoryName());
            String imageName = category.getCategoryGoalsImage();
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
        showMotionToast("Deposit",message, MotionToastStyle.SUCCESS);
        amountTextEdit.setText("");
        dateTextEdit.setText("");
        Intent intent = new Intent(DepositActivity.this, SetGoalsActivity.class);
        intent.putExtra("idGoals",goalsId);
        intent.putExtra("idCategory",categoryId);
        intent.putExtra("categoryImage",goalCategoryImage);
        intent.putExtra("categoryName", goalCategory);
        intent.putExtra("budgetAmount", goalBudget);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Deposit",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutDeposit.setVisibility(View.GONE);
        }else{
            layoutProgress.setVisibility(View.GONE);
            layoutDeposit.setVisibility(View.VISIBLE);
        }

    }
}