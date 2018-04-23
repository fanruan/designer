/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Component;

import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.grid.Grid;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;

/**
 * CellEditor used to edit general object.
 */
public class GeneralCellEditor extends TextCellEditor {

	private CellElement cellElement;
    /**
     * Gets the value of the CellEditor.
     */
    @Override
	public Object getCellEditorValue()  throws Exception {
        Object textValue = super.getCellEditorValue();

        //如果格式是TextFormat，就返回普通文本.
        //TODO, peter 这个地方需要重新设计,我感觉下面的 convertGeneralStringAccordingToExcel，可以用Foramt来实现。
        //peter:只读方式获得Style.
        Style style = cellElement.getStyle();
        if(style != null && style.getFormat() instanceof TextFormat) {
            return textValue;
        }

        return ReportHelper.convertGeneralStringAccordingToExcel(textValue);
    }

    /**
     * Sets an initial <code>cellElement</code> for the editor.  This will cause
     * the editor to <code>stopCellEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param grid        the <code>Grid</code> that is asking the
     *                    editor to edit; can be <code>null</code>
     * @param cellElement the value of the cell to be edited; it is
     *                    up to the specific editor to interpret
     *                    and draw the value.
     */
    @Override
	public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution) {
        this.cellElement = cellElement;

        return super.getCellEditorComponent(grid, cellElement, resolution);
    }
}