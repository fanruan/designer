/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.xtable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.fr.base.Formula;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.ColorEditor;
import com.fr.design.mainframe.widget.editors.DimensionEditor;
import com.fr.design.mainframe.widget.editors.DoubleEditor;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.FloatEditor;
import com.fr.design.mainframe.widget.editors.FontEditor;
import com.fr.design.mainframe.widget.editors.FormulaEditor;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.LongEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.design.mainframe.widget.renderer.ColorCellRenderer;
import com.fr.design.mainframe.widget.renderer.DimensionCellRenderer;
import com.fr.design.mainframe.widget.renderer.FontCellRenderer;
import com.fr.design.mainframe.widget.renderer.PointCellRenderer;
import com.fr.design.mainframe.widget.renderer.RectangleCellRenderer;
import com.fr.third.com.lowagie.text.Rectangle;

/**
 * @author richer
 * @since 6.5.3
 */
public class TableUtils {

    private static HashMap<Class, Class<? extends ExtendedPropertyEditor>> propertyEditorClasses;
    private static HashMap<Class, Class<? extends TableCellRenderer>> cellRendererClasses;

    static {
        propertyEditorClasses = new HashMap<Class, Class<? extends ExtendedPropertyEditor>>();
        propertyEditorClasses.put(String.class, StringEditor.class);
        propertyEditorClasses.put(boolean.class, BooleanEditor.class);
        propertyEditorClasses.put(Color.class, ColorEditor.class);
        propertyEditorClasses.put(Font.class, FontEditor.class);
        propertyEditorClasses.put(Dimension.class, DimensionEditor.class);
        propertyEditorClasses.put(int.class, IntegerPropertyEditor.class);
        propertyEditorClasses.put(Integer.class, IntegerPropertyEditor.class);
        propertyEditorClasses.put(long.class, LongEditor.class);
        propertyEditorClasses.put(Long.class, IntegerPropertyEditor.class);
        propertyEditorClasses.put(float.class, FloatEditor.class);
        propertyEditorClasses.put(Float.class, IntegerPropertyEditor.class);
        propertyEditorClasses.put(double.class, DoubleEditor.class);
        propertyEditorClasses.put(Double.class, IntegerPropertyEditor.class);

        propertyEditorClasses.put(Formula.class, FormulaEditor.class);
        // TODO ALEX_SEP
//        propertyEditorClasses.put(DSColumn.class, DSColumnEditor.class);

        cellRendererClasses = new HashMap<Class, Class<? extends TableCellRenderer>>();
        cellRendererClasses.put(Color.class, ColorCellRenderer.class);
        cellRendererClasses.put(Font.class, FontCellRenderer.class);
        cellRendererClasses.put(String.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(int.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(Integer.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(long.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(Long.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(float.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(Float.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(double.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(Double.class, DefaultTableCellRenderer.class);
        cellRendererClasses.put(Point.class, PointCellRenderer.class);
        cellRendererClasses.put(Dimension.class, DimensionCellRenderer.class);
        cellRendererClasses.put(Rectangle.class, RectangleCellRenderer.class);
    }

    public static Class<? extends ExtendedPropertyEditor> getPropertyEditorClass(Class propType) {
        return propertyEditorClasses.get(propType);
    }

    public static Class<? extends TableCellRenderer> getTableCellRendererClass(Class propType) {
        return cellRendererClasses.get(propType);
    }
}