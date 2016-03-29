package com.fr.design.designer.properties;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.form.layout.FRCardLayout;
import com.fr.form.ui.container.WCardLayout;

public class CardLayoutPropertiesGroupModel implements GroupModel {

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private CardDefaultShowRenderer defaultShowRenderer;
    private CardDefaultShowEditor defaultShowEditor;
    private WCardLayout layout;
    private XWCardLayout xLayout;

	public CardLayoutPropertiesGroupModel(XWCardLayout Container) {
		this.layout = Container.toData();
		this.xLayout = Container;
		this.renderer = new DefaultTableCellRenderer();
		this.editor = new PropertyCellEditor(new IntegerPropertyEditor());
		this.defaultShowRenderer = new CardDefaultShowRenderer(layout);
		this.defaultShowEditor = new CardDefaultShowEditor(layout);
	}

    @Override
    public String getGroupName() {
        return Inter.getLocText("FR-Engine-Tab_Layout_Widget_Size");
    }

    @Override
	public int getRowCount() {
		return 2;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		switch (row) {
		case 0:
			return renderer;
		case 1:
			return renderer;
		default:
			return defaultShowRenderer;
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
			return defaultShowEditor;
		}
	}

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("FR-Engine-Tab_Layout_Width");
                default:
                	return Inter.getLocText("FR-Engine-Tab_Layout_Height");
                
                	
            }
        } else {
            switch (row) {
                case 0:
                    return (int)xLayout.getSize().getHeight();
                default:
                	return (int)xLayout.getSize().getWidth();
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
					((FRCardLayout)xLayout.getLayout()).setHgap(v);
					return true;
				case 1:
					layout.setVgap(v);
					((FRCardLayout)xLayout.getLayout()).setVgap(v);
					return true;
				case 2:
					layout.setShowIndex(v);
					xLayout.showCard();
					return true;
				default:
					return false;
			}
		}
	}

    /**
     * 是否可以编辑
     * @param 列
     * @return 是否可以编辑
     */
    @Override
    public boolean isEditable(int row) {
        return false;
    }
}