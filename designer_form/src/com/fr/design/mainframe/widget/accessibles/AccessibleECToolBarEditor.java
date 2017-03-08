package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.FormWebWidgetConstants;
import com.fr.design.mainframe.widget.editors.ECToolBarPane;
import com.fr.design.mainframe.widget.wrappers.ECToolBarWrapper;
import com.fr.form.web.FToolBarManager;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by harry on 2017-2-23.
 */
public class AccessibleECToolBarEditor extends UneditableAccessibleEditor {
    private ECToolBarPane pane;

    public AccessibleECToolBarEditor() {
        super(new ECToolBarWrapper());
    }

    @Override
    protected void showEditorPane() {
        if (pane == null) {
            pane = new ECToolBarPane();
            pane.setDefaultToolBar(getDefaultToolBarManager(), getToolBarInstance());
        }
        BasicDialog dlg = pane.showToolBarWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(pane.updateBean());
                fireStateChanged();
            }
        });
        FToolBarManager[] managers = (FToolBarManager[]) getValue();
        pane.setCheckBoxSelected(ArrayUtils.isNotEmpty(managers));
        pane.populateBean((FToolBarManager[]) getValue());
        dlg.setVisible(true);
    }

    private FToolBarManager getDefaultToolBarManager() {
        return FToolBarManager.createDefaultToolBar();
    }

    private WidgetOption[] getToolBarInstance() {
        List<WidgetOption> defaultOptions = Arrays.asList(FormWebWidgetConstants.getFormECToolBarInstance());
        List<WidgetOption> options = new ArrayList<WidgetOption>();
        options.addAll(defaultOptions);
        return options.toArray(new WidgetOption[options.size()]);
    }
}
