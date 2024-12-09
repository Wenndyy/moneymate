package com.example.moneymate.Model;

import java.util.Date;
import java.util.List;

public class SavingGoals {
    private String idSavingGoals;
    private String idCategory;
    private String idUser;
    private double amount;
    private Date created_at;
    private Date updated_at;
    private List<String> itemSavingGoals;


    public SavingGoals() {
    }

    public SavingGoals(String idSavingGoals, String idCategory, String idUser, double amount, Date created_at, Date updated_at, List<String> itemSavingGoals) {
        this.idSavingGoals = idSavingGoals;
        this.idCategory = idCategory;
        this.idUser = idUser;
        this.amount = amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.itemSavingGoals = itemSavingGoals;
    }

    public String getIdSavingGoals() {
        return idSavingGoals;
    }

    public void setIdSavingGoals(String idSavingGoals) {
        this.idSavingGoals = idSavingGoals;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public List<String> getItemSavingGoals() {
        return itemSavingGoals;
    }

    public void setItemSavingGoals(List<String> itemSavingGoals) {
        this.itemSavingGoals = itemSavingGoals;
    }
}
