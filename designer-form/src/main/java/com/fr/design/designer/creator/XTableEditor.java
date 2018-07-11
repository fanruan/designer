/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.fr.form.ui.Table;

/**
 * @author richer
 * @since 6.5.3
 */
public class XTableEditor extends XWidgetCreator {

    public XTableEditor(Table widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
    	return new CRPropertyDescriptor[0];
    	// TODO ALEX_SEP
//        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
//            super.supportedDescriptor(),
//            new CRPropertyDescriptor[]{
//                new CRPropertyDescriptor("indexWidgets", this.data.getClass())
//                    .setEditorClass(GridEditor.class)
//                    .setRendererClass(GridWidgetRenderer.class),
//                new CRPropertyDescriptor("dataURL", this.data.getClass()),
//                new CRPropertyDescriptor("databinding", this.data.getClass())
//                    .setI18NName(Inter.getLocText("DataBinding"))
//                    .setEditorClass(DataBindingEditor.class)
//                    .setRendererClass(DataBindingCellRenderer.class)
//                    .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
//            });
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            DefaultTableModel tm = new DefaultTableModel(4, 2);
            editor = new JTable(tm);
        }
        return editor;
    }

    @Override
    public Dimension initEditorSize() {
        return MIDDLE_PREFERRED_SIZE;
    }

    @Override
    protected String getIconName() {
        return "table_16.png";
    }
}