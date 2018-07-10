package com.fr.design.cell.bar;

import com.fr.design.constants.UIConstants;
import com.fr.general.ComparatorUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-30
 * Time: 下午3:22
 */
public class DynamicScrollButton extends BasicArrowButton {


    private DynamicScrollBarUI scrollbarUI;


    static HashMap cache = new HashMap();

    private Dimension buttonSize = new Dimension(17, 17);

    public DynamicScrollButton(int direction, DynamicScrollBarUI scrollbarUI) {
        super(direction);

        this.scrollbarUI = scrollbarUI;
        setBorder(null);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        setSize(buttonSize);
    }

    public void paint(Graphics g) {
        Color c = null;

        if (!scrollbarUI.isThumbVisible()) {
            c = ScrollBarUIConstant.DISABLE_SCROLL_BAR_COLOR;
        } else if (getModel().isPressed()) {
            c = ScrollBarUIConstant.PRESS_SCROLL_BAR_COLOR;
        } else if (getModel().isRollover()) {
            c = ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR;
        } else {
            c = ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR;
        }

        g.setColor(c);
        paintButton(g, getSize(), c);
        if (!scrollbarUI.isThumbVisible()) {
            g.setColor(ScrollBarUIConstant.ARROW_DISABLED_COLOR);
        } else {
            g.setColor(ScrollBarUIConstant.SCROLL_ARROW_COLOR);
        }

        paintArrow(g, getSize());
    }


    private void paintArrow(Graphics g, Dimension size) {
        switch (direction) {
            case SwingConstants.NORTH:
                g.drawImage(UIConstants.ARROW_NORTH, 0, 0, this);
                break;
            case SwingConstants.SOUTH:
                g.drawImage(UIConstants.ARROW_SOUTH, 0, 0, this);
                break;
            case SwingConstants.EAST:
                g.drawImage(UIConstants.ARROW_EAST, 0, 0, this);
                break;
            case SwingConstants.WEST:
                g.drawImage(UIConstants.ARROW_WEST, 0, 0, this);
                break;
        }
    }


    private void paintButton(Graphics g, Dimension size, Color c) {
        boolean enabled = scrollbarUI.isThumbVisible();
        boolean pressed = getModel().isPressed();
        boolean rollover = getModel().isRollover();
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
                Color a = ScrollBarUIConstant.SCROLL_BORDER_COLOR;
                Graphics2D g2 = (Graphics2D) imgGraphics;
                GradientPaint scrollBarBg = null;
                if (!scrollbarUI.isThumbVisible()) {
                    scrollBarBg = new GradientPaint(1, 1, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR, 1, 16, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR);
                } else if (getModel().isPressed()) {
                    scrollBarBg = new GradientPaint(1, 1, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR, 1, 16, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR);
                } else if (getModel().isRollover()) {
                    scrollBarBg = new GradientPaint(1, 1, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR, 1, 16, ScrollBarUIConstant.PRESSED_SCROLL_BUTTON_COLOR);
                } else {
                    scrollBarBg = new GradientPaint(1, 1, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR, 1, 16, ScrollBarUIConstant.NORMAL_SCROLL_BUTTON_COLOR);
                }
                //GradientPaint scrollBarHight = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.5f), 0, 7, new Color(1.0f, 1.0f, 1.0f, 0.2f));
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

            return vertical == other.vertical &&
                    pressed == other.pressed &&
                    enabled == other.enabled &&
                    rollover == other.rollover &&
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