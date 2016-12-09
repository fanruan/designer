/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.report;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.parameter.ParameterArrayPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.parameter.ReportParameterAttr;

/**
 * Report Parameter
 */
public class ReportParameterAction extends JWorkBookAction{

    private UIObserverListener uiObserverListener = null;

    public ReportParameterAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.REPORT_PARAMETER_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public void actionPerformed(ActionEvent evt) {
        final JWorkBook jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final TemplateWorkBook wbTpl = jwb.getTarget();

        final ParameterArrayPane parameterArrayPane = new ParameterArrayPane();
        BasicDialog parameterArrayDialog = parameterArrayPane.showWindow(SwingUtilities.getWindowAncestor(jwb));
        parameterArrayDialog.setModal(true);

        final ReportParameterAttr copyReportParameterAttr = getParameter(wbTpl);
        parameterArrayPane.populate(copyReportParameterAttr.getParameters());

        parameterArrayDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                copyReportParameterAttr.clearParameters();
                Parameter[] parameters = parameterArrayPane.updateParameters();
                for (int i = 0; i < parameters.length; i++) {
                    copyReportParameterAttr.addParameter(parameters[i]);
                }
                jwb.fireTargetModified();
                jwb.updateReportParameterAttr();
                jwb.populateReportParameterAttr();

                //点击确定会后，需要出发插件界面的更新
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireElementCasePane();
            }
        });
        parameterArrayDialog.setVisible(true);
    }

    private ReportParameterAttr getParameter(TemplateWorkBook wbTpl) {
        ReportParameterAttr reportParameterAttr = wbTpl.getReportParameterAttr();
        if (reportParameterAttr == null) {
            reportParameterAttr = new ReportParameterAttr();
            wbTpl.setReportParameterAttr(reportParameterAttr);
        }

        return reportParameterAttr;
    }
}