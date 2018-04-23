package com.fr.design.gui.icheckbox;

import com.fr.general.ComparatorUtils;
import com.fr.design.utils.ThemeUtils;
import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.DrawRoutines;

import javax.swing.*;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午2:53
 */
public class UICheckBoxIcon extends MetalCheckBoxIcon {

    // cache for already drawn icons - speeds up drawing by A factor
    // of up to 100 if there are several check boxes or one check box
    // is painted several times


    private static HashMap cache = new HashMap();

    private static final int[][] A = {
            {255, 255, 255, 242, 228, 209, 187, 165, 142, 120, 104},
            {255, 255, 242, 228, 209, 187, 165, 142, 120, 104, 86},
            {255, 242, 228, 209, 187, 165, 142, 120, 104, 86, 72},
            {242, 228, 209, 187, 165, 142, 120, 104, 86, 72, 56},
            {228, 209, 187, 165, 142, 120, 104, 86, 72, 56, 42},
            {209, 187, 165, 142, 120, 104, 86, 72, 56, 42, 28},
            {187, 165, 142, 120, 104, 86, 72, 56, 42, 28, 17},
            {165, 142, 120, 104, 86, 72, 56, 42, 28, 17, 9},
            {142, 120, 104, 86, 72, 56, 42, 28, 17, 9, 0},
            {120, 104, 86, 72, 56, 42, 28, 17, 9, 0, 0},
            {104, 86, 72, 56, 42, 28, 17, 9, 0, 0, 0}
    };

    protected int getControlSize() {
        return getIconWidth();
    }

    /**
     * Draws the check box icon at the specified location.
     *
     * @param c The component to draw on.
     * @param g The graphics context.
     * @param x The x coordinate of the top left corner.
     * @param y The y coordinate of the top left corner.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        AbstractButton button = (AbstractButton) c;
        Color col = null;

        if (!button.isEnabled()) {
            col = ThemeUtils.BUTTON_DISABLE_COLOR;
        } else if (button.getModel().isPressed()) {
            if (button.getModel().isRollover()) {
                col = ThemeUtils.BUTTON_PRESS_COLOR;
            } else {
                col = ThemeUtils.BUTTON_NORMAL_COLOR;
            }
        } else if (button.getModel().isRollover()) {
            col = ThemeUtils.BUTTON_ROLLOVER_BG_COLOR;
        } else {
            col = ThemeUtils.BUTTON_NORMAL_COLOR;
        }

        g.setColor(col);

        drawXpCheck(g, button, col, x, y, getIconWidth(), getIconHeight());


        // checkmark
        if (!button.isSelected()) {
            return;
        }
        if (!button.isEnabled()) {
            g.setColor(ThemeUtils.BUTTON_CHECK_DISABLE_COLOR);
        } else {
            g.setColor(ThemeUtils.BUTTON_CHECK_COLOR);
        }

        drawXpCheckMark(g, x, y);

    }

    private void drawXpCheck(Graphics g, AbstractButton b, Color c,
                             int x, int y, int w, int h) {
        boolean pressed = b.getModel().isPressed();
        boolean armed = b.getModel().isArmed();
        boolean enabled = b.isEnabled();
        boolean rollover = b.getModel().isRollover();
        boolean focused = ThemeUtils.BUTTON_FOCUS_BORDER && !rollover && b.isFocusOwner();

        // In 1.3.5 key was build with argument rollover instead of (rollover || armed)
        // Fixed in 1.3.6
        CheckKey key = new CheckKey(c, pressed, enabled, (rollover || armed), focused);
        Object value = cache.get(key);

        if (value != null) {
            // image already cached - paint image and return
            g.drawImage((Image) value, x, y, b);
            return;
        }

        Image img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);


        // spread light is between 0 and 20
        int spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT;
        int spread2 = ThemeUtils.BUTTON_SPREAD_DARK;

        if (!b.isEnabled()) {
            spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT_DISABLE;
            spread2 = ThemeUtils.BUTTON_SPREAD_DARK_DISABLE;
        }

        int spreadStep1 = spread1 * 5;    // 20 -> 100
        // this means, we can never fully darken background,
        // but we also want it bright enough
        int spreadStep2 = spread2 * 4;    // 20 -> 80
        boolean isRolloverOrArmed = rollover || armed;
        if (pressed && isRolloverOrArmed) {
            spreadStep2 *= 2;
        }
        c = ColorRoutines.lighten(c, spreadStep1);
        drawImage(c, spreadStep2, img, b, w, h, rollover, pressed, focused);

        // draw the image
        g.drawImage(img, x, y, b);

        // add the image to the cache
        cache.put(key, img);
    }

    private void drawImage(Color c, int spreadStep2, Image img, AbstractButton b, int w, int h,
                           boolean rollover, boolean pressed, boolean focused) {
        Graphics imgGraphics = img.getGraphics();

        imgGraphics.setColor(ColorRoutines.darken(c, spreadStep2));
        imgGraphics.fillRect(1, 1, w - 2, h - 2);
        Color color;

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255 - A[col][row]);
                imgGraphics.setColor(color);
                imgGraphics.drawLine(col + 1, row + 1, col + 1, row + 1);
            }
        }

        // border
        if (!b.isEnabled()) {
            imgGraphics.setColor(ThemeUtils.BUTTON_BORDER_DISABLE_COLOR);
            imgGraphics.drawRect(0, 0, w - 1, h - 1);
        } else {
            imgGraphics.setColor(ThemeUtils.BUTTON_BORDER_COLOR);
            imgGraphics.drawRect(0, 0, w - 1, h - 1);

            if (rollover && ThemeUtils.BUTTON_ROLLOVER && !pressed) {
                DrawRoutines.drawRolloverCheckBorder(imgGraphics,
                        ThemeUtils.BUTTON_ROLLOVER_COLOR, 0, 0, w, h);
            } else if (focused && !pressed) {
                DrawRoutines.drawRolloverCheckBorder(imgGraphics,
                        ThemeUtils.BUTTON_DEFAULT_COLOR, 0, 0, w, h);
            }
        }

        // dispose of image graphics
        imgGraphics.dispose();
    }

    private void drawXpCheckMark(Graphics g, int x, int y) {
        g.drawLine(x + 3, y + 5, x + 3, y + 7);
        g.drawLine(x + 4, y + 6, x + 4, y + 8);
        g.drawLine(x + 5, y + 7, x + 5, y + 9);
        g.drawLine(x + 6, y + 6, x + 6, y + 8);
        g.drawLine(x + 7, y + 5, x + 7, y + 7);
        g.drawLine(x + 8, y + 4, x + 8, y + 6);
        g.drawLine(x + 9, y + 3, x + 9, y + 5);
    }

    public int getIconWidth() {
        return ThemeUtils.CHECK_SIZE.width;
    }

    public int getIconHeight() {
        return ThemeUtils.CHECK_SIZE.height;
    }

    /*
     * EnabledCheckKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     * Used only if we are run from ControlPanel.
     */
    protected static class EnabledCheckKey {
        public int spread1;
        public int spread2;
        public Color c, back;

        EnabledCheckKey(Color c, Color back) {
            spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT;
            spread2 = ThemeUtils.BUTTON_SPREAD_DARK;
            this.c = c;
            this.back = back;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof EnabledCheckKey)) {
                return false;
            }

            EnabledCheckKey other = (EnabledCheckKey) o;
            return ComparatorUtils.equals(c, other.c) &&
                    ComparatorUtils.equals(back, other.back) &&
                    ComparatorUtils.equals(spread1, other.spread1) &&
                    ComparatorUtils.equals(spread2, other.spread2);
        }

        public int hashCode() {
            return c.hashCode() * back.hashCode() * spread1 * spread2;
        }
    }

    /*
     * DisabledCheckKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     * Used only if we are run from ControlPanel.
     */
    protected static class DisabledCheckKey {
        public int spread1;
        public int spread2;
        public Color c, back;

        DisabledCheckKey(Color c, Color back) {
            spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT_DISABLE;
            spread2 = ThemeUtils.BUTTON_SPREAD_DARK_DISABLE;
            this.c = c;
            this.back = back;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof DisabledCheckKey)) {
                return false;
            }

            DisabledCheckKey other = (DisabledCheckKey) o;
            return ComparatorUtils.equals(c, other.c) &&
                    ComparatorUtils.equals(back, other.back) &&
                    ComparatorUtils.equals(spread1, other.spread1) &&
                    ComparatorUtils.equals(spread2, other.spread2);
        }

        public int hashCode() {
            return c.hashCode() * back.hashCode() * spread1 * spread2;
        }
    }

    /*
     * CheckKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     */
    protected static class CheckKey {

        private Color c;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;
        private boolean focused;

        CheckKey(Color c, boolean pressed, boolean enabled, boolean rollover, boolean focused) {
            this.c = c;
            this.pressed = pressed;
            this.enabled = enabled;
            this.rollover = rollover;
            this.focused = focused;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof CheckKey)) {
                return false;
            }

            CheckKey other = (CheckKey) o;
            return  ComparatorUtils.equals(c,other.c) &&
                    ComparatorUtils.equals(pressed,other.pressed) &&
                    ComparatorUtils.equals(enabled,other.enabled) &&
                    ComparatorUtils.equals(rollover,other.rollover)&&
                    ComparatorUtils.equals(focused,other.focused);
        }

        public int hashCode() {
            return c.hashCode() *
                    (pressed ? 1 : 2) *
                    (enabled ? 4 : 8) *
                    (rollover ? 16 : 32);
        }
    }
}