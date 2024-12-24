package com.example.moneymate.View.Dashboard;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moneymate.Controller.DashboardController;
import com.example.moneymate.Interface.DashboardListener;
import com.example.moneymate.Model.Expense;
import com.example.moneymate.Model.Income;
import com.example.moneymate.R;
import com.example.moneymate.View.Budget.SetBudgetActivity;
import com.example.moneymate.View.Expense.CategoryExpenseActivity;
import com.example.moneymate.View.Expense.RecordExpenseActivity;
import com.example.moneymate.View.Goals.SetGoalsActivity;
import com.example.moneymate.View.Income.CategoryIncomeActivity;
import com.example.moneymate.View.Income.RecordIncomeActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;


public class HomeFragment extends Fragment implements DashboardListener {

    private LinearLayout income, expense, budget, goals, recordIncomeLayout,recordExpenseLayout;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private TextView tvUsername;
    private FirebaseFirestore db;
    private TextView noValueIncome, noValueExpense;

    private DashboardController dashboardController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        budget = view.findViewById(R.id.budget);
        goals = view.findViewById(R.id.goals);

        noValueIncome = view.findViewById(R.id.noValueIncome);
        noValueExpense = view.findViewById(R.id.noValueExpense);

        tvUsername = view.findViewById(R.id.usernameLabelhome);
        progressBar = view.findViewById(R.id.loading);
        frameLayout = view.findViewById(R.id.layoutHome);
        recordIncomeLayout = view.findViewById(R.id.recordIncomeLayout);
        recordExpenseLayout = view.findViewById(R.id.recordExpenseLayout);

        db = FirebaseFirestore.getInstance();

        dashboardController = new DashboardController();
        dashboardController.setdashboardListener(this);




        income.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CategoryIncomeActivity.class);
            startActivity(intent);

        });
        expense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CategoryExpenseActivity.class);
            startActivity(intent);

        });
        budget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetBudgetActivity.class);
            startActivity(intent);

        });
        goals.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetGoalsActivity.class);
            startActivity(intent);

        });

    }


    @Override
    public void onMessageFailure(String message) {
        showMotionToast("Dashboard", message ,MotionToastStyle.ERROR);
    }

    @Override
    public void onMessageLoading(boolean isLoading) {
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataSuccess(Map<String, Object> data) {
        if (getView() != null) {
            if (data.containsKey("totalIncome")) {
                double totalIncome = (double) data.get("totalIncome");
                TextView incomeTotalTextView = getView().findViewById(R.id.totalIncome);
                incomeTotalTextView.setText("+" + formatRupiah(totalIncome));
            } else if (data.containsKey("fullname")) {
                String name = (String) data.get("fullname");
                tvUsername.setText(name);
            } else if (data.containsKey("totalExpense")) {
                double totalExpense = (double) data.get("totalExpense");
                TextView expenseTotalTextView = getView().findViewById(R.id.totalExpense);
                expenseTotalTextView.setText("-" + formatRupiah(totalExpense));
            }
        }
    }

    private void showMotionToast(String title, String message, MotionToastStyle style) {
        MotionToast.Companion.createColorToast(
                getActivity(),
                title,
                message,
                style,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.poppins_regular)
        );
    }

    public String formatRupiah(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        return formatRupiah.format(amount);
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardController.getDataUser();
        dashboardController.getTotalIncome();
        dashboardController.getTotalExpense();
        dashboardController.loadLastIncomeData();
        dashboardController.loadLastExpenseData();
    }

    @Override
    public void onLoadLastIncomeSuccess(Income lastIncome, List<Income> incomeList) {
        if (lastIncome != null) {
            if (isAdded() && getContext() != null) {
                noValueIncome.setVisibility(View.GONE);
                recordIncomeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


                View lastIncomeView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.record_income_item, recordIncomeLayout, false);

                TextView incomeType = lastIncomeView.findViewById(R.id.incomeType);
                TextView incomeAmount = lastIncomeView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = lastIncomeView.findViewById(R.id.categoryIcon);


                getIncomeCategory(lastIncome.getIdCategoryIncome(), incomeType, incomeAmount, categoryIcon, lastIncome);


                Collections.sort(incomeList, (income1, income2) -> {
                    if (income1.getDateOfIncome() == null || income2.getDateOfIncome() == null) {
                        return 0;
                    }
                    return income2.getDateOfIncome().compareTo(income1.getDateOfIncome());
                });


                displayLatestIncome(incomeList);
            }
        } else {

            noValueIncome.setVisibility(View.VISIBLE);
            recordIncomeLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }
    private void displayLatestIncome(List<Income> incomeList) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        if (!incomeList.isEmpty()) {

            Date latestDate = incomeList.get(0).getDateOfIncome();

            List<Income> latestIncomeList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Income income : incomeList) {
                String incomeDate = sdf.format(income.getDateOfIncome());
                String latestIncomeDate = sdf.format(latestDate);

                if (incomeDate.equals(latestIncomeDate)) {
                    latestIncomeList.add(income);
                }
            }

            recordIncomeLayout.removeAllViews();

            if (!latestIncomeList.isEmpty()) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("E, MM/dd");
                View dateView = inflater.inflate(R.layout.record_date_layout, null);
                TextView dateText = dateView.findViewById(R.id.dateText);
                dateText.setText(sdf2.format(latestDate));

                LinearLayout dateLayout = new LinearLayout(getContext());
                dateLayout.setOrientation(LinearLayout.VERTICAL);
                dateLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_item_record));

                dateLayout.addView(dateView);
                int itemCount = Math.min(latestIncomeList.size(), 4);
                for (int i = 0; i < itemCount; i++) {
                    Income income = latestIncomeList.get(i);

                    View incomeView = inflater.inflate(R.layout.record_income_item, null);
                    TextView incomeType = incomeView.findViewById(R.id.incomeType);
                    TextView incomeAmount = incomeView.findViewById(R.id.incomeAmount);
                    ImageView categoryIcon = incomeView.findViewById(R.id.categoryIcon);

                    getIncomeCategory(income.getIdCategoryIncome(), incomeType, incomeAmount, categoryIcon, income);

                    dateLayout.addView(incomeView);
                    View view = incomeView.findViewById(R.id.garis);
                    view.setVisibility(View.VISIBLE);
                }

                addShowMoreTextView(dateLayout);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 16);
                dateLayout.setLayoutParams(layoutParams);
                recordIncomeLayout.addView(dateLayout);
            }
        }
    }
    private void setDefaultCategory(TextView incomeType, TextView incomeAmount, ImageView categoryIcon, Income income) {
        incomeType.setText("Unknown Category");
        incomeAmount.setText("+"+formatRupiah(income.getAmount()));
        categoryIcon.setImageResource(R.drawable.ic_default);
    }
    private void getIncomeCategory(String categoryId, TextView incomeType, TextView incomeAmount, ImageView categoryIcon, Income income) {
        if (getContext() != null && isAdded()) {  // Check if fragment is attached
            db.collection("CategoryIncome")
                    .document(categoryId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (isAdded() && getContext() != null) {  // Double check here
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String categoryName = document.getString("incomeCategoryName");
                                    String iconName = document.getString("categoryIncomeImage");
                                    incomeType.setText(categoryName != null ? categoryName : "Unknown Category");
                                    if (iconName != null && !iconName.isEmpty()) {
                                        int iconResId = getResources().getIdentifier(iconName, "drawable", requireActivity().getPackageName());
                                        if (iconResId != 0) {
                                            categoryIcon.setImageResource(iconResId);
                                        } else {
                                            categoryIcon.setImageResource(R.drawable.ic_default);
                                        }
                                    } else {
                                        categoryIcon.setImageResource(R.drawable.ic_default); // Default icon
                                    }
                                    incomeAmount.setText("+" + formatRupiah(income.getAmount()));
                                } else {
                                    Log.d("getIncomeCategory", "No such category found!");
                                    setDefaultCategory(incomeType, incomeAmount, categoryIcon, income);
                                }
                            } else {
                                Log.d("getIncomeCategory", "Error getting category", task.getException());
                                setDefaultCategory(incomeType, incomeAmount, categoryIcon, income);
                            }
                        }
                    });
        }
    }


    @Override
    public void onLoadLastExpenseSuccess(Expense lastExpense, List<Expense> expenseList) {
        if (lastExpense != null) {
            if (isAdded() && getContext() != null) {
                noValueExpense.setVisibility(View.GONE);
                recordExpenseLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


                View lastIncomeView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.record_expense_item, recordExpenseLayout, false);

                TextView incomeType = lastIncomeView.findViewById(R.id.incomeType);
                TextView incomeAmount = lastIncomeView.findViewById(R.id.incomeAmount);
                ImageView categoryIcon = lastIncomeView.findViewById(R.id.categoryIcon);

                getExpenseCategory(lastExpense.getIdCategoryExpense(), incomeType, incomeAmount, categoryIcon, lastExpense);
                Collections.sort(expenseList, (expense1, expense2) -> {
                    if (expense1.getDateOfExpense() == null || expense2.getDateOfExpense() == null) {
                        return 0;
                    }
                    return expense2.getDateOfExpense().compareTo(expense1.getDateOfExpense()); // Urutkan dari terbaru
                });
                displayLatestExpense(expenseList);
            }
        } else {

            noValueExpense.setVisibility(View.VISIBLE);
            recordExpenseLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void addShowMoreTextView(LinearLayout dateLayout) {
        TextView showMoreText = new TextView(getContext());
        showMoreText.setText("SHOW MORE");
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.poppins_semibold);
        showMoreText.setTypeface(typeface);
        showMoreText.setTextColor(ContextCompat.getColor(getContext(), R.color.secondry));
        showMoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        showMoreText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        showMoreText.setPadding(16, 0, 16, 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) showMoreText.getLayoutParams();
        layoutParams.setMargins(10, 20, 10, 10);
        showMoreText.setLayoutParams(layoutParams);


        showMoreText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordIncomeActivity.class);
            startActivity(intent);

        });

        dateLayout.addView(showMoreText);
    }

    private void getExpenseCategory(String categoryId, TextView expenseType, TextView expenseAmount, ImageView categoryIcon, Expense expense) {
        if (isAdded() && getContext() != null) {  // Ensure the fragment is still attached to the activity and context is non-null
            db.collection("CategoryExpense")
                    .document(categoryId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (isAdded() && getContext() != null) {  // Check again within the callback to prevent updating the UI if fragment is detached
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String categoryName = document.getString("expenseCategoryName");
                                    String iconName = document.getString("categoryExpenseImage");
                                    expenseType.setText(categoryName != null ? categoryName : "Unknown Category");
                                    if (iconName != null && !iconName.isEmpty()) {
                                        int iconResId = getResources().getIdentifier(iconName, "drawable", requireActivity().getPackageName());
                                        if (iconResId != 0) {
                                            categoryIcon.setImageResource(iconResId);
                                        } else {
                                            categoryIcon.setImageResource(R.drawable.ic_default);
                                        }
                                    } else {
                                        categoryIcon.setImageResource(R.drawable.ic_default);
                                    }
                                    expenseAmount.setText("-"+formatRupiah(expense.getAmount()));
                                } else {
                                    Log.d("getExpenseCategory", "No such category found!");
                                    setDefaultCategoryExpense(expenseType, expenseAmount, categoryIcon, expense);
                                }
                            } else {
                                Log.d("getExpenseCategory", "Error getting category", task.getException());
                                setDefaultCategoryExpense(expenseType, expenseAmount, categoryIcon, expense);
                            }
                        }
                    });
        }
    }

    private void displayLatestExpense(List<Expense> expenseList) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (!expenseList.isEmpty()) {
            Date latestDate = expenseList.get(0).getDateOfExpense();
            List<Expense> latestExpenseList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Expense expense : expenseList) {
                String expenseDate = sdf.format(expense.getDateOfExpense());
                String latestExpenseDate = sdf.format(latestDate);

                if (expenseDate.equals(latestExpenseDate)) {
                    latestExpenseList.add(expense);
                }
            }

            recordExpenseLayout.removeAllViews();
            if (!latestExpenseList.isEmpty()) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("E, MM/dd");
                View dateView = inflater.inflate(R.layout.record_date_layout, null);
                TextView dateText = dateView.findViewById(R.id.dateText);
                dateText.setText(sdf2.format(latestDate));

                LinearLayout dateLayout = new LinearLayout(getContext());
                dateLayout.setOrientation(LinearLayout.VERTICAL);
                dateLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_item_record));
                dateLayout.addView(dateView);
                int itemCount = Math.min(latestExpenseList.size(), 4);
                for (int i = 0; i < itemCount; i++) {
                    Expense expense = latestExpenseList.get(i);

                    View expenseView = inflater.inflate(R.layout.record_expense_item, null);
                    TextView expenseType = expenseView.findViewById(R.id.incomeType);
                    TextView expenseAmount = expenseView.findViewById(R.id.incomeAmount);
                    ImageView categoryIcon = expenseView.findViewById(R.id.categoryIcon);
                    getExpenseCategory(expense.getIdCategoryExpense(), expenseType, expenseAmount, categoryIcon, expense);
                    dateLayout.addView(expenseView);

                    View view = expenseView.findViewById(R.id.garis);
                    view.setVisibility(View.VISIBLE);
                }

                addShowMoreTextViewExpense(dateLayout);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 16);
                dateLayout.setLayoutParams(layoutParams);
                recordExpenseLayout.addView(dateLayout);
            }
        }
    }
    private void setDefaultCategoryExpense(TextView expenseType, TextView expenseAmount, ImageView categoryIcon, Expense expense) {
        expenseType.setText("Unknown Category");
        expenseAmount.setText("-"+ formatRupiah(expense.getAmount()));
        categoryIcon.setImageResource(R.drawable.ic_default);
    }

    private void addShowMoreTextViewExpense(LinearLayout dateLayout) {
        TextView showMoreText = new TextView(getContext());
        showMoreText.setText("SHOW MORE");
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.poppins_semibold);
        showMoreText.setTypeface(typeface);
        showMoreText.setTextColor(ContextCompat.getColor(getContext(), R.color.secondry));
        showMoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        showMoreText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        showMoreText.setPadding(16, 0, 16, 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) showMoreText.getLayoutParams();
        layoutParams.setMargins(10, 20, 10, 10);
        showMoreText.setLayoutParams(layoutParams);


        showMoreText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordExpenseActivity.class);
            startActivity(intent);

        });

        dateLayout.addView(showMoreText);
    }

}