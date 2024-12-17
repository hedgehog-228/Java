/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kitchenhelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
        File file = new File(source);
        
        //CHECK FORMAT
        if (!source.endsWith(".cook")) {
            throw new IllegalArgumentException("Invalid file format. The file must have a .cook extension.");
        }
        
        //CHECK IF EXIST IN SOURCE
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException("File does not exist or is a directory: " + source);
        }
        
        
        //CREATING OBJECT RECIPE + START READING LINE BY LINE 
        Recipe recipe = new Recipe();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) { // try to catch IO Exception
              StringBuilder stepBuilder = new StringBuilder(); // StringBuilder for collecting steps of recipe
              String line;

              while ((line = reader.readLine()) != null) { // while file has next line
                  // if line is empty, ending step and adding it
                  if (line.trim().isEmpty()) {
                      if (stepBuilder.length() > 0) {
                          recipe.addStep(stepBuilder.toString().trim());
                          stepBuilder.setLength(0); // cleaning stepBuilder
                      }
                      continue;
                  }

                String modifiedLine =  parseLine(line, recipe);
                stepBuilder.append(modifiedLine).append(" ");
                  
                  
              }

              // Final step
              if (stepBuilder.length() > 0) {
                  recipe.addStep(stepBuilder.toString().trim());
              }
            } catch (IOException e) {
                System.err.println("Error reading file: " + source + " - " + e.getMessage());
                throw e; // throw again to catch when will call it somewhere else
            }

            // END OF READING FILE, checking for at least 1 ingredient in it
            if (recipe.getIngredients().isEmpty()) {
                throw new IllegalArgumentException("The recipe must contain at least one ingredient.");
            }

            // RESULT
            return recipe;
        }

    
    // PARSING OF THE LINE
    
    private String parseLine(String line, Recipe recipe) {
        // INGREDIENTS
        String ingredientProcessed = processIngredients(line, recipe);

        // UTENSILS
        String utensilProcessed = processUtensils(ingredientProcessed, recipe);

        // TIME
        String timeProcessed = processTime(utensilProcessed, recipe);

        return timeProcessed;
    }
      
    // INGREDIENTS
    private String processIngredients(String line, Recipe recipe) {
        Pattern ingredientPattern = Pattern.compile("@([a-zA-Z\u0370-\u03FF]+ [a-zA-Z\u0370-\u03FF]+(?: [a-zA-Z\u0370-\u03FF]+)*)\\{(\\d+(?:\\.\\d+)?)?(?:%([a-zA-Z\u0370-\u03FF]+))?\\}|@([a-zA-Z\u0370-\u03FF]+)(?:\\{(\\d+(?:\\.\\d+)?)(?:%([a-zA-Z\u0370-\u03FF]+))?\\})?"); // group(1) = name of ingredient, proup(2) = quantity, group(3) = unit 
        Matcher matcher = ingredientPattern.matcher(line);
        StringBuffer result = new StringBuffer();

       while (matcher.find()) {
             
            String name = matcher.group(1) != null ? matcher.group(1) : matcher.group(4);
            
            if (name == null) {
                System.err.println("Invalid ingredient name: " + name + ". Ingredient names cannot contain numbers. Please check your recipe.");
                continue; // skip to the next match
            }
            
            //CHECK
          
            double quantity = 1.0; // default value
            try {
                if (matcher.group(2) != null) { // if matcher found quantity in group 2 --> parse value and set it to 'quantity' 
                    quantity = Double.parseDouble(matcher.group(2));
                } else if (matcher.group(5) != null) {
                    quantity = Double.parseDouble(matcher.group(5)); // if matcher found quantity in group 5--> parse value and set it to 'quantity' 
                }
            } catch (NumberFormatException e) {  // parseDouble has exception! 
                System.err.println("Invalid quantity format in ingredient: " + (matcher.group(2) != null ? matcher.group(2) : matcher.group(5)) + "The quantity will be set to default. Please check your recipe!");          
            }

            String unit;
     
            if (matcher.group(3) != null) { // if matcher found unit in group 3 --> parse value and set it to 'quantity' 
                    unit = matcher.group(3);
            } else if (matcher.group(6) != null) {// if matcher found unit in group 6 --> parse value and set it to 'quantity' 
                    unit = matcher.group(6);
            } else {
                    unit = quantity > 1 ? "pieces" : "piece"; // depend on quantity choose correct format
            }
             
            recipe.addIngredient(new Ingredient(name, quantity, unit)); // creating + adding new ingredient
            matcher.appendReplacement(result, String.format("%s (%.1f %s)", name, quantity, unit));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    //
    private String processUtensils(String line, Recipe recipe) {
        Pattern utensilPattern = Pattern.compile("#([a-zA-Z\u0370-\u03FF]+ [a-zA-Z\u0370-\u03FF]+(?: [a-zA-Z\u0370-\u03FF]+)*)\\{\\}|#([a-zA-Z\u0370-\u03FF]+)"); // two/more words with {} or just word 
        Matcher matcher = utensilPattern.matcher(line);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String utensilName = matcher.group(1) != null ? matcher.group(1):matcher.group(2);
            
            if (utensilName != null) { // Add only if not already in recipe
                recipe.addUtensil(utensilName);
            }
            matcher.appendReplacement(result, utensilName);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    //
    private String processTime(String line, Recipe recipe) {
        Pattern timePattern = Pattern.compile("(?:~\\{(\\d+(?:\\.\\d+)?)%([a-zA-Z]+)\\})?"); // group(1) = value, group(2) = unit
        Matcher matcher = timePattern.matcher(line);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            if (matcher.group(1) != null && matcher.group(2) != null) {
                double timeValue = Double.parseDouble(matcher.group(1));
                String timeUnit = matcher.group(2);
                recipe.addTime(new Time(timeValue, timeUnit));
            matcher.appendReplacement(result, String.format("%.1f %s", timeValue, timeUnit)); 
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }
      /*
    private String parseLine(String line, Recipe recipe) {
        
         // PATTERNS 
        Pattern ingredientPattern = Pattern.compile("@([a-zA-Z\u0370-\u03FF]+ [a-zA-Z\u0370-\u03FF]+(?: [a-zA-Z\u0370-\u03FF]+)*)\\{(\\d+(?:\\.\\d+)?)?(?:%(\\w+))?\\}|@([a-zA-Z\u0370-\u03FF]+)(?:\\{(\\d+(?:\\.\\d+)?)(?:%(\\w+))?\\})?"); // group(1) = name of ingredient, proup(2) = quantity, group(3) = unit 
        Pattern utensilPattern = Pattern.compile("#([a-zA-Z\u0370-\u03FF]+ [a-zA-Z\u0370-\u03FF]+(?: [a-zA-Z\u0370-\u03FF]+)*)\\{\\}|#([a-zA-Z\u0370-\u03FF]+)"); // two/more words with {} or just word 
        Pattern timePattern = Pattern.compile("(?:~\\{(\\d+(?:\\.\\d+)?)%([a-zA-Z]+)\\})?"); // group(1) = value, group(2) = unit
        
        StringBuilder modifiedLine = new StringBuilder(line);

         // INGREDIENTS
        Matcher ingredientMatcher = ingredientPattern.matcher(line);
         
        while (ingredientMatcher.find()) {
             
            String name = ingredientMatcher.group(1) != null ? ingredientMatcher.group(1) : ingredientMatcher.group(4);
            
            if (name == null) {
                System.err.println("Invalid ingredient name: " + name + ". Ingredient names cannot contain numbers. Please check your recipe.");
                continue; // skip to the next match
            }
            
            //CHECK 
          /*  double quantity = ingredientMatcher.group(2) != null ? Double.parseDouble(ingredientMatcher.group(2)): (ingredientMatcher.group(5) != null ? Double.parseDouble(ingredientMatcher.group(5)): 1.0 ); // ToDouble
            
            String unit = ingredientMatcher.group(3) != null ? ingredientMatcher.group(3) : (ingredientMatcher.group(6)!= null ? ingredientMatcher.group(6): (quantity > 1 ? "peaces" : "peace")); // unit is optional (may be or not)
          
            double quantity = 1.0; // default value
            try {
                if (ingredientMatcher.group(2) != null) { // if matcher found quantity in group 2 --> parse value and set it to 'quantity' 
                    quantity = Double.parseDouble(ingredientMatcher.group(2));
                } else if (ingredientMatcher.group(5) != null) {
                    quantity = Double.parseDouble(ingredientMatcher.group(5)); // if matcher found quantity in group 5--> parse value and set it to 'quantity' 
                }
            } catch (NumberFormatException e) {  // parseDouble has exception! 
                System.err.println("Invalid quantity format in ingredient: " + (ingredientMatcher.group(2) != null ? ingredientMatcher.group(2) : ingredientMatcher.group(5)) + "The quantity will be set to default. Please check your recipe!");          
            }

            String unit;
     
            if (ingredientMatcher.group(3) != null) { // if matcher found unit in group 3 --> parse value and set it to 'quantity' 
                    unit = ingredientMatcher.group(3);
            } else if (ingredientMatcher.group(6) != null) {// if matcher found unit in group 6 --> parse value and set it to 'quantity' 
                    unit = ingredientMatcher.group(6);
            } else {
                    unit = quantity > 1 ? "pieces" : "piece"; // depend on quantity choose correct format
            }
             
            recipe.addIngredient(new Ingredient(name, quantity, unit)); // creating + adding new ingredient
            
            
            // Replace ingredient in line
            String replacement = String.format("%s (%.1f %s)", name, quantity, unit);
            
            int start = ingredientMatcher.start();
            int end = ingredientMatcher.end();
            
            modifiedLine.replace(start, end, replacement);
            ingredientMatcher = ingredientPattern.matcher(modifiedLine.toString());
         }

        // UTENSILS
        Matcher utensilMatcher = utensilPattern.matcher(line); 
         
        while (utensilMatcher.find()) {
            String utensilName = utensilMatcher.group(1) != null ? utensilMatcher.group(1):utensilMatcher.group(2);
            
            if (utensilName != null) { // Add only if not already in set
            recipe.addUtensil(utensilName);
            }
             
             // Replace utensil in line
            String replacement = utensilName;
            
            int start = utensilMatcher.start();
            int end = utensilMatcher.end();
            
            modifiedLine.replace(start, end, replacement);
            utensilMatcher = utensilPattern.matcher(modifiedLine.toString());
         }

        // TIME
        Matcher timeMatcher = timePattern.matcher(line);
         
        while (timeMatcher.find()) {
            if (timeMatcher.group(1) != null && timeMatcher.group(2) != null) {
                double timeValue = Double.parseDouble(timeMatcher.group(1));
                String timeUnit = timeMatcher.group(2);
                recipe.addTime(new Time(timeValue, timeUnit));
                
                // Replace time in line
                String replacement = String.format("%s %s", timeValue, timeUnit);
                int start = timeMatcher.start();
                int end = timeMatcher.end();
                modifiedLine.replace(start, end, replacement);
                timeMatcher = timePattern.matcher(modifiedLine.toString());
            }
         }
         
        return modifiedLine.toString();
     }*/

    
}
