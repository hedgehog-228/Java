package com.mycompany.kitchenhelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class ShoppingListPanel extends JPanel {
    private DefaultListModel<String> shoppingListModel;

    public ShoppingListPanel() throws IOException {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); 

        Font customFont = null;

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MISTRAL.ttf"));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            throw new IOException();
        }

        JLabel label = new JLabel("Λίστα αγορών", SwingConstants.CENTER);
        label.setFont(customFont.deriveFont(40f));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        add(label, BorderLayout.NORTH);

        shoppingListModel = new DefaultListModel<>();
        JList<String> shoppingList = new JList<>(shoppingListModel);
        shoppingList.setCellRenderer(new ShoppingListRenderer());
        
        JScrollPane scrollPane = new JScrollPane(shoppingList);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // button for clearing list of ingredients 
        JButton clearButton = new JButton("Clear Selected");
        clearButton.setFocusable(false);
        clearButton.setBackground(Color.lightGray);
        clearButton.setBorderPainted(false);
        
        clearButton.addActionListener(e -> {
            List<String> selectedItems = shoppingList.getSelectedValuesList();
            for (String item : selectedItems) {
                shoppingListModel.removeElement(item);
            }
        });
        add(clearButton, BorderLayout.SOUTH);
    }

    public void updateShoppingList(ShoppingList shoppingList) {
        shoppingListModel.clear();
        for (String ingredient : shoppingList.toStringList()) {
            shoppingListModel.addElement(ingredient);
        }
    }

    
    private static class ShoppingListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText((index + 1) + ". " + value); // number of element

            label.setHorizontalAlignment(SwingConstants.CENTER);
             label.setPreferredSize(new Dimension(200, 40));

            Border dashed  = BorderFactory.createDashedBorder(null, 5, 5); // underline
            Border empty  = BorderFactory.createEmptyBorder(-1, -1, 0, -1);// Відступ зліва на 10px
            Border compound = new CompoundBorder(empty, dashed);
            label.setBorder( compound );
            return label;
        }
    }
}
