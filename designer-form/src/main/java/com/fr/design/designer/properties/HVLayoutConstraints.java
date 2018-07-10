package com.fr.design.designer.properties;

import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;

public abstract class HVLayoutConstraints implements ConstraintsGroupModel {
	
	protected DefaultTableCellRenderer renderer;
	protected LayoutConstraintsEditor editor1;
	protected PropertyCellEditor editor2;
	protected Widget widget;
	protected XLayoutContainer parent;
	
    public HVLayoutConstraints(XLayoutContainer parent, Component comp) {
        this.parent = parent;
        this.widget = ((XWidgetCreator) comp).toData();
        this.renderer = new DefaultTableCellRenderer();
        
        this.editor2 = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("Layout_Constraints");
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        return this.renderer;
    }

    @Override
    public TableCellEditor getEditor(int row) {
        if (row == 0) {
            return this.editor1;
        } else {
            return this.editor2;
        }
    }

    @Override
    public boolean isEditable(int row) {
    	if(row == 1) {
    		return !(widget instanceof FreeButton && ((FreeButton) widget).isCustomStyle());
    	}
        return true;
    }
}