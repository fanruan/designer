/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.fr.design.gui.itextfield.UITextField;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class FRGridLayout extends GridLayout implements LayoutManager2, FRLayoutManager {

    private Map<Point, Component> map = new HashMap<Point, Component>();

    public FRGridLayout() {
        this(1, 1, 0, 0);

    }

    public FRGridLayout(int rows, int cols) {
        this(rows, cols, 0, 0);
    }

    public FRGridLayout(int rows, int cols, int hgap, int vgap) {
        super(rows, cols, hgap, vgap);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getColumns(); c++) {
                Point key = new Point(c, r);
                if (ComparatorUtils.equals(comp, map.get(key))) {
                    map.remove(key);
                    return;
                }
            }
        }
    }

    /**
     * 将组件添加到指定的位置上
     * @param comp
     * @param constraints new FRGridLayout.Grid(2, 3);
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        // 如果没有指定添加位置,就从第一排开始逐个添加
        if (constraints == null) {
            for (int r = 0; r < getRows(); r++) {
                for (int c = 0; c < getColumns(); c++) {
                    Point key = new Point(c, r);
                    if (map.get(key) == null) {
                        map.put(key, comp);
                        return;
                    }
                }
            }
            // 如果指定位置了，就不管原来位置上有没有组件都直接覆盖
        } else {
            Point point = (Point) constraints;
            if (point.x > getColumns() - 1 || point.y > getRows() - 1) {
                throw new IllegalArgumentException("Component cannot be add at this point!");
            }
            map.put(point, comp);
        }
    }

    public Point getPoint(Component comp) {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            Point key = (Point) it.next();
            if (ComparatorUtils.equals(map.get(key), comp)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int w = parent.getWidth() - (insets.left + insets.right);
            int h = parent.getHeight() - (insets.top + insets.bottom);
            w = (w - (getColumns() - 1) * getHgap()) / getColumns();
            h = (h - (getRows() - 1) * getVgap()) / getRows();
            Iterator<Point> it = map.keySet().iterator();
            while (it.hasNext()) {
                Point key = it.next();
                int row = key.y;
                int column = key.x;
                map.get(key).setBounds(column * (w + getHgap()) + insets.left, row * (h + getVgap()) + insets.top, w, h);
            }
        }
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        JPanel p = (JPanel) f.getContentPane();
        p.setLayout(FRGUIPaneFactory.createBorderLayout());
        FRGridLayout layout = new FRGridLayout(3, 4, 10, 4);
        JPanel pp = new JPanel(layout);
        p.add(pp, BorderLayout.CENTER);
        pp.add(new UIButton("1111"));
        pp.add(new UIButton("111122"));
        pp.add(new UITextField("1111222"), new Point(3, 2));
        f.setSize(400, 400);
        f.setVisible(true);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(0, 0);
    }
}