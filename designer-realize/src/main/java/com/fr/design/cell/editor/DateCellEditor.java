package com.fr.design.cell.editor;

import java.awt.Component;
import java.util.Date;

import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * Date CellEditor
 */
public class DateCellEditor extends AbstractCellEditor {
    private UIDatePicker comboBox; //text field.

    /**
     * Constructor.
     */
    public DateCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
    	super(ePane);
        comboBox = new UIDatePicker();
        this.comboBox.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Return the value of the CellEditor.
     */
    public Object getCellEditorValue()  throws Exception {
        return this.comboBox.getSelectedDate();
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
    public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution) {
        Object value = null;
        if(cellElement != null) {
            value = cellElement.getValue();
        }
        if (value == null || !(value instanceof Date)) {
            value = new Date();
        }

        this.comboBox.setSelectedItem(value);
        return this.comboBox;
    }
}