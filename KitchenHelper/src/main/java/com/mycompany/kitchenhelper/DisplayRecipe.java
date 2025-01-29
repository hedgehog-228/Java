package com.mycompany.kitchenhelper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayRecipe {
    private Recipe recipe;
    private int people;

    // CONSTRUCTOR
    public DisplayRecipe(Recipe recipe, int people) {
        if (people < 1) {
            throw new IllegalArgumentException("Number of people should be at least 1.");
        }
        this.recipe = recipe;
        this.people = people;
    }

    // PRINT RECIPE TO TEXT AREA
    public void printRecipe(JTextArea textArea) {
        textArea.setText("");
        textArea.append("Recipe scaled for " + people + " people:\n\n");

        List<Ingredient> scaledIngredients = scaleAndCombineIngredients(recipe.getIngredients(), people);
        printScaledRecipe(scaledIngredients, textArea);
    }
    
    // PRINT STEP BY STEP FOR RECIPE EXECUTING
    public void printStepByStep(JTextArea textArea) {
        textArea.setText("");
        textArea.append("Recipe scaled for " + people + " people:\n\n");
        
        List<Ingredient> scaledIngredients = scaleAndCombineIngredients(recipe.getIngredients(), people);
        printScaledSteps(scaledIngredients, textArea);
    }

    // SCALE AND COMBINE INGREDIENTS
    public List<Ingredient> scaleAndCombineIngredients(List<Ingredient> ingredients, int people) {
        IngredientConverter converter = new IngredientConverter();
        Map<String, List<Ingredient>> combinedIngredients = new HashMap<>();

        // START LOOP INGREDIENTS
        for (Ingredient ingredient : ingredients) {
            // Scaling quantity
            Ingredient scaledIngredient = new Ingredient(
                    ingredient.getName(),
                    ingredient.getQuantity() * people,
                    ingredient.getUnit()
            );

            // combine ingredients
            if (combinedIngredients.containsKey(scaledIngredient.getName().toLowerCase())) {
                List<Ingredient> ingredientList = combinedIngredients.get(scaledIngredient.getName().toLowerCase());
                boolean added = false;

                for (int i = 0; i < ingredientList.size(); i++) {
                    Ingredient existingIngredient = ingredientList.get(i);
                    Map<String, Double> combinedQuantities = converter.addQuantities(existingIngredient, scaledIngredient);

                    if (combinedQuantities.size() == 1) {
                        for (Map.Entry<String, Double> entry : combinedQuantities.entrySet()) {
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

                if (!added) {
                    ingredientList.add(scaledIngredient);
                }
            } else {
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

    // PRINT SCALED RECIPE TO TEXT AREA(GUI)
    private void printScaledRecipe(List<Ingredient> scaledIngredients, JTextArea textArea) {
        TimeConverter timeConverter = new TimeConverter();
        Time totalTime = new Time(0, "minutes");

        textArea.append("Συσκευές:\n");
        for (String utensil : recipe.getUtensils()) {
            textArea.append("- " + utensil + "\n");
        }

        textArea.append("\nΥλικά:\n");
        for (Ingredient ingredient : scaledIngredients) {
            textArea.append(String.format("- %s: %.1f %s\n",
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    "pieces".equals(ingredient.getUnit()) || "piece".equals(ingredient.getUnit()) ? "" : ingredient.getUnit()));
        }

        for (Time stepTime : recipe.getTime()) {
             if (stepTime == null) {
                continue;
            }
            totalTime = timeConverter.addQuantities(totalTime, stepTime)
                    .entrySet()
                    .stream()
                    .map(entry -> new Time(entry.getValue(), entry.getKey()))
                    .findFirst()
                    .orElse(totalTime);
        }

        Map.Entry<String, Double> optimalTime = timeConverter.convertToOptimalUnit(totalTime);
        textArea.append(String.format("\nΣυνολικός χρόνος: %.1f %s\n", optimalTime.getValue(), optimalTime.getKey()));

        textArea.append("\nΒήματα:\n");
        int stepIndex = 1;
        for (String step : recipe.getSteps()) {
            textArea.append(String.format("%d. %s\n", stepIndex++, step));
        }
    }
    
    // PRINT SCALED INGREDIENTS + STEPS FOR EXECUTION 
    private void printScaledSteps(List<Ingredient> scaledIngredients, JTextArea textArea) {
        textArea.append("Συσκευές:\n");
        for (String utensil : recipe.getUtensils()) {
            textArea.append("- " + utensil + "\n");
        }

        textArea.append("\nΥλικά:\n");
        for (Ingredient ingredient : scaledIngredients) {
            textArea.append(String.format("- %s: %.1f %s\n",
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    "pieces".equals(ingredient.getUnit()) || "piece".equals(ingredient.getUnit()) ? "" : ingredient.getUnit()));
        }
        

        textArea.append("\nLet's go!:\n");

    }


}
