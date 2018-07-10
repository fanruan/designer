package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUIPaintUtils;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuUI;
import java.awt.*;

public class UIMenuUI extends BasicMenuUI {
    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        super.paintBackground(g, menuItem, bgColor);
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
        if (menuItem.isOpaque()) {
            if (menuItem.getParent() instanceof JPopupMenu && model.isSelected()) {
                g.setColor(UIConstants.UI_MENU_BACKGOURND);
                g.fillRect(0, 0, menuWidth, menuHeight);
                GUIPaintUtils.fillPaint((Graphics2D) g, 30, 0, menuWidth - 30, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, 7);
            } else if (model.isSelected()) {
                paintPressed(g, menuWidth, menuHeight);
            } else {
                paintRollOver(g, menuWidth, menuHeight);
            }
            g.setColor(oldColor);
        } else if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
            g.setColor(bgColor);
            g.fillRect(0, 0, menuWidth, menuHeight);
            g.setColor(oldColor);
        }
    }

    protected void paintPressed(Graphics g, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(UIConstants.UI_MENU_BACKGOURND);
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(UIConstants.LINE_COLOR);
        g2d.drawLine(0, 0, 0, h - 1);
        g2d.drawLine(w - 1, 0, w - 1, h - 1);
    }

    protected void paintRollOver(Graphics g, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(UIConstants.UI_MENU_BACKGOURND);
        g2d.fillRect(0, 0, w, h);
    }

    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        ButtonModel model = menuItem.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(menuItem, g);
        int mnemIndex = -1;

        if (!model.isEnabled()) {
            // *** paint the text disabled
            if (UIManager.get("MenuItem.disabledForeground") instanceof Color) {
                g.setColor(UIManager.getColor("MenuItem.disabledForeground"));
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        mnemIndex, textRect.x, textRect.y + fm.getAscent());
            } else {
                g.setColor(menuItem.getBackground().brighter());
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        mnemIndex, textRect.x, textRect.y + fm.getAscent());
                g.setColor(menuItem.getBackground().darker());
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        mnemIndex, textRect.x - 1, textRect.y +
                        fm.getAscent() - 1);
            }
        } else {
            // *** paint the text normally
            if (model.isArmed() || (menuItem instanceof JMenu && menuItem.isSelected() && menuItem.getIcon() != null)) {
                g.setColor(Color.WHITE); // Uses protected field.
            }
            SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                    mnemIndex, textRect.x, textRect.y + fm.getAscent());
        }
    }

}