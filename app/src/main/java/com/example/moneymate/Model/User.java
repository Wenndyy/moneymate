package com.example.moneymate.Model;

import java.util.Date;

public abstract class User {
    private String idUser;
    private String email;
    private String fullname;
    private String noTelepon;

    private Date created_at;
    private Date updated_at;

    public User(String idUser, String email, String fullname, String noTelepon, Date created_at, Date updated_at){
        this.idUser = idUser;
        this.email = email;
        this.fullname = fullname;
        this.noTelepon = noTelepon;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String getFullname() {
        return fullname;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public abstract void login(String email, String password);
    public abstract void register(String email, String password);
    public abstract void logout();



}
