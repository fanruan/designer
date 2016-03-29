/*
 * AccessiblePropertyCellEditor.java
 *
 * Created on 2007-8-27, 22:08:32
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fr.design.mainframe.widget.accessibles;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author admin
 */
public class AccessiblePropertyCellEditor extends AbstractCellEditor implements TableCellEditor, PropertyChangeListener, Serializable {

    private PropertyEditor editor;
    protected Component editorComponent;
    protected int clickCountToStart;
    protected Object value;

    public AccessiblePropertyCellEditor(PropertyEditor propertyEditor) {
        this.editor = propertyEditor;
        this.editorComponent = this.editor.getCustomEditor();
        this.clickCountToStart = 1;
        editor.addPropertyChangeListener(this);
    }

    public Object getCellEditorValue() {
        return editor.getValue();
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

    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }

        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editorComponent.setForeground(table.getSelectionForeground());
        editorComponent.setBackground(table.getSelectionBackground());
        editorComponent.setFont(table.getFont());
        editor.setValue(value);

        return editorComponent;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        stopCellEditing();
    }
}