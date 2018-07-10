/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.report;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.PageSetupPane;
import com.fr.general.IOUtils;
import com.fr.report.report.TemplateReport;

import javax.swing.*;

/**
 * PageSetup action.
 */
public class ReportPageSetupAction extends ReportComponentAction<ReportComponent> {

    private boolean returnValue;

    public ReportPageSetupAction(ReportComponent rc) {
		super(rc);
        this.setMenuKeySet(KeySetUtils.REPORT_PAGE_SETUP);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_file/pageSetup.png"));
        this.generateAndSetSearchText(PageSetupPane.class.getName());
    }
    
    /**
     * 执行动作
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
    	ReportComponent rc = getEditingComponent();
        if (rc == null) {
            return false;
        }
        final TemplateReport report = rc.getTemplateReport();//当前的报表.
        final PageSetupPane pageSetupPane = new PageSetupPane();
        pageSetupPane.populate(report, DesignerEnvManager.getEnvManager().getPageLengthUnit());
        BasicDialog dlg = pageSetupPane.showWindow(SwingUtilities.getWindowAncestor(rc));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
            	pageSetupPane.update(report);
                returnValue = true;
            }
            @Override
        	public void doCancel() {
            	returnValue = false;
            }
        });
        dlg.setVisible(true);
        return returnValue;
    }
}