/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nikol
 */
public class RecipeParser implements FileParser{

    @Override
    public Recipe parse(String source) throws IOException {
 Recipe recipe = new Recipe();

        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            StringBuilder stepBuilder = new StringBuilder(); // StringBuilder for collecting steps of recipe
            String line;
            
            while ((line = reader.readLine()) != null) {
                
                // if line is empty, ending step and adding it
                if (line.trim().isEmpty()) {
                    if (stepBuilder.length() > 0) {
                        recipe.addStep(stepBuilder.toString().trim());
                        stepBuilder.setLength(0); // cleaning stepBuilder
                    }
                    continue;
                }

                // Checking line for ingredients, utensil or time 
                if (!parseLine(line, recipe)) {
                    stepBuilder.append(line).append(" "); // Adding text to the step
                }
            }

            // Final step
            if (stepBuilder.length() > 0) {
                recipe.addStep(stepBuilder.toString().trim());
            }
        }

        return recipe;
    }

    // PARSING OF LINE
    private boolean parseLine(String line, Recipe recipe) {
        
         // PATTERNS 
         Pattern ingredientPattern = Pattern.compile("@([a-zA-Z ]+)\\{(?:(\\d+)%?(\\w+)?)?\\}|@(\\w+)(?:\\{(\\d+)%?(\\w+)?\\})?"); // group(1) = name of ingredient, proup(2) = quantity, group(3) = unit 
         Pattern utensilPattern = Pattern.compile("#([a-zA-Z ]+)\\{\\}|#(\\w+)"); // two/more words with {} or just word 
         Pattern timePattern = Pattern.compile("(?:~\\{(\\d+)%(\\w+)\\})?"); // group(1) = value, group(2) = unit

         boolean matched = false; // --> IF SOMETHING WAS FOUND true

         // INGREDIENTS
         Matcher ingredientMatcher = ingredientPattern.matcher(line);
         
         while (ingredientMatcher.find()) {
             
             String name = ingredientMatcher.group(1);
             int quantity = ingredientMatcher.group(2) != null ? Integer.parseInt(ingredientMatcher.group(2)): 1; // ToInt 
             String unit = ingredientMatcher.group(3) != null ? ingredientMatcher.group(3) : "piece"; // unit is optional (may be or not)
             
             recipe.addIngredient(new Ingredient(name, quantity, unit)); // creating + adding new ingredient
             matched = true; // was found
         }

         // UTENSILS
         Matcher utensilMatcher = utensilPattern.matcher(line); 
         
         while (utensilMatcher.find()) {
             String utensilName = utensilMatcher.group(1);
             
             recipe.addUtensil(utensilName);
             matched = true; // was found
         }

         // TIME
         Matcher timeMatcher = timePattern.matcher(line);
         
         while (timeMatcher.find()) {
             int timeValue = Integer.parseInt(timeMatcher.group(1));
             String timeUnit = timeMatcher.group(2);
             recipe.addTime(new Time(timeValue, timeUnit));
             matched = true;
         }

         return matched;
     }

    
}
