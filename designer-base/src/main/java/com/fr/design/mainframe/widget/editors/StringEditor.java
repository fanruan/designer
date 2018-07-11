package com.fr.design.mainframe.widget.editors;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;

/**
 * @author richer
 * @since 6.5.3
 * 字符串编辑器
 */
public class StringEditor extends AbstractPropertyEditor {

    private JPanel panel;
    private UITextField textField;

    public StringEditor() {
        panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        textField = new UITextField();
		panel.add(textField, BorderLayout.CENTER);
		textField.setBorder(null);
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				firePropertyChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				firePropertyChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				firePropertyChanged();
			}
		});
    }

    @Override
    public void setValue(Object value) {
        textField.setText((String) value);
    }

    @Override
    public Object getValue() {
        return textField.getText();
    }

    @Override
    public Component getCustomEditor() {
        return textField;
    }

    @Override
    public void validateValue() throws ValidationException {
    }
}