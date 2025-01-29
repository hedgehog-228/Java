package com.mycompany.kitchenhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * This class for recipe buttons ( A button that represents the recipes which were loaded)
 */

public class RecipeButton extends JButton {
    private boolean expanded = false; // button status 
    private JPanel parentPanel; // panel where recipe buttons are
    private JPanel detailsPanel; 
    private Recipe recipe;
    int people;

    public RecipeButton(String recipeName, Recipe recipe, JPanel parentPanel) {
        super(recipeName);
        this.recipe = recipe;
        this.parentPanel = parentPanel;
        
        addActionListener(new RecipeAction());
    }
    
    // ACTION FOR RECIPE BUTTON 

    private class RecipeAction implements ActionListener {
    
        @Override
        public void actionPerformed(ActionEvent e) {
            
            // if somebody clicked the button
            if (!expanded) {
                people = requestNumberOfPeople();
                if (people < 1) {
                    return; // Exit if invalid input
                }

                detailsPanel = createDetailsPanel();
                int index = parentPanel.getComponentZOrder(RecipeButton.this);

                // button size fixation 
                RecipeButton.this.setMaximumSize(new Dimension(200, 50));
                RecipeButton.this.setMinimumSize(new Dimension(200, 50));
                RecipeButton.this.setPreferredSize(new Dimension(200, 50));

                RecipeButton.this.setBackground(new Color(0xD9893F));
                RecipeButton.this.setForeground(Color.white);

                parentPanel.add(detailsPanel, index + 1);
                parentPanel.revalidate();
                parentPanel.repaint();
                expanded = true;
            } else {
                // if unclicked -> delete the button 
                parentPanel.remove(detailsPanel);
                RecipeButton.this.setBackground(Color.lightGray);
                RecipeButton.this.setForeground(Color.black);
                parentPanel.revalidate();
                parentPanel.repaint();
                expanded = false;
            }
}

        
        
    }

    
     // Request the number of people from the user
    private int requestNumberOfPeople() {
        String input = JOptionPane.showInputDialog(
                RecipeButton.this,
                "Enter the number of people:",
                "Number of People",
                JOptionPane.QUESTION_MESSAGE
            );

        if (input == null || input.trim().isEmpty()) {
            return -1; // Return invalid input if user cancels or provides no input
        }

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                RecipeButton.this,
                "Invalid input. Please enter a valid number.",
                "Error",
                JOptionPane.ERROR_MESSAGE
                );
                return -1; // Return invalid input if parsing fails
            }
    }
    
    // DETAILS PANEL AFTER BUTTON ACTION

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setLayout(new BorderLayout());

        JTextArea recipeDetails = new JTextArea();
        recipeDetails.setEditable(false);
        recipeDetails.setFont(new Font("SansSerif", Font.PLAIN, 14));
        recipeDetails.setWrapStyleWord(true);
        recipeDetails.setLineWrap(true);
        recipeDetails.setMargin(new Insets(5, 5, 5, 5));
        DisplayRecipe displayRecipe = new DisplayRecipe(recipe, people);
        displayRecipe.printRecipe(recipeDetails);

        panel.add(new JScrollPane(recipeDetails), BorderLayout.CENTER);
        
        // EXECUTE RECIPE BUTTON 
        
        JButton executeButton = new JButton("Execute Recipe");

        executeButton.addActionListener(e -> {
        SwingUtilities.invokeLater(() -> {
            StepExecutor executor = new StepExecutor(recipe, people);
            executor.setVisible(true);
        });
    });

        executeButton.setFocusable(false);
        
        // SHOPPING BUTTON
        
        ShoppingListButton addShopListButton = new ShoppingListButton(
                KitchenHelper.shoppingList, KitchenHelper.shoppingListPanel, recipe, people );

        // adding the elements to the panel 
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(executeButton);
        buttonPanel.add(addShopListButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

}
