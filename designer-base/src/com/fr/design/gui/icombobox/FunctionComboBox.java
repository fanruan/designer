/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

import com.fr.data.core.DataCoreUtils;
import com.fr.data.util.function.DataFunction;

/**
 * DataFunction combobox.
 */
public class FunctionComboBox extends UIComboBox {

	/**
	 * Constructor.
	 */
	public FunctionComboBox(DataFunction[] functionArray) {
		this.setModel(new DefaultComboBoxModel(functionArray));
		this.setRenderer(functionCellRenderer);
	}

	/**
	 * Return function.
	 */
	public DataFunction getFunction() {
		return (DataFunction) this.getSelectedItem();
	}

	/**
	 * Set function.
	 */
	public void setFunction(DataFunction function) {
		if (function == null) {
			return;
		}

		DefaultComboBoxModel model = (DefaultComboBoxModel) this.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			DataFunction tmpFunction = (DataFunction) model.getElementAt(i);
			if (tmpFunction == null) {
				continue;
			}

			if (function.getClass().equals(tmpFunction.getClass())) {
				this.setSelectedIndex(i);
				break;
			}
		}
	}

	UIComboBoxRenderer functionCellRenderer = new UIComboBoxRenderer() {
		public Component getListCellRendererComponent(JList list,
													  Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof DataFunction) {
				DataFunction function = (DataFunction) value;
				this.setText(" " + DataCoreUtils.getFunctionDisplayName(function));
			}

			return this;
		}
	};
}