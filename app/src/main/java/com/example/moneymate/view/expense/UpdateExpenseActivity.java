package com.example.moneymate.view.expense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.R;
import com.example.moneymate.view.dashboard.DashboardActivity;
import com.example.moneymate.view.income.UpdateIncomeActivity;

public class UpdateExpenseActivity extends AppCompatActivity {
    private LinearLayout btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_expense);

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateExpenseActivity.this, CategoryExpenseActivity.class);
            startActivity(intent);
            finish();
        });
    }
}