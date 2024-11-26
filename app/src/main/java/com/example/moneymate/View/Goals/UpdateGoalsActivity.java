package com.example.moneymate.View.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.R;

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