package com.example.fooddemoapp.Models;

public class AppConfig {
    // Nombre de la Aplicación
    private String mAppName;

    // Estado de la Aplicación
    // 0 -> Apagada
    // 1 -> Prendida
    private int mAppStatus;

    // Cantidad de Pedidos
    private int mAppBudgetAmount;

    public AppConfig(){

    }

    public AppConfig(String mAppName, int mAppStatus, int mAppBudgetAmount) {
        this.mAppName = mAppName;
        this.mAppStatus = mAppStatus;
        this.mAppBudgetAmount = mAppBudgetAmount;
    }

    public String getmAppName() {
        return mAppName;
    }

    public int getmAppStatus() {
        return mAppStatus;
    }

    public int getmAppBudgetAmount() {
        return mAppBudgetAmount;
    }
}
