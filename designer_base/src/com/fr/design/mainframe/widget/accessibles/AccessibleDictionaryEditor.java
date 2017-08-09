package com.fr.design.mainframe.widget.accessibles;

import javax.swing.*;

import com.fr.data.Dictionary;
import com.fr.design.mainframe.widget.wrappers.DictionaryWrapper;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;

import java.awt.*;

public class AccessibleDictionaryEditor extends UneditableAccessibleEditor {

    private DictionaryPane dictPane;

    public AccessibleDictionaryEditor() {
        super(new DictionaryWrapper());
    }


    @Override
    protected void showEditorPane() {
        if (dictPane == null) {
            dictPane = new DictionaryPane();
        }

        BasicDialog dlg = dictPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                Dictionary dict = dictPane.updateBean();
                setValue(dict);
                fireStateChanged();
            }
        });
        dictPane.populateBean((Dictionary) getValue());
        dlg.setVisible(true);
    }
}