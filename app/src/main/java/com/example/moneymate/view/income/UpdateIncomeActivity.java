package com.example.moneymate.view.income;

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

public class UpdateIncomeActivity extends AppCompatActivity {
    private LinearLayout btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_income);

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateIncomeActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}