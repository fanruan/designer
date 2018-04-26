package com.fr.design.gui.iscrollbar;

import com.fr.general.ComparatorUtils;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-17
 * Time: 上午10:00
 */
public class UIScrollButton extends BasicArrowButton {

    // cache for already drawn icons - speeds up drawing by a factor
    // of 2.2 if there are several scroll buttons or one scroll button
    // is painted several times
    static HashMap cache = new HashMap();

    private boolean isRollover;

    private UIBasicScrollBarUI scrollbarUI;

    protected static Dimension size = new Dimension(17, 17);


    /**
     * Create a new ScrollButton.
     *
     * @see javax.swing.plaf.metal.MetalScrollButton#MetalScrollButton(int, int, boolean)
     */
    public UIScrollButton(int direction, UIBasicScrollBarUI scrollbarUI) {
        super(direction);

        this.scrollbarUI = scrollbarUI;
        setBorder(null);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        setSize(size);
    }

    /**
     * Paints the button
     *
     * @see java.awt.Component#paint(Graphics)
     */
    public void paint(Graphics g) {
        isRollover = false;
        Color c = null;

        if (!scrollbarUI.isThumbVisible()) {
            c = ThemeUtils.SCROLL_BUTTON_DISABLED_COLOR;
        } else if (getModel().isPressed()) {
            c = ThemeUtils.SCROLL_BUTTON_PRESSED_COLOR;
        } else if (getModel().isRollover() && ThemeUtils.SCROLL_ROLLOVER) {
            c = ThemeUtils.SCROLL_BUTTON_ROLLOVER_COLOR;
            isRollover = true;
        } else {
            c = ThemeUtils.SCROLL_BUTTON_COLOR;
        }

        g.setColor(c);

        drawXpButton(g, getSize(), c);

        // arrows depend on scrollbar's style
        if (!scrollbarUI.isThumbVisible()) {
            g.setColor(ThemeUtils.SCROLL_ARROW_DISABLED_COLOR);
        } else {
            g.setColor(ThemeUtils.SCROLL_ARROW_COLOR);
        }
        drawXpArrow(g, getSize());
    }

    private void drawXpButton(Graphics g, Dimension size, Color c) {
        boolean enabled = scrollbarUI.isThumbVisible();
        boolean pressed = getModel().isPressed();
        boolean rollover = getModel().isRollover() && ThemeUtils.SCROLL_ROLLOVER;
        ScrollButtonKey key = new ScrollButtonKey(
                (direction == NORTH || direction == SOUTH),
                c, pressed, enabled, rollover);
        Object value = cache.get(key);
        if (value != null) {
            // image was cached - paint image and return
            g.drawImage((Image) value, 0, 0, this);
            return;
        }
        Image img = new BufferedImage(17, 17, BufferedImage.TYPE_INT_ARGB);
        Graphics imgGraphics = img.getGraphics();
        switch (direction) {
            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:
                Color a = ThemeUtils.SCROLL_BORDER_COLOR;
                Graphics2D g2 = (Graphics2D) imgGraphics;
                GradientPaint scrollBarBg = null;
                if (!scrollbarUI.isThumbVisible()) {
                    scrollBarBg = new GradientPaint(1, 1, new Color(236, 236, 236), 1, 16, c);
                } else if (getModel().isPressed()) {
                    scrollBarBg = new GradientPaint(1, 1, new Color(0xd2d2d2), 1, 16, new Color(0xd2d2d2));
                } else if (getModel().isRollover() && ThemeUtils.SCROLL_ROLLOVER) {
                    scrollBarBg = new GradientPaint(1, 1, new Color(0xd2d2d2), 1, 16, new Color(0xd2d2d2));
                } else {
                    scrollBarBg = new GradientPaint(1, 1, new Color(0xffffff), 1, 16, new Color(0xffffff));
                }
                g2.setPaint(scrollBarBg);
                g2.fillRoundRect(1, 1, 16, 16, 0, 0);
                break;
        }
        // dispose of image graphics
        imgGraphics.dispose();
        // draw the image
        g.drawImage(img, 0, 0, this);
        // add the image to the cache
        cache.put(key, img);
    }


    private void drawXpArrow(Graphics g, Dimension size) {
        switch (direction) {
            case SwingConstants.NORTH:
                g.drawLine(8, 5, 8, 5);
                g.drawLine(7, 6, 9, 6);
                g.drawLine(6, 7, 10, 7);
                g.drawLine(5, 8, 7, 8);
                g.drawLine(9, 8, 11, 8);
                g.drawLine(4, 9, 6, 9);
                g.drawLine(10, 9, 12, 9);
                g.drawLine(5, 10, 5, 10);
                g.drawLine(11, 10, 11, 10);
                break;
            case SwingConstants.SOUTH:
                g.drawLine(5, 6, 5, 6);
                g.drawLine(11, 6, 11, 6);
                g.drawLine(4, 7, 6, 7);
                g.drawLine(10, 7, 12, 7);
                g.drawLine(5, 8, 7, 8);
                g.drawLine(9, 8, 11, 8);
                g.drawLine(6, 9, 10, 9);
                g.drawLine(7, 10, 9, 10);
                g.drawLine(8, 11, 8, 11);
                break;
            case SwingConstants.EAST:
                g.drawLine(6, 5, 6, 5);
                g.drawLine(6, 11, 6, 11);
                g.drawLine(7, 4, 7, 6);
                g.drawLine(7, 10, 7, 12);
                g.drawLine(8, 5, 8, 7);
                g.drawLine(8, 9, 8, 11);
                g.drawLine(9, 6, 9, 10);
                g.drawLine(10, 7, 10, 9);
                g.drawLine(11, 8, 11, 8);
                break;
            case SwingConstants.WEST:
                g.drawLine(4, 8, 4, 8);
                g.drawLine(5, 7, 5, 9);
                g.drawLine(6, 6, 6, 10);
                g.drawLine(7, 5, 7, 7);
                g.drawLine(7, 9, 7, 11);
                g.drawLine(8, 4, 8, 6);
                g.drawLine(8, 10, 8, 12);
                g.drawLine(9, 5, 9, 5);
                g.drawLine(9, 11, 9, 11);
                break;
        }
    }

    /**
     * Returns the preferred size of the component wich is the size of the skin
     *
     * @see java.awt.Component#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        return size;
    }

    /*
     * ScrollButtonKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     */
    static class ScrollButtonKey {

        private boolean vertical;
        private Color c;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;

        ScrollButtonKey(boolean vertical, Color c,
                        boolean pressed, boolean enabled, boolean rollover) {
            this.vertical = vertical;
            this.c = c;
            this.pressed = pressed;
            this.enabled = enabled;
            this.rollover = rollover;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof ScrollButtonKey)) {
                return false;
            }

            ScrollButtonKey other = (ScrollButtonKey) o;
            return ComparatorUtils.equals(vertical, other.vertical) &&
                    ComparatorUtils.equals(pressed, other.pressed) &&
                    ComparatorUtils.equals(enabled, other.enabled) &&
                    ComparatorUtils.equals(rollover, other.rollover) &&
                    ComparatorUtils.equals(c, other.c);
        }

        public int hashCode() {
            return c.hashCode() *
                    (pressed ? 1 : 2) *
                    (enabled ? 4 : 8) *
                    (rollover ? 16 : 32) *
                    (vertical ? 64 : 128);
        }
    }
}