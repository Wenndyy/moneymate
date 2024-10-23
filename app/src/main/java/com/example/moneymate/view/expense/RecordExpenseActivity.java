package com.example.moneymate.view.expense;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.moneymate.R;

public class RecordExpenseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backArrow;
    private LinearLayout itemIncome;
    private ImageView closeButton,editButton,deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_expense);

        itemIncome  = findViewById(R.id.itemIncome);
        itemIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseDetailDialog();
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void showExpenseDetailDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RecordExpenseActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_expense_detail, null);

        dialogView.setBackgroundResource(R.drawable.bg_dialog);
        builder.setView(dialogView);


        closeButton = dialogView.findViewById(R.id.close_button);
        editButton = dialogView.findViewById(R.id.edit_button);
        deleteButton = dialogView.findViewById(R.id.delete_button);


        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 235, getResources().getDisplayMetrics());
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 197, getResources().getDisplayMetrics());
        dialog.getWindow().setAttributes(params);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordExpenseActivity.this, UpdateExpenseActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });


    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordExpenseActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_expense, null);
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