package com.mycompany.themagicshop;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class OrderWindow {
    public static void create() {
        // Получаем все палочки из БД
        List<MagicWand> allWands = Storage.getAllWands();
        
        // Фильтруем только доступные палочки (со статусом "на складе")
        List<MagicWand> availableWands = allWands.stream()
                .filter(w -> "на складе".equals(w.getStatus()))
                .collect(Collectors.toList());

        // Формируем список названий в формате "Палочка [сердцевина]+[корпус]"
        String[] wandNames = availableWands.stream()
                .map(w -> w.getCore() + "+" + w.getCorpus())
                .toArray(String[]::new);

        // Создаем GUI
        JFrame frame = new JFrame("Оформление заказа");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Выпадающий список с палочками
        JLabel wandLabel = new JLabel("Выберите палочку:");
        wandLabel.setBounds(10, 40, 150, 25);
        JComboBox<String> wandComboBox = new JComboBox<>(wandNames);
        wandComboBox.setBounds(170, 40, 250, 25);

        // Поле для имени покупателя
        JLabel nameLabel = new JLabel("Введите имя покупателя:");
        nameLabel.setBounds(10, 100, 150, 25);
        JTextField nameField = new JTextField();
        nameField.setBounds(170, 100, 250, 25);

        // Кнопка оформления заказа
        JButton orderButton = new JButton("Оформить заказ");
        
        orderButton.setBounds(100, 200, 200, 40);
        
        // Обработчик кнопки
         // Обработчик кнопки
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

        frame.add(wandLabel);
        frame.add(wandComboBox);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(orderButton);
        frame.setVisible(true);
    }
}