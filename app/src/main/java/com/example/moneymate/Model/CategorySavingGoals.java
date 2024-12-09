package com.example.moneymate.Model;

import java.util.Date;

public class CategorySavingGoals {
    private String idGoalsCategory;
    private String goalsCategoryName;
    private String categoryGoalsImage;
    private Date created_at;
    private Date updated_at;

    public CategorySavingGoals() {
    }

    public CategorySavingGoals(String idGoalsCategory, String goalsCategoryName, String categoryGoalsImage, Date created_at, Date updated_at) {
        this.idGoalsCategory = idGoalsCategory;
        this.goalsCategoryName = goalsCategoryName;
        this.categoryGoalsImage = categoryGoalsImage;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getIdGoalsCategory() {
        return idGoalsCategory;
    }

    public void setIdGoalsCategory(String idGoalsCategory) {
        this.idGoalsCategory = idGoalsCategory;
    }

    public String getGoalsCategoryName() {
        return goalsCategoryName;
    }

    public void setGoalsCategoryName(String goalsCategoryName) {
        this.goalsCategoryName = goalsCategoryName;
    }

    public String getCategoryGoalsImage() {
        return categoryGoalsImage;
    }

    public void setCategoryGoalsImage(String categoryGoalsImage) {
        this.categoryGoalsImage = categoryGoalsImage;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
