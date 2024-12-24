package com.example.moneymate.View.Budget;


import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.View.Goals.GoalsDetailActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private List<Map<String, Object>> budgetList;
    private Context context;

    public BudgetAdapter(Context context) {
        this.context = context;
        this.budgetList = new ArrayList<>();
    }

    public void setBudgetList(List<Map<String, Object>> budgetList) {
        this.budgetList = budgetList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Map<String, Object> budgetItem = budgetList.get(position);

        String categoryId = (String) budgetItem.get("categoryId");
        String idBudget = (String) budgetItem.get("idBudget");
        String categoryName = (String) budgetItem.get("categoryName");
        String categoryImage = (String) budgetItem.get("categoryImage");
        double budgetAmount = (double) budgetItem.get("budgetAmount");
        double totalSpent = (double) budgetItem.get("totalSpent");
        double percentage = (double) budgetItem.get("percentage");

        // Mengatur gambar kategori
        if (categoryImage != null && !categoryImage.isEmpty()) {
            int iconResId = context.getResources().getIdentifier(categoryImage, "drawable", context.getPackageName());
            if (iconResId != 0) {
                holder.categoryImage.setImageResource(iconResId);
            } else {
                holder.categoryImage.setImageResource(R.drawable.ic_default);
            }
        } else {
            holder.categoryImage.setImageResource(R.drawable.ic_default);
        }

        holder.categoryName.setText(categoryName);
        holder.amount.setText(formatCurrency(budgetAmount));


        holder.progressFood.setMax((int) budgetAmount);
        holder.progressFood.setProgress((int) totalSpent);
        holder.tvFoodPercentage.setText(String.format("%.1f%%", percentage));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BudgetDetailActivity.class);
            intent.putExtra("categoryId",categoryId);
            intent.putExtra("idBudget",idBudget);
            intent.putExtra("categoryImage",categoryImage);
            intent.putExtra("categoryName", categoryName);
            intent.putExtra("budgetAmount", budgetAmount);
            intent.putExtra("totalSpent", totalSpent);
            intent.putExtra("percentage", percentage);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return budgetList.size();
    }


    private String formatCurrency(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        return formatRupiah.format(amount);
    }

    class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, amount, tvFoodPercentage;
        ProgressBar progressFood;
        ImageView categoryImage, detailFood;

        BudgetViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            amount = itemView.findViewById(R.id.amount);
            tvFoodPercentage = itemView.findViewById(R.id.tvFoodPercentage);
            progressFood = itemView.findViewById(R.id.progressFood);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            detailFood = itemView.findViewById(R.id.detailFood);
        }
    }
}