/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.general.Inter;

/**
 * @author richer
 * @since 6.5.3
 */
public class AdjustModeEditor extends AbstractPropertyEditor {
	public static final String[] AjustRowTypes = new String[] {
		Inter.getLocText("No"), Inter.getLocText("Utils-Row_Height"), Inter.getLocText("Utils-Column_Width"), Inter.getLocText("Default")};

    private UIComboBox combobox;

    public AdjustModeEditor() {
        combobox = new DictionaryComboBox<Integer>(new Integer[]{0, 1, 2, 3}, 
        		AjustRowTypes);
        combobox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                firePropertyChanged();
            }
        });
    }

    @Override
    public void validateValue() throws ValidationException {
       
    }

    @Override
    public void setValue(Object o) {
        combobox.setSelectedItem((Integer)o);
    }

    @Override
    public Object getValue() {
        return combobox.getSelectedItem();
    }

    @Override
    public Component getCustomEditor() {
        return combobox;
    }
}