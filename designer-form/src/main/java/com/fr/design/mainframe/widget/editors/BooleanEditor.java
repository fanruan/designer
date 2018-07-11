package com.fr.design.mainframe.widget.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icheckbox.UICheckBox;

/**
 * @author richer
 * @since 6.5.3
 * 布尔类型值的编辑器
 */
public class BooleanEditor extends AbstractPropertyEditor {

    private UICheckBox checkBox;

    public BooleanEditor() {
        checkBox = new UICheckBox();
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                firePropertyChanged();
            }
        });
    }

    @Override
    public void setValue(Object value) {
        Boolean b = (Boolean) value;
        checkBox.setSelected((b == null) ? false : b.booleanValue());
    }

    @Override
    public Object getValue() {
        return checkBox.isSelected() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Component getCustomEditor() {
        return checkBox;
    }

    @Override
    public void validateValue() throws ValidationException {
    }
}