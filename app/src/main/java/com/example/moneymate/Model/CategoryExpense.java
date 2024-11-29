package com.example.moneymate.Model;

import java.util.Date;

public class CategoryExpense {
    private String idCategoryExpense;
    private String ExpenseCategoryName;
    private String categoryExpenseImage;
    private Date created_at;
    private Date updated_at;

    public CategoryExpense() {
    }

    public CategoryExpense(String idCategoryExpense, String expenseCategoryName, String categoryExpenseImage, Date created_at, Date updated_at) {
        this.idCategoryExpense = idCategoryExpense;
        ExpenseCategoryName = expenseCategoryName;
        this.categoryExpenseImage = categoryExpenseImage;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getIdCategoryExpense() {
        return idCategoryExpense;
    }

    public void setIdCategoryExpense(String idCategoryExpense) {
        this.idCategoryExpense = idCategoryExpense;
    }

    public String getExpenseCategoryName() {
        return ExpenseCategoryName;
    }

    public void setExpenseCategoryName(String expenseCategoryName) {
        ExpenseCategoryName = expenseCategoryName;
    }

    public String getCategoryExpenseImage() {
        return categoryExpenseImage;
    }

    public void setCategoryExpenseImage(String categoryExpenseImage) {
        this.categoryExpenseImage = categoryExpenseImage;
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
