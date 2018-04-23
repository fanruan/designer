/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.form;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.form.FormElementCasePaneDelegate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.ReportBackgroundPane;
import com.fr.design.dialog.DialogActionAdapter;

/**
 * Background action.
 */
public class FormECBackgroundAction extends AbastractFormECAction<FormElementCasePaneDelegate>{

	public FormECBackgroundAction(FormElementCasePaneDelegate rc) {
		super(rc);
        this.setMenuKeySet(KeySetUtils.REPORT_BACKGROUND);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/background.png"));
    }

    /**
     * 执行动作
     * @return 是否执行成功
     */
	public boolean executeActionReturnUndoRecordNeeded() {
        final FormElementCasePaneDelegate elementCasePane = this.getEditingComponent();
        if (elementCasePane == null) {
            return false;
        }
        final ReportBackgroundPane bPane = new ReportBackgroundPane();
        bPane.populate(elementCasePane.getReportSettings());
        bPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
			public void doOk() {
                bPane.update(elementCasePane.getReportSettings());
                elementCasePane.fireTargetModified();
            }
        }).setVisible(true);
        return false;
    }
}