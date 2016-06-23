package com.fr.design.designer.properties.mobile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BooleanRender extends JCheckBox implements TableCellRenderer, UIResource {
	private static final Border noFocusBorder = new EmptyBorder(1, 0, 0, 0);

	public BooleanRender() {
		super();
		setHorizontalAlignment(JLabel.LEFT);
		setBorderPainted(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
												   boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		}
		else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelected((value != null && ((Boolean)value).booleanValue()));

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(noFocusBorder);
		}

		return this;
	}
}