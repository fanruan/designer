package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.mobile.ui.MobileStylePane;
import com.fr.design.mainframe.widget.wrappers.MobileStyleWrapper;
import com.fr.form.ui.mobile.MobileStyle;

import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class AccessibleMobileStyleEditor extends UneditableAccessibleEditor {

    private MobileStylePane stylePane;
    private static final Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

    public AccessibleMobileStyleEditor(MobileStylePane stylePane) {
        super(new MobileStyleWrapper());
        this.stylePane = stylePane;
    }

    @Override
    protected void showEditorPane() {
        stylePane.setPreferredSize(DEFAULT_DIMENSION);
        BasicDialog dlg = stylePane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                setValue(stylePane.update());
                fireStateChanged();
            }
        });
        stylePane.populate((MobileStyle) getValue());
        dlg.setVisible(true);
    }
}
