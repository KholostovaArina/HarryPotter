package com.mycompany.themagicshop;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddSupplyWindow {
    private int currentSupplyId = -1;
    private List<Warehouse> tempComponents = new ArrayList<>();
    private JFrame frame;
    private JTextField nameField;
    private JSpinner quantitySpinner;
    private JTextField dateField;
    private JComboBox<String> typeCombo;

    // Для отслеживания открытого окна
    private static boolean isOpen = false;
    private static AddSupplyWindow instance;

    public static void create() {
        if (isOpen) return; // Не открываем второе окно
        new AddSupplyWindow().initializeUI();
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static AddSupplyWindow getInstance() {
        return instance;
    }

    private void initializeUI() {
        isOpen = true;
        instance = this;

        frame = new JFrame("Новая поставка");
        frame.setSize(450, 300);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        // Панель ввода данных
        JPanel panel = BeautyUtils.createPanelWithPhoto(BeautyUtils.getBuyImage());
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setOpaque(false);
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

        addButton.addActionListener(e -> addComponent());
        saveButton.addActionListener(e -> saveSupply());

        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.setOpaque(false);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        //frame.add(panel);

        BeautyUtils.setFontForAllComponents(frame);
        frame.setVisible(true);
    }

    private void addComponent() {
        String type = (String) typeCombo.getSelectedItem();
        String name = nameField.getText().trim();
        nameField.setText("");
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
            }

            // Сохраняем компонент во временный список
            tempComponents.add(new Warehouse(
                    0,
                    type,
                    name,
                    quantity,
                    currentSupplyId
            ));

            JOptionPane.showMessageDialog(frame, "Компонент добавлен!", "Информация", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException ex) {
            showError("Ошибка добавления компонента: " + ex.getMessage());
        }
    }

    private void saveSupply() {
        if (tempComponents.isEmpty()) {
            showError("Добавьте хотя бы один компонент!");
            return;
        }

        try {
            // Сохраняем компоненты во временное хранилище
            AddSupplyStorage.addComponents(currentSupplyId, tempComponents);
            showSuccess();
        } catch (Exception ex) {
            showError("Ошибка сохранения поставки: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess() {
        StringBuilder sb = new StringBuilder("Поставка #")
                .append(currentSupplyId)
                .append("\nСостав:\n");
        for (Warehouse component : tempComponents) {
            sb.append("• ")
              .append(component.getType())
              .append(": ")
              .append(component.getName())
              .append(" (")
              .append(component.getQuantity())
              .append(" шт)\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    public void closeAndReset() {
        resetState();
    }

    private void resetState() {
        isOpen = false;
        currentSupplyId = -1;
        tempComponents.clear();
        frame.dispose();
    }

    // Временное хранилище для компонентов поставок
    public static class AddSupplyStorage {
        private static final java.util.Map<Integer, List<Warehouse>> supplyComponents = new java.util.HashMap<>();

        public static void addComponents(int supplyId, List<Warehouse> components) {
            supplyComponents.put(supplyId, components);
        }

        public static List<Warehouse> getComponents(int supplyId) {
            return supplyComponents.getOrDefault(supplyId, new ArrayList<>());
        }

        public static void clearComponents(int supplyId) {
            supplyComponents.remove(supplyId);
        }
    }
}