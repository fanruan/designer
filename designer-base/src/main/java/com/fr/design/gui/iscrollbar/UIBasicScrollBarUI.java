package com.fr.design.gui.iscrollbar;

import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-17
 * Time: 上午9:58
 */
public class UIBasicScrollBarUI extends BasicScrollBarUI {

    static final int ALPHA = 92;    // 255 is full opaque

    private static final int WIDTH_GAP = 10;

    /**
     * true if thumb is in rollover state
     */
    protected boolean isRollover = false;
    /**
     * true if thumb was in rollover state
     */
    protected boolean wasRollover = false;

    /**
     * The free standing property of this scrollbar UI delegate.
     */
    private boolean freeStanding = false;

    private int scrollBarWidth;

    public UIBasicScrollBarUI() {
    }

    /**
     * Installs some default values.
     */
    protected void installDefaults() {
        scrollBarWidth = UIScrollButton.size.width;
        super.installDefaults();
        scrollbar.setBorder(null);
        minimumThumbSize = new Dimension(17, 17);
    }

    protected Dimension getMaximumThumbSize() {
        return maximumThumbSize;
    }

    /**
     * 创建组件UI
     *
     * @param c 组件
     * @return 返回组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIBasicScrollBarUI();
    }

    /**
     * Creates the decrease button of the scrollbar.
     *
     * @param orientation The button's orientation.
     * @return The created button.
     */
    protected JButton createDecreaseButton(int orientation) {
        return new UIScrollButton(orientation, this);
    }

    /**
     * Creates the increase button of the scrollbar.
     *
     * @param orientation The button's orientation.
     * @return The created button.
     */
    protected JButton createIncreaseButton(int orientation) {
        return new UIScrollButton(orientation, this);
    }

    /// From MetalUI
    public Dimension getPreferredSize(JComponent c) {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(scrollBarWidth, scrollBarWidth * 3 + WIDTH_GAP);
        } else // Horizontal
        {
            return new Dimension(scrollBarWidth * 3 + WIDTH_GAP, scrollBarWidth);
        }

    }

    public void paintTrack(Graphics g, JComponent c, Rectangle t) {
        // borders depend on the scrollbar's style
        if (isThumbVisible()) {
            g.setColor(ThemeUtils.SCROLL_TRACK_COLOR);
        } else {
            g.setColor(ThemeUtils.SCROLL_TRACK_DISABLED_COLOR);
        }

        g.fillRect(t.x, t.y, t.width, t.height);

        if (isThumbVisible()) {
            g.setColor(ThemeUtils.SCROLL_TRACK_BORDER_COLOR);
        } else {
            g.setColor(ThemeUtils.SCROLL_TRACK_BORDER_DISABLED_COLOR);
        }

        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            g.drawLine(t.x, t.y, t.x, t.y + t.height - 1);
            g.drawLine(t.x + t.width - 1, t.y, t.x + t.width - 1, t.y + t.height - 1);
        } else {
            g.drawLine(t.x, t.y, t.x + t.width - 1, t.y);
            g.drawLine(t.x, t.y + t.height - 1, t.x + t.width - 1, t.y + t.height - 1);
        }
    }


    public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        drawXpThumb(g, thumbBounds);
    }

    private void drawXpThumb(Graphics g, Rectangle t) {
        Color color = null;
        if (isDragging && isRollover) {
            color = ThemeUtils.SCROLL_THUMB_PRESSED_COLOR;
        } else if (isRollover && ThemeUtils.SCROLL_ROLLOVER) {
            color = ThemeUtils.SCROLL_THUMB_ROLLOVER_COLOR;
        } else {
            color = ThemeUtils.SCROLL_THUMB_COLOR;
        }

        g.setColor(color);

        int xs = t.x + 1;
        int ys = t.y + 1;
        int x2 = t.x + t.width - 1;
        int y2 = t.y + t.height - 1;

        Color pressedColor = ThemeUtils.ROLLOVER_PRESSED_COLOR_8;
        Color rolloverColor = ThemeUtils.ROLLOVER_PRESSED_COLOR_8;
        Color normalColor = ThemeUtils.NORMAL__COLOR_8;

        switch (scrollbar.getOrientation()) {
            //harry: 垂直滚动条和水平滚动条的画法分开。
            case JScrollBar.VERTICAL:
                drawVertical(g, t, xs, ys, x2, pressedColor,  rolloverColor, normalColor);
                break;
            case JScrollBar.HORIZONTAL:
                drawHorizontal(g, t, xs, ys, x2, pressedColor,  rolloverColor, normalColor);
                break;
        }

        // draw Grip
//        if (t.height < 11) {
//            return;
//        }
//        drawColorAndLength(g, t, x2, y2, color);
    }

    private void drawVertical(Graphics g, Rectangle t, int xs, int ys, int x2,
                              Color pressedColor,  Color rolloverColor,
                              Color normalColor) {
        Color a = ThemeUtils.SCROLL_BORDER_COLOR;
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint scrollBarBg = null;
        if (isDragging && isRollover) {
            scrollBarBg = new GradientPaint(xs, ys, pressedColor, x2, ys, pressedColor);
        } else if (isRollover && ThemeUtils.SCROLL_ROLLOVER) {
            scrollBarBg = new GradientPaint(xs, ys, rolloverColor, x2, ys, rolloverColor);
        } else {
            scrollBarBg = new GradientPaint(xs, ys, normalColor, x2, ys, normalColor);
        }

        g2.setPaint(scrollBarBg);
        g2.fillRoundRect(xs, ys, t.width - 2, t.height - 2, 0, 0);
    }

    private void drawHorizontal(Graphics g, Rectangle t, int xs, int ys, int y2,
                                Color pressedColor, Color rolloverColor,
                                Color normalColor) {
        GradientPaint scrollBarBg = null;
        Graphics2D g2H = (Graphics2D) g;
        if (isDragging && isRollover) {
            scrollBarBg = new GradientPaint(xs, ys, pressedColor, xs, y2, pressedColor);
        } else if (isRollover && ThemeUtils.SCROLL_ROLLOVER) {
            scrollBarBg = new GradientPaint(xs, ys, rolloverColor, xs, y2, rolloverColor);
        } else {
            scrollBarBg = new GradientPaint(xs, ys, normalColor, xs, y2, normalColor);
        }

        g2H.setPaint(scrollBarBg);
        g2H.fillRoundRect(xs, ys, t.width - 2, t.height - 2, 0, 0);
    }

    /**
     * 是否可见
     *
     * @return 可见返回true
     */
    public boolean isThumbVisible() {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return getThumbBounds().height > 0;
        } else {
            return getThumbBounds().width > 0;
        }
    }

    // From BasicUI
    protected TrackListener createTrackListener() {
        return new MyTrackListener();
    }

    /**
     * Basically does BasicScrollBarUI.TrackListener the right job, it just needs
     * an additional repaint and rollover management
     */
    protected class MyTrackListener extends BasicScrollBarUI.TrackListener {
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            scrollbar.repaint();
        }

        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            scrollbar.repaint();
        }

        public void mouseEntered(MouseEvent e) {
            isRollover = false;
            wasRollover = false;
            if (getThumbBounds().contains(e.getPoint())) {
                isRollover = true;
                wasRollover = isRollover;
                scrollbar.repaint();
            }
        }

        public void mouseExited(MouseEvent e) {
            isRollover = false;
            if (isRollover != wasRollover) {
                wasRollover = isRollover;
                scrollbar.repaint();
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (getThumbBounds().contains(e.getPoint())) {
                isDragging = true;
            }
            super.mouseDragged(e);
        }

        public void mouseMoved(MouseEvent e) {
            if (getThumbBounds().contains(e.getPoint())) {
                isRollover = true;
                if (isRollover != wasRollover) {
                    scrollbar.repaint();
                    wasRollover = isRollover;
                }
            } else {
                isRollover = false;
                if (isRollover != wasRollover) {
                    scrollbar.repaint();
                    wasRollover = isRollover;
                }
            }
        }
    }

    protected class OrientationChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent e) {
            Integer orient = (Integer) e.getNewValue();

            if (scrollbar.getComponentOrientation().isLeftToRight()) {
                if (incrButton instanceof UIScrollButton) {
                    ((UIScrollButton) incrButton).setDirection(orient.intValue() == HORIZONTAL ?
                            EAST : SOUTH);
                }
                if (decrButton instanceof UIScrollButton) {
                    ((UIScrollButton) decrButton).setDirection(orient.intValue() == HORIZONTAL ?
                            WEST : NORTH);
                }
            } else {
                if (incrButton instanceof UIScrollButton) {
                    ((UIScrollButton) incrButton).setDirection(orient.intValue() == HORIZONTAL ?
                            WEST : SOUTH);
                }
                if (decrButton instanceof UIScrollButton) {
                    ((UIScrollButton) decrButton).setDirection(orient.intValue() == HORIZONTAL ?
                            EAST : NORTH);
                }
            }
        }
    }
}