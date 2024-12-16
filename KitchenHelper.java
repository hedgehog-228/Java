package com.mycompany.kitchenhelper;

import java.io.IOException;

public class KitchenHelper {
    public static void main(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("Please give at least one argument.");
        }

        if (!args[0].equals("-list")) {
            try {
                RecipeParser recipeParser = new RecipeParser();
                Recipe recipe = recipeParser.parse(args[0]);

                int people = 1;

                if (args.length > 2 && args[1].equals("-people")) {
                    try {
                        people = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Number of people should be an integer.");
                        return;
                    }

                    DisplayRecipe displayRecipe = new DisplayRecipe(recipe, people);
                    displayRecipe.printRecipe();
                }

            } catch (IOException e) {

            }

        } else if (args[0].equals("-list")) {

        }


    }
}
