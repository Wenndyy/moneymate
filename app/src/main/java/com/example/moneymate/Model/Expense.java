package com.example.moneymate.Model;

import java.util.Date;

public class Expense {
    private String idExpense;
    private String idCategoryExpense;
    private String idUser;
    private double amount;
    private Date dateOfExpense;
    private Date created_at;
    private Date updated_at;

    public Expense() {
    }

    public Expense(String idExpense, String idCategoryExpense, String idUser, double amount, Date dateOfExpense, Date created_at, Date updated_at) {
        this.idExpense = idExpense;
        this.idCategoryExpense = idCategoryExpense;
        this.idUser = idUser;
        this.amount = amount;
        this.dateOfExpense = dateOfExpense;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(String idExpense) {
        this.idExpense = idExpense;
    }

    public String getIdCategoryExpense() {
        return idCategoryExpense;
    }

    public void setIdCategoryExpense(String idCategoryExpense) {
        this.idCategoryExpense = idCategoryExpense;
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

    public Date getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(Date dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
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
