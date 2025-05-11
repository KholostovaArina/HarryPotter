package com.mycompany.themagicshop;

import java.sql.*;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class StickCreation {
    public static void create() {
        // Получаем данные со склада
        List<Warehouse> warehouseItems = Storage.getWarehouse();

        // Фильтруем корпуса и сердцевины
        List<String> bodies = warehouseItems.stream()
                .filter(item -> item.getType().equals("корпус"))
                .map(Warehouse::getName)
                .distinct()
                .collect(Collectors.toList());

        List<String> cores = warehouseItems.stream()
                .filter(item -> item.getType().equals("сердцевина"))
                .map(Warehouse::getName)
                .distinct()
                .collect(Collectors.toList());

        // Создаем GUI
        JFrame frame = new JFrame("Создание волшебной палочки");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Выпадающие списки
        JComboBox<String> bodyComboBox = new JComboBox<>(bodies.toArray(new String[0]));
        JComboBox<String> coreComboBox = new JComboBox<>(cores.toArray(new String[0]));

        // Кнопка создания
        JButton okButton = new JButton("Создать палочку");
        okButton.addActionListener(e -> {
            String selectedBody = (String) bodyComboBox.getSelectedItem();
            String selectedCore = (String) coreComboBox.getSelectedItem();

            try (Connection conn = PostgresConnecter.getConnection()) {
                conn.setAutoCommit(false);

                // Получаем компоненты
                Warehouse body = Storage.getWarehouseItem("корпус", selectedBody);
                Warehouse core = Storage.getWarehouseItem("сердцевина", selectedCore);

                if (body == null || core == null || body.getQuantity() < 1 || core.getQuantity() < 1) {
                    JOptionPane.showMessageDialog(frame, "Недостаточно материалов!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Списываем материалы
                boolean bodyUpdated = Storage.updateWarehouse(body.getId(), body.getQuantity() - 1);
                boolean coreUpdated = Storage.updateWarehouse(core.getId(), core.getQuantity() - 1);

                if (!bodyUpdated || !coreUpdated) {
                    conn.rollback();
                    throw new SQLException("Ошибка списания");
                }

                // Создаем палочку
                Storage.addMagicWand(new MagicWand(0, selectedBody, selectedCore, "на складе", null, null));
                conn.commit();

                JOptionPane.showMessageDialog(frame, "Палочка создана: " + selectedBody + "+" + selectedCore, "Успех", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Расположение элементов
        JLabel labelBody = new JLabel("Выберите корпус:");
        labelBody.setBounds(10, 40, 150, 25);
        bodyComboBox.setBounds(270, 40, 200, 25);

        JLabel labelCore = new JLabel("Выберите сердцевину:");
        labelCore.setBounds(10, 100, 250, 25);
        coreComboBox.setBounds(270, 100, 200, 25);

        okButton.setBounds(150, 200, 200, 40);

        // Добавление компонентов
        frame.add(labelBody);
        frame.add(bodyComboBox);
        frame.add(labelCore);
        frame.add(coreComboBox);
        frame.add(okButton);
 
        BeautyUtils.setFontForAllComponents(frame);
        frame.setVisible(true);
    }
}