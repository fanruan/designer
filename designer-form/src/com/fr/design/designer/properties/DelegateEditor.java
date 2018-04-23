package com.fr.design.designer.properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class DelegateEditor  extends AbstractCellEditor implements TableCellEditor {
	
	protected JComponent editorComponent;
    protected EditorDelegate delegate;
    protected int clickCountToStart = 1;
    
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
    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }

    @Override
    public boolean stopCellEditing() {
        return delegate.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        delegate.setValue(value);
        return editorComponent;
    }

    protected class EditorDelegate implements ActionListener, ItemListener, ChangeListener,
        Serializable {

        protected Object value;

        public Object getCellEditorValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
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

        public boolean startCellEditing(EventObject anEvent) {
            return true;
        }

        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	DelegateEditor.this.stopCellEditing();
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
        	DelegateEditor.this.stopCellEditing();
        }

		@Override
		public void stateChanged(ChangeEvent e) {
			DelegateEditor.this.stopCellEditing();
			
		}
    }

}