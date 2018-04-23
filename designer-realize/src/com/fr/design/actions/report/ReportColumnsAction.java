package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.ReportColumnsPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.report.worksheet.WorkSheet;

public class ReportColumnsAction extends ReportComponentAction<WorkSheetDesigner> {

    public ReportColumnsAction(WorkSheetDesigner t) {
        super(t);
        this.setMenuKeySet(KeySetUtils.REPORT_COLUMN);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/linearAttr.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final WorkSheetDesigner jws = this.getEditingComponent();
        if (jws == null) {
            return false;
        }
        final WorkSheet workSheet = jws.getTemplateReport();

        final ReportColumnsPane cPane = new ReportColumnsPane();
        cPane.populate(workSheet);
        cPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
            public void doOk() {
                cPane.update(workSheet);
                jws.fireTargetModified();
            }
        }).setVisible(true);
        return false;
    }
}