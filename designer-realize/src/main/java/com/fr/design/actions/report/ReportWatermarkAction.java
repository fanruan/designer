package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.WatermarkPane;
import com.fr.intelli.record.Focus;
import com.fr.intelli.record.Original;
import com.fr.main.impl.WorkBook;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.report.core.ReportUtils;

import java.awt.event.ActionEvent;

/**
 * Created by plough on 2018/5/15.
 */
@EnableMetrics
public class ReportWatermarkAction extends JWorkBookAction {
    public ReportWatermarkAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.REPORT_WATERMARK);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/watermark.png"));
        this.generateAndSetSearchText(WatermarkPane.class.getName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JWorkBook jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final WorkBook wbTpl = jwb.getTarget();
        WatermarkAttr watermark = ReportUtils.getWatermarkFromAttrMarkFile(wbTpl);

        final WatermarkPane watermarkPane = new WatermarkPane();
        watermarkPane.populate(watermark);
        watermarkPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                wbTpl.addAttrMark(watermarkPane.update());
                jwb.fireTargetModified();
                recordFunction();
            }
        }).setVisible(true);
    }

    @Focus(id = "com.fr.watermark", text = "Fine-Design_Form_WaterMark", source = Original.EMBED)
    private void recordFunction() {
        // do nothing
    }
}
