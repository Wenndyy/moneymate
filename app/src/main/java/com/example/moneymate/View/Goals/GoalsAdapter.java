package com.example.moneymate.View.Goals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>{
    private List<Map<String, Object>> goalsList;
    private Context context;
    public GoalsAdapter(Context context) {
        this.context = context;
        this.goalsList = new ArrayList<>();
    }

    public void setGoalsList(List<Map<String, Object>> goalsList) {
        this.goalsList = goalsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GoalsAdapter.GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new GoalsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {

        Map<String, Object> goalsItem = goalsList.get(position);

        String categoryId = (String) goalsItem.get("categoryId");
        String idGoals = (String) goalsItem.get("idGoals");
        String categoryName = (String) goalsItem.get("categoryName");
        String categoryImage = (String) goalsItem.get("categoryImage");
        double budgetAmount = (double) goalsItem.get("budgetAmount");
        double totalSpent = (double) goalsItem.get("totalSpent");
        double percentage = (double) goalsItem.get("percentage");

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

        // Update progress bar berdasarkan jumlah yang telah dibelanjakan
        holder.progressFood.setMax((int) budgetAmount);
        holder.progressFood.setProgress((int) totalSpent);
        holder.tvFoodPercentage.setText(String.format("%.1f%%", percentage));


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GoalsDetailActivity.class);
            intent.putExtra("categoryId",categoryId);
            intent.putExtra("idGoals",idGoals);
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
        return goalsList.size();
    }


    private String formatCurrency(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }

    class GoalsViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, amount, tvFoodPercentage;
        ProgressBar progressFood;
        ImageView categoryImage, detailFood;

        GoalsViewHolder(@NonNull View itemView) {
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
