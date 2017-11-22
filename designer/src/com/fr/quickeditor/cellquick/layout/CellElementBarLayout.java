package com.fr.quickeditor.cellquick.layout;

import javax.swing.*;
import java.awt.*;

/**
 * 单元格元素面板的滚动条
 *
 * @see com.fr.design.mainframe.AbstractAttrPane.BarLayout
 */
public abstract class CellElementBarLayout implements LayoutManager {

    private JPanel leftContentPane;

    protected CellElementBarLayout(JPanel leftContentPane) {
        this.leftContentPane = leftContentPane;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return leftContentPane.getPreferredSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return leftContentPane.getMinimumSize();
    }

    @Override
    public abstract void layoutContainer(Container parent);
}