package com.fr.design.webattr;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-6-29
 * Time: 下午4:49
 */
public abstract class AbstractEditToolBarPane extends ReportSelectToolBarPane.EditToolBarPane<WebContent> {
    protected ToolBarManager[] toolBarManagers = null;
    private AbstractEditToolBarPane abstractEditToolBarPane;

    protected ActionListener editBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final DragToolBarPane dragToolbarPane = new DragToolBarPane();
            dragToolbarPane.setDefaultToolBar(ToolBarManager.createDefaultToolBar(), getToolBarInstanceWithExtra());
            dragToolbarPane.populateBean(AbstractEditToolBarPane.this.toolBarManagers);

            BasicDialog toobarDialog = dragToolbarPane.showWindow(SwingUtilities.getWindowAncestor(AbstractEditToolBarPane.this));
            toobarDialog.addDialogActionListener(new DialogActionAdapter() {
                @Override
				public void doOk() {
                    AbstractEditToolBarPane.this.toolBarManagers = dragToolbarPane.updateBean();
                }
            });

            toobarDialog.setVisible(true);
        }
    };

    protected WidgetOption[] getToolBarInstanceWithExtra() {
        WidgetOption[] extraOptions = ExtraDesignClassManager.getInstance().getWebWidgetOptions();
        return (WidgetOption[]) ArrayUtils.addAll(getToolBarInstance(), extraOptions);
    }
    
    protected abstract WidgetOption[] getToolBarInstance();
}