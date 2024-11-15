package com.example.moneymate.model;

import java.util.Date;

public abstract class User {
    private int idUser;
    private String email;
    private String fullname;
    private String noTelepon;
    private String password;
    private Date created_at;
    private Date updated_at;

    public User(int idUser, String email, String fullname, String noTelepon, String password, Date creted_at, Date updated_at){
        this.idUser = idUser;
        this.email = email;
        this.fullname = fullname;
        this.noTelepon = noTelepon;
        this.password = password;
        this.created_at = creted_at;
        this.updated_at = updated_at;
    }

    public int getIdUser() {
        return idUser;
    }
    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }


    public String getNoTelepon() {
        return noTelepon;
    }
    public String getPassword() {
        return password;
    }


    public abstract void login(String email, String password);
    public abstract void register(String email, String password);
    public abstract void logout();



}
