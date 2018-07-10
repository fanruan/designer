/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.utils.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

/**
 * Created by IntelliJ IDEA.
 * User   : Richer
 * Version: 6.5.5
 * Date   : 11-7-19
 * Time   : 上午10:17
 */
public class LayoutUtils {

    /**
     * 主要用于布局管理器改变时，或者组件尺寸被拖拽 发生变化时重新布局。
     */
	
    private LayoutUtils() {

    }

	/**
     * 仅对当前容器重新布局，不考虑其父容器
     */
    public static void layoutContainer(Container container) {
        _layoutContainer(container);
    }

	/**
	 * 获得组件最上层容器，递归调用完成设计界面的更新。
	 */
	public static void layoutRootContainer(Component comp) {
		Container parentContainer = ((comp instanceof Container) ? (Container) comp : comp.getParent());
		if (parentContainer != null) {
			_layoutContainer(getTopContainer(parentContainer));
		}
	}
    
    /**
     * 由container开始上溯到第一个没有布局管理器的组件容器
     */
    public static Container getTopContainer(Container container) {
        Container parent = container.getParent();

        if (parent == null) {
            return container;
        }

        LayoutManager layout = parent.getLayout();

        if (layout == null) {
            return container;
        }

        return getTopContainer(parent);
    }

    /**
     * 递归方式布局整个由container开始的容器
     */
    private static void _layoutContainer(Container container) {
        LayoutManager layout = container.getLayout();
        if (layout != null) {
            container.doLayout();
        }

        int count = container.getComponentCount();

        for (int i = 0; i < count; i++) {
            Component child = container.getComponent(i);
            if (child instanceof Container) {
                _layoutContainer((Container) child);
            }
        }
    }
}