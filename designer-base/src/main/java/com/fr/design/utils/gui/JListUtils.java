/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.utils.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Some utils method used in JList
 */
public class JListUtils {
	private JListUtils() {
	}

	public static void removeSelectedListItems(JList sourceList) {
		int sItemSelectedIndex = sourceList.getSelectedIndex();
		Object[] sItemSelectedValues = sourceList.getSelectedValues();

		DefaultListModel selectedTableListModel = (DefaultListModel)sourceList.getModel();
		if (selectedTableListModel.size() > 0) {
			for (int i = sItemSelectedValues.length - 1; i >= 0; i--) {
				selectedTableListModel.removeElement(sItemSelectedValues[i]);
			}
		}

		if (sItemSelectedIndex >= 0) {
			if (sItemSelectedIndex >= selectedTableListModel.size()) {
				sourceList.setSelectedIndex(selectedTableListModel.size() - 1);
			} else {
				sourceList.setSelectedIndex(sItemSelectedIndex);
			}
		}
	}

	public static void removeAllListItems(JList sourceList) {
		DefaultListModel selectedTableListModel = (DefaultListModel)sourceList.getModel();
		selectedTableListModel.removeAllElements();
	}

	public static void upListSelectedIndex(JList sourceList) {
		int selectedIndex = sourceList.getSelectedIndex();
		if (selectedIndex > 0) {
			DefaultListModel listModel = (DefaultListModel)sourceList.getModel();

			Object selectedValue = listModel.get(selectedIndex);
			Object newSelectedValue = listModel.get(selectedIndex - 1);
			listModel.set(selectedIndex, newSelectedValue);
			listModel.set(selectedIndex - 1, selectedValue);
			sourceList.setSelectedIndex(selectedIndex - 1);
			sourceList.ensureIndexIsVisible(sourceList.getSelectedIndex());
		}
	}

	public static void downListSelectedIndex(JList sourceList) {
		int selectedIndex = sourceList.getSelectedIndex();
		DefaultListModel listModel = (DefaultListModel)sourceList.getModel();
		if (selectedIndex < listModel.size() - 1) {

			Object selectedValue = listModel.get(selectedIndex);
			Object newSelectedValue = listModel.get(selectedIndex + 1);
			listModel.set(selectedIndex, newSelectedValue);
			listModel.set(selectedIndex + 1, selectedValue);
			sourceList.setSelectedIndex(selectedIndex + 1);
			sourceList.ensureIndexIsVisible(sourceList.getSelectedIndex());
		}
	}
}