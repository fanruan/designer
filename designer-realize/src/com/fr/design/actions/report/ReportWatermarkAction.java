package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.base.iofileattr.WatermarkAttr;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.WatermarkPane;
import com.fr.main.impl.WorkBook;
import com.fr.report.core.ReportUtils;

import java.awt.event.ActionEvent;

/**
 * Created by plough on 2018/5/15.
 */
public class ReportWatermarkAction extends JWorkBookAction {
    public ReportWatermarkAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.REPORT_WATERMARK);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/watermark.png"));
        this.setSearchText(new WatermarkPane());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JWorkBook jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final WorkBook wbTpl = jwb.getTarget();
        WatermarkAttr watermark = ReportUtils.getWatermarkFromIOFile(wbTpl);

        final WatermarkPane watermarkPane = new WatermarkPane();
        watermarkPane.populate(watermark);
        watermarkPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                wbTpl.addAttrMark(watermarkPane.update());
                jwb.fireTargetModified();
            }
        }).setVisible(true);
    }
}
