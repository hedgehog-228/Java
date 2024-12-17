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
        private List<Time> time = new ArrayList<>();
        private List<String> steps = new ArrayList<>();       
        
        public Recipe() {

        }

        public Recipe(List<Ingredient> ingredients, List<String> utensils, List<String> steps, List<Time> time) { //for tests
            if (ingredients != null) this.ingredients = new ArrayList<>(ingredients);
            if (utensils != null) this.utensils = new ArrayList<>(utensils);
            if (steps != null) this.steps = new ArrayList<>(steps);
            if (time != null) this.time = new ArrayList<>(time);
        }
        
        public List<Ingredient> getIngredients() {
        return ingredients;
        }
        
        public void addIngredient(Ingredient ingredient) {
            if(!ingredients.contains(ingredient)){
            ingredients.add(ingredient);
            }
        }

        public void addUtensil(String utensil) {
            if(!utensils.contains(utensil)){
            utensils.add(utensil);
            } 
        }
       
        
        public void addStep(String step) {
            steps.add(step);
        }

        public void addTime(Time time) {
            this.time.add(time);
        }

        public List<Time> getTime() {
            return time;
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
