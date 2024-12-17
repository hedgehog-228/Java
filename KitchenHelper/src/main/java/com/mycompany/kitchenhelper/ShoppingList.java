/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * shopping list class that generates a consolidated list of ingredients 
 * required for multiple recipes.
 */
public class ShoppingList {
    private Map<String, List<Ingredient>> groupedIngredients; // Назва -> список інгредієнтів
    private IngredientConverter converter;

    // Constructor
    public ShoppingList() {
        this.groupedIngredients = new HashMap<>();
        this.converter = new IngredientConverter();
    }

    // Add a recipe to the shopping list
    public void addRecipe(Recipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            String ingredientName = ingredient.getName().toLowerCase();

            // if the ingridient has been added check if may be added
            if (groupedIngredients.containsKey(ingredientName)) {
                List<Ingredient> ingredientList = groupedIngredients.get(ingredientName);
                boolean added = false;

                for (int i = 0; i < ingredientList.size(); i++) {
                    Ingredient existingIngredient = ingredientList.get(i);

                    // try to combine
                    Map<String, Double> combinedQuantities = converter.addQuantities(existingIngredient, ingredient);

                    if (combinedQuantities.size() == 1) { // if able to combine 
                        for (Map.Entry<String, Double> entry : combinedQuantities.entrySet()) {
                            ingredientList.set(i, new Ingredient(
                                    existingIngredient.getName(),
                                    entry.getValue(),
                                    entry.getKey()
                            ));
                        }
                        added = true;
                        break;
                    }
                }

                if (!added) { // if combining failed -> just add to the list 
                    ingredientList.add(ingredient);
                }
            } else {
                // new ingredient in the group
                List<Ingredient> newList = new ArrayList<>();
                newList.add(ingredient);
                groupedIngredients.put(ingredientName, newList);
            }
        }
    }

    // Print the shopping list
    public void printShoppingList() {
        System.out.println("Shopping List:");
        for (Map.Entry<String, List<Ingredient>> entry : groupedIngredients.entrySet()) {
            for (Ingredient ingredient : entry.getValue()) {
                System.out.printf("- %s: %.2f %s%n", 
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    "pieces".equals(ingredient.getUnit()) || "piece".equals(ingredient.getUnit()) ? "" : ingredient.getUnit());
            }
        }
    }
}
