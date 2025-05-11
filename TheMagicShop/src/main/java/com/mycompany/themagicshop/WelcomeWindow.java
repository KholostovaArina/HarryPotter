package com.mycompany.themagicshop;

import java.awt.*;
import javax.swing.*;


public class WelcomeWindow {
    public static void start(){
        JFrame frame = new JFrame();
        frame.setTitle("Лавка волшебных палочек «Олливандеры»");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        
        JPanel contentPanel = BeautyUtils.createPanelWithPhoto(BeautyUtils.getWelcomeImage());
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 150, 300));
        JButton startButton = new JButton("Войти в магазин");
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.addActionListener(e -> {
            SelectionWindow sw = new SelectionWindow();
            frame.dispose();
        });
      
        startButton.setFont(BeautyUtils.getBigFont());
        contentPanel.add(startButton, FlowLayout.LEFT);
        frame.add(contentPanel);       
        frame.setVisible(true);
        BeautyUtils.playMusic();
    }
    
}
