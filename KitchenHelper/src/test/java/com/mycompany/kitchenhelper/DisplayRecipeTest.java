package com.mycompany.kitchenhelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayRecipeTest {

    private Recipe recipe;
    private DisplayRecipe displayRecipe;

    @BeforeEach
    public void setup() throws IOException {
        recipe = new Recipe(
            Arrays.asList(
                new Ingredient("Αυγά", 3, ""),
                new Ingredient("Αλεύρι", 125, "gr"), 
                new Ingredient("Γάλα", 250, "ml"), 
                new Ingredient("Αλάτι θαλασσινό", 1, "πρέζα"), 
                new Ingredient("Βούτυρο", 50, "gr") 
            ),
            Arrays.asList(
                "Μπλέντερ", 
                "Μπολ", 
                "Μεγάλο αντικολλητικό τηγάνι" 
            ),
            Arrays.asList(
                "Σπάστε τα αυγά σε ένα μπλέντερ και προσθέστε αλεύρι, γάλα και αλάτι.",
                "Χτυπήστε μέχρι να γίνει λείο.", // Змішати до однорідності
                "Ρίξτε το σε ένα μπολ και αφήστε το να σταθεί για 15 λεπτά.", 
                "Λιώστε το βούτυρο σε ένα μεγάλο αντικολλητικό τηγάνι.", 
                "Μαγειρέψτε για 2 λεπτά από τη μία πλευρά και 1 λεπτό από την άλλη."
            ),
            Arrays.asList(
                new Time(15, "minutes"),
                new Time(2, "minutes"),
                new Time(1, "minutes")
            )
        );
    }

    @Test
    public void testScalingAndCombiningIngredients() {
        // Масштабування рецепта на 2 осіб
        displayRecipe = new DisplayRecipe(recipe, 2);

        List<Ingredient> scaledIngredients = displayRecipe.scaleAndCombineIngredients(recipe.getIngredients(), 2);

        assertEquals(6.0, scaledIngredients.stream().filter(i -> i.getName().equalsIgnoreCase("Αυγά")).findFirst().get().getQuantity());
        assertEquals(250.0, scaledIngredients.stream().filter(i -> i.getName().equalsIgnoreCase("Αλεύρι")).findFirst().get().getQuantity());
        assertEquals(500.0, scaledIngredients.stream().filter(i -> i.getName().equalsIgnoreCase("Γάλα")).findFirst().get().getQuantity());
        assertEquals(2.0, scaledIngredients.stream().filter(i -> i.getName().equalsIgnoreCase("Αλάτι θαλασσινό")).findFirst().get().getQuantity());
        assertEquals(100.0, scaledIngredients.stream().filter(i -> i.getName().equalsIgnoreCase("Βούτυρο")).findFirst().get().getQuantity());
    }

    @Test
    public void testPrintRecipe() {

        displayRecipe = new DisplayRecipe(recipe, 1);
        
        assertDoesNotThrow(() -> displayRecipe.printRecipe());
    }

    @Test
    public void testScalingWithInvalidPeople() {
        assertThrows(IllegalArgumentException.class, () -> new DisplayRecipe(recipe, 0));
        assertThrows(IllegalArgumentException.class, () -> new DisplayRecipe(recipe, -1));
    }

    @Test
    public void testTotalTimeCalculation() {
        displayRecipe = new DisplayRecipe(recipe, 1);

        TimeConverter timeConverter = new TimeConverter();
        Time totalTime = new Time(0, "minutes");

        for (Time stepTime : recipe.getTime()) {
            totalTime = timeConverter.addQuantities(totalTime, stepTime)
                .entrySet()
                .stream()
                .map(entry -> new Time(entry.getValue(), entry.getKey()))
                .findFirst()
                .orElse(totalTime);
        }

        assertEquals(18.0, totalTime.getValue());
        assertEquals("minutes", totalTime.getUnit());
    }

    @Test
    public void testIngredientCaseInsensitivity() {

        displayRecipe = new DisplayRecipe(recipe, 1);

        List<Ingredient> ingredients = Arrays.asList(
            new Ingredient("Ζάχαρη", 100, "gr"), 
            new Ingredient("ζάχαρη", 200, "gr")  
        );

        List<Ingredient> scaledIngredients = displayRecipe.scaleAndCombineIngredients(ingredients, 1);

        assertEquals(1, scaledIngredients.size());
        assertEquals(300.0, scaledIngredients.stream().filter(i -> i.getName().equalsIgnoreCase("Ζάχαρη")).findFirst().get().getQuantity());
    }
}
