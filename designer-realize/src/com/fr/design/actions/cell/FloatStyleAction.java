/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.ReportStylePane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.ElementCasePane;

/**
 * Cell Style.
 */
public class FloatStyleAction extends ElementCaseAction {
    boolean okreturn = false;

    public FloatStyleAction(ElementCasePane t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
    }

    /**
     * 执行动作
     * @return 成功返回true
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final ElementCasePane reportPane = this.getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        final ReportStylePane reportStylePane = new ReportStylePane();
        final BasicDialog styleDialog = reportStylePane.showWindow(SwingUtilities.getWindowAncestor(reportPane));
        reportStylePane.populate(reportPane);
        styleDialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                reportStylePane.update(reportPane);
                FloatStyleAction.this.okreturn = true;
            }
        });
        styleDialog.setVisible(true);
        return okreturn;
    }
}