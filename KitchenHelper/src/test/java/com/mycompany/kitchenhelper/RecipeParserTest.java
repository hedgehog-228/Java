/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.kitchenhelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeParserTest {

    private final RecipeParser parser = new RecipeParser();

    @TempDir
    Path tempDir;

    @Test
    public void testValidRecipeFile() throws IOException { 
   
        Path validRecipeFile = tempDir.resolve("recipe.cook");
        Files.writeString(validRecipeFile, """
            ertgregsergter
            @flour{200%g}
            @sugar{1%g}
            @sugar and brown{100}
                        
            Mix all ingredients together. Bake for ~{30%minutes}.
                                           
            #plate , #soup , #bowl
                       
       
            """);

        Recipe recipe = parser.parse(validRecipeFile.toString());
        
          System.out.println("Utensils:");
        for (String utensil : recipe.getUtensils()) {
            System.out.printf("- %s  %n", utensil);
        }

         System.out.println("Ingredients:");
        for (Ingredient ingredient : recipe.getIngredients()) {
            System.out.printf("- %s: %.1f %s %n", ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit());
        }
         System.out.println("\nSteps:");
        int stepIndex = 1;
        for (String step : recipe.getSteps()) {
            System.out.printf("%d. %s%n", stepIndex++, step);
        }


        assertEquals(3, recipe.getIngredients().size());
        assertEquals(3, recipe.getSteps().size());
    }

    @Test
    public void testFileDoesNotExist() {
        String invalidPath = "nonexistent.cook";

        Exception exception = assertThrows(IOException.class, () -> parser.parse(invalidPath));

        assertEquals("File does not exist or is a directory: " + invalidPath, exception.getMessage());
    }

    @Test
    public void testInvalidFileFormat() { 
        String invalidFormatPath = tempDir.resolve("recipe.txt").toString();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(invalidFormatPath));

        assertEquals("Invalid file format. The file must have a .cook extension.", exception.getMessage());
    }

    @Test
    public void testEmptyRecipeFile() throws IOException {
        Path emptyRecipeFile = tempDir.resolve("empty.cook");
        Files.writeString(emptyRecipeFile, "sdsdasdasdad");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(emptyRecipeFile.toString()));

        assertEquals("The recipe must contain at least one ingredient.", exception.getMessage());
    }

    @Test
    public void testRecipeWithNoIngredients() throws IOException { 
        Path noIngredientsFile = tempDir.resolve("noIngredients.cook");
        Files.writeString(noIngredientsFile, """
            This is a recipe without ingredients.
            Bake for 20 minutes.
            """);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(noIngredientsFile.toString()));

        assertEquals("The recipe must contain at least one ingredient.", exception.getMessage());
    }
}
