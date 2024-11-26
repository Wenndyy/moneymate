package com.example.moneymate.Model;

import java.util.Date;

public class CategoryIncome {
    private String idCategoryIncome;
    private String IncomeCategoryName;
    private String categoryIncomeImage;
    private Date created_at;
    private Date updated_at;

    public CategoryIncome() {
    }

    public CategoryIncome(String idCategoryIncome, String incomeCategoryName, String categoryIncomeImage, Date created_at, Date updated_at) {
        this.idCategoryIncome = idCategoryIncome;
        this.IncomeCategoryName = incomeCategoryName;
        this.categoryIncomeImage = categoryIncomeImage;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String getIdCategoryIncome() {
        return idCategoryIncome;
    }

    public void setIdCategoryIncome(String idCategoryIncome) {
        this.idCategoryIncome = idCategoryIncome;
    }

    public String getIncomeCategoryName() {
        return IncomeCategoryName;
    }

    public void setIncomeCategoryName(String incomeCategoryName) {
        this.IncomeCategoryName = incomeCategoryName;
    }

    public String getCategoryIncomeImage() {
        return categoryIncomeImage;
    }

    public void setCategoryIncomeImage(String categoryIncomeImage) {
        this.categoryIncomeImage = categoryIncomeImage;
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
