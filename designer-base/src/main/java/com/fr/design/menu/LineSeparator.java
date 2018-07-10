package com.fr.design.menu;

import com.fr.design.constants.UIConstants;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenuItem;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-5-10
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
public class LineSeparator extends UpdateAction{

    private  Color color = new Color(152, 152, 152);

    public LineSeparator() {

    }

    public LineSeparator(Color color) {
        this.color = color;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public UIMenuItem createMenuItem() {
        Object object = this.getValue(UIMenuItem.class.getName());
        UIMenuItem UIMenuItem = null;
        if (object == null && !(object instanceof UIMenuItem)) {
            UIMenuItem = new MenuItem();
            this.putValue(UIMenuItem.class.getName(), UIMenuItem);
            object = UIMenuItem;
        }

        return (UIMenuItem)object;
    }

    private class MenuItem extends UIMenuItem {
        public MenuItem() {
            this.setUI(null);
            this.removeAll();
        }

        public void paint(Graphics g) {
            int w = this.getWidth();
            int h = this.getHeight();
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(UIConstants.NORMAL_BACKGROUND);
            g2d.fillRect(0, 0, w, h);
            g2d.setColor(color);
            g2d.drawLine(4, h / 2+1, w-4, h / 2+1);
            this.setForeground(color);
            super.paint(g);
        }

        public Dimension getSize() {
            return new Dimension(super.getSize().width, 8);
        }

        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 8);
        }
    }
}