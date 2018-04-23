package com.fr.design.gui.icombobox;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.plaf.basic.BasicComboBoxEditor;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

public class UIComboBoxEditor extends BasicComboBoxEditor {
    protected UITextField textField;
    private Object oldValue;

    public UIComboBoxEditor() {
        textField = new UITextField();
        textField.setRectDirection(Constants.RIGHT);
    }

    @Override
    public Component getEditorComponent() {
        return textField;
    }

    @Override
    public void setItem(Object anObject) {
        if (anObject != null) {

            textField.setText(anObject.toString());
            oldValue = anObject;
        } else {
            textField.setText(StringUtils.EMPTY);
        }

    }

    @Override
    public Object getItem() {
        Object newValue = textField.getText();
        if (oldValue != null && !(oldValue instanceof String)) {
            // The original value is not a string. Should return the value in it's
            // original type.
            if(ComparatorUtils.equals(newValue,oldValue.toString())) {
                return oldValue;
            } else {
                // Must take the value from the textField and get the value and cast it to the new type.
                Class cls = oldValue.getClass();
                try {
                    Method method = cls.getMethod("valueOf", new Class[]{String.class});
                    newValue = method.invoke(oldValue, new Object[]{textField.getText()});
                } catch (Exception ex) {
                    // Fail silently and return the newValue (a String object)
                }
            }
        }
        return newValue;

    }

    @Override
    /**
     *
     */
    public void selectAll() {
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    /**
     *
     */
    public void addActionListener(ActionListener l) {
        textField.addActionListener(l);
    }

    @Override
    /**
     *
     */
    public void removeActionListener(ActionListener l) {
        textField.removeActionListener(l);
    }

}