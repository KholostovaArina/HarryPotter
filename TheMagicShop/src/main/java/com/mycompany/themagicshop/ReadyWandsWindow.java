package com.mycompany.themagicshop;

import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

public class ReadyWandsWindow {
    public static void create() {
        // Получаем палочки на складе
        List<MagicWand> readyWands = Storage.getAllWands().stream()
                .filter(w -> "на складе".equals(w.getStatus()))
                .toList();

        // Создаем данные для таблицы
        String[] columns = {"Волшебные палочки"};
        Object[][] data = new Object[readyWands.size()][1];
        
        for(int i = 0; i < readyWands.size(); i++) {
            MagicWand wand = readyWands.get(i);
            data[i][0] = wand.getCore() + " + " + wand.getCorpus();
        }

        // Настраиваем окно
        JFrame frame = new JFrame("Готовые палочки на складе");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Создаем таблицу с возможностью сортировки
        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setAutoCreateRowSorter(true);
        
        // Настраиваем отображение
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(300);

        // Центрируем текст
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        frame.add(scrollPane);
        frame.setVisible(true);
    }
}