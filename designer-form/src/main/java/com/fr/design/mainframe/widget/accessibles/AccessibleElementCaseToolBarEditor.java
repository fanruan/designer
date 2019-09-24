package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.fun.ToolbarItemProvider;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.FormWebWidgetConstants;
import com.fr.design.mainframe.widget.editors.ElementCaseToolBarPane;
import com.fr.design.mainframe.widget.wrappers.ElementCaseToolBarWrapper;
import com.fr.form.web.FormToolBarManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Filter;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by harry on 2017-2-23.
 */
public class AccessibleElementCaseToolBarEditor extends UneditableAccessibleEditor {
    private ElementCaseToolBarPane pane;

    public AccessibleElementCaseToolBarEditor() {
        super(new ElementCaseToolBarWrapper());
    }

    @Override
    protected void showEditorPane() {
        if (pane == null) {
            pane = new ElementCaseToolBarPane();
            pane.setDefaultToolBar(getDefaultToolBarManager(), getToolBarInstance());
        }
        BasicDialog dlg = pane.showToolBarWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(pane.updateBean());
                fireStateChanged();
            }
        });
        FormToolBarManager[] managers = (FormToolBarManager[]) getValue();
        pane.setCheckBoxSelected(ArrayUtils.isNotEmpty(managers));
        pane.populateBean((FormToolBarManager[]) getValue());
        dlg.setVisible(true);
    }

    private FormToolBarManager getDefaultToolBarManager() {
        return FormToolBarManager.createDefaultToolBar();
    }

    private WidgetOption[] getToolBarInstance() {
        List<WidgetOption> defaultOptions = Arrays.asList(FormWebWidgetConstants.getFormElementCaseToolBarInstance());
        List<WidgetOption> options = new ArrayList<WidgetOption>();
        options.addAll(defaultOptions);
        WidgetOption[] widgetOptions = ExtraDesignClassManager.getInstance().getWebWidgetOptions(new Filter<ToolbarItemProvider>() {
            @Override
            public boolean accept(ToolbarItemProvider toolbarItemProvider) {
                return toolbarItemProvider.accept(HistoryTemplateListCache.getInstance().getCurrentEditingTemplate());
            }
        });
        options.addAll(Arrays.asList(widgetOptions));
        return options.toArray(new WidgetOption[options.size()]);
    }

}
