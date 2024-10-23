package com.example.moneymate.view.goals;

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

public class UpdateGoalsActivity extends AppCompatActivity {
    private LinearLayout btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_goals);
        btnCancel = findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateGoalsActivity.this, SetGoalsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}