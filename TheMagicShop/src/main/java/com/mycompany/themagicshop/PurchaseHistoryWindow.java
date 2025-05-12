package com.mycompany.themagicshop;

import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

public class PurchaseHistoryWindow {
    public static void create() {
        List<MagicWand> soldWands = Storage.getAllWands().stream()
                .filter(w -> "продана".equals(w.getStatus()))
                .toList();

        String[] columns = {"Палочка (сердцевина+корпус)", "Покупатель", "Дата продажи"};
        Object[][] data = new Object[soldWands.size()][3];
        
        for(int i = 0; i < soldWands.size(); i++) {
            MagicWand wand = soldWands.get(i);
            data[i][0] = wand.getCore() + "+" + wand.getCorpus();
            data[i][1] = wand.getOwnerName() != null ? wand.getOwnerName() : "Не указано";
            data[i][2] = wand.getPurchaseDate();
        }

        // Создаем GUI
        JFrame frame = new JFrame("История заказов");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        
        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setFillsViewportHeight(true);
        
        // Настройка отображения
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoCreateRowSorter(true);
        
        // Центрирование данных
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(220);

        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);
        
        BeautyUtils.setFontForAllComponents(scrollPane);
        table.setEnabled(false);
        
        frame.setVisible(true);
    }
}
