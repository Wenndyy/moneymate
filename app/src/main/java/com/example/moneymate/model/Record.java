package com.example.moneymate.model;

import java.util.List;

public class Record {
    private String date;
    private List<Income> incomeList;

    public Record(String date, List<Income> incomeList) {
        this.date = date;
        this.incomeList = incomeList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }
}
