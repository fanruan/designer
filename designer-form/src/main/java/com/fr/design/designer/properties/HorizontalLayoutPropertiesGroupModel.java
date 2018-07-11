package com.fr.design.designer.properties;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.form.layout.FRHorizontalLayout;
import com.fr.form.ui.container.WHorizontalBoxLayout;

/**
 * 流式布局的属性组
 * @author richer
 * @since 6.5.3
 */
public class HorizontalLayoutPropertiesGroupModel implements GroupModel {

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private HorizontalAlignmentRenderer alignmentRenderer;
    private HorizontalAlignmentEditor alignmentEditor;
    private WHorizontalBoxLayout layout;
    private XWHorizontalBoxLayout wLayout;

    public HorizontalLayoutPropertiesGroupModel(XWHorizontalBoxLayout container) {
    	wLayout = container;
        this.layout = container.toData();
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
        alignmentRenderer = new HorizontalAlignmentRenderer();
        alignmentEditor = new HorizontalAlignmentEditor();
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("Layout-HBox");
    }

    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        switch (row) {
            case 0:
                return renderer;
            case 1:
                return renderer;
            default:
                return alignmentRenderer;
        }
    }

    @Override
    public TableCellEditor getEditor(int row) {
        switch (row) {
            case 0:
                return editor;
            case 1:
                return editor;
            default:
                return alignmentEditor;
        }
    }

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("Hgap");
                case 1:
                    return Inter.getLocText("Vgap");
                default:
                    return Inter.getLocText("Alignment");
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getHgap();
                case 1:
                    return layout.getVgap();
                default:
                    return layout.getAlignment();
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
                    ((FRHorizontalLayout)wLayout.getLayout()).setHgap(v);
                    return true;
                case 1:
                    layout.setVgap(v);
                    ((FRHorizontalLayout)wLayout.getLayout()).setVgap(v);
                    return true;
                case 2:
                    layout.setAlignment(v);
                    ((FRHorizontalLayout)wLayout.getLayout()).setAlignment(v);
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