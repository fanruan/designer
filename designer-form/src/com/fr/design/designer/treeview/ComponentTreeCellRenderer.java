package com.fr.design.designer.treeview;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import java.awt.Dimension;

public class ComponentTreeCellRenderer extends DefaultTreeCellRenderer {

	public ComponentTreeCellRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if (value instanceof XCreator) {
			String name = ((XCreator) value).toData().getWidgetName();
			setText(name);
			Icon icon = XCreatorUtils.getCreatorIcon((XCreator) value);
			if (icon != null) {
				setIcon(icon);
			}
		}
		UILabel label = new UILabel();
		label.setText(getText());
		label.setIcon(getIcon());
		Dimension dim = label.getPreferredSize();
		dim.height += 2;
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.setBackgroundNonSelectionColor(UIConstants.TREE_BACKGROUND);
		return this;
	}

	@Override
	public Icon getClosedIcon() {
		return getIcon();
	}

	@Override
	public Icon getLeafIcon() {
		return getIcon();
	}

	@Override
	public Icon getOpenIcon() {
		return getIcon();
	}
}