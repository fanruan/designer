/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.report;

import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.freeze.RepeatAndFreezeSettingPane;
import com.fr.general.IOUtils;
import com.fr.report.worksheet.WorkSheet;

import javax.swing.SwingUtilities;

/**
 * @author richer
 * @since 6.5.5
 *        创建于2011-6-14
 */
public class ReportPageAttrAction extends ReportComponentAction<WorkSheetDesigner> {
    private boolean returnValue;

    public ReportPageAttrAction(WorkSheetDesigner t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.REPORT_PAGE);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_file/pageSetup.png"));
        this.generateAndSetSearchText(RepeatAndFreezeSettingPane.class.getName());
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        WorkSheetDesigner jws = getEditingComponent();
        if (jws == null) {
            return false;
        }
        final WorkSheet tplECReport = jws.getTemplateReport();
        final RepeatAndFreezeSettingPane attrPane = new RepeatAndFreezeSettingPane();
        attrPane.populate(tplECReport.getReportPageAttr());
        attrPane.populateWriteFrozenColumnRow(tplECReport.getReportSettings().getWriteFrozenColumnRow());
        BasicDialog dlg = attrPane.showWindow(SwingUtilities.getWindowAncestor(jws));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                tplECReport.setReportPageAttr(attrPane.update());
                tplECReport.getReportSettings().setWriteFrozenColumnRow(attrPane.updateWriteFrozenColumnRow());
                returnValue = true;
            }
        });
        dlg.setVisible(true);
        return returnValue;
    }
}