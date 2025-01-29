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
public class TimeConverter extends Converter<Time> {
    
    public TimeConverter() {
        this.conversionTable = new HashMap<>();
        conversionTable.put("seconds", 1.0 / 60);  // 1 sec = 1/60 minute
        conversionTable.put("minutes", 1.0);       // 1 minute
        conversionTable.put("hours", 60.0);        // 1 hour = 60 minutes
    }
     
    protected String normalizeUnit(String unit) {
        return switch (unit.toLowerCase()) {
            case "second" -> "seconds";
            case "minute" -> "minutes";
            case "hour" -> "hours";
            default -> unit;
        }; 
    }

    @Override
    protected Map.Entry<String, Double> convertToBaseUnit(Time time) {
        String unit = time.getUnit();
        double amount = time.getValue();
        
        String normalizedUnit = normalizeUnit(unit); 
        validateUnit(normalizedUnit); 
        
        double baseAmount = amount * conversionTable.get(unit); //calculation of amount
        
        return Map.entry("minutes", baseAmount);

    
    }

    @Override
    public Map.Entry<String, Double> convertToOptimalUnit(Time time) {
        double amount = convertToBaseUnit(time).getValue();

     
        if (amount >= 60) {
            return Map.entry("hours", amount / conversionTable.get("hours"));
        } else if (amount >= 1) {
            return Map.entry("minutes", amount);
        } else {
            return Map.entry("seconds", amount * 60); // less than 1 minute
        }
    }

    @Override
    public Map<String, Double> addQuantities(Time time1, Time time2) {
        Map<String, Double> result = new HashMap<>();
        
        Map.Entry<String, Double> base1 = convertToBaseUnit(time1);
        Map.Entry<String, Double> base2 = convertToBaseUnit(time2);

        //adding 
        double totalAmount = base1.getValue() + base2.getValue();
        Time  time = new Time(totalAmount, base1.getKey());
        // converting
        Map.Entry<String, Double> temp = convertToOptimalUnit(time);
        result.put(temp.getKey(), temp.getValue());
        return result;
        
    }
}
