package com.mycompany.themagicshop;

import javax.swing.*;

public class StickCreation {
    public static void create(){
        JFrame frame = new JFrame("Пример окна с выпадающими списками");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null); 
        frame.setLocationRelativeTo(null);

        JLabel labelCore = new JLabel("Выберите сердцевину:");
        labelCore.setBounds(10, 40, 150, 25); 
        String[] cores = {"Сердцевина 1", "Сердцевина 2", "Сердцевина 3"};
        JComboBox<String> coreComboBox = new JComboBox<>(cores);
        coreComboBox.setBounds(170, 40, 200, 25);

        JLabel labelBody = new JLabel("Выберите корпус:");
        labelBody.setBounds(10, 100, 150, 25);
        String[] bodies = {"Корпус 1", "Корпус 2", "Корпус 3"};
        JComboBox<String> bodyComboBox = new JComboBox<>(bodies);
        bodyComboBox.setBounds(170, 100, 200, 25);
        
        JButton okButton = new JButton("Создать палочку");
        okButton.setBounds(100, 200, 200, 40);

        frame.add(labelCore);
        frame.add(coreComboBox);
        frame.add(labelBody);
        frame.add(bodyComboBox);
        frame.add(okButton);
        
        frame.setVisible(true);
    }
    
}
