package com.fr.design.designer.properties;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.BorderLayoutDirectionEditor;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.renderer.PropertyCellRenderer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.form.ui.container.WBorderLayout;

public class FRBorderLayoutPropertiesGroupModel implements GroupModel {

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor gapEditor;
    private PropertyCellEditor directionEditor;
    private PropertyCellRenderer directionRenderer;
    private WBorderLayout layout;
    private XWBorderLayout xbl;

    public FRBorderLayoutPropertiesGroupModel(XWBorderLayout xbl) {
    	this.xbl = xbl;
        this.layout = xbl.toData();
        renderer = new DefaultTableCellRenderer();
        gapEditor = new PropertyCellEditor(new IntegerPropertyEditor());
        final ExtendedPropertyEditor propertyeditor = new BorderLayoutDirectionEditor();
//		propertyeditor.addPropertyChangeListener(new PropertyChangeAdapter() {
//			public void propertyChange(PropertyChangeEvent evt) {
//				Object val = propertyeditor.getValue();
//				if (val instanceof Object[]) {
//					Object[] dirs = (Object[]) val;
//					String[] directions = new String[dirs.length];
//					for (int i=0; i<dirs.length; i++) {
//						if (dirs[i] instanceof Item) {
//							directions[i] = Utils.objectToString(((Item) dirs[i]).getValue());
//						}
//					}
//					if (Math.abs(layout.getDirections().length - directions.length) == 1) {
//						layout.refreshDirections(directions);
//					}
//				}
//			}
//		});
        directionEditor = new PropertyCellEditor(propertyeditor);
        directionRenderer = new PropertyCellRenderer(propertyeditor);
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("BorderLayout");
    }

    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
    	if(row == 2) {
    		return directionRenderer;
    	}
        return renderer;
    }

    @Override
    public TableCellEditor getEditor(int row) {
    	if(row == 2) {
    		return directionEditor;
    	}
        return gapEditor;
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
                	return Inter.getLocText(new String[]{"Form-Layout", "Style"});
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getHgap();
                case 1 :
                	return layout.getVgap();
                default:
                    return layout.getDirections();
            }
        }
    }

    @Override
	public boolean setValue(Object value, int row, int column) {
		if (column == 0) {
			return false;
		} else {
			if (row == 0 || row == 1) {
				int v = 0;
				if (value != null) {
					v = ((Number) value).intValue();
				}
				switch (row) {
				case 0:
					layout.setHgap(v);
					((FRBorderLayout)xbl.getLayout()).setHgap(v);
					return true;
				case 1:
					layout.setVgap(v);
					((FRBorderLayout)xbl.getLayout()).setVgap(v);
					return true;
				}
			} else if (row == 2) {
				String[] v = null;
				if (value instanceof Object[]) {
					v = new String[((Object[]) value).length];
					for (int i = 0, len = v.length; i < len; i++) {
						v[i] = (String) ((Item) ((Object[])value)[i]).getValue();
					}
				}
				layout.setDirections(v);
				xbl.convert();
				return true;
			}
			return false;
		}
	}

    @Override
    public boolean isEditable(int row) {
        return true;
    }
}