/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import com.fr.design.mainframe.RGridLayout;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-8
 */
public class PolyEditorLayout extends RGridLayout {
	public static final String BottomCorner = "BottomCorner";

	protected Component bottomCorner;

	@Override
	public void addLayoutComponent(String name, Component comp) {
		super.addLayoutComponent(name, comp);
		if (BottomCorner.equals(name)) {
			bottomCorner = comp;
		}
	}

	@Override
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int bottom = target.getHeight() - insets.bottom;
			int left = insets.left;
			int right = target.getWidth() - insets.right;

			Dimension gridRowPreferredSize = this.gridRow.getPreferredSize();
			Dimension gridColumnPreferredSize = this.gridColumn.getPreferredSize();

			int cornerWidth = gridRowPreferredSize.width;
			int cornerHeight = gridColumnPreferredSize.height;
			this.gridCorner.setBounds(left, top, cornerWidth, cornerHeight);

			int verticalWith = PolyConstants.OPERATION_SIZE;
			int horizontalHeight = PolyConstants.OPERATION_SIZE;

			this.verticalBar.setBounds(right - verticalWith, 0, verticalWith, cornerHeight);
			this.horizontalBar.setBounds(0, bottom - horizontalHeight, cornerWidth, horizontalHeight);

			this.gridColumn.setBounds(left + gridRowPreferredSize.width, top, right - gridRowPreferredSize.width - verticalWith, gridColumnPreferredSize.height);
			this.gridRow.setBounds(left, top + gridColumnPreferredSize.height, gridRowPreferredSize.width, bottom - gridColumnPreferredSize.height - horizontalHeight);
			
			int settingWidth = PolyConstants.OPERATION_SIZE * 2;
			int settingHeight = PolyConstants.OPERATION_SIZE * 2;
			if (this.bottomCorner != null) {
				this.bottomCorner.setBounds(right - settingWidth, bottom - settingHeight, settingWidth, settingHeight);
			}
			// Grid
			this.grid.setBounds(left + gridRowPreferredSize.width, top + gridColumnPreferredSize.height, right - gridRowPreferredSize.width - verticalWith, bottom - gridColumnPreferredSize.height
					- horizontalHeight);
		}
	}
}