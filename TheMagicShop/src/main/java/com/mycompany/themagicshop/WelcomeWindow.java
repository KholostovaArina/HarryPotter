package com.mycompany.themagicshop;

import java.awt.*;
import javax.swing.*;


public class WelcomeWindow {
    public static void start(){
        JFrame frame = new JFrame();
        frame.setTitle("Лавка волшебных палочек «Олливандеры»");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150, 300));
        JButton startButton = new JButton("Войти в магазин");
        startButton.addActionListener(e -> {
            SelectionWindow sw = new SelectionWindow();
            frame.setVisible(false);
        });
      
        
        contentPanel.add(startButton, FlowLayout.LEFT);
        frame.add(contentPanel);       
        frame.setVisible(true);
    }
    
}
