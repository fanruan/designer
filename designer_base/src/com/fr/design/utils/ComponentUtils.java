package com.fr.design.utils;

import com.fr.general.ComparatorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * 工具类，提供常用的工具方法
 */
public class ComponentUtils {

    private ComponentUtils() {
    }

    public static boolean isComponentVisible(Component comp) {
        if (!comp.isVisible() && !isRootComponent(comp)) {
            return false;
        }
        Component parent = comp.getParent();

        return parent == null || isComponentVisible(parent);

    }

    /**
     * 获取component所在的容器的绝对位置
     */
    public static Rectangle getRelativeBounds(Component component) {
        Rectangle bounds = new Rectangle(0, 0, component.getWidth(), component.getHeight());
        Container parent = component.getParent();

        while (parent != null) {
            bounds.x += component.getX();
            bounds.y += component.getY();
            component = parent;
            parent = component.getParent();
        }

        return bounds;
    }

    /**
     * 恢复双缓冲状态，dbcomponents保存着初始状态为启动双缓冲的组件
     */
    public static void resetBuffer(ArrayList<JComponent> dbcomponents) {
        for (JComponent jcomponent : dbcomponents) {
            jcomponent.setDoubleBuffered(true);
        }
    }

    /**
     * 禁止双缓冲状态，并将初始状态为启动双缓冲的组件保存到dbcomponents中
     */
    public static void disableBuffer(Component comp, ArrayList<JComponent> dbcomponents) {
        if ((comp instanceof JComponent) && comp.isDoubleBuffered()) {
            JComponent jcomponent = (JComponent) comp;

            dbcomponents.add(jcomponent);
            jcomponent.setDoubleBuffered(false);
        }

        if (comp instanceof Container) {
            Container container = (Container) comp;
            int count = container.getComponentCount();

            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    Component component = container.getComponent(i);

                    disableBuffer(component, dbcomponents);
                }
            }
        }
    }

    public static int indexOfComponent(Container container, Component target) {
        int count = container.getComponentCount();

        for (int i = 0; i < count; i++) {
            Component child = container.getComponent(i);

            if (child.equals(target)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 计算组件root相对于其顶层容器的可见区域
     */
    public static Rectangle computeVisibleRectRel2Root(Component root) {
        Container container = findAncestorScrollPane(root);

        if (container == null) {
            return getRelativeBounds(root);
        } else {
            // 如果是JScrollPane的子组件，需要计算其viewport与改组件的交叉的可见区域
            return getBoundsRel2Parent(root, container);
        }
    }

    /**
     * 计算组件root相对于其顶层容器的可见区域
     */
    public static Rectangle computeVisibleRect(JComponent root) {
        Rectangle rootBounds = ComponentUtils.getRelativeBounds(root);
        Rectangle rect = computeVisibleRectRel2Root(root);
        rect.x -= rootBounds.x;
        rect.y -= rootBounds.y;

        return rect;
    }

    private static Rectangle getBoundsRel2Parent(Component child, Container parent) {
        Rectangle cRect = getRelativeBounds(child);
        Rectangle pRect = getRelativeBounds(parent);
        Rectangle bounds = new Rectangle();
        Rectangle2D.intersect(cRect, pRect, bounds);

        return bounds;
    }

    public static Container findAncestorScrollPane(Component p) {
        if ((p == null) || !(p instanceof Container)) {
            return null;
        }

        Container c = p.getParent();

        return findAncestorScrollPane(c);
    }

    public static boolean isRootComponent(Component root) {
        Container parent = root.getParent();
        return parent == null;
    }

    public static boolean isChildOf(Component component, Class parent) {
        Container container = component.getParent();
        if (container != null) {
            if (ComparatorUtils.equals(container.getClass(), parent)) {
                return true;
            } else {
                return isChildOf(container, parent);
            }
        }
        return false;
    }
}