package com.mycompany.kitchenhelper;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class KitchenHelper extends MyFrame {
    public static ShoppingList shoppingList = new ShoppingList();
    public static ShoppingListPanel shoppingListPanel;
    public static void main(String[] args) throws IOException {
        
        SwingUtilities.invokeLater(() -> {
            // main frame
            MyFrame frame = new MyFrame();
            frame.setLayout(new GridLayout(1,2)); // 2 columns

            // Creating the panels
            RecipePanel recipePanel = null;
            try {
                recipePanel = new RecipePanel();
                recipePanel.setBorder(null);
            } catch (IOException ex) {
                Logger.getLogger(KitchenHelper.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                shoppingListPanel = new ShoppingListPanel();
                shoppingListPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
            } catch (IOException ex) {
                Logger.getLogger(KitchenHelper.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Connecting the panels
            recipePanel.setShoppingListPanel(shoppingListPanel);
            
            recipePanel.setBackground(Color.WHITE);
            shoppingListPanel.setBackground(Color.WHITE);
            
            // adding panels to the frame
            frame.add(recipePanel);
            frame.add(shoppingListPanel);

            frame.setVisible(true);
        });

    }
  
}