package com.example.fooddemoapp.Models;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Food implements Serializable {
    // ID de la Comida
    // Este ID es unico y se replica a todas las referencias en la aplicacion
    private String foodId;

    // El Nombre del restaurante
    private String foodName;

    // Una pequeñas descripcion del restaurante
    private String foodDesc;

    // Un enlace html que redirige a la Firebase Storage
    private String foodImgUrl;

    // Precio de la Comida
    private double foodPrice;

    // Disponibilidad de la Comida
    private Boolean foodAvailability;

    public Food(){

    }

    public Food(String foodId, String foodName, String foodDesc, String foodImgUrl, double foodPrice, Boolean foodAvailability) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodImgUrl = foodImgUrl;
        this.foodPrice = foodPrice;
        this.foodAvailability = foodAvailability;
    }

    // Public Getters


    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public String getFoodImgUrl() {
        return foodImgUrl;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public Boolean getFoodAvailability() {
        return foodAvailability;
    }

} // Class End Food
