package com.fr.design.mainframe.widget.accessibles;

import javax.swing.SwingUtilities;

import com.fr.design.mainframe.widget.wrappers.RegexWrapper;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.RegPane;
import com.fr.form.ui.reg.RegExp;

/*
 * 正则表达式编辑器
 */
public class AccessibleRegexEditor extends UneditableAccessibleEditor {

    private RegPane regPane;

    public AccessibleRegexEditor() {
        super(new RegexWrapper());
    }
    
	protected RegPane initRegPane() {
		return new RegPane();
	}

    @Override
    protected void showEditorPane() {
        if (regPane == null) {
            regPane = initRegPane();
        }

        BasicDialog dlg = regPane.showWindow(SwingUtilities.getWindowAncestor(this));
        regPane.populate((RegExp) getValue());
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                RegExp regExp = regPane.update();
                setValue(regExp);
                fireStateChanged();
            }
        });
        dlg.setVisible(true);
    }
    
	public static class AccessibleRegexEditor4TextArea extends AccessibleRegexEditor {
		protected RegPane initRegPane() {
			return new RegPane(RegPane.TEXTAREA_REG_TYPE);
		}
	}
}