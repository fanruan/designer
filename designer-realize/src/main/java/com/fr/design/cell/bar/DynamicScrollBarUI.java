package com.fr.design.cell.bar;

import com.fr.design.gui.iscrollbar.UISBChooser;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-30
 * Time: 下午2:28
 */
public class DynamicScrollBarUI extends BasicScrollBarUI {


    private boolean isRollover = false;
    //是否
    private boolean wasRollover = false;


    public DynamicScrollBarUI() {

    }

    /**
     * 是否可见
     * @return 可见返回true
     */
    public boolean isThumbVisible() {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return getThumbBounds().height > 0;
        } else {
            return getThumbBounds().width > 0;
        }
    }

    public void paintThumb(Graphics g, JComponent c, Rectangle t) {
//       super.paintThumb(g,c,t);
        paintXP(g, t);
    }

    private void paintXP(Graphics g, Rectangle t) {
        Color c = null;
        if (isDragging && isRollover) {
            c = ScrollBarUIConstant.PRESS_SCROLL_BAR_COLOR;
        } else if (isRollover) {
            c = ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR;
        } else {
            c = ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR;
        }

        g.setColor(c);

        int xs = t.x + 1;
        int ys = t.y + 1;
        int x2 = t.x + t.width - 1;
        int y2 = t.y + t.height - 1;

        paintScrollBar(g, xs, ys, x2, y2, t);

        // draw Grip
//        if (t.height < 11) {
//            return;
//        }
//
//        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
//            drawVertical(t, y2, g, c);
//        } else {
//            drawHrizontal(t, x2, g, c);
//        }
    }


    private void drawHrizontal(Rectangle t, int x2, Graphics g, Color c) {
        int x1 = t.x + (t.width) / 2 - 4;
        x2 = Math.min(x1 + 8, t.x + t.width - 5);

        int x = x1 + 1;
        // we take only saturation & brightness and apply them
        // to the background color (normal/rollover/pressed)
        g.setColor(UISBChooser.getAdjustedColor(c, 0, 71));
        while (x < x2) {
            g.drawLine(x, 5, x, 11);
            x += 2;
        }

        x = x1;
        g.setColor(UISBChooser.getAdjustedColor(c, 0, -13));
        while (x < x2) {
            g.drawLine(x, 6, x, 12);
            x += 2;
        }
    }

    private void drawVertical(Rectangle t, int y2, Graphics g, Color c) {
        int y1 = t.y + (t.height) / 2 - 4;
        y2 = Math.min(y1 + 8, t.y + t.height - 5);

        int y = y1;
        // we take only saturation & brightness and apply them
        // to the background color (normal/rollover/pressed)
        g.setColor(UISBChooser.getAdjustedColor(c, 0, 71));
        while (y < y2) {
            g.drawLine(5, y, 11, y);
            y += 2;
        }

        y = y1 + 1;
        g.setColor(UISBChooser.getAdjustedColor(c, 0, -13));
        while (y < y2) {
            g.drawLine(6, y, 12, y);
            y += 2;
        }
    }

    private void paintScrollBar(Graphics g, int xs, int ys, int x2, int y2, Rectangle t) {
        switch (scrollbar.getOrientation()) {
            //harry: 垂直滚动条和水平滚动条的画法分开。
            case JScrollBar.VERTICAL:
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint scrollBarBg = null;
                if (isRollover) {
                    scrollBarBg = new GradientPaint(xs, ys, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR, x2, ys, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR);
                } else {
                    scrollBarBg = new GradientPaint(xs, ys, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR, x2, ys, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR);
                }
                g2.setPaint(scrollBarBg);
                g2.fillRoundRect(xs, ys, t.width - 2, t.height - 2, 0, 0);
                break;
            case JScrollBar.HORIZONTAL:
                Graphics2D g2H = (Graphics2D) g;
                if (isRollover) {
                    scrollBarBg = new GradientPaint(xs, ys, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR, xs, y2, ScrollBarUIConstant.ROLL_OVER_SCROLL_BAR_COLOR);
                } else {
                    scrollBarBg = new GradientPaint(xs, ys, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR, xs, y2, ScrollBarUIConstant.NORMAL_SCROLL_BAR_COLOR);
                }
                g2H.setPaint(scrollBarBg);
                g2H.fillRoundRect(xs, ys, t.width - 2, t.height - 2, 0, 0);
                break;
        }
    }


    protected JButton createDecreaseButton(int orientation) {
        return new DynamicScrollButton(orientation, this);
    }

    /**
     * Creates the increase button of the scrollbar.
     *
     * @param orientation The button's orientation.
     * @return The created button.
     */
    protected JButton createIncreaseButton(int orientation) {
        return new DynamicScrollButton(orientation, this);
    }

    protected TrackListener createTrackListener() {
        return new ScrollBarTrackListener();
    }


    private class ScrollBarTrackListener extends BasicScrollBarUI.TrackListener {
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


}