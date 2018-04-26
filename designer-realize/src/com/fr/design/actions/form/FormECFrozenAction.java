/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.form;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.freeze.FormECRepeatAndFreezeSettingPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.worksheet.FormElementCase;

/**
 * 表单报表块冻结
 */
public class FormECFrozenAction extends AbastractFormECAction<FormElementCasePaneDelegate>{
    private boolean returnValue;

    public FormECFrozenAction(FormElementCasePaneDelegate t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.EC_FROZEN);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/form/toolbar/ec_frozen.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
    	FormElementCasePaneDelegate jws = getEditingComponent();
        if (jws == null) {
            return false;
        }
        final FormElementCase fc = jws.getTarget();
        final FormECRepeatAndFreezeSettingPane attrPane = new FormECRepeatAndFreezeSettingPane();
        attrPane.populate(fc.getReportPageAttr());
        BasicDialog dlg = attrPane.showWindow(SwingUtilities.getWindowAncestor(jws));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                fc.setReportPageAttr(attrPane.update());
                returnValue = true;
            }
        });
        dlg.setVisible(true);
        return returnValue;
    }
}