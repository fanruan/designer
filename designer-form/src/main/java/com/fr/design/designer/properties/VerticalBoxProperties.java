package com.fr.design.designer.properties;


import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XWVerticalBoxLayout;
import com.fr.design.form.layout.FRVerticalLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.form.ui.container.WVerticalBoxLayout;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class VerticalBoxProperties implements GroupModel {

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private WVerticalBoxLayout layout;
    private XWVerticalBoxLayout wLayout;

    public VerticalBoxProperties(XWVerticalBoxLayout container) {
    	wLayout = container;
        this.layout = container.toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    @Override
    public String getGroupName() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Vertical_Box_Layout_Duplicate");
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        return renderer;
    }

    @Override
    public TableCellEditor getEditor(int row) {
        return editor;
    }

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            switch (row) {
                case 0:
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Hgap");
                case 1:
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Vgap");
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getHgap();
                case 1:
                    return layout.getVgap();
            }
        }
        return null;
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 0) {
            return false;
        } else {
            switch (row) {
                case 0:
                    int v = 0;
                    if (value != null) {
                        v = ((Number) value).intValue();
                    }
                    layout.setHgap(v);
                    ((FRVerticalLayout)wLayout.getLayout()).setHgap(v);
                    return true;
                case 1:
                    v = 0;
                    if (value != null) {
                        v = ((Number) value).intValue();
                    }
                    layout.setVgap(v);
                    ((FRVerticalLayout)wLayout.getLayout()).setVgap(v);
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public boolean isEditable(int row) {
        return true;
    }
}
