package com.fr.design.widget.btn;

import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.dialog.BasicPane;
import com.fr.design.widget.btn.ButtonConstants;
import com.fr.form.ui.Button;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午6:21
 */
public abstract class ButtonDetailPane<T extends Button> extends BasicPane {
    private List<ChangeListener> ls = new ArrayList<ChangeListener>();

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Button");
    }

    public void populate(T button) {

    }

    public abstract T createButton();

    public abstract T update();

    protected void typeChange(Object obj) {
        for (int i = 0, len = ls.size(); i < len; i++) {
            ls.get(i).stateChanged(new ChangeEvent(obj));
        }
    }

    public void addTypeChangeListener(ChangeListener l) {
        ls.add(l);
    }

    public abstract Class classType();

    protected DictionaryComboBox createButtonTypeComboBox() {
        final DictionaryComboBox dictionaryComboBox = new DictionaryComboBox(ButtonConstants.CLASSES4BUTTON, ButtonConstants.TYPES4BUTTON, false);
        dictionaryComboBox.setSelectedItem(classType());
        dictionaryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeChange(dictionaryComboBox.getSelectedItem());
            }
        });
        return dictionaryComboBox;
    }
}