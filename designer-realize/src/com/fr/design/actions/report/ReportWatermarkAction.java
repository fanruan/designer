package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.WatermarkPane;
import com.fr.report.core.ReportUtils;
import com.fr.report.stable.ReportSettings;

/**
 * Created by plough on 2018/5/15.
 */
public class ReportWatermarkAction extends ReportComponentAction<ReportComponent> {

    public ReportWatermarkAction(ReportComponent rc) {
        super(rc);
        this.setMenuKeySet(KeySetUtils.REPORT_WATERMARK);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/watermark.png"));
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
        final WatermarkPane watermarkPane = new WatermarkPane();
        ReportSettings reportSettings = (ReportSettings) ReportUtils.getReportSettings(reportPane.getTemplateReport());
        watermarkPane.populate(reportSettings.getWatermark());
        watermarkPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                reportSettings.setWatermark(watermarkPane.update());
                reportPane.fireTargetModified();
            }
        }).setVisible(true);

        return false;
    }
}
