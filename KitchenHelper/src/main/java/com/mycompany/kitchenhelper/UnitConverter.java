/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nikol
 */
public class UnitConverter implements Converter{
    private static final HashMap<String, Double> conversionTable = new HashMap<>(); //constant for kg, g, l, ml

    static {
        conversionTable.put("kg", 1000.0); // 1 kg = 1000 g
        conversionTable.put("g", 1.0); 
        conversionTable.put("l", 1000.0); // 1 l = 1000 ml
        conversionTable.put("ml", 1.0);
    }
    
    //convert unit to base (g, ml)
    @Override
    public Map.Entry<String, Double> convertToBaseUnit(String unit, double amount) {
        
    //checking units (if unit from recipe == with unit that we have in the table)
    if (conversionTable.containsKey(unit)) {
        String baseUnit = unit.equals("g") || unit.equals("kg") ? "g" : "ml"; //declearing base unit
        double baseAmount = amount * conversionTable.get(unit); //calculation of amount
        
        return Map.entry(baseUnit, baseAmount);
    }
    throw new IllegalArgumentException();//glass/cup or something else
    }
    
    //Convert to optimal unit
    @Override
    public Map.Entry<String, Double> convertToOptimalUnit(String unit, double baseAmount) {
        
        if (unit.equals("g") && baseAmount >= 1000) { //if for example 2000g --> change to 2kg for easier understanding 
            return Map.entry("kg", baseAmount / 1000.0);
        } else if (unit.equals("ml") && baseAmount >= 1000) {
            return Map.entry("l", baseAmount / 1000.0);//if for example 2000ml --> change to 2l for easier understanding 
        } else {
            return Map.entry(unit, baseAmount); //kg, l or something unexpected 
        }
        
    }

    @Override
    public Map.Entry<String, Double> addQuantities(String unit1, double amount1, String unit2, double amount2) {
        Map<String, Double> result = new HashMap<>();
        
        boolean canConvUnit1 = conversionTable.containsKey(unit1);
        boolean canConvUnit2 = conversionTable.containsKey(unit2);
        
         if (canConvUnit1 && canConvUnit2) {
        
            Map.Entry<String, Double> base1 = convertToBaseUnit(unit1, amount1);
            Map.Entry<String, Double> base2 = convertToBaseUnit(unit2, amount2);

            //is it same base unit?
            String baseUnit1 = base1.getKey();
            String baseUnit2 = base2.getKey();

            if (!baseUnit1.equals(baseUnit2)) {
                throw new IllegalArgumentException("Incompatible units of measurement: " + unit1 + " and " + unit2);
            }

            //adding 
            double totalAmount = base1.getValue() + base2.getValue();

            // converting if neccasery 
            return convertToOptimalUnit(baseUnit1, totalAmount);
         }
       
         /*Map<String, Double> result = new HashMap<>();
        
        //check for units (if they are standart or cups, mags e.t.c.) 
        boolean canConvertUnit1 = conversionTable.containsKey(unit1);
        boolean canConvertUnit2 = conversionTable.containsKey(unit2);
        
        if (canConvertUnit1 && canConvertUnit2) {
            
            double baseAmount1 = convertToBaseUnit(unit1, amount1);
            double baseAmount2 = convertToBaseUnit(unit2, amount2);

            
          
            result.put("g", baseAmount1 + baseAmount2); // Або будь-яка інша базова одиниця
        } else {
            result.put(unit1, amount1);
            result.put(unit2, amount2);
        }

        return result;*/
    }
    
}
