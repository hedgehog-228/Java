/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.kitchenhelper;

import java.util.Map;

/**
 *
 * @author Nikol
 */
public interface Converter {
    
    public Map.Entry<String, Double> convertToBaseUnit(String unit, double amount);

    Map.Entry<String, Double> convertToOptimalUnit(String unit, double baseAmount);

    Map.Entry<String, Double> addQuantities(String unit1, double amount1, String unit2, double amount2);
}
