package com.example.moneymate.View.Income;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.moneymate.Controller.IncomeController;
import com.example.moneymate.Interface.IncomeListener;
import com.example.moneymate.Model.CategoryIncome;
import com.example.moneymate.R;
import com.example.moneymate.Model.Income;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class RecordIncomeActivity extends AppCompatActivity implements IncomeListener {
    private Toolbar toolbar;
    private ImageView backArrow;
    private LinearLayout recordLayout;

    private FirebaseFirestore db;


    private ProgressBar progressBar;
    private TextView noValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_income);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        db = FirebaseFirestore.getInstance();


        progressBar = findViewById(R.id.progressBar);
        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
           onBackPressed();
        });

        recordLayout = findViewById(R.id.recordLayout);
        noValue = findViewById(R.id.noValue);

        progressBar.setVisibility(View.VISIBLE);
        recordLayout.setVisibility(View.GONE);
        noValue.setVisibility(View.GONE);


        IncomeController incomeController = new IncomeController("","","",0.0,new Date(),new Date(),new Date());
        incomeController.setIncomeListener(RecordIncomeActivity.this);
        incomeController.loadIncomeDataByUser();
    }




    private void displayGroupedIncome(Map<String, ArrayList<Income>> groupedData) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Map.Entry<String, ArrayList<Income>> entry : groupedData.entrySet()) {
            String date = entry.getKey();
            ArrayList<Income> incomeList = entry.getValue();


            View dateView = inflater.inflate(R.layout.record_date_layout, null);
            TextView dateText = dateView.findViewById(R.id.dateText);
            dateText.setText(date);


            LinearLayout dateLayout = new LinearLayout(this);
            dateLayout.setOrientation(LinearLayout.VERTICAL);
            dateLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_item_record));

            dateLayout.addView(dateView);


            for (int i = 0; i < incomeList.size(); i++) {
                Income income = incomeList.get(i);

                View incomeView = inflater.inflate(R.layout.record_income_item, null);
                TextView incomeType = incomeView.findViewById(R.id.incomeType);
                TextView incomeAmount = incomeView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = incomeView.findViewById(R.id.categoryIcon);

                getIncomeCategory(income.getIdCategoryIncome(), incomeType, incomeAmount, categoryIcon, income);
                incomeView.setOnClickListener(v -> showIncomeDialog(income));

                dateLayout.addView(incomeView);

                // Hanya tambahkan divider jika bukan item terakhir
                if (i < incomeList.size() - 1) {
                    View view  = incomeView.findViewById(R.id.garis);
                    view.setVisibility(View.VISIBLE);
                }
            }



            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 16);

            dateLayout.setLayoutParams(layoutParams);
            recordLayout.addView(dateLayout);
        }
    }



    private void getIncomeCategory(String categoryId, TextView incomeType, TextView incomeAmount, ImageView categoryIcon, Income income) {
        db.collection("CategoryIncome")
                .document(categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String categoryName = document.getString("incomeCategoryName");
                            String iconName = document.getString("categoryIncomeImage");
                            incomeType.setText(categoryName);
                            if (iconName != null && !iconName.isEmpty()) {
                                int iconResId = getResources().getIdentifier(iconName, "drawable", getPackageName());
                                if (iconResId != 0) {
                                    categoryIcon.setImageResource(iconResId);
                                } else {
                                    Log.w("getIncomeCategory", "Icon not found for " + iconName);
                                    categoryIcon.setImageResource(R.drawable.ic_default);
                                }
                            } else {

                                categoryIcon.setImageResource(R.drawable.ic_default);
                            }


                            incomeAmount.setText(formatRupiah(income.getAmount()));
                        } else {
                            Log.d("getIncomeCategory", "No such category found!");
                            incomeType.setText("Unknown Category");
                            categoryIcon.setImageResource(R.drawable.ic_default);
                        }
                    } else {
                        Log.d("getIncomeCategory", "Error getting category", task.getException());
                        incomeType.setText("Error");
                        categoryIcon.setImageResource(R.drawable.ic_default);
                    }
                });
    }
    private void showIncomeDialog(Income income) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_income_detail, null);


        ImageView closeButton = dialogView.findViewById(R.id.close_button);
        ImageView deleteButton = dialogView.findViewById(R.id.delete_button);
        ImageView editButton = dialogView.findViewById(R.id.edit_button);
        ImageView imgCategory = dialogView.findViewById(R.id.img_category);
        TextView categoryTitle = dialogView.findViewById(R.id.category_title);
        TextView amountText = dialogView.findViewById(R.id.amount_text);
        TextView dateText = dialogView.findViewById(R.id.date_text);


        getIncomeCategory(income.getIdCategoryIncome(), categoryTitle, amountText, imgCategory, income);
        amountText.setText(formatRupiah(income.getAmount()));

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        dateText.setText(sdf.format(income.getDateOfIncome()));

        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        deleteButton.setOnClickListener(v -> {

            LayoutInflater inflaterDelete= LayoutInflater.from(this);
            View dialogViewDelete = inflaterDelete.inflate(R.layout.dialog_delete_income, null);


            TextView btnCancel = dialogViewDelete.findViewById(R.id.btn_cancel);
            TextView btnOk = dialogViewDelete.findViewById(R.id.btn_ok);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogViewDelete);

            AlertDialog confirmationDialog = builder.create();
            confirmationDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
            btnCancel.setOnClickListener(view -> confirmationDialog.dismiss());


            btnOk.setOnClickListener(view -> {
                IncomeController incomeController = new IncomeController(income.getIdIncome(),income.getIdCategoryIncome(),income.getIdUser(),income.getAmount(),income.getDateOfIncome(),income.getCreated_at(),income.getUpdated_at());
                incomeController.setIncomeListener(RecordIncomeActivity.this);
                incomeController.deleteIncome(income);
                confirmationDialog.dismiss();
                dialog.dismiss();
            });


            confirmationDialog.show();
        });



        editButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, UpdateIncomeActivity.class);
            intent.putExtra("income_id", income.getIdIncome());
            intent.putExtra("income_amount", income.getAmount());
            intent.putExtra("income_date", income.getDateOfIncome().toString());
            intent.putExtra("income_category_id", income.getIdCategoryIncome());

            startActivity(intent);
        });

        dialogBuilder.setView(dialogView);

        dialog.show();
    }
    public String formatRupiah(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }



    @Override
    public void onGetIncomeSuccess(CategoryIncome category) {

    }

    @Override
    public void onMessageSuccess(String message) {

    }

    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Record Income",message, MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            recordLayout.setVisibility(View.GONE);

        } else {
            progressBar.setVisibility(View.GONE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataIncomeSuccess(ArrayList<Income> incomeList) {
        if (incomeList.isEmpty()) {
            noValue.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            recordLayout.setVisibility(View.GONE);
        } else {

            Collections.sort(incomeList, (income1, income2) -> income2.getDateOfIncome().compareTo(income1.getDateOfIncome()));


            Map<String, ArrayList<Income>> groupedData = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("E, MM/dd");

            for (Income income : incomeList) {
                Date dateOfIncome = income.getDateOfIncome();
                String formattedDate = sdf.format(dateOfIncome);

                if (!groupedData.containsKey(formattedDate)) {
                    groupedData.put(formattedDate, new ArrayList<>());
                }
                groupedData.get(formattedDate).add(income);
            }

            displayGroupedIncome(groupedData);
            progressBar.setVisibility(View.GONE);
            recordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataIncomeSuccess(Income income) {
        showMotionToast("Record Income","Income successfully deleted!", MotionToastStyle.SUCCESS);
        recordLayout.removeAllViews();
        IncomeController incomeController = new IncomeController(income.getIdIncome(),income.getIdCategoryIncome(),income.getIdUser(),income.getAmount(),income.getDateOfIncome(),income.getCreated_at(),income.getUpdated_at());
        incomeController.setIncomeListener(RecordIncomeActivity.this);
        incomeController.loadIncomeDataByUser();
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
