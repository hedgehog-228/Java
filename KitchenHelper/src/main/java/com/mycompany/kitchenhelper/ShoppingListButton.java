package com.mycompany.kitchenhelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShoppingListButton extends JButton {
    private ShoppingList shoppingList;
    private ShoppingListPanel shoppingListPanel;
    private Recipe recipe;
    private int people; 

    public ShoppingListButton(ShoppingList shoppingList, ShoppingListPanel shoppingListPanel, Recipe recipe, int people) {
        super("Add to Shopping List");
        this.shoppingList = shoppingList;
        this.shoppingListPanel = shoppingListPanel;
        this.recipe = recipe;
        this.people = people; 

        addActionListener(new AddToShoppingListAction());
    }

    private class AddToShoppingListAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            shoppingList.addRecipe(recipe, people); 
            shoppingListPanel.updateShoppingList(shoppingList); // updating panel
            JOptionPane.showMessageDialog(null, "Recipe added to shopping list!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
