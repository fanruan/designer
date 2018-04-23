/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import java.awt.Component;

import javax.swing.JList;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;

/**
 * Int combobox.
 * @author zhou
 * @since 2012-6-1下午2:00:14
 */
public class IntComboBox extends UIComboBox {
    public IntComboBox() {
        this.setRenderer(new UIComboBoxRenderer() {
            public Component getListCellRendererComponent(JList list,
                                                          Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Integer) {
                    this.setText(" "+value + "  ");
                }

                return this;
            }
        });
    }

    public int getSelectedInt() {
    	if (this.getSelectedItem() == null) {
    		return -1;
    	}
		return ((Integer)this.getSelectedItem()).intValue();
    }

    public void setSelectedInt(int selectedInt) {
        int index = -1;
        for (int i = 0; i < this.getItemCount(); i++) {
            if (((Integer) this.getItemAt(i)).intValue() == selectedInt) {
                index = i;
                break;
            }
        }
        this.setSelectedIndex(index);
    }
}