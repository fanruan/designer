package com.fr.design.editor.editor;

import java.awt.BorderLayout;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;

public class NoneEditor extends Editor {

    private UITextField textField;
    private String displayValue;

    public NoneEditor() {
        this(null);
    }

    public NoneEditor(String displayValue, String name) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.displayValue = displayValue;
        textField = new UITextField();
        this.add(textField, BorderLayout.CENTER);
        if (displayValue != null) {
            textField.setText(displayValue);
        }
        textField.setEditable(false);
        this.setName(name);
    }

    public NoneEditor(String displayValue) {
        this(displayValue, "");
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
        if (displayValue != null) {
            textField.setText(displayValue);
            textField.setEditable(false);
        }
    }

	public String getIconName(){
		return "type_none";
	}

    @Override
    public boolean accept(Object object) {
        return false;
    }
}