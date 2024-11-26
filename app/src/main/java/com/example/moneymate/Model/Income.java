package com.example.moneymate.Model;

import java.util.Date;

public  class Income {
    private String idIncome;
    private String idCategoryIncome;
    private String idUser;
    private double amount;
    private Date dateOfIncome;
    private Date created_at;
    private Date updated_at;

    public Income(){}
    public Income(String idIncome,String idCategoryIncome, String idUser, double amount,Date dateOfIncome, Date created_at, Date updated_at){
        this.idIncome = idIncome;
        this.idCategoryIncome = idCategoryIncome;
        this.idUser = idUser;
        this.amount = amount ;
        this.dateOfIncome = dateOfIncome;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    public String getIdIncome() {
        return idIncome;
    }

    public void setIdIncome(String idIncome) {
        this.idIncome = idIncome;
    }

    public String getIdCategoryIncome() {
        return idCategoryIncome;
    }

    public void setIdCategoryIncome(String idCategoryIncome) {
        this.idCategoryIncome = idCategoryIncome;
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


    public Date getDateOfIncome() {
        return dateOfIncome;
    }

    public void setDateOfIncome(Date dateOfIncome) {
        this.dateOfIncome = dateOfIncome;
    }
}
