/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.kitchenhelper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingListTest {

    private Recipe recipe1;
    private Recipe recipe2;
    private ShoppingList shoppingList;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        Locale.setDefault(Locale.US); 
        recipe1 = new Recipe();
        recipe2 = new Recipe();
        shoppingList = new ShoppingList();


        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testPrintShoppingList() {
        Ingredient ingredient1 = new Ingredient("Flour", 200, "gr");
        Ingredient ingredient2 = new Ingredient("Sugar", 100, "gr");

        recipe1.addIngredient(ingredient1);
        recipe1.addIngredient(ingredient2);

        shoppingList.addRecipe(recipe1);
        shoppingList.printShoppingList();

        String expectedOutput = "Shopping List:\n" +
                                "- Flour: 200.00 gr\n" +
                                "- Sugar: 100.00 gr";

        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintShoppingListUnsupportedUnit() {
        Ingredient ingredient1 = new Ingredient("Flour", 200, "gr");
        Ingredient ingredient2 = new Ingredient("Sugar", 100, "gr");
        Ingredient ingredient3 = new Ingredient("Eggs", 2, "pieces");
        Ingredient ingredient4 = new Ingredient("Eggs", 2, "cups");

        recipe1.addIngredient(ingredient1);
        recipe1.addIngredient(ingredient2);
        recipe1.addIngredient(ingredient3);
        recipe1.addIngredient(ingredient4);

        recipe2.addIngredient(ingredient1);
        recipe2.addIngredient(ingredient2);
        recipe2.addIngredient(ingredient3);
        recipe2.addIngredient(ingredient4);

        shoppingList.addRecipe(recipe1);
        shoppingList.addRecipe(recipe2);

        shoppingList.printShoppingList();

        String expectedOutput = "Shopping List:\n" +
                                "- Eggs: 4.00 \n" +
                                "- Eggs: 4.00 cups\n" +
                                "- Flour: 400.00 gr\n" +
                                "- Sugar: 200.00 gr";

        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }
    }