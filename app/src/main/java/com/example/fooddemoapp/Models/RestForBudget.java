package com.example.fooddemoapp.Models;

public class RestForBudget {
    // Rest UID
    private String mRestId;

    // El Nombre del restaurante
    private String mRestName;

    public RestForBudget() {

    }

    public RestForBudget(String mRestId, String mRestName) {
        this.mRestId = mRestId;
        this.mRestName = mRestName;
    }

    public String getmRestId() {
        return mRestId;
    }

    public String getmRestName() {
        return mRestName;
    }
}
