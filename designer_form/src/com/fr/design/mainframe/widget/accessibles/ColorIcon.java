package com.fr.design.mainframe.widget.accessibles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class ColorIcon implements Icon {

    private static int BOX = 11;
    static Color BORDER_COLOR = new Color(172, 168, 153);
    private String name;
    private Color color;

    public ColorIcon(Color color) {
        this("[" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "]", color);
    }

    public ColorIcon(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, getIconWidth(), getIconHeight());
        g.setColor(BORDER_COLOR);
        g.drawRect(x, y, getIconWidth(), getIconHeight());
    }

    @Override
    public int getIconWidth() {
        return BOX;
    }

    @Override
    public int getIconHeight() {
        return BOX;
    }
}