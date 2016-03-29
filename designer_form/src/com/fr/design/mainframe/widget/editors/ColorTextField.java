package com.fr.design.mainframe.widget.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import com.fr.design.gui.itextfield.UITextField;

/**
 * 显示颜色的控件,能同时显示颜色和文字
 * @since 6.5.2
 */
public class ColorTextField extends JComponent implements ITextComponent {

    private static int BOX = 12;
    private static int LEFT = 4;
    private static int ICON_TEXT_PAD = 4;
    private Color color;
    private UITextField textField;

    public ColorTextField() {
        setLayout(new ColorTextFieldLayout());
        textField = new UITextField();
        textField.setBorder(null);
        textField.setOpaque(false);
        add(textField);
    }

    @Override
    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        int x = LEFT;
        int y = (height - BOX) / 2;
        if (getColor() != null) {
            g.setColor(getColor());
            g.fillRect(x, y, BOX, BOX);
            g.setColor(Color.black);
            g.drawRect(x, y, BOX, BOX);
            x += (BOX + ICON_TEXT_PAD);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    class ColorTextFieldLayout implements LayoutManager {

        public ColorTextFieldLayout() {
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int h = 22;
            int w = LEFT + BOX + ICON_TEXT_PAD + 60;
            return new Dimension(w, h);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            int h = 22;
            int w = LEFT + BOX + ICON_TEXT_PAD;
            return new Dimension(w, h);
        }

        @Override
        public void layoutContainer(Container parent) {
            int x = 0;
            int w = parent.getWidth();
            int h = parent.getHeight();
            if (getColor() != null) {
                x += LEFT + BOX + ICON_TEXT_PAD;
                w -= x;
            }
            textField.setBounds(x, 0, w, h);
        }
    }

    @Override
    public void setText(String text) {
        textField.setText(text);
    }

    @Override
    public String getText() {
        return textField.getText();
    }

    @Override
    public void setEditable(boolean editable) {
        textField.setEditable(editable);
        textField.setBackground(Color.white);
    }

    @Override
    public void requestFocus() {
        textField.requestFocus();
    }

    @Override
    public void addActionListener(ActionListener l) {
        textField.addActionListener(l);
    }

    @Override
    public void selectAll() {
        textField.selectAll();
    }

    @Override
    public void setValue(Object v) {
        color = (Color) v;
    }
}