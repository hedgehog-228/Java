/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.kitchenhelper;

import java.util.Map;

/**
 * abstract class Converter
 * 
 */
public abstract class Converter<T> {
    protected Map<String, Double> conversionTable; //table with default values

    protected abstract Map.Entry<String, Double> convertToBaseUnit(T object); // converter to base unit 
    
    public abstract Map.Entry<String, Double> convertToOptimalUnit(T object); // converter to optimal unit (1000 g --> kg)

    public abstract Map<String, Double> addQuantities(T object1, T object2); // find teh sum of ingredients
    
    protected boolean validateUnit(String unit) { //check unit (if it is in the table)
        if (!conversionTable.containsKey(unit)) {
            throw new IllegalArgumentException("Unit '" + unit + "' is not in the conversion table.");
        }
        return true;
    }

}
