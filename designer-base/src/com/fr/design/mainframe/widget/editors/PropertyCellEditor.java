package com.fr.design.mainframe.widget.editors;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.fr.design.Exception.ValidationException;

public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor, Serializable {

    private ExtendedPropertyEditor editor;
    protected Component editorComponent;
    protected int clickCountToStart;
    protected Object value;

    public PropertyCellEditor(ExtendedPropertyEditor propertyEditor) {
        this.editor = propertyEditor;
        this.editorComponent = this.editor.getCustomEditor();
        this.clickCountToStart = 1;
    }

    @Override
    public Object getCellEditorValue() {
        return editor.getValue();
    }
    
    public ExtendedPropertyEditor getCellEditor() {
    	return editor;
    }
    
    public Component getComponent() {
        return editorComponent;
    }

    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    public int getClickCountToStart() {
        return clickCountToStart;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }

        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        try {
            editor.validateValue();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(editorComponent, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            editorComponent.requestFocus();
            return false;
        }

        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editorComponent.setForeground(table.getSelectionForeground());
        editorComponent.setBackground(table.getSelectionBackground());
        editorComponent.setFont(table.getFont());
        editor.setValue(value);

        return editorComponent;
    }
}