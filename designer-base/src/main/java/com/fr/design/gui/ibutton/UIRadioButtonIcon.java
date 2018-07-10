package com.fr.design.gui.ibutton;

import com.fr.design.gui.icheckbox.UICheckBoxIcon;
import com.fr.general.ComparatorUtils;
import com.fr.design.utils.ThemeUtils;
import com.fr.design.utils.ColorRoutines;
import com.fr.design.utils.DrawRoutines;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 下午2:52
 */
public class UIRadioButtonIcon extends UICheckBoxIcon {
    private static HashMap cache = new HashMap();

    private static final int[][] A = {
            {255, 255, 255, 242, 228, 209, 187, 165, 142, 255, 255},
            {255, 255, 242, 228, 209, 187, 165, 142, 120, 104, 255},
            {255, 242, 228, 209, 187, 165, 142, 120, 104, 86, 72},
            {242, 228, 209, 187, 165, 142, 120, 104, 86, 72, 56},
            {228, 209, 187, 165, 142, 120, 104, 86, 72, 56, 42},
            {209, 187, 165, 142, 120, 104, 86, 72, 56, 42, 28},
            {187, 165, 142, 120, 104, 86, 72, 56, 42, 28, 17},
            {165, 142, 120, 104, 86, 72, 56, 42, 28, 17, 9},
            {142, 120, 104, 86, 72, 56, 42, 28, 17, 9, 0},
            {255, 104, 86, 72, 56, 42, 28, 17, 9, 0, 255},
            {255, 255, 72, 56, 42, 28, 17, 9, 0, 255, 255}
    };

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

        // inner space
        Color col = null;

        if (!button.isEnabled()) {
            col = ThemeUtils.BUTTON_DISABLE_COLOR;
        } else if (button.getModel().isPressed()) {
            if (button.getModel().isRollover() || button.getModel().isArmed()) {
                col = ThemeUtils.BUTTON_PRESS_COLOR;
            } else {
                col = ThemeUtils.BUTTON_NORMAL_COLOR;
            }
        } else if (button.getModel().isRollover() && ThemeUtils.BUTTON_ROLLOVER) {
            col = ThemeUtils.BUTTON_ROLLOVER_BG_COLOR;
        } else {
            col = ThemeUtils.BUTTON_NORMAL_COLOR;
        }

        g.setColor(col);
        drawXpRadio(g, button, col, x, y, getIconWidth(), getIconHeight());

        // checkmark
        if (!button.isSelected()) {
            return;
        }

        if (!button.isEnabled()) {
            col = ThemeUtils.BUTTON_CHECK_DISABLE_COLOR;
        } else {
            col = ThemeUtils.RADIO_CHECK_ROLLOVER_COLOR;
        }
        g.setColor(col);
        drawXpCheckMark(g, col, x, y);
    }


    private void drawXpRadio(Graphics g, AbstractButton b, Color c,
                             int x, int y, int w, int h) {
        boolean pressed = b.getModel().isPressed();
        boolean armed = b.getModel().isArmed();
        boolean enabled = b.isEnabled();
        boolean rollover = b.getModel().isRollover();
        boolean focused = (ThemeUtils.BUTTON_FOCUS_BORDER && !rollover && b.isFocusOwner());

        Color bg = b.getBackground();

        if (!b.isOpaque()) {
            Container parent = b.getParent();
            bg = parent.getBackground();

            while (parent != null && !parent.isOpaque()) {
                parent = parent.getParent();
                bg = parent.getBackground();
            }
        }

        RadioKey key = new RadioKey(c, bg, pressed, enabled, (rollover || armed), focused);
        Object value = cache.get(key);

        if (value != null) {
            // image already cached - paint image and return
            g.drawImage((Image) value, x, y, b);
            return;
        }

        Image img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        drawcalculate(img,bg,w,h,enabled, pressed ,armed,rollover,c,focused);
        // draw the image
        g.drawImage(img, x, y, b);

        // add the image to the cache
        cache.put(key, img);
    }

    private void drawcalculate(Image img, Color bg, int w, int h, boolean enabled,
                           boolean pressed, boolean armed, boolean rollover, Color c,boolean focused ) {
        Graphics imgGraphics = img.getGraphics();
        imgGraphics.setColor(bg);
        imgGraphics.fillRect(0, 0, w, h);
        // spread light is between 0 and 20
        int spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT;
        int spread2 = ThemeUtils.BUTTON_SPREAD_DARK;
        if (!enabled) {
            spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT_DISABLE;
            spread2 = ThemeUtils.BUTTON_SPREAD_DARK_DISABLE;
        }
        int spreadStep1 = spread1 * 5;    // 20 -> 100
        int spreadStep2 = spread2 * 4;    // 20 -> 80
        boolean isRollverOrArmed = rollover||armed;
        if (pressed && isRollverOrArmed) {
            spreadStep2 *= 2;
        }
        c = ColorRoutines.lighten(c, spreadStep1);
        imgGraphics.setColor(ColorRoutines.darken(c, spreadStep2));
        imgGraphics.fillRect(2, 2, w - 4, h - 4);
        imgGraphics.fillRect(1, 5, 1, 3);
        imgGraphics.fillRect(11, 5, 1, 3);
        imgGraphics.fillRect(5, 1, 3, 1);
        imgGraphics.fillRect(5, 11, 3, 1);
        Color color;
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255 - A[col][row]);
                imgGraphics.setColor(color);
                imgGraphics.drawLine(col + 1, row + 1, col + 1, row + 1);
            }
        }
        if (!enabled) {
            DrawRoutines.drawXpRadioBorder(imgGraphics,
                    ThemeUtils.BUTTON_BORDER_DISABLE_COLOR, 0, 0, w, h);
        } else {
            if (rollover && ThemeUtils.BUTTON_ROLLOVER && !pressed) {
                DrawRoutines.drawXpRadioBorder(imgGraphics,
                        ThemeUtils.RADIO_CHECK_ROLLOVER_COLOR, 0, 0, w, h);
            } else if (focused && !pressed) {
                DrawRoutines.drawXpRadioBorder(imgGraphics,
                        ThemeUtils.RADIO_BORDER_NORMAL_COLOR, 0, 0, w, h);
            }

            DrawRoutines.drawXpRadioBorder(imgGraphics,
                    ThemeUtils.RADIO_BORDER_NORMAL_COLOR, 0, 0, w, h);
        }
        // dispose of image graphics
        imgGraphics.dispose();
    }


    private void drawXpCheckMark(Graphics g, Color c, int x, int y) {
        g.translate(x, y);

        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 224));
        g.fillRect(5, 5, 3, 3);

        g.setColor(c);
        g.drawLine(6, 6, 6, 6);

        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 192));
        g.drawLine(6, 4, 6, 4);
        g.drawLine(4, 6, 4, 6);
        g.drawLine(8, 6, 8, 6);
        g.drawLine(6, 8, 6, 8);

        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 128));
        g.drawLine(5, 4, 5, 4);
        g.drawLine(7, 4, 7, 4);
        g.drawLine(4, 5, 4, 5);
        g.drawLine(8, 5, 8, 5);
        g.drawLine(4, 7, 4, 7);
        g.drawLine(8, 7, 8, 7);
        g.drawLine(5, 8, 5, 8);
        g.drawLine(7, 8, 7, 8);

        g.translate(-x, -y);
    }

    /*
     * EnabledRadioKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     * Used only if we are run from ControlPanel.
     */
    static class EnabledRadioKey {
        int spread1;
        int spread2;
        Color c, back;

        EnabledRadioKey(Color c, Color back) {
            spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT;
            spread2 = ThemeUtils.BUTTON_SPREAD_DARK;
            this.c = c;
            this.back = back;
        }

        public boolean equals(Object o) {
            if (o == null){
                return false;
            }
            if (!(o instanceof UICheckBoxIcon.EnabledCheckKey)) {
                return false;
            }

            UICheckBoxIcon.EnabledCheckKey other = (UICheckBoxIcon.EnabledCheckKey) o;

            return  ComparatorUtils.equals(c,other.c) &&
                    ComparatorUtils.equals(back,other.back) &&
                    ComparatorUtils.equals(spread1,other.spread1) &&
                    ComparatorUtils.equals(spread2,other.spread2);
        }

        public int hashCode() {
            return c.hashCode() * back.hashCode() * spread1 * spread2;
        }
    }

    /*
     * DisabledRadioKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     * Used only if we are run from ControlPanel.
     */
    static class DisabledRadioKey {
        int spread1;
        int spread2;
        Color c, back;

        DisabledRadioKey(Color c, Color back) {
            spread1 = ThemeUtils.BUTTON_SPREAD_LIGHT_DISABLE;
            spread2 = ThemeUtils.BUTTON_SPREAD_DARK_DISABLE;
            this.c = c;
            this.back = back;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof UICheckBoxIcon.DisabledCheckKey)) {
                return false;
            }

            UICheckBoxIcon.DisabledCheckKey other = (UICheckBoxIcon.DisabledCheckKey) o;

            return  ComparatorUtils.equals(c,other.c) &&
                    ComparatorUtils.equals(back,other.back) &&
                    ComparatorUtils.equals(spread1,other.spread1) &&
                    ComparatorUtils.equals(spread2,other.spread2);
        }

        public int hashCode() {
            return c.hashCode() * back.hashCode() * spread1 * spread2;
        }
    }

    /*
     * RadioKey is used as key in the cache HashMap.
     * Overrides equals() and hashCode().
     */
    static class RadioKey {

        private Color c, background;
        private boolean pressed;
        private boolean enabled;
        private boolean rollover;
        private boolean focused;

        RadioKey(Color c, Color background,
                 boolean pressed, boolean enabled, boolean rollover, boolean focused) {
            this.c = c;
            this.background = background;
            this.pressed = pressed;
            this.enabled = enabled;
            this.rollover = rollover;
            this.focused = focused;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof RadioKey)){
                return false;
            }

            RadioKey other = (RadioKey) o;

            return  ComparatorUtils.equals(c,other.c) &&
                    ComparatorUtils.equals(pressed,other.pressed) &&
                    ComparatorUtils.equals(enabled,other.enabled) &&
                    ComparatorUtils.equals(rollover,other.rollover)&&
                    ComparatorUtils.equals(focused,other.focused)&&
                    ComparatorUtils.equals(background,other.background);
        }

        public int hashCode() {
            return c.hashCode() *
                    background.hashCode() *
                    (pressed ? 1 : 2) *
                    (enabled ? 4 : 8) *
                    (rollover ? 16 : 32);
        }
    }
}