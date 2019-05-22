package com.fr.design.update.ui.widget;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class ColorfulCellRender extends JPanel implements ListCellRenderer {
    private static int CELLCOLOR_DARK = 0xf6f6f6;
    private static int CELLCOLOR_LIGHT = 0xffffff;
    private static int CELLCOLOR_SELECTED = 0xdfecfd;
    private static int TEXT_COORDINATE_X = 10;
    private static int TEXT_COORDINATE_Y = 20;
    private static final int LISTFONTSIZE = 12;
    private Color[] colors = new Color[]{new Color(CELLCOLOR_DARK), new Color(CELLCOLOR_LIGHT)};

    private String text;

    public ColorfulCellRender() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        text = ((null != value) ? (value.toString()) : null);

        Color background;
        Color foreground = Color.BLACK;
        //当前Renderer是否是拖拽目标
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = new Color(CELLCOLOR_SELECTED);
            //当前Renderer是否被选中
        } else if (isSelected) {
            background = new Color(CELLCOLOR_SELECTED);
        } else {
            background = colors[index % 2];
        }

        setBackground(background);
        setForeground(foreground);

        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        g.setFont(new Font("Default", Font.PLAIN, LISTFONTSIZE));
        if (text != null) {
            g.drawString(text, TEXT_COORDINATE_X, TEXT_COORDINATE_Y);
        }
    }
}