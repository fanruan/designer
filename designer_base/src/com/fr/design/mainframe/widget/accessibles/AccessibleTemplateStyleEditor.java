package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.base.TemplateStyle;
import com.fr.design.mainframe.widget.wrappers.TemplateStyleWrapper;

import javax.swing.SwingUtilities;
import java.awt.Dimension;

/**
 * Created by kerry on 2017/11/23.
 */
public class AccessibleTemplateStyleEditor extends UneditableAccessibleEditor {

    private static final Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

    private TemplateStylePane stylePane;

    public AccessibleTemplateStyleEditor() {
        super(new TemplateStyleWrapper());
    }

    @Override
    protected void showEditorPane() {
        if (stylePane == null) {
            stylePane = new TemplateStylePane();
            stylePane.setPreferredSize(DEFAULT_DIMENSION);
        }
        BasicDialog dlg = stylePane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(stylePane.update());
                fireStateChanged();
            }
        });
        stylePane.populate((TemplateStyle) getValue());
        dlg.setVisible(true);
    }
}
