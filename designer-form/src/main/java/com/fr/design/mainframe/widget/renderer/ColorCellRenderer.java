package com.fr.design.mainframe.widget.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.fr.design.mainframe.widget.wrappers.ColorWrapper;
import com.fr.design.designer.properties.Encoder;

public class ColorCellRenderer extends GenericCellRenderer {

    private static Encoder wrapper = new ColorWrapper();
    private static int BOX = 12;
    private static int LEFT = 4;
    private static int ICON_TEXT_PAD = 4;
    private Color color;

    public ColorCellRenderer() {
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);
        int x = 0;
        int y = (height - BOX) / 2;
        if (color != null) {
            x += LEFT;
            g.setColor(color);
            g.fillRect(x, y, BOX, BOX);
            g.setColor(getForeground());
            g.drawRect(x, y, BOX, BOX);
            x += (BOX + ICON_TEXT_PAD);
        } else {
            g.setColor(getForeground());
        }
        FontMetrics fm = g.getFontMetrics();
        y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        String colorText = getColorText();
        g.drawString(colorText, x, y);
        if (getBorder() != null) {
            getBorder().paintBorder(this, g, 0, 0, width, height);
        }
    }

    private String getColorText() {
        return wrapper.encode(color);
    }

    @Override
    public void setValue(Object value) {
        Color color = (Color) value;
        this.color = color;
    }
}