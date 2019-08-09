package com.example.fooddemoapp.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Budget {
    // Budget Unique ID
    private String mBudgetId;

    // Id del restaurante
    private RestForBudget mBudgetRestInfo;

    // Datos del Usuario
    private CustomUser mBudgetUserInfo;

    // Budget Delivery Address
    private String mBudgetDeliveryAddress;

    // Budget Delivery Phone
    private String mBudgetDeliveryPhone;

    // Budget Additional Info
    private String mBudgetAdditionalInfo;

    // Budget Timestamp
    private Map mBudgetTimestamp;

    // Budget State
    // Aqui pondremos el estado del pedido
    /*
    new -> Pedido Nuevo, Registrado en la Base de Datos
    confirmed -> Pedido Confirmado, Visto por el administrador y confirmado para su preparacion y envio
    canceled -> Pedido Cancelado, Cuando ya se pago por el servicio y este se da de alta en la base de datos
    finished -> Pedido Realizado, Cuando ya se pago por el servicio y este se da de alta en la base de datos */
    private String mBudgetState;

    // Valor total del pedido
    private double mBudgetTotalPrice;

    // Lista de las Comidas
    private List<FoodForBudget> mBudgetFoodList;


    public Budget(){

    }

    public Budget(String mBudgetId, RestForBudget mBudgetRestInfo, CustomUser mBudgetUserInfo, String mBudgetDeliveryAddress, String mBudgetDeliveryPhone, String mBudgetAdditionalInfo, Map mBudgetTimestamp, String mBudgetState, double mBudgetTotalPrice, List<FoodForBudget> mBudgetFoodList) {
        this.mBudgetId = mBudgetId;
        this.mBudgetRestInfo = mBudgetRestInfo;
        this.mBudgetUserInfo = mBudgetUserInfo;
        this.mBudgetDeliveryAddress = mBudgetDeliveryAddress;
        this.mBudgetDeliveryPhone = mBudgetDeliveryPhone;
        this.mBudgetAdditionalInfo = mBudgetAdditionalInfo;
        this.mBudgetTimestamp = mBudgetTimestamp;
        this.mBudgetState = mBudgetState;
        this.mBudgetTotalPrice = mBudgetTotalPrice;
        this.mBudgetFoodList = mBudgetFoodList;
    }

    public String getmBudgetId() {
        return mBudgetId;
    }

    public RestForBudget getmBudgetRestInfo() {
        return mBudgetRestInfo;
    }

    public CustomUser getmBudgetUserInfo() {
        return mBudgetUserInfo;
    }

    public String getmBudgetDeliveryAddress() {
        return mBudgetDeliveryAddress;
    }

    public String getmBudgetDeliveryPhone() {
        return mBudgetDeliveryPhone;
    }

    public String getmBudgetAdditionalInfo() {
        return mBudgetAdditionalInfo;
    }

    public Map getmBudgetTimestamp() {
        return mBudgetTimestamp;
    }

    public String getmBudgetState() {
        return mBudgetState;
    }

    public double getmBudgetTotalPrice() {
        return mBudgetTotalPrice;
    }

    public List<FoodForBudget> getmBudgetFoodList() {
        return mBudgetFoodList;
    }

} // End of Budget Class
