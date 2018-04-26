/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.report;

import java.util.Hashtable;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.menu.KeySetUtils;
import com.fr.page.ReportSettingsProvider;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.headerfooter.EditHeaderPane;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.core.ReportHF;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.report.TemplateReport;
import com.fr.report.stable.ReportConstants;

/**
 * Header action.
 */
public class ReportHeaderAction extends ReportComponentAction<ReportComponent> {

    private boolean returnVal = false;

    public ReportHeaderAction(ReportComponent rc) {
        super(rc);
        this.setMenuKeySet(KeySetUtils.REPORT_HEADER);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/header.png"));
    }

    /**
     * 执行动作
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final ReportComponent reportPane = this.getEditingComponent();
        if (reportPane == null){
            return false;
        }
        final TemplateReport report = reportPane.getTemplateReport();
        final EditHeaderPane headerEditDialog = new EditHeaderPane();
        final ReportSettingsProvider set = ReportUtils.getReportSettings(report);

        //Clone 给当前hashtable.
        Hashtable cloneeportHFHash = new Hashtable();
        for (int i = 0; i != ReportConstants.PAGE_INFO.length; i++){
            this.cloneReportHFHashHeadtable(cloneeportHFHash, report, ReportConstants.PAGE_INFO[i]);
        }
        headerEditDialog.populate(set);
        headerEditDialog.populate(cloneeportHFHash);
        headerEditDialog.showWindow(
                SwingUtilities.getWindowAncestor(reportPane),
                new DialogActionAdapter() {

                    @Override
                    public void doOk() {
                        Hashtable newReportHFHash = headerEditDialog.update();

                        report.setHeader(ReportConstants.REPORTPAGE_DEFAULT,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_DEFAULT)));
                        report.setHeader(ReportConstants.REPORTPAGE_FIRST,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_FIRST)));
                        report.setHeader(ReportConstants.REPORTPAGE_LAST,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_LAST)));
                        report.setHeader(ReportConstants.REPORTPAGE_ODD,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_ODD)));
                        report.setHeader(ReportConstants.REPORTPAGE_EVEN,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_EVEN)));
                        set.setHeaderHeight(headerEditDialog.updateReportSettings());
                        returnVal = true;
                        reportPane.fireTargetModified();
                    }
                }).setVisible(true);
        return returnVal;
    }

    private void cloneReportHFHashHeadtable(Hashtable reportHFHash, Report report, int reportHFType) {
        if (report.getHeader(reportHFType) != null) {
            try {
                reportHFHash.put(new Integer(reportHFType), report.getHeader(reportHFType).clone());
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }
}