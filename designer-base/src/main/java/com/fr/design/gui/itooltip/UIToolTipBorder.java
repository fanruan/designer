package com.fr.design.gui.itooltip;

import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-31
 * Time: 上午10:38
 */
public class UIToolTipBorder implements Border {

    private static final ColorUIResource TIP_BORDER_COLOR = new ColorUIResource(0, 0, 0);
    private static final ColorUIResource TIP_BORDER_DIS = new ColorUIResource(143, 141, 139);

    private static final InsetsUIResource INSETS = new InsetsUIResource(3, 3, 3, 3);
    private boolean active;

    public UIToolTipBorder(boolean b) {
        active = b;
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (active) {
            g.setColor(TIP_BORDER_COLOR);
        } else {
            g.setColor(TIP_BORDER_DIS);
        }

        g.drawRect(x, y, w - 1, h - 1);
    }

    public Insets getBorderInsets(Component c) {
        return INSETS;
    }
}