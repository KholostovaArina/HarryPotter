package com.mycompany.themagicshop;

import javax.swing.*;

public class OrderWindow {
    public static void create(){
        JFrame frame = new JFrame("Пример окна с выпадающими списками");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null); 
        frame.setLocationRelativeTo(null);

        JLabel labelCore = new JLabel("Выберите палку:");
        labelCore.setBounds(10, 40, 150, 25); 
        String[] cores = {"Палка 1", "Палка 2", "Палка 3"};
        JComboBox<String> coreComboBox = new JComboBox<>(cores);
        coreComboBox.setBounds(170, 40, 200, 25);

        JLabel labelBody = new JLabel("Введите имя покупателя:");
        labelBody.setBounds(10, 100, 150, 25);
        JTextField nameConsumer = new JTextField();
        nameConsumer.setBounds(170, 100, 200, 25);
        
        JButton okButton = new JButton("Создать палочку");
        okButton.setBounds(100, 200, 200, 40);

        frame.add(labelCore);
        frame.add(coreComboBox);
        frame.add(labelBody);
        frame.add(nameConsumer);
        frame.add(okButton);
        
        frame.setVisible(true);
    }
}
