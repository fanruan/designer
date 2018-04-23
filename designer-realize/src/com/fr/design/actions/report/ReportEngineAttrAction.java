package com.fr.design.actions.report;

import com.fr.design.actions.ReportComponentAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.WorkSheetDesigner;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.LayerReportPane;
import com.fr.general.IOUtils;
import com.fr.report.worksheet.WorkSheet;

public class ReportEngineAttrAction extends ReportComponentAction<WorkSheetDesigner> {

    public ReportEngineAttrAction(WorkSheetDesigner tc) {
        super(tc);
        this.setMenuKeySet(KeySetUtils.REPORT_ENGINE);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_report/reportEngineAttr.png"));
        this.setSearchText(new LayerReportPane(null));

    }

    private boolean isChange;

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
        final WorkSheet tplEC = jws.getTemplateReport();

        final LayerReportPane layerReportPane = new LayerReportPane(tplEC);
        layerReportPane.populateBean(tplEC.getLayerReportAttr());
        BasicDialog dialog = layerReportPane.showWindow(DesignerContext.getDesignerFrame());
        isChange = false;

        dialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                isChange = true;
                tplEC.setLayerReportAttr(layerReportPane.updateBean());
            }
        });

        dialog.setVisible(true);

        return isChange;
    }

}