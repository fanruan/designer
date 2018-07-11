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
import com.fr.design.headerfooter.EditFooterPane;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.core.ReportHF;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.report.TemplateReport;
import com.fr.report.stable.ReportConstants;

/**
 * Footer action.
 */
public class ReportFooterAction extends ReportComponentAction<ReportComponent> {
    public ReportFooterAction(ReportComponent rc) {
        super(rc);
        this.setMenuKeySet(KeySetUtils.REPORT_FOOTER);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/footer.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final ReportComponent reportPane = this.getEditingComponent();
        if (reportPane == null){
            return false;
        }
        final TemplateReport report = reportPane.getTemplateReport();

        final EditFooterPane footerEditDialog = new EditFooterPane();

        //Clone 给当前hashtable.
        Hashtable cloneeportHFHash = new Hashtable();
        for (int i = 0; i != ReportConstants.PAGE_INFO.length; i++){
            this.cloneReportHFHashFoottable(cloneeportHFHash, report, ReportConstants.PAGE_INFO[i]);
        }
        final ReportSettingsProvider set = ReportUtils.getReportSettings(report);
        footerEditDialog.populate(set, false);
        footerEditDialog.populate(cloneeportHFHash);
        footerEditDialog.showWindow(
                SwingUtilities.getWindowAncestor(reportPane),
                new DialogActionAdapter() {
                    @Override
                    public void doOk() {
                        Hashtable newReportHFHash = footerEditDialog.update();

                        report.setFooter(ReportConstants.REPORTPAGE_DEFAULT,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_DEFAULT)));
                        report.setFooter(ReportConstants.REPORTPAGE_FIRST,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_FIRST)));
                        report.setFooter(ReportConstants.REPORTPAGE_LAST,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_LAST)));
                        report.setFooter(ReportConstants.REPORTPAGE_ODD,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_ODD)));
                        report.setFooter(ReportConstants.REPORTPAGE_EVEN,
                                (ReportHF) newReportHFHash.get(new Integer(ReportConstants.REPORTPAGE_EVEN)));
                        set.setFooterHeight(footerEditDialog.updateReportSettings());

                        reportPane.fireTargetModified();
                    }
                }).setVisible(true);
        return false;
    }

    private void cloneReportHFHashFoottable(Hashtable reportHFHash, Report report, int reportHFType) {
        if (report.getFooter(reportHFType) != null) {
            try {
                reportHFHash.put(new Integer(reportHFType), report.getFooter(reportHFType).clone());
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }
}