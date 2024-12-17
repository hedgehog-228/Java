package com.mycompany.kitchenhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayRecipe {
    private Recipe recipe;
    private int people;

    //CONSTRUCTOR
    public DisplayRecipe(Recipe recipe, int people) {
        if (people < 1) {
            throw new IllegalArgumentException("Number of people should be at least 1.");
        }
        this.recipe = recipe;
        this.people = people;
    }
    
  // PRINT RECIPE
    public void printRecipe() {
        System.out.println("Recipe scaled for " + people + " people:");
        List<Ingredient> scaledIngredients = scaleAndCombineIngredients(recipe.getIngredients(), people);
        printScaledRecipe(scaledIngredients);
    }

    // SCALE AND COMBINE INGREDIENTS
    public List<Ingredient> scaleAndCombineIngredients(List<Ingredient> ingredients, int people) {
        IngredientConverter converter = new IngredientConverter();
        Map<String, Ingredient> combinedIngredients = new HashMap<>(); // map for processed recipe data ( adding same ingredients, scale and e.t.c.) 
        
        //START LOOP INGREDIENTS
        for (Ingredient ingredient : ingredients) {
            // Scaling of quantitY
            Ingredient scaledIngredient = new Ingredient(
                    ingredient.getName(),
                    ingredient.getQuantity() * people,
                    ingredient.getUnit()
            );

            // if ingredients is exist in the map combinedIngredients then adding quantities 
            if (combinedIngredients.containsKey(scaledIngredient.getName().toLowerCase())) {
                Ingredient existingIngredient = combinedIngredients.get(scaledIngredient.getName().toLowerCase()); // take existing ingredient from combinedIngredients
                Map<String, Double> combinedQuantities = converter.addQuantities(existingIngredient, scaledIngredient);

                // Updating data after addition
                for (Map.Entry<String, Double> entry : combinedQuantities.entrySet()) {
                    combinedIngredients.put(scaledIngredient.getName().toLowerCase(),
                            new Ingredient(scaledIngredient.getName(), entry.getValue(), entry.getKey()));
                }
            } else {
                // new ingridient to map 
                combinedIngredients.put(scaledIngredient.getName().toLowerCase(), scaledIngredient);
            }
        }

        // MAP BACK TO LIST OF INGREDIENTS 
        return new ArrayList<>(combinedIngredients.values());
    }

    // PRINT SCALED RECIPE
    private void printScaledRecipe(List<Ingredient> scaledIngredients) {
        TimeConverter timeConverter = new TimeConverter(); // for total time
        Time totalTime = new Time(0, "minutes");
        
        System.out.println("Σκευη:");
        for (String utensil : recipe.getUtensils()) {
            System.out.printf("- %s  %n", utensil);
        }

        System.out.println("Υλικα:");
        for (Ingredient ingredient : scaledIngredients) {
             System.out.printf("- %s: %.1f %s %n", ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit());
        }
        
        
        for (Time stepTime : recipe.getTime()) { // Припускаємо, що метод getStepTimes() повертає час для кожного кроку
        totalTime = timeConverter.addQuantities(totalTime, stepTime)
                                .entrySet()
                                .stream()
                                .map(entry -> new Time(entry.getValue(), entry.getKey()))
                                .findFirst()
                                .orElse(totalTime);
        }

        // convert to optimal 
        Map.Entry<String, Double> optimalTime = timeConverter.convertToOptimalUnit(totalTime);

        // printing result of total time
        System.out.printf("\nΣυνολικη ωρα: %.1f %s%n", optimalTime.getValue(), optimalTime.getKey());
        
        System.out.println("\nΒηματα:");
        int stepIndex = 1;
        for (String step : recipe.getSteps()) {
            System.out.printf("%d. %s%n", stepIndex++, step);
        }
    }
}