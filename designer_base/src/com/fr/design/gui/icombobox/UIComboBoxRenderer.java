package com.fr.design.gui.icombobox;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;

import com.fr.design.constants.UIConstants;

import javax.swing.JList;
import javax.swing.JLabel;

/**
 * 
 * @author zhou
 * @since 2012-5-18上午11:18:30
 */
public class UIComboBoxRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	public UIComboBoxRenderer() {
		super();
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		cellHasFocus = cellHasFocus && !this.isEnabled();
		JLabel renderer =(JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		renderer.setForeground(Color.black);
		list.setSelectionBackground(UIConstants.COMBOBOX_BTN_PRESS);
		list.setSelectionForeground(Color.black);
		renderer.setText(" " + renderer.getText());
		return renderer;
	}
}