package com.fr.design.gui.icombobox;

import com.fr.design.gui.UILookAndFeel;
import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 上午11:23
 */
public class UIComboBoxButton extends JButton {

    protected JComboBox comboBox;
    protected JList listBox;
    protected CellRendererPane rendererPane;
    protected Icon comboIcon;
    protected boolean iconOnly = false;
    private static BufferedImage focusImg;

    public final JComboBox getComboBox() {
        return comboBox;
    }

    public final void setComboBox(JComboBox cb) {
        comboBox = cb;
    }

    public final Icon getComboIcon() {
        return comboIcon;
    }

    public final void setComboIcon(Icon i) {
        comboIcon = i;
    }


    public final void setIconOnly(boolean isIconOnly) {
        iconOnly = isIconOnly;
    }

    UIComboBoxButton() {
        super("");

        DefaultButtonModel model = new DefaultButtonModel() {
            public void setArmed(boolean armed) {
                armed = isPressed() ? true : armed;
                super.setArmed(armed);
            }
        };

        setModel(model);

        // Set the background and foreground to the combobox colors.
        setBackground(UIManager.getColor("ComboBox.background"));
        setForeground(UIManager.getColor("ComboBox.foreground"));

        if (focusImg == null) {
            ImageIcon icon = UILookAndFeel.loadIcon("ComboBoxFocus.png", this);

            if (icon != null) {
                focusImg = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
                Graphics g = focusImg.getGraphics();
                icon.paintIcon(this, g, 0, 0);
            }
        }
    }

    public UIComboBoxButton(JComboBox cb, Icon i,
                            boolean onlyIcon, CellRendererPane pane, JList list) {
        this();
        comboBox = cb;
        comboIcon = i;
        rendererPane = pane;
        listBox = list;
        setEnabled(comboBox.isEnabled());
    }

    /**
     * Mostly taken from the swing sources
     *
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        // Note: border was already painted in TinyButtonBorder
        boolean leftToRight = getComponentOrientation().isLeftToRight();
        if (comboBox.isEnabled()) {
            if (comboBox.isEditable()) {
                g.setColor(ThemeUtils.TEXT_BG_COLOR);
            } else {
                g.setColor(comboBox.getBackground());
            }
        } else {
            g.setColor(ThemeUtils.TEXT_DISABLED_BG_COLOR);
        }

        g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
        if (getParent() != null && getParent().getParent() != null) {
            // paint border background - next parent is combo box
            Color bg = getParent().getParent().getBackground();
            g.setColor(bg);
        }
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        drawArrow(g);
        Insets insets = new Insets(ThemeUtils.COMBO_INSETS.top, ThemeUtils.COMBO_INSETS.left,
                ThemeUtils.COMBO_INSETS.bottom, 0);
        int width = getWidth() - (insets.left + insets.right);
        int widthFocus = width;
        int height = getHeight() - (insets.top + insets.bottom);
        if (height <= 0 || width <= 0) {
            return;
        }
        int left = insets.left;
        int top = insets.top;
        int right = left + (width - 1);
        int bottom = top + (height - 1);
        int iconWidth = ThemeUtils.COMBOBUTTTON_WIDTH;
        int iconLeft = (leftToRight) ? right : left;
        // Let the renderer paint
        Component c = null;
        boolean mustResetOpaque = false;
        boolean savedOpaque = false;

        paintRender(g, c, mustResetOpaque, savedOpaque, leftToRight, width, insets, iconWidth, left, top, height);
    }

    private void paintRender(Graphics g, Component c, boolean mustResetOpaque, boolean savedOpaque, boolean leftToRight,
                             int width, Insets insets, int iconWidth, int left, int top, int height) {
        boolean paintFocus = false;
        if (!iconOnly && comboBox != null) {
            ListCellRenderer renderer = comboBox.getRenderer();
            boolean renderPressed = getModel().isPressed();
            c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, renderPressed, false);
            c.setFont(rendererPane.getFont());
            calculateColor(c, mustResetOpaque, savedOpaque, paintFocus);
            int cWidth = width - (insets.right + iconWidth);
            boolean shouldValidate = false;
            if (c instanceof JPanel) {
                shouldValidate = true;
            }
            if (leftToRight) {
                rendererPane.paintComponent(g, c, this, left, top, cWidth, height, shouldValidate);
            } else {
                rendererPane.paintComponent(g, c, this, left + iconWidth, top, cWidth, height, shouldValidate);
            }
        }
    }

    private void calculateColor(Component c, boolean mustResetOpaque, boolean savedOpaque, boolean paintFocus) {
        if (model.isArmed() && model.isPressed()) {
            if (isOpaque()) {
                c.setBackground(UIManager.getColor("Button.select"));
            }
            c.setForeground(comboBox.getForeground());
        } else if (!comboBox.isEnabled()) {
            if (isOpaque()) {
                c.setBackground(ThemeUtils.TEXT_DISABLED_BG_COLOR);
            } else {
                comboBox.setBackground(ThemeUtils.TEXT_DISABLED_BG_COLOR);
            }
            c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
        } else if (comboBox.hasFocus() && !comboBox.isPopupVisible()) {
            if (comboBox.isEditable()) {
                c.setForeground(ThemeUtils.MAIN_COLOR);
            } else {
                c.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
            }
            c.setBackground(UIManager.getColor("ComboBox.focusBackground"));
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                jc.isOpaque();
                jc.setOpaque(true);
            }
        } else {
            c.setForeground(comboBox.getForeground());
            c.setBackground(comboBox.getBackground());
        }
    }

    private void drawArrow(Graphics g) {

        Color col = null;

        if (!isEnabled()) {
            col = ThemeUtils.COMBOBUTT_DISABLED_COLOR;
        } else if (model.isPressed()) {
            col = ThemeUtils.COMBOBUTT_PRESSED_COLOR;
        } else if (model.isRollover()) {
            col = ThemeUtils.COMBOBUTT_ROLLOVER_COLOR;
        } else {
            col = ThemeUtils.COMBOBUTT_COLOR;
        }

        g.setColor(col);

        Rectangle buttonRect = new Rectangle(
                getWidth() - ThemeUtils.COMBOBUTTTON_WIDTH,
                1, ThemeUtils.COMBOBUTTTON_WIDTH, getHeight() - 2);
        drawXpButton(g, buttonRect, col);

        // draw arrow
        if (isEnabled()) {
            g.setColor(ThemeUtils.COMBO_ARROW_COLOR);
        } else {
            g.setColor(ThemeUtils.COMBO_ARROW_DISABLED_COLOR);
        }

        drawXpArrow(g, buttonRect);
    }


    private void drawXpButton(Graphics g, Rectangle r, Color c) {
        int xs = r.x;
        int ys = r.y;
        int y2 = r.y + r.height;

        Color a = ThemeUtils.COMBO_BORDER_COLOR;

        Color pressedLightColor = ThemeUtils.PRESSED_LIGHT_COLOR;
        Color pressedDarkColor = ThemeUtils.PRESSED_DARK_COLOR;
        Color rolloverLightColor = ThemeUtils.ROLLOVER_LIGHT_COLOR;
        Color rolloverDarkColor = ThemeUtils.ROLLOVER_DARK_COLOR;
        Color normalLightColor = ThemeUtils.NORMAL_LIGHT_COLOR;
        Color normalDarkColor = ThemeUtils.NORMAL_DARK_COLOR;
        Color lightBorderColor = ThemeUtils.WHITE_BORDER_COLOR;

        Graphics2D g2 = (Graphics2D) g;
        GradientPaint scrollBarBg = null;
        if (!isEnabled()) {
            scrollBarBg = new GradientPaint(xs, ys, normalLightColor, xs, y2, normalDarkColor);
        } else if (model.isPressed()) {
            scrollBarBg = new GradientPaint(xs, ys, pressedLightColor, xs, y2, pressedDarkColor);
        } else if (model.isRollover()) {
            scrollBarBg = new GradientPaint(xs, ys, rolloverLightColor, xs, y2, rolloverDarkColor);
        } else {
            scrollBarBg = new GradientPaint(xs, ys, normalLightColor, xs, y2, normalDarkColor);
        }

        GradientPaint scrollBarHight = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.5f), 0, 7, new Color(1.0f, 1.0f, 1.0f, 0.2f));
        g2.setPaint(scrollBarBg);
        g2.fillRoundRect(xs, ys, r.width, r.height, 3, 3);//画渐变的背景
        g2.setPaint(scrollBarHight);
        g2.fillRoundRect(xs, ys, r.width, r.height / 2, 3, 3);//画高光
        g2.setColor(a);
        g2.drawLine(xs, ys, xs, y2);
        g2.setColor(lightBorderColor);
        g2.drawRoundRect(xs + 1, ys, r.width - 3, r.height - 1, 1, 1);//画高光线（内发光，0%阻塞）

    }

    private void drawXpArrow(Graphics g, Rectangle r) {
        int x = r.x + (r.width - 8) / 2 - 1;
        int y = r.y + (r.height - 6) / 2 + 1;
        g.drawLine(x + 1, y + 1, x + 7, y + 1);
        g.drawLine(x + 2, y + 2, x + 6, y + 2);
        g.drawLine(x + 3, y + 3, x + 5, y + 3);
        g.drawLine(x + 4, y + 4, x + 4, y + 4);
    }
}