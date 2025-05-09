package com.mycompany.themagicshop;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddSupplyWindow {
    private int currentSupplyId = -1;
    private List<String> componentsList = new ArrayList<>();
    private JFrame frame;
    private JTextField nameField;
    private JSpinner quantitySpinner;
    private JTextField dateField;
    private JComboBox<String> typeCombo;

    public static void create() {
        new AddSupplyWindow().initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Новая поставка");
        frame.setSize(450, 300);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        // Панель ввода данных
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        typeCombo = new JComboBox<>(new String[]{"корпус", "сердцевина"});
        nameField = new JTextField();
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        dateField = new JTextField(LocalDate.now().toString());

        inputPanel.add(new JLabel("Тип компонента:"));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("Название:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Количество:"));
        inputPanel.add(quantitySpinner);
        inputPanel.add(new JLabel("Дата:"));
        inputPanel.add(dateField);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Добавить компонент");
        JButton saveButton = new JButton("Поставка закончена");

        // Обработчики событий
        addButton.addActionListener(e -> addComponent());
        saveButton.addActionListener(e -> saveSupply());

        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addComponent() {
    String type = (String) typeCombo.getSelectedItem();
    String name = nameField.getText().trim();
    int quantity = (Integer) quantitySpinner.getValue();

    if (name.isEmpty()) {
        showError("Введите название компонента!");
        return;
    }

    try {
        // Создаем поставку при первом добавлении компонента
        if (currentSupplyId == -1) {
            Supply supply = new Supply(0, LocalDate.parse(dateField.getText()), false);
            currentSupplyId = Storage.addSupplyAndGetId(supply);
            System.out.println("Создана поставка #" + currentSupplyId);
        }

        // Добавляем компонент в БД
        Storage.addToWarehouse(type, name, quantity, currentSupplyId);
        
        // Обновляем список компонентов
        componentsList.add(String.format("%s: %s (%d шт)", type, name, quantity));
        System.out.println("Добавлен компонент: " + componentsList.getLast());

        // Очищаем поля
        nameField.setText("");
        quantitySpinner.setValue(1);

    } catch (Exception ex) {
        showError("Ошибка добавления: " + ex.getMessage());
        ex.printStackTrace();
    }
}

private void saveSupply() {
    if (componentsList.isEmpty()) {
        showError("Добавьте хотя бы один компонент!");
        return;
    }

    try {
        // Обновляем статус поставки
        Supply supply = new Supply(
            currentSupplyId,
            LocalDate.parse(dateField.getText()),
            true
        );
        Storage.updateSupply(supply);

        // Показываем результат
        showSuccess();

    } catch (Exception ex) {
        showError("Ошибка сохранения: " + ex.getMessage());
    } finally {
        resetState();
    }
}

private void showError(String message) {
    JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
}

private void showSuccess() {
    StringBuilder sb = new StringBuilder("Поставка #")
        .append(currentSupplyId)
        .append("\nСостав:\n");
    
    componentsList.forEach(item -> sb.append("• ").append(item).append("\n"));
    
    JOptionPane.showMessageDialog(frame, sb.toString(), "Успех", JOptionPane.INFORMATION_MESSAGE);
}

private void resetState() {
    currentSupplyId = -1;
    componentsList.clear();
    frame.dispose();
}
}
