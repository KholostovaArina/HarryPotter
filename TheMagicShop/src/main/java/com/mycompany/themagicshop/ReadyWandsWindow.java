package com.mycompany.themagicshop;

import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

public class ReadyWandsWindow {
    public static void create() {
        List<MagicWand> readyWands = Storage.getAllWands().stream()
                .filter(w -> "на складе".equals(w.getStatus()))
                .toList();

        String[] columns = {"Волшебные палочки"};
        Object[][] data = new Object[readyWands.size()][1];
        
        for(int i = 0; i < readyWands.size(); i++) {
            MagicWand wand = readyWands.get(i);
            data[i][0] = wand.getCore() + " + " + wand.getCorpus();
        }

        JFrame frame = new JFrame("Готовые палочки на складе");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(300);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        frame.add(scrollPane);
        BeautyUtils.setFontForAllComponents(scrollPane);
        table.setEnabled(false);
        frame.setVisible(true);
    }
}