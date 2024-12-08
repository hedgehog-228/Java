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
    protected Map<String, Double> conversionTable;
    
    protected abstract Map.Entry<String, Double> convertToBaseUnit(T object);

    public abstract Map.Entry<String, Double> convertToOptimalUnit(T object);

    public abstract Map<String, Double> addQuantities(T object1, T object2);
    
    protected boolean validateUnit(String unit) {
        if (!conversionTable.containsKey(unit)) {
            throw new IllegalArgumentException("Unit '" + unit + "' is not in the conversion table.");
        }
        return true;
    }

}
