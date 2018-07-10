/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

import com.fr.data.util.SortOrder;
import com.fr.general.Inter;

/**
 * Sort order combo box.
 */
public class SortOrderComboBox extends UIComboBox {
    /**
     * Constructor.
     */
    public SortOrderComboBox() {
        this(SortOrderComboBox.SortOrderArray);
    }

    /**
     * Constructor.
     */
    public SortOrderComboBox(SortOrder[] sortOrderArray) {
        this.setModel(new DefaultComboBoxModel(sortOrderArray));
        this.setRenderer(sortOrderCellRenderer);
    }

    /**
     * Return selected SortOrder.
     */
    public SortOrder getSortOrder() {
        return (SortOrder) this.getSelectedItem();
    }

    /**
     * Set selected SortOrder.
     */
    public void setSortOrder(SortOrder sortOrder) {
    	this.setSelectedItem(sortOrder);
    }

    UIComboBoxRenderer sortOrderCellRenderer = new UIComboBoxRenderer() {
        public Component getListCellRendererComponent(JList list,
                                                      Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof SortOrder) {
                SortOrder sortOrder = (SortOrder) value;

                if (sortOrder.getOrder() == SortOrder.ASC) {
                    this.setText(Inter.getLocText("Sort-Ascending"));
                } else if (sortOrder.getOrder() == SortOrder.DESC) {
                    this.setText(Inter.getLocText("Sort-Descending"));
                } else if (sortOrder.getOrder() == SortOrder.ORIGINAL) {
                    this.setText(Inter.getLocText("Sort-Original"));
                }
            }

            return this;
        }
    };

    private static SortOrder[] SortOrderArray = new SortOrder[]{
            new SortOrder(SortOrder.ASC),
            new SortOrder(SortOrder.DESC),
            new SortOrder(SortOrder.ORIGINAL)
    };
}