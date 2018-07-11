package com.fr.design.gui.isplitpanedivider;

import com.fr.design.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-12
 * Time: 下午4:50
 */
public class UISplitPaneDivider extends BasicSplitPaneDivider {

    private static final int BOUND5 = 5;

    private int inset = 2;
    private Color controlColor = MetalLookAndFeel.getControl();
    private Color primaryControlColor = MetalLookAndFeel.getPrimaryControl();

    public UISplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
        setLayout(new MetalDividerLayout());
    }

    public void paint(Graphics g) {
        g.setColor(controlColor);

        Rectangle clip = g.getClipBounds();
        Insets insets = getInsets();
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        Dimension size = getSize();
        size.width -= inset * 2;
        size.height -= inset * 2;
        int drawX = inset;
        int drawY = inset;

        if (insets != null) {
            size.width -= (insets.left + insets.right);
            size.height -= (insets.top + insets.bottom);
            drawX += insets.left;
            drawY += insets.top;
        }

        super.paint(g);
    }

    /**
     * Creates and return an instance of JButton that can be used to
     * collapse the left component in the metal split pane.
     */
    protected JButton createLeftOneTouchButton() {
        JButton b = new JButton() {
            public void setBorder(Border b) {
            }

            public void paint(Graphics g) {
                JSplitPane splitPane = getSplitPaneFromSuper();

                // changed this in 1.3
                if (splitPane != null) {
                    int oneTouchSize = getOneTouchSizeFromSuper();
                    int orientation = getOrientationFromSuper();

                    // Fill the background first ...
                    g.setColor(ThemeUtils.BACK_COLOR);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());

                    // use scrollArrowColor as button color
                    g.setColor(ThemeUtils.SCROLL_ARROW_COLOR);

                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        g.drawLine(2, 1, 3, 1);
                        g.drawLine(1, 2, 4, 2);
                        g.drawLine(0, 3, 5, 3);
                    } else {
                        // HORIZONTAL_SPLIT
                        g.drawLine(1, 2, 1, 3);
                        g.drawLine(2, 1, 2, 4);
                        g.drawLine(3, 0, 3, 5);
                    }
                }
            }

            // Don't want the button to participate in focus traversable.
            public boolean isFocusTraversable() {
                return false;
            }
        };
        b.setRequestFocusEnabled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        return b;
    }

    /**
     * Creates and return an instance of JButton that can be used to
     * collapse the right component in the metal split pane.
     */
    protected JButton createRightOneTouchButton() {
        JButton b = new JButton() {
            public void setBorder(Border border) {
            }

            public void paint(Graphics g) {
                JSplitPane splitPane = getSplitPaneFromSuper();

                // changed this in 1.3
                if (splitPane != null) {
                    int oneTouchSize = getOneTouchSizeFromSuper();
                    int orientation = getOrientationFromSuper();

                    // Fill the background first ...
                    g.setColor(ThemeUtils.BACK_COLOR);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());

                    // use scrollArrowColor as button color
                    g.setColor(ThemeUtils.SCROLL_ARROW_COLOR);

                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        g.drawLine(2, 3, 3, 3);
                        g.drawLine(1, 2, 4, 2);
                        g.drawLine(0, 1, 5, 1);
                    } else {
                        // HORIZONTAL_SPLIT
                        g.drawLine(3, 2, 3, 3);
                        g.drawLine(2, 1, 2, 4);
                        g.drawLine(1, 0, 1, 5);
                    }
                }
            }

            // Don't want the button to participate in focus traversable.
            public boolean isFocusTraversable() {
                return false;
            }
        };

        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        return b;
    }

    /**
     * Used to layout a TinySplitPaneDivider. Layout for the divider
     * involves appropriately moving the left/right buttons around.
     * <p/>
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of TinySplitPaneDivider.
     */
    public class MetalDividerLayout implements LayoutManager {
        /**
         * 布局
         *
         * @param c 容器
         */
        public void layoutContainer(Container c) {
            JButton leftButton = getLeftButtonFromSuper();
            JButton rightButton = getRightButtonFromSuper();
            JSplitPane splitPane = getSplitPaneFromSuper();
            int orientation = getOrientationFromSuper();
            int oneTouchSize = getOneTouchSizeFromSuper();
            int oneTouchOffset = getOneTouchOffsetFromSuper();
            Insets insets = getInsets();

            // This layout differs from the one used in BasicSplitPaneDivider.
            // It does not center justify the oneTouchExpadable buttons.
            // This was necessary in order to meet the spec of the Metal
            // splitpane divider.
            if (leftButton != null && rightButton != null && c == UISplitPaneDivider.this) {
                if (splitPane.isOneTouchExpandable()) {
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        int extraY = (insets != null) ? insets.top : 0;
                        int blockSize = getDividerSize();

                        if (insets != null) {
                            blockSize -= (insets.top + insets.bottom);
                        }
                        blockSize = Math.min(blockSize, oneTouchSize);
                        leftButton.setBounds(oneTouchOffset, extraY, blockSize * 2, blockSize);
                        rightButton.setBounds(oneTouchOffset + oneTouchSize * 2, extraY, blockSize * 2, blockSize);
                    } else {
                        int blockSize = getDividerSize();
                        int extraX = (insets != null) ? insets.left : 0;

                        if (insets != null) {
                            blockSize -= (insets.left + insets.right);
                        }
                        blockSize = Math.min(blockSize, oneTouchSize);
                        leftButton.setBounds(extraX, oneTouchOffset, blockSize, blockSize * 2);
                        rightButton.setBounds(extraX, oneTouchOffset + oneTouchSize * 2, blockSize, blockSize * 2);
                    }
                } else {
                    leftButton.setBounds(-BOUND5, -BOUND5, 1, 1);
                    rightButton.setBounds(-BOUND5, -BOUND5, 1, 1);
                }
            }
        }

        /**
         * 最小布局
         *
         * @param c 容器
         * @return 最小
         */
        public Dimension minimumLayoutSize(Container c) {
            return new Dimension(0, 0);
        }

        /**
         *合适的布局大小
         * @param c 容器
         * @return 大小
         */
        public Dimension preferredLayoutSize(Container c) {
            return new Dimension(0, 0);
        }

        /**
         * 去掉布局组件
         * @param c 组件
         */
        public void removeLayoutComponent(Component c) {
        }

        /**
         * 增加布局组件
         * @param string 名字
         * @param c 组件
         */
        public void addLayoutComponent(String string, Component c) {
        }
    }

   	/*
        * The following methods only exist in order to be able to access protected
   	 * members in the superclass, because these are otherwise not available
   	 * in any inner class.
   	 */

    int getOneTouchSizeFromSuper() {
        return super.ONE_TOUCH_SIZE;
    }

    int getOneTouchOffsetFromSuper() {
        return super.ONE_TOUCH_OFFSET;
    }

    int getOrientationFromSuper() {
        return super.orientation;
    }

    JSplitPane getSplitPaneFromSuper() {
        return super.splitPane;
    }

    JButton getLeftButtonFromSuper() {
        return super.leftButton;
    }

    JButton getRightButtonFromSuper() {
        return super.rightButton;
    }

}