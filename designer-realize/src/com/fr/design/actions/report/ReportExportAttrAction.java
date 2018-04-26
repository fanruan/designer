package com.fr.design.actions.report;

import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.ReportExportAttrPane;
import com.fr.general.IOUtils;
import com.fr.main.TemplateWorkBook;

import java.awt.event.ActionEvent;

public class ReportExportAttrAction extends JWorkBookAction {

    public ReportExportAttrAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.REPORT_EXPORT_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_report/exportAttr.png"));
        this.setSearchText(new ReportExportAttrPane());
    }

    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
    	final JWorkBook jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final TemplateWorkBook wbTpl = jwb.getTarget();
        final ReportExportAttrPane dialog = new ReportExportAttrPane();
        dialog.populate(wbTpl.getReportExportAttr());
        dialog.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
			public void doOk() {
                wbTpl.setReportExportAttr(dialog.update());
                jwb.fireTargetModified();
            }
        }).setVisible(true);
        
    }
}