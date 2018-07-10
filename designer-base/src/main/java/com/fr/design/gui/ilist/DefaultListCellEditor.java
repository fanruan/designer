package com.fr.design.gui.ilist;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JList;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;



public class DefaultListCellEditor extends DefaultCellEditor implements ListCellEditor {
	private UITextField textField;
	
	public DefaultListCellEditor(final UITextField textField) {
		super(textField);
		this.textField = textField;
	}
	
	public DefaultListCellEditor(final UICheckBox checkBox) {
		super(checkBox);
	}
	
	public DefaultListCellEditor(final UIComboBox comboBox) {
		super(comboBox);
	}

	public Component getListCellEditorComponent(JList list, Object value, boolean isSelected, int index) {
		delegate.setValue(value);
		
		if (textField != null) {
			textField.addFocusListener(new FocusAdapter() {

				public void focusLost(FocusEvent e) {

				stopCellEditing();

				}

			});
		}
		
		
		return editorComponent;
	}
}