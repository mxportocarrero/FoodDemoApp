package com.example.fooddemoapp.Models;

import com.example.fooddemoapp.Models.Food;

import java.util.HashMap;

public class Restaurant {
    // ID del restaraurante
    private String mRestId;

    // El Nombre del restaurante
    private String mRestTitle;

    // Una pequeñas descripcion del restaurante
    private String mRestDesc;

    // Un enlace html que redirige a la Firebase Storage
    private String mRestImgUrl;

    // Lista con todas las comidas que pueden hacerse en el Restaurant
    // Estoy recibiendo el hashmap de Firebase correspondiente a rest/Foods
    // Pero aun falta procesar si se quisiera tener las comidas en el MainActivity
    private HashMap<String, Food> Foods = new HashMap<>();

    public Restaurant(){

    }

    public Restaurant(String mRestId, String mRestTitle, String mRestDesc, String mRestImgUrl, HashMap<String, Food> foods) {
        this.mRestId = mRestId;
        this.mRestTitle = mRestTitle;
        this.mRestDesc = mRestDesc;
        this.mRestImgUrl = mRestImgUrl;
        Foods = foods;
    }

    public String getmRestId() {
        return mRestId;
    }

    public String getmRestTitle() {
        return mRestTitle;
    }

    public String getmRestDesc() {
        return mRestDesc;
    }

    public String getmRestImgUrl() {
        return mRestImgUrl;
    }

    public HashMap<String, Food> getFoods() {
        return Foods;
    }
}
