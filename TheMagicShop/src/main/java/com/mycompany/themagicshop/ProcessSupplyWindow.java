package com.mycompany.themagicshop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProcessSupplyWindow {
    public static void create() {
        JFrame frame = new JFrame("Обработка поставок");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        List<Supply> supplies = Storage.getAllSupplies();
        DefaultListModel<Supply> listModel = new DefaultListModel<>();
        supplies.forEach(listModel::addElement);

        JList<Supply> list = new JList<>(listModel);
        list.setCellRenderer(new SupplyListRenderer());

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    Supply selected = listModel.get(index);
                    processSupply(selected, frame, listModel);
                }
            }
        });

        frame.add(new JScrollPane(list), BorderLayout.CENTER);
        BeautyUtils.setFontForAllComponents(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void processSupply(Supply supply, JFrame parent, DefaultListModel<Supply> model) {
        if (supply.getInWarehouse()) {
            JOptionPane.showMessageDialog(parent,
                "Эта поставка уже обработана!",
                "Информация",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parent,
            "Перенести поставку #" + supply.getId() + " на склад?",
            "Подтверждение",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                List<Warehouse> components = AddSupplyWindow.AddSupplyStorage.getComponents(supply.getId());
                System.out.println("Компоненты для добавления на склад: " + components);

                if (components.isEmpty()) {
                    throw new RuntimeException("Нет компонентов для этой поставки.");
                }

                for (Warehouse component : components) {
                    Storage.addToWarehouse(
                        component.getType(),
                        component.getName(),
                        component.getQuantity(),
                        component.getIdSupply()
                    );
                }

                supply.setInWarehouse(true);
                Storage.updateSupply(supply);
                model.setElementAt(supply, model.indexOf(supply));

                AddSupplyWindow.AddSupplyStorage.clearComponents(supply.getId());

                if (AddSupplyWindow.isOpen()) {
                    AddSupplyWindow window = AddSupplyWindow.getInstance();
                    window.closeAndReset();
                }

                SwingUtilities.invokeLater(() -> {
                    SupplyWindow.create();
                });

                JOptionPane.showMessageDialog(parent,
                    "Поставка успешно перенесена на склад!",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent,
                    "Ошибка обработки поставки: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class SupplyListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Supply supply = (Supply) value;
            String status = supply.getInWarehouse() ? "[НА СКЛАДЕ] " : "[НЕ ОБРАБОТАНА] ";
            setText(status + "Поставка #" + supply.getId() + " от " + supply.getDate());
            setForeground(supply.getInWarehouse() ? Color.GRAY : Color.BLACK);
            return c;
        }
    }
}