/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.grid;

import com.fr.design.mainframe.ElementCasePane;

import javax.swing.JComponent;


/**
 * <div class="cn">所有格子中组件的父类,GridColumn, GridRow和Grid都继承了这个类.</div>
 * <div class="en">Base component.</div>
 */
public abstract class BaseGridComponent extends JComponent {
    private ElementCasePane reportPane;

    public BaseGridComponent() {
        this(null);
    }
    public BaseGridComponent(ElementCasePane reportPane) {
        this.setElementCasePane(reportPane);
    }

    /**
     * Sets the rpt panel associated with this row.
     *
     * @param reportPane the new rpt panel
     */
    public void setElementCasePane(ElementCasePane reportPane) {
        this.reportPane = reportPane;
    }

    /**
     * Gets the rpt panel associated with this row.
     */
    public ElementCasePane getElementCasePane() {
        return this.reportPane;
    }
}