package com.fr.design.mainframe;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**custom the layout used in Finereport
 */
public class RGridLayout implements LayoutManager, java.io.Serializable {
    public static final String GridCorner = "GridCorner";
    public static final String GridColumn = "GridColumn";
    public static final String GridRow = "GridRow";
    public static final String Grid = "Grid";

    public static final String HorizontalBar = "HorizontalBar";
    public static final String VerticalBar = "VerticalBar";

    protected Component gridCorner;
    protected Component gridColumn;
    protected Component gridRow;
    protected Component grid;

    protected Component horizontalBar;
    protected Component verticalBar;

    /**
     * Constructs a new border layout with
     * no gaps between components.
     */
    public RGridLayout() {
    }

    /**
     */
    public void addLayoutComponent(String name, Component comp) {
        if (RGridLayout.GridCorner.equals(name)) {
            this.gridCorner = comp;
        } else if (RGridLayout.GridColumn.equals(name)) {
            this.gridColumn = comp;
        } else if (RGridLayout.GridRow.equals(name)) {
            this.gridRow = comp;
        } else if (RGridLayout.Grid.equals(name)) {
            this.grid = comp;

        //滚动条
        } else if (RGridLayout.HorizontalBar.equals(name)) {
            this.horizontalBar = comp;
        } else if (RGridLayout.VerticalBar.equals(name)) {
            this.verticalBar = comp;
        }
    }

    /**
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     */
    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);

            Dimension gridRowMinimumSize = this.gridRow.getMaximumSize();
            Dimension gridColumnMinimumSize = this.gridColumn.getMaximumSize();
            Dimension gridMinimumSize = this.grid.getMaximumSize();
            Dimension verScrollBarMinimumSize = this.verticalBar.getMaximumSize();
            Dimension horScrollBarMinimumSize = this.horizontalBar.getMaximumSize();

            //调整高度.
            dim.width += gridRowMinimumSize.width + gridMinimumSize.width +
                    verScrollBarMinimumSize.width;
            dim.height = gridColumnMinimumSize.height + gridMinimumSize.height +
                    horScrollBarMinimumSize.height;

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    /**
     */
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);

            Dimension gridRowPreferredSize = this.gridRow.getPreferredSize();
            Dimension gridColumnPreferredSize = this.gridColumn.getPreferredSize();
            Dimension gridPreferredSize = this.grid.getPreferredSize();
            Dimension verScrollBarPreferredSize = this.verticalBar.getPreferredSize();
            Dimension horScrollBarPreferredSize = this.horizontalBar.getPreferredSize();

            //调整高度.
            dim.width += gridRowPreferredSize.width + gridPreferredSize.width +
                    verScrollBarPreferredSize.width;
            dim.height = gridColumnPreferredSize.height + gridPreferredSize.height +
                    horScrollBarPreferredSize.height;

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    /**
     */
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int top = insets.top;
            int bottom = target.getHeight() - insets.bottom;
            int left = insets.left;
            int right = target.getWidth() - insets.right;

            Dimension gridRowPreferredSize = this.gridRow.getPreferredSize();
            Dimension gridColumnPreferredSize = this.gridColumn.getPreferredSize();
            Dimension verScrollBarPreferredSize = this.verticalBar.getPreferredSize();
//            Dimension horScrollBarPreferredSize = this.horizontalBar.getPreferredSize();

            this.gridCorner.setBounds(left, top,
                    gridRowPreferredSize.width, gridColumnPreferredSize.height);

            //处理滚动条.
            this.verticalBar.setBounds(right - verScrollBarPreferredSize.width, 0,
                    verScrollBarPreferredSize.width, bottom );
//            this.horizontalBar.setBounds(0, bottom - horScrollBarPreferredSize.height,
//                    right - verScrollBarPreferredSize.width, horScrollBarPreferredSize.height);

            //处理列和行
            this.gridColumn.setBounds(left + gridRowPreferredSize.width, top,
                    right - gridRowPreferredSize.width - verScrollBarPreferredSize.width,
                    gridColumnPreferredSize.height);
            this.gridRow.setBounds(left, top + gridColumnPreferredSize.height,
                    gridRowPreferredSize.width,
                    bottom - gridColumnPreferredSize.height );

            //Grid
            this.grid.setBounds(left + gridRowPreferredSize.width, top + gridColumnPreferredSize.height,
                    right - gridRowPreferredSize.width - verScrollBarPreferredSize.width,
                    bottom - gridColumnPreferredSize.height);
        }
    }

    /**
     */
    @Override
	public String toString() {
        return getClass().getName();
    }
}