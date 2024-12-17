/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;


import java.util.HashMap;
import java.util.Map;

/**
 *Converter for ingredients 
 *
 */
public class IngredientConverter extends Converter<Ingredient>{
    
    //make constructor for initialization after creating an object IngredientConverter
    public IngredientConverter() {
        this.conversionTable = new HashMap<>();
        conversionTable.put("kg", 1000.0); // 1 kg = 1000 g
        conversionTable.put("gr", 1.0);
        conversionTable.put("l", 1000.0); // 1 l = 1000 ml
        conversionTable.put("ml", 1.0);
        conversionTable.put("pieces", 1.0);
    }
    
    //for being sure that if we have 1 piece my methods will read it correctly 
    protected String normalizeUnit(String unit){
        return switch (unit.toLowerCase()){
            case "piece" -> "pieces";
            default -> unit;
        };
    }
    
    //convert unit to base (gr, ml)
    @Override
    protected Map.Entry<String, Double> convertToBaseUnit(Ingredient ingredient) {
        double quantity = ingredient.getQuantity();
        String normalizedUnit = normalizeUnit(ingredient.getUnit()); 
        
        //checking unit (if unit from recipe == unit that we have in the table)
        try {
            validateUnit(normalizedUnit);
        } catch (IllegalArgumentException e) {
           return Map.entry(normalizedUnit, quantity);//glass/cup or something else
        }

        String baseUnit = normalizedUnit.equals("pieces") ? "pieces" : (normalizedUnit.equals("gr") || normalizedUnit.equals("kg")? "gr" : "ml"); //declearing base unit
        double baseAmount = quantity * conversionTable.get(normalizedUnit); //calculation of amount

        return Map.entry(baseUnit, baseAmount);

    }
    
    //Convert to optimal unit
    @Override
    public Map.Entry<String, Double> convertToOptimalUnit(Ingredient ingredient) {
        
        String unit = ingredient.getUnit();
        double baseQuantity = ingredient.getQuantity();
        
        if (unit.equals("gr") && baseQuantity >= 1000) { //if for example 2000gr --> change to 2kg for easier understanding 
            return Map.entry("kg", baseQuantity / 1000.0);
        } else if (unit.equals("ml") && baseQuantity >= 1000) {
            return Map.entry("l", baseQuantity / 1000.0);//if for example 2000ml --> change to 2l for easier understanding 
        } else {
            return Map.entry(unit, baseQuantity); //kg, l, pieces or something unexpected 
        }
        
    }
//CHECK IF INGREDIENTS HAVE THE SAME NAME
    @Override
    public Map<String, Double> addQuantities(Ingredient ingredient1, Ingredient ingredient2){
        Map<String, Double> result = new HashMap<>();
        String unit1 = normalizeUnit(ingredient1.getUnit());
        String unit2 =  normalizeUnit(ingredient2.getUnit());
        
        if(!ingredient2.getName().equalsIgnoreCase(ingredient1.getName())){
            throw new IllegalArgumentException("It is not allowed to add quantities of two different ingredients");
        }
         try {
             
            //important part for understanding if unit is standart or not, because in convertToBaseUnit we are catching exception and not getting helpfull information 
            validateUnit(unit1);
            validateUnit(unit2);

            //CONVERTING to base unit
            Map.Entry<String, Double> base1 = convertToBaseUnit(ingredient1);
            Map.Entry<String, Double> base2 = convertToBaseUnit(ingredient2);

            //CHECK IF SAME UNIT OR NOT
            String baseUnit1 = base1.getKey();
            String baseUnit2 = base2.getKey();

            if (!baseUnit1.equalsIgnoreCase(baseUnit2)) {
                throw new IllegalArgumentException("Incompatible units of measurement: " + unit1 + " and " + unit2);
            }

            //adding 
            double totalAmount = base1.getValue() + base2.getValue();
            Ingredient ingredient = new Ingredient(ingredient1.getName(), totalAmount, baseUnit1); //may add low case or something else for beautifull output  
            
            
            // converting
            Map.Entry<String, Double> temp = convertToOptimalUnit(ingredient);
            result.put(temp.getKey(), temp.getValue());
            return result;
         } catch (IllegalArgumentException e){
             
                // if units are not declared but the same then 
                if (unit1.equalsIgnoreCase(unit2)) {
                    double totalAmount = ingredient1.getQuantity() + ingredient2.getQuantity();
                    result.put(unit1, totalAmount);
               } else {
                result.put(unit1, ingredient1.getQuantity());
                result.put(unit2, ingredient2.getQuantity());
                }
                return result;
         }
    }
    
}
