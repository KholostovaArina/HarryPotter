
package com.mycompany.themagicshop;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;

public class BeautyUtils {
    private static Font bigFont;
    private static Font miniFont;
    private static Color mildYellow = new Color(255,240,191);
    private static final Color MILD_PINK = new Color(245,221,246);
    
    static {
        try (InputStream fontStream = BeautyUtils.class.getResourceAsStream("/HarryPotter.ttf")) {
            bigFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            bigFont = bigFont.deriveFont(Font.BOLD, 24);
        } catch (IOException | FontFormatException e) {
            bigFont = new Font("Serif", Font.PLAIN, 22);
        }
    }
    
    static {
        try (InputStream fontStream = BeautyUtils.class.getResourceAsStream("/font.ttf")) {
            miniFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            miniFont = miniFont.deriveFont(16f);
        } catch (IOException | FontFormatException e) {
            miniFont = new Font("Serif", Font.PLAIN, 22);
        }
    }
    
    private static Image warehouseImage;
    private static Image wandImage;
    private static Image boxImage;
    private static Image skyImage;
    private static Image welcomeImage;
    
    static {
        try {
           //warehouseImage = ImageIO.read(BeautyUtils.class.getResourceAsStream("/склад.jpg"));
           // wandImage = ImageIO.read(BeautyUtils.class.getResourceAsStream("/палочка.jpg"));
            skyImage = ImageIO.read(BeautyUtils.class.getResourceAsStream("/небо.jpg"));
            welcomeImage = ImageIO.read(BeautyUtils.class.getResourceAsStream("/вход.png"));
          //  boxImage = ImageIO.read(BeautyUtils.class.getResourceAsStream("/поставка.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void playMusic() {
    try {
        InputStream is = BeautyUtils.class.getClassLoader().getResourceAsStream("music.mp3");
        if (is == null) throw new RuntimeException("Не найден ресурс: music.mp3");
        Player player = new Player(is);
        player.play();
    } catch (RuntimeException | JavaLayerException e) {
        e.printStackTrace();
    }
}

    
    public static Font getBigFont() {
        return bigFont;
    }
    
    public static Font getMiniFont() {
        return miniFont;
    }
     
    public static Image getWandImage() {
        return wandImage;
    }

    public static Image getWarehouseImage() {
        return warehouseImage;
    }

    public static Image getBoxImage() {
        return boxImage;
    }
    
    public static Image getSkyImage() {
        return skyImage;
    }
    
    public static Image getWelcomeImage() {
        return welcomeImage;
    }
    
    public static Color getMyYellow(){
        return mildYellow;
    }
    
    public static Color getMyPink(){
        return MILD_PINK;
    }
     
     public static void setFontForAllComponents(Container container) {
        for (Component component : container.getComponents()) {
            component.setFont(miniFont.deriveFont(14f));
           // component.setForeground(mildYellow);
            
            if (component instanceof Container) {
                Container container1 = (Container) component;
                setFontForAllComponents(container1);
            }
        }
    }

    public static JPanel createPanelWithPhoto(Image image) {
        if (image == null) {
            System.out.println("Изображение skyImage не загружено.");
            return (new JPanel(new BorderLayout())); 
        }

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setOpaque(false);
        return backgroundPanel;
    }
    
   private static void enableLastColumnWrap(JTable table) {
        int col = table.getColumnCount() - 1;
        table.getColumnModel().getColumn(col).setCellRenderer(new WrappingCellRenderer());
        updateRowHeights(table, col);
    }

    // Выставить корректную высоту строк по содержимому ПОСЛЕДНЕГО столбца (там где перенос)
    private static void updateRowHeights(JTable table, int columnToMeasure) {
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, columnToMeasure);
            Component comp = table.prepareRenderer(renderer, row, columnToMeasure);

            int rowHeight = comp.getPreferredSize().height;
            table.setRowHeight(row, rowHeight);
        }
    }

    // Рендерер для переноса текста в ячейке
    private static class WrappingCellRenderer extends JTextArea implements TableCellRenderer {
        public WrappingCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setFont(table.getFont());

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

            // ! Ключевой момент: подтянуть высоту строки
            setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
            int preferredHeight = getPreferredSize().height;
            if (table.getRowHeight(row) != preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }
            return this;
        }
    }
    
    public static void wrapTable(JTable table, int index){
        enableLastColumnWrap(table);
        updateRowHeights(table, index);
    }

}
