package com.mycompany.themagicshop;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SelectionWindow {

    private final JFrame frame = new JFrame();
    private final JButton sellButton = new JButton("Продать палочку");
    private final JButton historyButton = new JButton("История покупок");
    private final JButton readyWandsButton = new JButton("Готовые палочки");
    private final JButton createWandButton = new JButton("Создать палочку");
    private final JButton viewShipmentsButton = new JButton("Смотреть поставки");
    private final JButton addShipmentButton = new JButton("Добавить поставку");
    private final JButton updateShipmentButton = new JButton("Обработать поставку");
    private final JButton clearAllButton = new JButton("Очистить всё");

    public SelectionWindow() {
        frame.setTitle("Лавка волшебных палочек «Олливандеры»");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        
        JPanel contentPanel = BeautyUtils.createPanelWithPhoto(BeautyUtils.getSkyImage());
        contentPanel.add(createMainPanel(), BorderLayout.CENTER);
        contentPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        setButtons();
        frame.add(contentPanel);
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        mainPanel.setOpaque(false);
        mainPanel.add(createCashRegisterPanel());
        mainPanel.add(createWarehousePanel());
        mainPanel.add(createShipmentPanel());
        return mainPanel;
    }

    private JPanel createCashRegisterPanel() {
        return createSectionPanel("Прилавок",
                new JComponent[]{sellButton, historyButton, new JLabel(), new JLabel(), new JLabel(), new JLabel()}, BeautyUtils.getWandImage());
    }

    private JPanel createWarehousePanel() {
        return createSectionPanel("Склад",
                new JComponent[]{readyWandsButton, createWandButton, new JLabel(), new JLabel(), new JLabel(), new JLabel()}, BeautyUtils.getWarehouseImage());
    }

    private JPanel createShipmentPanel() {
        return createSectionPanel("Поставки",
                new JComponent[]{viewShipmentsButton, addShipmentButton, updateShipmentButton}, BeautyUtils.getBoxImage());
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        clearAllButton.setBackground(BeautyUtils.getMyPink());
        clearAllButton.setFont(BeautyUtils.getMiniFont());
        panel.add(clearAllButton);
        return panel;
    }

    private JPanel createSectionPanel(String title, JComponent[] components, Image image) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Заголовок
        JLabel label = new JLabel(title);
        label.setForeground(BeautyUtils.getMyYellow());
        label.setFont(BeautyUtils.getBigFont());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));

        // Кнопки
        for (JComponent component : components) {
            component.setAlignmentX(Component.CENTER_ALIGNMENT);
            component.setBackground(BeautyUtils.getMyPink());
            component.setFont(BeautyUtils.getMiniFont());
            panel.add(component);
            panel.add(Box.createVerticalStrut(10));
        }

        // Добавляем изображение под кнопками
        if (image != null) {
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(15)); // отступ сверху
            panel.add(imageLabel);
        }

        return panel;
    }

    private void setButtons() {
        sellButton.addActionListener(e -> OrderWindow.create());
        createWandButton.addActionListener(e -> StickCreation.create());
        historyButton.addActionListener(e -> PurchaseHistoryWindow.create());
        readyWandsButton.addActionListener(e -> ReadyWandsWindow.create());
        viewShipmentsButton.addActionListener(e -> SupplyWindow.create());
        addShipmentButton.addActionListener(e -> AddSupplyWindow.create());
        updateShipmentButton.addActionListener(e -> ProcessSupplyWindow.create());
        clearAllButton.addActionListener(e -> clearAllData());
    }

    private void clearAllData() {
        int choice = JOptionPane.showConfirmDialog(null,
                "Вы уверены, что хотите удалить ВСЕ данные? Это действие нельзя отменить!",
                "Очистка базы данных",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try (Connection conn = PostgresConnecter.getConnection()) {
                conn.createStatement().executeUpdate("DELETE FROM magic_shop.magic_wand");
                conn.createStatement().executeUpdate("DELETE FROM magic_shop.warehouse");
                conn.createStatement().executeUpdate("DELETE FROM magic_shop.supply");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,
                    "Все данные успешно удалены!",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }
}