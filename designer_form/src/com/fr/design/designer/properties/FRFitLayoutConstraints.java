package com.fr.design.designer.properties;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-23
 * Time: 上午10:53
 */
//控件在自适应布局中宽度、高度属性，7.1.1不可编辑
public class FRFitLayoutConstraints implements ConstraintsGroupModel {
    private static final int MINHEIGHT = XCreator.SMALL_PREFERRED_SIZE.height;
    private static final int MINWIDTH = XCreator.SMALL_PREFERRED_SIZE.width;
    private static final int ROWNUM = 2;

    private DefaultTableCellRenderer renderer;
    private PropertyCellEditor editor;
    private XCreator xCreator;
    private XWFitLayout parent;

    public FRFitLayoutConstraints(XWFitLayout parent, XCreator comp) {
        this.parent = parent;
        xCreator = comp;
        renderer = new DefaultTableCellRenderer();
        editor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    @Override
    public String getGroupName() {
        return Inter.getLocText("Widget-Size");
    }

    @Override
    public int getRowCount() {
        return ROWNUM;
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
                    return Inter.getLocText("Tree-Width");
                default:
                    return Inter.getLocText("Tree-Height");
            }
        } else {
            switch (row) {
                case 0:
                    return xCreator.getWidth();
                default:
                    return xCreator.getHeight();
            }
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 1) {
            int v = value == null ? 0 : ((Number) value).intValue();
            int difference =  0;
            Rectangle bounds = getBounds();
            switch (row) {
                case 0:
                    if(v < MINWIDTH){
                        JOptionPane.showMessageDialog(null, Inter.getLocText("Min-Width") + Integer.toString(MINWIDTH));
                        v = xCreator.getWidth();
                    }
                    if (bounds.width == v){
                        return false;
                    }
                    difference = bounds.width - v;
                    bounds.width = v;
                    break;
                case 1:
                    if(v < MINHEIGHT){
                        JOptionPane.showMessageDialog(null, Inter.getLocText("Min-Height") + Integer.toString(MINHEIGHT));
                        v = xCreator.getHeight();
                    }
                    if (bounds.height == v){
                        return false;
                    }
                    difference = bounds.height - v;
                    bounds.height = v;
                    break;
            }
            WFitLayout wFitLayout = parent.toData();
            wFitLayout.setBounds(xCreator.toData(),bounds);
            FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
            Rectangle backupBounds = getBounds();
            FRFitLayoutAdapter layoutAdapter = (FRFitLayoutAdapter) AdapterBus.searchLayoutAdapter(formDesigner, xCreator);
            if (layoutAdapter != null) {
                layoutAdapter.calculateBounds(backupBounds, bounds, xCreator, row, difference);
            }
            return true;
        } else {
            return false;
        }
    }

    public Rectangle getBounds(){
        Rectangle bounds = new Rectangle(xCreator.getBounds());
        XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(xCreator);
        if (parent == null) {
            return bounds;
        }
        Rectangle rec = ComponentUtils.getRelativeBounds(parent);
        bounds.x += rec.x;
        bounds.y += rec.y;
        return bounds;

    }

    /**
     * 该行是否可编辑
     * @param row  行
     * @return 第row行可编辑返回true，否则返回false
     */
    public boolean isEditable(int row) {
        return true;
    }

}