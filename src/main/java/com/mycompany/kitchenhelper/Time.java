/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;

/**
 *
 * @author Nikol
 */
public class Time {
    private double value;
    private String unit;
    public Time(double value, String unit){
        if (value < 0) {
            throw new IllegalArgumentException("Time value cannot be negative");
        }
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
    
    
}
