package com.mycompany.themagicshop;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class SupplyWindow {

    public static void create() {
        List<Supply> supplies = Storage.getAllSupplies();
        List<Warehouse> warehouseItems = Storage.getWarehouse();

        String[] columns = {"ID", "Дата поставки", "Обработано", "Состав поставки"};
        Object[][] data = new Object[supplies.size()][4];

        for (int i = 0; i < supplies.size(); i++) {
            Supply supply = supplies.get(i);
            int supplyId = supply.getId();

            List<Warehouse> componentsFromDb = warehouseItems.stream()
                    .filter(item -> item.getIdSupply() == supplyId)
                    .toList();

            List<Warehouse> componentsFromTemp = AddSupplyWindow.AddSupplyStorage.getComponents(supplyId);

            String components = "";
            if (!componentsFromDb.isEmpty()) {
                components = componentsFromDb.stream()
                        .map(item -> String.format("%s: %s (%d шт)", item.getType(), item.getName(), item.getQuantity()))
                        .collect(Collectors.joining(", "));
            } else {
                components = "Нет данных";
            }


            data[i][0] = supply.getId();
            data[i][1] = supply.getDate();
            data[i][2] = supply.getInWarehouse() ? "да" : "нет";
            data[i][3] = components;
        }

        JFrame frame = new JFrame("Список поставок");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(3).setPreferredWidth(450);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        BeautyUtils.setFontForAllComponents(frame);
        table.setEnabled(false);
        frame.setVisible(true);
    }
}