package com.fr.design.gui.imenu;


import com.fr.design.gui.iscrollbar.UIScrollBar;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Author : daisy
 * Date: 13-8-27
 * Time: 上午9:12
 */
public class UIScrollPopUpMenu extends UIPopupMenu {
    private static final int MAX_SHOW_NUM = 27;

    private UIScrollBar scrollBar;


    public UIScrollPopUpMenu() {
        super();
        setLayout(new ScrollPopupMenuLayout());
        super.add(getScrollBar());
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                UIScrollBar scrollBar = getScrollBar();
                int amount = (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
                        ? e.getUnitsToScroll() * scrollBar.getUnitIncrement()
                        : (e.getWheelRotation() < 0 ? -1 : 1) * scrollBar.getBlockIncrement();

                scrollBar.setValue(scrollBar.getValue() + amount);
                e.consume();
            }
        });
    }


    public void paintChildren(Graphics g) {
        Insets insets = getInsets();
        g.clipRect(insets.left, insets.top, getWidth(), getHeight() - insets.top - insets.bottom);
        super.paintChildren(g);
    }

    /**
     * 展现 popupmenu
     * @param invoker 组件
     * @param x 定位
     * @param y   定位
     */
    public void show(Component invoker, int x, int y) {
        UIScrollBar scrollBar = getScrollBar();
        if (scrollBar.isVisible()) {
            int extent = 0;
            int max = 0;
            int i = 0;
            int unit = -1;
            int width = 0;
            for (Component comp : getComponents()) {
                if (!(comp instanceof UIScrollBar)) {
                    Dimension preferredSize = comp.getPreferredSize();
                    width = Math.max(width, preferredSize.width);
                    if (unit < 0) {
                        unit = preferredSize.height;
                    }
                    if (i++ < MAX_SHOW_NUM) {
                        extent += preferredSize.height;
                    }
                    max += preferredSize.height;
                }
            }

            Insets insets = getInsets();
            width += insets.left + insets.right + scrollBar.getWidth();
            int heightMargin = insets.top + insets.bottom;
            scrollBar.setUnitIncrement(unit);
            scrollBar.setBlockIncrement(extent);
            scrollBar.setValues(0, heightMargin + extent, 0, heightMargin + max);
            int height = heightMargin + extent;

            setPopupSize(new Dimension(width, height));
        }

        super.show(invoker, x, y);
    }


    private UIScrollBar getScrollBar() {
        if (scrollBar == null) {
            scrollBar = new UIScrollBar(UIScrollBar.VERTICAL);
            scrollBar.addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    doLayout();
                    repaint();
                }
            });

            scrollBar.setVisible(false);
        }

        return scrollBar;
    }


    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);

        if (MAX_SHOW_NUM < getComponentCount() - 1) {
            getScrollBar().setVisible(true);
        }
    }
    /**
     * 移除
     * @param index 指定位置
     */
    public void remove(int index) {
        // can't remove the scrollbar
        ++index;

        super.remove(index);

        if (MAX_SHOW_NUM >= getComponentCount() - 1) {
            getScrollBar().setVisible(false);
        }
    }


    private class ScrollPopupMenuLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            int scrollBarWidth = 0;
            int visibleAmount = Integer.MAX_VALUE;
            Dimension dim = new Dimension();
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    if (comp instanceof UIScrollBar) {
                        UIScrollBar scrollBar = (UIScrollBar) comp;
                        scrollBarWidth = scrollBar.getWidth();
                        visibleAmount = scrollBar.getVisibleAmount();
                    } else {
                        Dimension pref = comp.getPreferredSize();
                        dim.width = Math.max(dim.width, pref.width);
                        dim.height += pref.height;
                    }
                }
            }

            Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right + scrollBarWidth;
            dim.height = Math.min(dim.height + insets.top + insets.bottom, visibleAmount);

            return dim;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            int visibleAmount = Integer.MAX_VALUE;
            Dimension dim = new Dimension();
            for (Component comp : parent.getComponents()) {
                if (comp.isVisible()) {
                    if (comp instanceof UIScrollBar) {
                        UIScrollBar scrollBar = (UIScrollBar) comp;
                        visibleAmount = scrollBar.getVisibleAmount();
                    } else {
                        Dimension min = comp.getMinimumSize();
                        dim.width = Math.max(dim.width, min.width);
                        dim.height += min.height;
                    }
                }
            }

            Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right;
            dim.height = Math.min(dim.height + insets.top + insets.bottom, visibleAmount);

            return dim;
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();

            int width = parent.getPreferredSize().width - insets.left - insets.right;
            int height = parent.getHeight() - insets.top - insets.bottom;

            int x = insets.left;
            int y = insets.top;
            int position = 0;

            for (Component comp : parent.getComponents()) {
                if ((comp instanceof UIScrollBar) && comp.isVisible()) {
                    UIScrollBar scrollBar = (UIScrollBar) comp;
                    Dimension dim = scrollBar.getPreferredSize();
                    scrollBar.setBounds(x + width - 1, y, dim.width, height);
                    width -= dim.width;
                    position = scrollBar.getValue();
                }
            }

            y -= position;
            for (Component comp : parent.getComponents()) {
                if (!(comp instanceof UIScrollBar) && comp.isVisible()) {
                    Dimension pref = comp.getPreferredSize();
                    comp.setBounds(x, y, width, pref.height);
                    y += pref.height;
                }
            }

        }
    }
}