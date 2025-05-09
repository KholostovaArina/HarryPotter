package com.mycompany.themagicshop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class SupplyWindow {
    public static void create() {
        List<Supply> supplies = Storage.getAllSupplies();
        System.out.println(supplies.getFirst().getDate());
        List<Warehouse> warehouseItems = Storage.getWarehouse();

        System.out.println(warehouseItems.getFirst().getName());
        String[] columns = {"ID","Дата поставки", "Обработано", "Состав поставки"};
        Object[][] data = new Object[supplies.size()][4];
        
        System.out.println(supplies.size());
        
        for(int i = 0; i < supplies.size(); i++) {
            Supply supply = supplies.get(i);
            int supplyId = supply.getId();
            
            // Получаем компоненты этой поставки
            String components = warehouseItems.stream()
                .filter(item -> item.getIdSupply() == supplyId)
                .map(item -> String.format("%s: %s (%d шт)", 
                     item.getType(), item.getName(), item.getQuantity()))
                .collect(Collectors.joining(", "));
            
            data[i][0] = supply.getId();
            data[i][1] = supply.getDate();
            data[i][2] = supply.getInWarehouse() ? "Да" : "Нет";
            data[i][3] = components.isEmpty() ? "Нет данных" : components;
        }

        JFrame frame = new JFrame("Список поставок");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setAutoCreateRowSorter(true);
        
        // Настройка отображения
        table.getColumnModel().getColumn(3).setPreferredWidth(400);
        JScrollPane scrollPane = new JScrollPane(table);
        
        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}