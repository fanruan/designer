package com.fr.design.gui.icombobox;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

/*
 * 显示和选择的项不同的下拉框
 */
public class DictionaryComboBox<T> extends UIComboBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = -19512646054322609L;
	private Object[] keys;
	private String[] values;


    public DictionaryComboBox(T[] keys, String[] displays) {
        this(keys, displays, true);
    }

	public DictionaryComboBox(Object[] keys, String[] displays, boolean editable) {
		this.initComboBox(keys, displays, editable);
	}
	
	private void initComboBox(Object[] keys, String[] displays, boolean editable){
		this.setPreferredSize(new Dimension(60, 20));
		this.keys = keys;
		this.values = displays;
		this.setModel(new DefaultComboBoxModel(keys));
		this.setRenderer(new UIComboBoxRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6497147896537056134L;

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);

				for (int i = 0, len = DictionaryComboBox.this.keys.length; i < len; i++) {
					if (DictionaryComboBox.this.keys[i].equals(value)) {
						this.setText(DictionaryComboBox.this.values[i]);
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