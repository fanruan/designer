package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.background.ColorBackground;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.wrappers.BackgroundWrapper;
import com.fr.design.style.background.BackgroundTabPane;
import com.fr.general.Background;

import javax.swing.SwingUtilities;
import java.awt.Dimension;

/**
 * @author kerry
 * @date 2018/1/17
 */
public class AccessibleTabPaneBackgroundEditor extends UneditableAccessibleEditor {
    private BackgroundTabPane choosePane;

    public AccessibleTabPaneBackgroundEditor() {
        super(new BackgroundWrapper());
    }

    @Override
    protected void showEditorPane() {
        choosePane = new BackgroundTabPane();
        choosePane.setPreferredSize(new Dimension(600, 400));
        BasicDialog dlg = choosePane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(choosePane.update());
                fireStateChanged();
            }
        });
        choosePane.populate(getValue() instanceof Background ? (Background) getValue() : new ColorBackground());
        dlg.setVisible(true);
    }
}