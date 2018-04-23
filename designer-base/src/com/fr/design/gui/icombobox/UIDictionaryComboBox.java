package com.fr.design.gui.icombobox;

import javax.swing.*;
import java.awt.*;

public class UIDictionaryComboBox<T> extends UIComboBox {
	private static final long serialVersionUID = -19512646054322609L;
	private T[] keys;
	private String[] values;

	public UIDictionaryComboBox(T[] keys, String[] displays) {
		super();
		this.initComboBox(keys, displays);
	}

	private void initComboBox(T[] keys, String[] displays) {
		this.keys = keys;
		this.values = displays;
		this.setModel(new DefaultComboBoxModel(keys));
		this.setRenderer(new UIComboBoxRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6497147896537056134L;

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				for (int i = 0, len = UIDictionaryComboBox.this.keys.length; i < len; i++) {
					if (UIDictionaryComboBox.this.keys[i].equals(value)) {
						this.setText(" " + UIDictionaryComboBox.this.values[i]);
						break;
					}
				}
				return this;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T)super.getSelectedItem();
	}
}