/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.icontainer;

import com.fr.design.utils.ThemeUtils;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-5
 * Time: 下午2:25
 */
public class UIScrollPaneBorder extends AbstractBorder implements UIResource {

    private static final Insets DEFAULT_INSETS = new Insets(1, 1, 1, 1);

    public Insets getBorderInsets(Component c) {
        return DEFAULT_INSETS;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        g.setColor(ThemeUtils.SCROLL_PANE_BORDER_COLOR);
        g.drawRect(x, y, w - 1, h - 1);
    }

}