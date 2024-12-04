package com.example.moneymate.Model;

import java.util.Date;

public class CategoryBudget extends CategoryExpense{

    public CategoryBudget() {
    }

    public CategoryBudget(String idCategoryBudget,String budgetCategoryName,String categoryBudgetImage,  Date created_at, Date updated_at) {
        super(idCategoryBudget, budgetCategoryName, categoryBudgetImage, created_at, updated_at);

    }


}
