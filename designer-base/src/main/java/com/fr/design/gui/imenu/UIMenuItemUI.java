package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.stable.Constants;
import sun.swing.SwingUtilities2;

import javax.swing.ButtonModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * created by Harrison on 2020/03/22
 **/
public class UIMenuItemUI extends BasicMenuItemUI {
    
    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        if (menuItem.getIcon() == null) {
            super.paintBackground(g, menuItem, bgColor);
            return;
        }
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
    
        g.setColor(UIConstants.NORMAL_BACKGROUND);
        g.fillRect(0, 0, menuWidth, menuHeight);
        if (menuItem.isOpaque()) {
            if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
                GUIPaintUtils.fillPaint((Graphics2D) g, 30, 0, menuWidth - 30, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, 7);
            } else {
                GUIPaintUtils.fillPaint((Graphics2D) g, 30, 0, menuWidth - 30, menuHeight, true, Constants.NULL, menuItem.getBackground(), 7);
            }
            g.setColor(oldColor);
        } else if (model.isArmed() || (menuItem instanceof JMenu &&
                model.isSelected())) {
            GUIPaintUtils.fillPaint((Graphics2D) g, 30, 0, menuWidth - 30, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, 7);
            g.setColor(oldColor);
        }
    }
    
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        ButtonModel model = menuItem.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(menuItem, g);
        int mnemIndex = menuItem.getDisplayedMnemonicIndex();
        
        if (!model.isEnabled()) {
            // *** paint the text disabled
            if (UIManager.get("MenuItem.disabledForeground") instanceof Color) {
                g.setColor(UIManager.getColor("MenuItem.disabledForeground"));
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        -1, textRect.x, textRect.y + fm.getAscent());
            } else {
                g.setColor(menuItem.getBackground().brighter());
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        -1, textRect.x, textRect.y + fm.getAscent());
                g.setColor(menuItem.getBackground().darker());
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        -1, textRect.x - 1, textRect.y +
                                fm.getAscent() - 1);
            }
        } else {
            // *** paint the text normally
            if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
                g.setColor(Color.WHITE); // Uses protected field.
            }
            SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                    -1, textRect.x, textRect.y + fm.getAscent());
        }
    }
    
}
