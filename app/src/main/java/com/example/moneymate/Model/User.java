package com.example.moneymate.Model;

import java.util.Date;

public abstract class User {
    private String idUser;
    private String email;
    private String fullname;
    private String noTelepon;
    private String password;
    private Date created_at;
    private Date updated_at;

    public User(String idUser, String email, String fullname, String noTelepon, String password, Date creted_at, Date updated_at){
        this.idUser = idUser;
        this.email = email;
        this.fullname = fullname;
        this.noTelepon = noTelepon;
        this.password = password;
        this.created_at = creted_at;
        this.updated_at = updated_at;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public abstract void login(String email, String password);
    public abstract void register(String email, String password);
    public abstract void logout();



}
