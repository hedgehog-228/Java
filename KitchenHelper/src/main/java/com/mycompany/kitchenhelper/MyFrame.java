
package com.mycompany.kitchenhelper;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * frame initilization
 *
 */
public class MyFrame extends JFrame {
    
    MyFrame() {
        this.setTitle("Kitchen Companion");
        
        ImageIcon icon = new ImageIcon("icons/logo.png"); 
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width/2 - 500, dimension.height/2 - 400, 1000, 800);
        this.setVisible(true);
        
    }
}
