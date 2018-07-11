package com.fr.design.mainframe.widget.editors;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.form.ui.WidgetInfoConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

public class NameWidgetComboboxEditor extends AbstractPropertyEditor {

	private Object value;
	private UIComboBox comboBox;

	public NameWidgetComboboxEditor() {
		Vector<String> items = new Vector<String>();
		WidgetInfoConfig manager = WidgetInfoConfig.getInstance();
		java.util.Iterator<String> nameIt = manager.getWidgetConfigNameIterator();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			items.add(name);
		}
		comboBox = new UIComboBox(new DefaultComboBoxModel(items));
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					firePropertyChanged();
				}
			}
		});
	}

    /**
     * 校验值
     *
     */
	public void validateValue() throws ValidationException {

	}

	@Override
	public Component getCustomEditor() {
		return comboBox;
	}

	@Override
	public Object getValue() {
		Object selected = comboBox.getSelectedItem();
		return selected == null ? value : selected;
	}

	@Override
	public void setValue(Object value) {
		comboBox.setSelectedItem(value);
		this.value = value;
	}
}