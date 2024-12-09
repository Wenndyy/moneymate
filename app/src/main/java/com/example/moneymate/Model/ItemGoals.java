package com.example.moneymate.Model;

import java.util.Date;

public class ItemGoals {
    private String idItemGoals;
    private String idCategoryGoals;
    private String idUser;
    private double amount;
    private Date dateOfGoals;
    private Date created_at;
    private Date updated_at;

    public ItemGoals() {
    }

    public ItemGoals(String idItemGoals, String idCategoryGoals, String idUser, double amount, Date dateOfGoals, Date created_at, Date updated_at) {
        this.idItemGoals = idItemGoals;
        this.idCategoryGoals = idCategoryGoals;
        this.idUser = idUser;
        this.amount = amount;
        this.dateOfGoals = dateOfGoals;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getIdItemGoals() {
        return idItemGoals;
    }

    public void setIdItemGoals(String idItemGoals) {
        this.idItemGoals = idItemGoals;
    }

    public String getIdCategoryGoals() {
        return idCategoryGoals;
    }

    public void setIdCategoryGoals(String idCategoryGoals) {
        this.idCategoryGoals = idCategoryGoals;
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

    public Date getDateOfGoals() {
        return dateOfGoals;
    }

    public void setDateOfGoals(Date dateOfGoals) {
        this.dateOfGoals = dateOfGoals;
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
