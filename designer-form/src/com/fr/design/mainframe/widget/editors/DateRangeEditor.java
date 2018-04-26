package com.fr.design.mainframe.widget.editors;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.Exception.ValidationException;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;

public class DateRangeEditor extends AbstractPropertyEditor {

	private ValueEditorPane dv;

	public DateRangeEditor() {
		dv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
		dv.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChanged();
			}
		});
		for (Editor editor : dv.getCards()) {
			editor.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					firePropertyChanged();
				}

			});
		}
	}

	@Override
	/**
	 * validate value
	 */
	public void validateValue() throws ValidationException {

	}

	@Override
	public Component getCustomEditor() {
		return dv;
	}

	@Override
	public Object getValue() {
		return dv.update();
	}

	@Override
	public void setValue(Object value) {
		dv.populate(value);
	}
}