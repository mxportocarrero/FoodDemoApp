package com.example.fooddemoapp.Models;

public class FoodForBudget {
    // ID de la Comida
    // Este ID es unico y se replica a todas las referencias en la aplicacion
    private String foodId;

    // El Nombre del restaurante
    private String foodName;

    // Precio de la Comida
    private double foodUnitPrice;

    // Cantidad del Item
    private int foodQuantity;

    // Sub total
    private double foodSubTotal;

    public FoodForBudget(){

    }

    public FoodForBudget(String foodId, String foodName, double foodUnitPrice, int foodQuantity, double foodSubTotal) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodUnitPrice = foodUnitPrice;
        this.foodQuantity = foodQuantity;
        this.foodSubTotal = foodSubTotal;
    }

    public FoodForBudget(Food food, int quantity){
        this.foodId = food.getFoodId();
        this.foodName = food.getFoodName();
        this.foodUnitPrice = food.getFoodPrice();
        this.foodQuantity = quantity;
        this.foodSubTotal = (double) quantity * food.getFoodPrice();
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getFoodUnitPrice() {
        return foodUnitPrice;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public double getFoodSubTotal() {
        return foodSubTotal;
    }
}
