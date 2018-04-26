package com.fr.design.designer.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;

public class FRBorderLayoutConstraints implements ConstraintsGroupModel {

    private FRBorderLayoutConstraintsRenderer renderer0;
    private DefaultTableCellRenderer renderer;
    private FRBorderConstraintsEditor editor1;
    private PropertyCellEditor editor2;
    private PropertyCellEditor editor3;
    private Widget widget;
    private WBorderLayout layout;
    private XWBorderLayout container;

    public FRBorderLayoutConstraints(Container parent, Component comp) {
        this.container = ((XWBorderLayout) parent);
        this.layout = ((XWBorderLayout) parent).toData();
        this.widget = ((XWidgetCreator) comp).toData();
        this.renderer0 = new FRBorderLayoutConstraintsRenderer();
        this.renderer = new DefaultTableCellRenderer();
        this.editor1 = new FRBorderConstraintsEditor(layout.getDirections());
        this.editor2 = new PropertyCellEditor(new StringEditor());
        this.editor3 = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("Layout_Constraints");
    }

    @Override
    public int getRowCount() {
        Object obj = layout.getConstraints(widget);
        if (obj == null) {
            return 0;
        }
        return BorderLayout.CENTER.equals(obj) ? 2 : 3;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        if (row == 0) {
            return this.renderer0;
        }
        return renderer;
    }

    @Override
    public TableCellEditor getEditor(int row) {
        switch (row) {
            case 0:
                return editor1;
            case 1:
                return editor2;
            default:
                return editor3;
        }
    }

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("BorderLayout-Constraints");
                case 1:
                    return Inter.getLocText("Title");
                default:
                    return getSizeDisplayName();
            }
        } else {
            switch (row) {
                case 0:
                    return getChildPositionDisplayName();
                case 1:
                    return getChildTitle();
                default:
                    return getChildSize();
            }
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 1) {
            switch (row) {
                case 0:
                    return switchWidgets(value);
                case 1:
                    return setChildTitle(value);
                default:
                    return setChildSize(value);
            }
        }
        return true;
    }

    @Override
    public boolean isEditable(int row) {
    	if(row == 2) {
    		return !(widget instanceof FreeButton && ((FreeButton) widget).isCustomStyle());
    	}
        return true;
    }

    private String getSizeDisplayName() {
        Object obj = layout.getConstraints(widget);
        if (BorderLayout.NORTH.equals(obj) || BorderLayout.SOUTH.equals(obj)) {
            return Inter.getLocText("Tree-Height");
        } else if (BorderLayout.WEST.equals(obj) || BorderLayout.EAST.equals(obj)) {
            return Inter.getLocText("Tree-Width");
        } else {
            return StringUtils.EMPTY;
        }
    }

    private String getChildPositionDisplayName() {
        Object obj = layout.getConstraints(widget);
        //return Inter.getLocText("BorderLayout-" + obj);
        return obj.toString();
    }

    private boolean switchWidgets(Object value) {
        Widget nwidget = layout.getLayoutWidget(value);
        if (nwidget == null) {
            layout.removeWidget(widget);
            XWBorderLayout.add(layout, widget, value);
        } else {
            Object constraints = layout.getConstraints(widget);
            layout.removeWidget(widget);
            layout.removeWidget(nwidget);
            XWBorderLayout.add(layout, widget, value);
            XWBorderLayout.add(layout, nwidget, constraints);
        }
        container.convert();
        //TODO:convert后丢失焦点
        return true;
    }

    private int getChildSize() {
        Object obj = layout.getConstraints(widget);
        try {
            Method m = layout.getClass().getDeclaredMethod("get" + obj + "Size");
            Object v = m.invoke(layout);
            if (v instanceof Number) {
                return ((Number) v).intValue();
            }
        } catch (Exception ex) {
            Logger.getLogger(FRBorderLayoutConstraints.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private boolean setChildSize(Object value) {
        int v = 0;
        if (value != null) {
            v = ((Number) value).intValue();
        }
        Object constraints = layout.getConstraints(widget);
        try {
            Method m = layout.getClass().getDeclaredMethod("set" + constraints + "Size", int.class);
            m.invoke(layout, v);
            container.recalculateChildrenPreferredSize();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String getChildTitle() {
        Object obj = layout.getConstraints(widget);
        try {
            Method m = layout.getClass().getDeclaredMethod("get" + obj + "Title");
            return (String) m.invoke(layout);
        } catch (Exception ex) {
            Logger.getLogger(FRBorderLayoutConstraints.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StringUtils.EMPTY;
    }

    private boolean setChildTitle(Object value) {
        Object constraints = layout.getConstraints(widget);
        try {
            Method m = layout.getClass().getDeclaredMethod("set" + constraints + "Title",String.class);
            m.invoke(layout, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}