
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
     private Map<String, List<Ingredient>> groupedIngredients; // Name + List of ingredients 
    private IngredientConverter converter;

    public ShoppingList() {
        this.groupedIngredients = new HashMap<>();
        this.converter = new IngredientConverter();
    }

    public void addRecipe(Recipe recipe, int people) {
    for (Ingredient ingredient : recipe.getIngredients()) {
        String ingredientName = ingredient.getName().toLowerCase();

        // scale ingredients quantity
        double scaledQuantity = ingredient.getQuantity() * people;
        Ingredient scaledIngredient = new Ingredient(ingredient.getName(), scaledQuantity, ingredient.getUnit());

        if (groupedIngredients.containsKey(ingredientName)) {
            List<Ingredient> ingredientList = groupedIngredients.get(ingredientName);
            boolean added = false;

            for (int i = 0; i < ingredientList.size(); i++) {
                Ingredient existingIngredient = ingredientList.get(i);

                Map<String, Double> combinedQuantities = converter.addQuantities(existingIngredient, scaledIngredient);

                if (combinedQuantities.size() == 1) {
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

            if (!added) {
                ingredientList.add(scaledIngredient);
            }
        } else {
            List<Ingredient> newList = new ArrayList<>();
            newList.add(scaledIngredient);
            groupedIngredients.put(ingredientName, newList);
        }
    }
}


    public List<String> toStringList() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<Ingredient>> entry : groupedIngredients.entrySet()) {
            for (Ingredient ingredient : entry.getValue()) {
                result.add(String.format("%s: %.2f %s",
                        ingredient.getName(),
                        ingredient.getQuantity(),
                        "pieces".equals(ingredient.getUnit()) || "piece".equals(ingredient.getUnit()) ? "" : ingredient.getUnit()));
            }
        }
        return result;
    }
}