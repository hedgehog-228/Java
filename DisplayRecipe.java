package com.mycompany.kitchenhelper;

import java.util.List;

public class DisplayRecipe {
    private Recipe recipe;
    private int people;

    public DisplayRecipe(Recipe recipe, int people) {
        if (people <= 0) {
            throw new IllegalArgumentException("Number of people should be a positive value.");
        }
        this.recipe = recipe;
        this.people = people;
    }

    public void printRecipe() {

        if (people > 1) {
            scaleQuantities(recipe.getIngredients(), people);
        }
    }


    private void scaleQuantities(List<Ingredient> ingredients, int people) {
        IngredientConverter converter = new IngredientConverter();

    }

}
