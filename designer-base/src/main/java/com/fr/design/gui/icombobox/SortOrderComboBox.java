/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import com.fr.data.util.SortOrder;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import java.awt.Component;


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
                    this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Ascending"));
                } else if (sortOrder.getOrder() == SortOrder.DESC) {
                    this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Descending"));
                } else if (sortOrder.getOrder() == SortOrder.ORIGINAL) {
                    this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sort_Original"));
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
