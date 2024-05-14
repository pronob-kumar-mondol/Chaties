package com.example.chaties.Model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phoneNumber;
    private String userName;
    private Timestamp createdAt;


    public UserModel() {
    }

    public UserModel(String phoneNumber, String userName, Timestamp createdAt) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
