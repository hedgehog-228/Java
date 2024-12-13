/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;

/**
 *
 * @author Nikol
 */
public class Ingredient {
    private String name;
    private double quantity;
    private String unit;
    
    public Ingredient(String name, double quantity, String unit){
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
    
    
    
@Override
    public String toString() {
        if (quantity % 1 == 0) {
            return String.format("%d %s", (int) quantity, unit);
        } else {
            return String.format("%.2f %s", quantity, unit);
        }
    }
}
