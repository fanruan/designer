package com.fr.design.gui.imenu;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-1-2
 * Time: 上午10:10
 */
public class UIPopupMenuBorder extends AbstractBorder implements UIResource {

    /**
     * Draws a simple 3d border for the given component.
     *
     * @param mainColor The component to draw its border.
     * @param g         The graphics context.
     * @param x         The x coordinate of the top left corner.
     * @param y         The y coordinate of the top left corner.
     * @param w         The width.
     * @param h         The height.
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

        g.translate(x, y);
        // Inner highlight:
        g.setColor(new ColorUIResource(255, 255, 255));
        g.drawLine(1, 1, w - 3, 1);
        g.drawLine(1, 1, 1, h - 3);

        // Inner shadow:
        g.setColor(new ColorUIResource(213, 212, 207));
        g.drawLine(w - 2, 1, w - 2, h - 2);
        g.drawLine(1, h - 2, w - 2, h - 2);

        // Outer highlight:
        g.setColor(new ColorUIResource(173, 170, 153));
        g.drawLine(0, 0, w - 2, 0);
        g.drawLine(0, 0, 0, h - 1);

        // Outer shadow:
        g.setColor(new ColorUIResource(173, 170, 153));
        g.drawLine(w - 1, 0, w - 1, h - 1);
        g.drawLine(0, h - 1, w - 1, h - 1);
        g.translate(-x, -y);

    }

    /**
     * Gets the border insets for a given component.
     *
     * @param mainColor The component to get its border insets.
     * @return Always returns the same insets as defined in <code>insets</code>.
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(2, 2, 2, 2);
    }
}