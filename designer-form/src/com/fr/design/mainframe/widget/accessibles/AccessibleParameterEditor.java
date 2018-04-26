package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.mainframe.widget.wrappers.ParameterWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.stable.ParameterProvider;

import javax.swing.*;


public class AccessibleParameterEditor extends UneditableAccessibleEditor {
    private ReportletParameterViewPane parameterPane;

    public AccessibleParameterEditor() {
        super(new ParameterWrapper());
        // TODO Auto-generated constructor stub
    }

    protected void showEditorPane() {
        if (parameterPane == null) {
            parameterPane = new ReportletParameterViewPane(ParameterTableModel.FORM_NORMAL_USE);
        }
        BasicDialog dlg = parameterPane.showWindow(SwingUtilities.getWindowAncestor(this));
        parameterPane.populate((ParameterProvider[]) getValue());
        dlg.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                java.util.List<ParameterProvider> list = parameterPane.update();
                setValue(list.toArray(new ParameterProvider[list.size()]));
                fireStateChanged();
            }
        });
        dlg.setVisible(true);
    }

}