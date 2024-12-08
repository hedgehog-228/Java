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
public class IngredientConverter extends Converter<Ingredient>{
    
    //make constructor for initialization after creating an object IngredientConverter
    public IngredientConverter() {
        conversionTable = new HashMap<>();
        conversionTable.put("kg", 1000.0); // 1 kg = 1000 g
        conversionTable.put("g", 1.0);
        conversionTable.put("l", 1000.0); // 1 l = 1000 ml
        conversionTable.put("ml", 1.0);
    }
    
    //convert unit to base (g, ml)
    @Override
    protected Map.Entry<String, Double> convertToBaseUnit(Ingredient ingredient) {
        String unit = ingredient.getUnit();
        double quantity = ingredient.getQuantity();
        
        
        //checking unit (if unit from recipe == with unit that we have in the table)
        try {
            validateUnit(unit);
        } catch (IllegalArgumentException e) {
           return Map.entry(unit, quantity);//glass/cup or something else
        }

        String baseUnit = unit.equals("g") || unit.equals("kg") ? "g" : "ml"; //declearing base unit
        double baseAmount = quantity * conversionTable.get(unit); //calculation of amount

        return Map.entry(baseUnit, baseAmount);

    }
    
    //Convert to optimal unit
    @Override
    public Map.Entry<String, Double> convertToOptimalUnit(Ingredient ingredient) {
        
        String unit = ingredient.getUnit();
        double baseQuantity = ingredient.getQuantity();
        
        if (unit.equals("g") && baseQuantity >= 1000) { //if for example 2000g --> change to 2kg for easier understanding 
            return Map.entry("kg", baseQuantity / 1000.0);
        } else if (unit.equals("ml") && baseQuantity >= 1000) {
            return Map.entry("l", baseQuantity / 1000.0);//if for example 2000ml --> change to 2l for easier understanding 
        } else {
            return Map.entry(unit, baseQuantity); //kg, l or something unexpected 
        }
        
    }
//CHECK IF INGREDIENTS HAVE THE SAME NAME
    @Override
    public Map<String, Double> addQuantities(Ingredient ingredient1, Ingredient ingredient2) {
        Map<String, Double> result = new HashMap<>();
        String unit1 = ingredient1.getUnit();
        String unit2 = ingredient2.getUnit();
        
        if(!ingredient2.getName().equals(ingredient1.getName())){
            throw new IllegalArgumentException("It is not allowed to add quantities of two different ingredients");
        }
         try {
        validateUnit(unit1);
        validateUnit(unit2);

        Map.Entry<String, Double> base1 = convertToBaseUnit(ingredient1);
        Map.Entry<String, Double> base2 = convertToBaseUnit(ingredient2);

        //is it same base unit?
        String baseUnit1 = base1.getKey();
        String baseUnit2 = base2.getKey();

        if (!baseUnit1.equals(baseUnit2)) {
            System.out.println("Incompatible units of measurement: " + unit1 + " and " + unit2); //if units are different and valid (in the table) 
            return Map.of();
        }

        //adding 
        double totalAmount = base1.getValue() + base2.getValue();
        Ingredient ingredient = new Ingredient(ingredient1.getName(), totalAmount, baseUnit1);
        // converting
        Map.Entry<String, Double> temp = convertToOptimalUnit(ingredient);
        result.put(temp.getKey(), temp.getValue());
        return result;
         } catch (IllegalArgumentException e){
             // if units are not declared then just putting it in the list
                  result.put(unit1, ingredient1.getQuantity());
                  result.put(unit2, ingredient2.getQuantity());
                  return result;
         }
    }
    
}
