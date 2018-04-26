package com.fr.design.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Create a LayoutManager which contains three parts: left, center and right
 */
public class LeftCenterRightLayout implements LayoutManager, java.io.Serializable {
    public static final String LEFT = "LEFT";
    public static final String CENTER = "CENTER";
    public static final String RIGHT = "RIGHT";

    private Component leftComponent;
    private Component centerComponent;
    private Component rightComponent;

    public LeftCenterRightLayout() {
    }

    /**
     * If the layout manager uses a per-component string,
     * adds the component <code>comp</code> to the layout,
     * associating it
     * with the string specified by <code>name</code>.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
        synchronized (comp.getTreeLock()) {
            /* Special case:  treat null the same as "Centerup". */
            if (name == null) {
                name = "centerup";
            }

            /* Assign the component to one of the known regions of the layout.
        */
            if (LeftCenterRightLayout.LEFT.equals(name)) {
                leftComponent = comp;
            } else if (LeftCenterRightLayout.CENTER.equals(name)) {
                centerComponent = comp;
            } else if (LeftCenterRightLayout.RIGHT.equals(name)) {
                rightComponent = comp;
            } else {
                throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
            }
        }
    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            if (comp == leftComponent) {
                leftComponent = null;
            } else if (comp == centerComponent) {
                centerComponent = null;
            } else if (comp == rightComponent) {
                rightComponent = null;
            }
        }
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param target the container to be laid out
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);

            Dimension leftPreferredSize = leftComponent.getPreferredSize();
            Dimension centerPreferredSize = centerComponent.getPreferredSize();
            Dimension rightPreferredSize = rightComponent.getPreferredSize();

            dim.width += leftPreferredSize.width + centerPreferredSize.width
                    + rightPreferredSize.width;
            dim.height += leftPreferredSize.height;

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param target the component to be laid out
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);

            Dimension leftMinimumSize = leftComponent.getMinimumSize();
            Dimension centerMinimumSize = centerComponent.getMinimumSize();
            Dimension rightMinimumSize = rightComponent.getMinimumSize();

            dim.width += leftMinimumSize.width + centerMinimumSize.width
                    + rightMinimumSize.width;
            dim.height += leftMinimumSize.height;

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    /**
     * Lays out the specified container.
     *
     * @param target the container to be laid out
     */
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int top = insets.top;
            int bottom = target.getHeight() - insets.bottom;
            int left = insets.left;
            int right = target.getWidth() - insets.right;

            Dimension centerPreferredSize = this.centerComponent.getPreferredSize();

            int centerWidth = centerPreferredSize.width;
            int leftOrRightWidth = (right - left - centerWidth) / 2;

            this.leftComponent.setBounds(left, top, leftOrRightWidth, bottom - top);
            this.centerComponent.setBounds(left + leftOrRightWidth, top,
                    centerWidth, bottom - top);
            this.rightComponent.setBounds(right - leftOrRightWidth, top,
                    leftOrRightWidth, bottom - top); //right与left等宽等高
        }
    }
}