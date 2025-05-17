package com.mycompany.themagicshop;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class OrderWindow {
    public static void create() {
        List<MagicWand> allWands = Storage.getAllWands();
        
        List<MagicWand> availableWands = allWands.stream()
                .filter(w -> "на складе".equals(w.getStatus()))
                .collect(Collectors.toList());

        String[] wandNames = availableWands.stream()
                .map(w -> w.getCore() + "+" + w.getCorpus())
                .toArray(String[]::new);

        JPanel mainPanel = BeautyUtils.createPanelWithPhoto(BeautyUtils.getBuyImage());
        mainPanel.setLayout(null);

        JFrame frame = new JFrame("Оформление заказа");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 300);
        frame.setLocationRelativeTo(null);

        JLabel wandLabel = new JLabel("Выберите палочку:");
        wandLabel.setBounds(10, 40, 150, 25);
        JComboBox<String> wandComboBox = new JComboBox<>(wandNames);
        wandComboBox.setBounds(270, 40, 250, 25);

        JLabel nameLabel = new JLabel("Введите имя покупателя:");
        nameLabel.setBounds(10, 100, 200, 25);
        JTextField nameField = new JTextField();
        nameField.setBounds(270, 100, 250, 25);

        JButton orderButton = new JButton("Оформить заказ");
        orderButton.setBounds(150, 200, 200, 40);
        
        orderButton.addActionListener(e -> {
            int index = wandComboBox.getSelectedIndex();
            if (index == -1) {
                return;
            }

            MagicWand wand = availableWands.get(index);
            wand.setStatus("продана");
            wand.setOwnerName(nameField.getText().trim());
            wand.setPurchaseDate(java.time.LocalDate.now());

            Storage.updateWand(wand);
            JOptionPane.showMessageDialog(frame, "Покупка \nот " + wand.getPurchaseDate() +"\n\t"+
                                                  wand.getOwnerName()+"\n\t"+ 
                                                 wand.getCorpus() + "+" + wand.getCore());
            frame.dispose();
        });

        mainPanel.add(wandLabel);
        mainPanel.add(wandComboBox);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(orderButton);

        frame.add(mainPanel);
        BeautyUtils.setFontForAllComponents(frame);
        frame.setVisible(true);
    }
}