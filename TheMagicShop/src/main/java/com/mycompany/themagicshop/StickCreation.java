package com.mycompany.themagicshop;

import java.sql.*;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class StickCreation {
    public static void create() {
        JPanel mainPanel = BeautyUtils.createPanelWithPhoto(BeautyUtils.getCreateImage());
        mainPanel.setLayout(null);

        List<Warehouse> warehouseItems = Storage.getWarehouse();

        List<String> bodies = filterWarehouseItems(warehouseItems, "корпус");
        List<String> cores = filterWarehouseItems(warehouseItems, "сердцевина");

        if (bodies.isEmpty() || cores.isEmpty()) {
            showMaterialError();
            return;
        }

        JFrame frame = createFrame();
        
        JComboBox<String> bodyComboBox = new JComboBox<>(bodies.toArray(new String[0]));
        JComboBox<String> coreComboBox = new JComboBox<>(cores.toArray(new String[0]));
        JButton createButton = createButton(frame, bodyComboBox, coreComboBox);

        setupComponents(mainPanel, bodyComboBox, coreComboBox, createButton);

        frame.add(mainPanel);
        BeautyUtils.setFontForAllComponents(frame);
        frame.setVisible(true);
    }

    private static List<String> filterWarehouseItems(List<Warehouse> items, String type) {
        return items.stream()
                .filter(item -> item.getType().equals(type))
                .map(Warehouse::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    private static void showMaterialError() {
        JOptionPane.showMessageDialog(null,
            "Недостаточно материалов на складе!",
            "Ошибка",
            JOptionPane.ERROR_MESSAGE);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Создание волшебной палочки");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private static JButton createButton(JFrame frame, JComboBox<String> bodyCombo, JComboBox<String> coreCombo) {
        JButton button = new JButton("Создать палочку");
        button.addActionListener(event -> handleCreation(frame, bodyCombo, coreCombo));
        return button;
    }

    private static void handleCreation(JFrame frame, JComboBox<String> bodyCombo, JComboBox<String> coreCombo) {
        String selectedBody = (String) bodyCombo.getSelectedItem();
        String selectedCore = (String) coreCombo.getSelectedItem();

        Connection conn = null;
        try {
            conn = PostgresConnecter.getConnection();
            conn.setAutoCommit(false);

            if (!processMaterials(conn, frame, selectedBody, selectedCore)) {
                return;
            }

            createWand(conn, selectedBody, selectedCore);
            conn.commit();

            showSuccessMessage(frame, selectedBody, selectedCore);
            frame.dispose();

        } catch (SQLException ex) {
            handleSQLException(conn, frame, ex);
        } finally {
            closeConnection(conn);
        }
    }

    private static boolean processMaterials(Connection conn, JFrame frame, String body, String core) throws SQLException {
        Warehouse bodyItem = Storage.getWarehouseItem("корпус", body);
        Warehouse coreItem = Storage.getWarehouseItem("сердцевина", core);

        if (bodyItem == null || coreItem == null || bodyItem.getQuantity() < 1 || coreItem.getQuantity() < 1) {
            JOptionPane.showMessageDialog(frame,
                "Недостаточно материалов!",
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Storage.updateWarehouse(bodyItem.getId(), bodyItem.getQuantity() - 1) ||
            !Storage.updateWarehouse(coreItem.getId(), coreItem.getQuantity() - 1)) {
            conn.rollback();
            JOptionPane.showMessageDialog(frame,
                "Ошибка списания материалов!",
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static void createWand(Connection conn, String body, String core) throws SQLException {
        Storage.addMagicWand(new MagicWand(0, body, core, "на складе", null, null));
    }

    private static void showSuccessMessage(JFrame frame, String body, String core) {
        JOptionPane.showMessageDialog(frame,
            "Палочка создана: " + body + " + " + core,
            "Успех",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void handleSQLException(Connection conn, JFrame frame, SQLException ex) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(frame,
            "Ошибка: " + ex.getMessage(),
            "Ошибка",
            JOptionPane.ERROR_MESSAGE);
    }

    private static void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void setupComponents(JPanel panel, JComboBox<String> bodyCombo, 
                                      JComboBox<String> coreCombo, JButton button) {
        JLabel bodyLabel = new JLabel("Выберите корпус:");
        bodyLabel.setBounds(10, 40, 150, 25);
        bodyCombo.setBounds(170, 40, 300, 25);

        JLabel coreLabel = new JLabel("Выберите сердцевину:");
        coreLabel.setBounds(10, 100, 150, 25);
        coreCombo.setBounds(170, 100, 300, 25);

        button.setBounds(150, 200, 200, 40);

        panel.add(bodyLabel);
        panel.add(bodyCombo);
        panel.add(coreLabel);
        panel.add(coreCombo);
        panel.add(button);
    }
}