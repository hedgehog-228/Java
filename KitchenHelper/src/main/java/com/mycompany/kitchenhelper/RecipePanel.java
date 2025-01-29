
package com.mycompany.kitchenhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 *
 * The recipe panel for recipes list + button for loading recipes to the app
 */

public class RecipePanel extends JPanel{
    private JPanel recipeListPanel;
    private ShoppingListPanel shoppingListPanel;
    
    public RecipePanel() throws IOException {
        setLayout(new BorderLayout());
        setBorder(null);
        
        Font customFont = null;
        
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MISTRAL.ttf"));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            throw new IOException();
        }
         
        JLabel label = new JLabel("Συνταγές", SwingConstants.CENTER);
        label.setFont(customFont.deriveFont(40f));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        add(label, BorderLayout.NORTH);

        JButton loadButton = new JButton("Load Recipes"); //BUTTON LOAD RECIPES 
        loadButton.setFocusable(false);
        loadButton.setBackground(Color.lightGray);
        loadButton.setBorderPainted(false);
        
        loadButton.addActionListener(new LoadRecipesAction());
        add(loadButton, BorderLayout.SOUTH);

        recipeListPanel = new JPanel(); // initialization 
        recipeListPanel.setBackground(Color.WHITE);
        recipeListPanel.setBorder(null);

        recipeListPanel.setLayout(new BoxLayout(recipeListPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(recipeListPanel);
        scrollPane.setBorder(null);  
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void setShoppingListPanel(ShoppingListPanel shoppingListPanel) {
        this.shoppingListPanel = shoppingListPanel;
    }
    
    // CHECK FOR RECIPE BUTTONS 
    public boolean doesRecipeButtonExist(String recipeName) {
        for (Component component : recipeListPanel.getComponents()) {
            if (component instanceof RecipeButton button) {
                if (button.getText().equals(recipeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // LOAD BUTTON ACTION 
    
private class LoadRecipesAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        RecipeParser recipeParser = new RecipeParser();
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(RecipePanel.this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                try {
                    // parsing from choosen file 
                    Recipe recipe = recipeParser.parse(file.getPath());
                    
                    // Check if the button already exists
                    if (doesRecipeButtonExist(file.getName())) {
                        JOptionPane.showMessageDialog(RecipePanel.this,
                            "Recipe button with name \"" + file.getName() + "\" already exists.",
                            "Duplicate Recipe",
                            JOptionPane.WARNING_MESSAGE);
                        continue;
                    }
        
                    // new button for every choosen recipe 
                    JButton recipeButton = new RecipeButton(file.getName(), recipe, recipeListPanel);
                    recipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    recipeButton.setMaximumSize(new Dimension(200, 50));
                    recipeButton.setFocusable(false);
                    recipeButton.setBorderPainted(false);
                    recipeButton.setBackground(Color.lightGray);
                    
                    recipeListPanel.add(recipeButton);
                    
                    recipeListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                } catch (IOException ex) { // ERRORS 
                    JOptionPane.showMessageDialog(RecipePanel.this,
                            "Error loading recipe: " + file.getName(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(RecipePanel.this,
                            "Invalid recipe in file \"" + file.getName() + "\": " + ex.getMessage(),
                            "Invalid Recipe",
                            JOptionPane.WARNING_MESSAGE);
                    }
            }
            
            // UPDATING 
             
            recipeListPanel.revalidate();
            recipeListPanel.repaint();
        }
    }
}
}

