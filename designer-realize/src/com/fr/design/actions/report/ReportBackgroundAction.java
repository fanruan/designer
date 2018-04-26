/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.ReportBackgroundPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.core.ReportUtils;

/**
 * Background action.
 */
public class ReportBackgroundAction extends ReportComponentAction<ReportComponent> {

	public ReportBackgroundAction(ReportComponent rc) {
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
        final ReportComponent reportPane = this.getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        final ReportBackgroundPane bPane = new ReportBackgroundPane();
        bPane.populate(ReportUtils.getReportSettings(reportPane.getTemplateReport()));
        bPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
			public void doOk() {
                bPane.update(reportPane.getTemplateReport().getReportSettings());
                reportPane.fireTargetModified();
            }
        }).setVisible(true);
        return false;
    }
}