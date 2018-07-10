package com.fr.design.gui.itoolbar;

import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-14
 * Time: 下午5:25
 */
public class UIToolBarSeparatorUI extends BasicToolBarSeparatorUI {

    private static final int YQ_SIZE = 7;
    private int defaultSize = YQ_SIZE;

    /**
     * 创建组件UI
     *
     * @param c 组件
     * @return 组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIToolBarSeparatorUI();
    }

    /**
     * Overridden to do nothing
     */
    protected void installDefaults(JSeparator s) {
    }

    public Dimension getMinimumSize(JComponent c) {
        JToolBar.Separator sep = (JToolBar.Separator) c;

        if (sep.getOrientation() == JSeparator.HORIZONTAL) {
            return new Dimension(0, 1);
        } else {
            return new Dimension(1, 0);
        }
    }

    public Dimension getMaximumSize(JComponent c) {
        JToolBar.Separator sep = (JToolBar.Separator) c;
        Dimension size = sep.getSeparatorSize();

        if (sep.getOrientation() == JSeparator.HORIZONTAL) {
            if (size != null) {
                return new Dimension(32767, size.height);
            }
            return new Dimension(32767, defaultSize);
        } else {
            if (size != null) {
                return new Dimension(32767, size.width);
            }

            return new Dimension(defaultSize, 32767);
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        JToolBar.Separator sep = (JToolBar.Separator) c;

        Dimension size = sep.getSeparatorSize();

        if (size != null) {
            return size.getSize();
        }

        if (sep.getOrientation() == JSeparator.HORIZONTAL) {
            return new Dimension(0, defaultSize);
        } else {
            return new Dimension(defaultSize, 0);
        }
    }

    public void paint(Graphics g, JComponent c) {
        JToolBar.Separator sep = (JToolBar.Separator) c;

        if (sep.getOrientation() == JSeparator.HORIZONTAL) {
            int y = sep.getHeight() / 2;    // centered if height is odd

            g.setColor(ThemeUtils.TOOL_SEP_DARK_COLOR);
            g.drawLine(0, y, sep.getWidth(), y);
        } else {
            int x = sep.getWidth() / 2;    // centered if width is odd

            g.setColor(ThemeUtils.TOOL_SEP_DARK_COLOR);
            g.drawLine(x, 0, x, sep.getHeight());
        }

    }
}