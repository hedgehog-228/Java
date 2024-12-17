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
        Map<String, List<Ingredient>> combinedIngredients = new HashMap<>(); // map for processed recipe data ( adding same ingredients, scale and e.t.c.) 
        
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
                List<Ingredient> ingredientList = combinedIngredients.get(scaledIngredient.getName().toLowerCase());
                boolean added = false;
              //  Ingredient existingIngredient = combinedIngredients.get(scaledIngredient.getName().toLowerCase()); // take existing ingredient from combinedIngredients
              
                for (int i = 0; i < ingredientList.size(); i++) {
                    Ingredient existingIngredient = ingredientList.get(i);

                    // try to combine
                    Map<String, Double> combinedQuantities = converter.addQuantities(existingIngredient, scaledIngredient);

                    if (combinedQuantities.size() == 1) { // if able to combine 
                        for (Map.Entry<String, Double> entry : combinedQuantities.entrySet()) {
                            
                    // Updating data after addition
                            ingredientList.set(i, new Ingredient(
                                    scaledIngredient.getName(),
                                    entry.getValue(),
                                    entry.getKey()
                            ));
                        }
                        added = true;
                        break;
                    }
                }

                if (!added) { // if combining failed -> just add to the list 
                    ingredientList.add(scaledIngredient);
                }
            } else {
                // new ingredient in the group
                List<Ingredient> newList = new ArrayList<>();
                newList.add(scaledIngredient);
                combinedIngredients.put(scaledIngredient.getName().toLowerCase(), newList);
            }
        }

        // MAP BACK TO LIST OF INGREDIENTS 
        List<Ingredient> flattenedIngredients = new ArrayList<>();
        for (List<Ingredient> ingredientList : combinedIngredients.values()) {
            flattenedIngredients.addAll(ingredientList);
        }
        return flattenedIngredients;
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
             System.out.printf("- %s: %.1f %s %n", ingredient.getName(), ingredient.getQuantity(), "pieces".equals(ingredient.getUnit()) || "piece".equals(ingredient.getUnit()) ? "" : ingredient.getUnit());
        }
        
        
        for (Time stepTime : recipe.getTime()) { 
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