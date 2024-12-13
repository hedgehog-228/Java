/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * class just for my part
 */
public class Recipe {
        private List<Ingredient> ingredients = new ArrayList<>();
        private List<String> utensils = new ArrayList<>();
        private List<String> steps = new ArrayList<>();
        
        public List<Ingredient> getIngredients() {
        return ingredients;
        }
        
        public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        }

        public void addUtensil(String utensil) {
            utensils.add(utensil);
        }
       
        
        public void addStep(String step) {
            steps.add(step);
        }

        public void addTime(Time time) {
            steps.add("Wait for " + time.getValue() + " " + time.getUnit());
        }

        public List<String> getSteps() {
            return steps;
        }

        public List<String> getUtensils() {
            return utensils;
        }
        
        

        @Override
        public String toString() {
            return "Ingredients: " + ingredients + "\nUtensils: " + utensils + "\nSteps: " + steps;
        }

}
