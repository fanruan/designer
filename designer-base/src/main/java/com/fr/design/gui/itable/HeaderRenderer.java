package com.fr.design.gui.itable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTable;
import javax.swing.border.Border;

public class HeaderRenderer extends GroupRenderer {

    private static final Color GRID__COLOR = new Color(127, 157, 185);
    private static final Color CONTROL_COLOR = new Color(229, 229, 229);
    private HeaderBorder border = new HeaderBorder();

    public HeaderRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        border.setGridColor(GRID__COLOR);
        setBackground(CONTROL_COLOR);
        setText("  " + value);
        setBorder(border);
        return this;
    }

    private class HeaderBorder implements Border {

        private Color grid_color;

        public HeaderBorder() {
        }

        public void setGridColor(Color c) {
            grid_color = c;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color old = g.getColor();
            g.setColor(grid_color);
            g.drawLine(x + width - 1, y, x + width - 1, y + height);
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
            g.setColor(old);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 1, 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }
}