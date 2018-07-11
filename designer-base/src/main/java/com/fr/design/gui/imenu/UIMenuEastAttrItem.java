package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * 右侧属性面板悬浮元素、超级链接弹窗重绘
 */
public class UIMenuEastAttrItem extends JMenuItem {

    public UIMenuEastAttrItem() {
        this(StringUtils.BLANK);
    }

    public UIMenuEastAttrItem(String string) {
        this(string, null);

    }

    public UIMenuEastAttrItem(String string, Icon pageSmallIcon) {
        super(string, pageSmallIcon);
        setBackground(UIConstants.NORMAL_BACKGROUND);
        setUI(new UIMenuItemEastAttrUI());
    }

    public UIMenuEastAttrItem(String string, int key) {
        super(string, key);
        setBackground(UIConstants.NORMAL_BACKGROUND);
        setUI(new UIMenuItemEastAttrUI());
    }

    public UIMenuEastAttrItem(Action action) {
        this();
        setAction(action);
    }

    @Override
    public String getText() {
        return StringUtils.BLANK + super.getText();
    }

    private class UIMenuItemEastAttrUI extends BasicMenuItemUI {
        @Override
        protected void paintBackground(Graphics g, JMenuItem menuItem,Color bgColor) {
            if(menuItem.getIcon() != null) {
                menuItem.setIcon(null);
            }

            ButtonModel model = menuItem.getModel();
            Color oldColor = g.getColor();
            int menuWidth = menuItem.getWidth();
            int menuHeight = menuItem.getHeight();
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillRect(0, 0, menuWidth, menuHeight);

            if (menuItem.isOpaque()) {
                if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
                    GUIPaintUtils.fillPaint((Graphics2D) g, 0, 0, menuWidth, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, 0);
                } else {
                    GUIPaintUtils.fillPaint((Graphics2D) g, 0, 0, menuWidth, menuHeight, true, Constants.NULL, UIConstants.TOOLBARUI_BACKGROUND, 0);
                }
                g.setColor(oldColor);
            }
            else if (model.isArmed() || (menuItem instanceof JMenu &&
                    model.isSelected())) {
                GUIPaintUtils.fillPaint((Graphics2D)g, 0, 0, menuWidth, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, 7);
                g.setColor(oldColor);
            }
        }

        protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
            ButtonModel model = menuItem.getModel();
            FontMetrics fm = SwingUtilities2.getFontMetrics(menuItem, g);

            if(!model.isEnabled()) {
                // *** paint the text disabled
                if ( UIManager.get("MenuItem.disabledForeground") instanceof Color ) {
                    g.setColor( UIManager.getColor("MenuItem.disabledForeground") );
                    SwingUtilities2.drawStringUnderlineCharAt(menuItem, g,text,
                            -1, textRect.x,  textRect.y + fm.getAscent());
                } else {
                    g.setColor(menuItem.getBackground().brighter());
                    SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                            -1, textRect.x, textRect.y + fm.getAscent());
                    g.setColor(menuItem.getBackground().darker());
                    SwingUtilities2.drawStringUnderlineCharAt(menuItem, g,text,
                            -1,  textRect.x - 1, textRect.y +
                                    fm.getAscent() - 1);
                }
            } else {
                menuItem.setForeground(isArmed() ? Color.white : Color.black);
                SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                        -1, 0, textRect.y + fm.getAscent());
            }
        }
    }
}