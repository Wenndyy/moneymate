package com.example.moneymate.Model;

import java.util.Date;
import java.util.List;

public class Budget {
    private String idBudget;
    private String idCategory;
    private String idUser;
    private double amount;
    private Date created_at;
    private Date updated_at;
    private List<String> itemBudget;

    public Budget() {
    }

    public Budget(String idBudget, String idCategory, String idUser, double amount, Date created_at, Date updated_at, List<String> itemBudget) {
        this.idBudget = idBudget;
        this.idCategory = idCategory;
        this.idUser = idUser;
        this.amount = amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.itemBudget = itemBudget;
    }

    public String getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(String idBudget) {
        this.idBudget = idBudget;
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

    public List<String> getItemBudget() {
        return itemBudget;
    }

    public void setItemBudget(List<String> itemBudget) {
        this.itemBudget = itemBudget;
    }
}
