package com.example.moneymate.view.goals;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneymate.R;
import com.example.moneymate.view.budget.BudgetDetailActivity;
import com.example.moneymate.view.budget.UpdateBudgetActivity;

public class GoalsDetailActivity extends AppCompatActivity {
    private ImageView editButton,deleteButton;
    private TextView title;
    private String nameCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goals_detail);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoalsDetailActivity.this, UpdateGoalsActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        nameCategory = getIntent().getStringExtra("detailGoalsCategory");
        title = findViewById(R.id.toolbarTitle);
        title.setText(nameCategory);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoalsDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_budget, null);
        builder.setView(dialogView);



        TextView cancelButton = dialogView.findViewById(R.id.btn_cancel);
        TextView okButton = dialogView.findViewById(R.id.btn_ok);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 265, getResources().getDisplayMetrics());
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 109, getResources().getDisplayMetrics());
        dialog.getWindow().setAttributes(params);
    }
}