package com.mycompany.themagicshop;

import javax.swing.*;
import java.awt.*;

public class SelectionWindow {
    private static final Color BACKGROUND_CASH_REGISTER = Color.WHITE;
    private static final Color BACKGROUND_WAREHOUSE = Color.GRAY;
    private static final Color BACKGROUND_SHIPMENT = Color.LIGHT_GRAY;
    private static final Color BACKGROUND_FOOTER = Color.DARK_GRAY;

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
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(createMainPanel(), BorderLayout.CENTER);
        contentPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        frame.add(contentPanel);       
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        mainPanel.add(createCashRegisterPanel());
        mainPanel.add(createWarehousePanel());
        mainPanel.add(createShipmentPanel());
        return mainPanel;
    }

    private JPanel createCashRegisterPanel() {
        return createSectionPanel("Прилавок", BACKGROUND_CASH_REGISTER, 
            sellButton, historyButton);
    }

    private JPanel createWarehousePanel() {
        return createSectionPanel("Склад", BACKGROUND_WAREHOUSE,
            readyWandsButton, createWandButton);
    }

    private JPanel createShipmentPanel() {
        return createSectionPanel("Поставки", BACKGROUND_SHIPMENT,
            viewShipmentsButton, addShipmentButton, updateShipmentButton);
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_FOOTER);
        panel.add(clearAllButton);
        return panel;
    }

    private JPanel createSectionPanel(String title, Color background, JComponent... components) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(background);
        
        JLabel label = new JLabel(title);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        
        for (JComponent component : components) {
            component.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(component);
            panel.add(Box.createVerticalStrut(10));
        }
        return panel;
    }

    public JButton getSellButton() { return sellButton; }
    public JButton getHistoryButton() { return historyButton; }
    public JButton getReadyWandsButton() { return readyWandsButton; }
    public JButton getCreateWandButton() { return createWandButton; }
    public JButton getViewShipmentsButton() { return viewShipmentsButton; }
    public JButton getAddShipmentButton() { return addShipmentButton; }
    public JButton getClearAllButton() { return clearAllButton; }
}
