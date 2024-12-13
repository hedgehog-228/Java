/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.kitchenhelper;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientConverterTest {

    private final IngredientConverter ingredientConverter = new IngredientConverter();

    @Test
    public void testConvertToBaseUnitValidUnit() {
        Ingredient ingredient = new Ingredient("Flour", 2.5, "kg");
        Map.Entry<String, Double> result = ingredientConverter.convertToBaseUnit(ingredient);

        assertEquals("g", result.getKey());
        assertEquals(2500.0, result.getValue()); // 2.5 kg = 2500 g
    }

    @Test
    public void testConvertToBaseUnitInvalidUnit() {
        Ingredient ingredient = new Ingredient("Flour", 1, "cup");
        Map.Entry<String, Double> result = ingredientConverter.convertToBaseUnit(ingredient);

        assertEquals("cup", result.getKey()); 
        assertEquals(1.0, result.getValue()); 
    }

    @Test
    public void testConvertToOptimalUnitGramsToKilograms() {
        Ingredient ingredient = new Ingredient("Sugar", 1500, "g");
        Map.Entry<String, Double> result = ingredientConverter.convertToOptimalUnit(ingredient);

        assertEquals("kg", result.getKey());
        assertEquals(1.5, result.getValue()); 
    }

    @Test
    public void testConvertToOptimalUnitMillilitersToLiters() {
        Ingredient ingredient = new Ingredient("Milk", 2000, "ml");
        Map.Entry<String, Double> result = ingredientConverter.convertToOptimalUnit(ingredient);

        assertEquals("l", result.getKey()); 
        assertEquals(2.0, result.getValue());
    }

    @Test
    public void testConvertToOptimalUnitNoConversionNeeded() {
        Ingredient ingredient = new Ingredient("Flour", 500, "g");
        Map.Entry<String, Double> result = ingredientConverter.convertToOptimalUnit(ingredient);

        assertEquals("g", result.getKey());
        assertEquals(500.0, result.getValue()); 
    }

    @Test
    public void testAddQuantitiesSameUnits() {
        Ingredient ingredient1 = new Ingredient("Sugar", 500, "g");
        Ingredient ingredient2 = new Ingredient("Sugar", 1500, "g");

        Map<String, Double> result = ingredientConverter.addQuantities(ingredient1, ingredient2);

        assertTrue(result.containsKey("kg"));
        assertEquals(2.0, result.get("kg")); // 500 g + 1500 g = 2 kg
    }

    @Test
    public void testAddQuantitiesDifferentUnits() {
        Ingredient ingredient1 = new Ingredient("Milk", 1, "l");
        Ingredient ingredient2 = new Ingredient("Milk", 500, "ml");

        Map<String, Double> result = ingredientConverter.addQuantities(ingredient1, ingredient2);

        assertTrue(result.containsKey("l"));
        assertEquals(1.5, result.get("l")); // 1 l + 500 ml = 1.5 l
    }

    @Test
    public void testAddQuantitiesIncompatibleUnits() {
        Ingredient ingredient1 = new Ingredient("Sugar", 500, "g");
        Ingredient ingredient2 = new Ingredient("Sugar", 1, "cup");

        Map<String, Double> result = ingredientConverter.addQuantities(ingredient1, ingredient2);

        assertTrue(result.containsKey("g")); 
        assertTrue(result.containsKey("cup"));
        assertEquals(500.0, result.get("g")); 
        assertEquals(1.0, result.get("cup"));
    }

    @Test
    public void testAddQuantitiesDifferentIngredients() {
        Ingredient ingredient1 = new Ingredient("Sugar", 500, "g");
        Ingredient ingredient2 = new Ingredient("Flour", 500, "g");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ingredientConverter.addQuantities(ingredient1, ingredient2));

        assertEquals("It is not allowed to add quantities of two different ingredients", exception.getMessage());
    }
}


