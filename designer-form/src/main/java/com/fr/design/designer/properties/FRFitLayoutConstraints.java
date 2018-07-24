package com.fr.design.designer.properties;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;


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
    private static final int MINHEIGHT = WLayout.MIN_HEIGHT;
    private static final int MINWIDTH = WLayout.MIN_WIDTH;
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
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget_Size");
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
                    return com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Tree_Width");
                default:
                    return com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Tree_Height");
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
            Rectangle rec = ComponentUtils.getRelativeBounds(parent);
            WFitLayout wFitLayout = parent.toData();
            int minHeight = (int)(MINHEIGHT * wFitLayout.getResolutionScaling());
            int minWidth = (int)(MINWIDTH * wFitLayout.getResolutionScaling());
            PaddingMargin margin= wFitLayout.getMargin();
            switch (row) {
                case 0:
                    if (bounds.width == v){
                        return false;
                    }
                    if(bounds.width == rec.width - margin.getLeft() - margin.getRight()){
                        JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Beyond_Bounds"));
                    }else if(v < minWidth){
                        JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Min_Width") + Integer.toString(minWidth));
                        v = xCreator.getWidth();
                    }
                    difference = bounds.width - v;
                    bounds.width = v;
                    break;
                case 1:
                    if (bounds.height == v){
                        return false;
                    }
                    if(bounds.height == rec.height - margin.getTop() - margin.getBottom()){
                        JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Beyond_Bounds"));
                    }else if(v < minHeight){
                        JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Min_Height") + Integer.toString(minHeight));
                        v = xCreator.getHeight();
                    }
                    difference = bounds.height - v;
                    bounds.height = v;
                    break;
            }
            wFitLayout.setBounds(xCreator.toData(),bounds);
            FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
            Rectangle backupBounds = getBounds();
            FRFitLayoutAdapter layoutAdapter = (FRFitLayoutAdapter) AdapterBus.searchLayoutAdapter(formDesigner, xCreator);
            if (layoutAdapter != null) {
                layoutAdapter.setEdit(true);
                layoutAdapter.calculateBounds(backupBounds, bounds, xCreator, row, difference);
            }
            return true;
        } else {
            return false;
        }
    }

    public Rectangle getBounds(){
        Rectangle bounds = new Rectangle(xCreator.getBounds());
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