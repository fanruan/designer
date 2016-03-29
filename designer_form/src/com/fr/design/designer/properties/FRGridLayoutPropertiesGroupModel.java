package com.fr.design.designer.properties;


import java.awt.Container;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.creator.XWGridLayout;
import com.fr.form.ui.container.WGridLayout;

/**
 * 格子布局的属性组
 * @author richer
 * @since  6.5.3
 */
public class FRGridLayoutPropertiesGroupModel implements GroupModel {

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private WGridLayout layout;

    public FRGridLayoutPropertiesGroupModel(Container container) {
        this.layout = ((XWGridLayout)container).toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("GridLayout");
    }

    @Override
    public int getRowCount() {
        return 4;
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
                    return Inter.getLocText("Hgap");
                case 1:
                    return Inter.getLocText("Vgap");
                case 2:
                    return Inter.getLocText("Edit-Row_Count");
                case 3:
                    return Inter.getLocText("Edit-Column_Count");
                default:
                    return null;
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getHgap();
                case 1:
                    return layout.getVgap();
                case 2:
                    return layout.getRows();
                case 3:
                    return layout.getColumns();
                default:
                    return null;
            }
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 0) {
            return false;
        } else {
            int v = 0;
            if (value != null) {
                v = ((Number) value).intValue();
            }
            switch (row) {
                case 0:
                    layout.setHgap(v);
                    return true;
                case 1:
                    layout.setVgap(v);
                    return true;
                case 2:
                    layout.setRows(v);
                    return true;
                case 3:
                    layout.setColumns(v);
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