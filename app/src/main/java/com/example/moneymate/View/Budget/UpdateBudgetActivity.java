package com.example.moneymate.View.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;

public class UpdateBudgetActivity extends AppCompatActivity {
    private LinearLayout btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget);

        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateBudgetActivity.this, SetBudgetActivity.class);
            startActivity(intent);
            finish();
        });
    }
}